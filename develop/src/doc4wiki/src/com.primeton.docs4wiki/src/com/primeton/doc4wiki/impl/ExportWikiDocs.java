/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/ExportWikiDocs.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2009 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-3
 *******************************************************************************/

/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/ExportWikiDocs.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2009 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-3
 *******************************************************************************/

package com.primeton.doc4wiki.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

import com.primeton.doc4wiki.util.WikiConstant;
import com.primeton.doc4wiki.util.WikiFileUtils;
import com.primeton.doc4wiki.util.WikiIOUtils;
import com.primeton.doc4wiki.util.WikiInfo;
import com.primeton.doc4wiki.util.WikiStringUtils;
import com.primeton.doc4wiki.util.WikiXmlUtil;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:11:28
 */
public class ExportWikiDocs implements WikiConstant{
	
	private HttpClient httpClient;
	//设置超时时间为30分钟
	private static final long TIME_OUT = 30*60*1000/*1800000*/;
	
    private static final Log LOG = LogFactory.getLog(ExportWikiDocs.class);
	
	/**
	 * 执行导出操作.<br/>
	 * 步骤为 ：登陆 --> 执行wiki的导出操作 --> 下载导出的文档
	 * @param path 导出包所在位置。
	 * @return 是否执行成功，true表示执行成功，否则执行错误。
	 */
	public boolean excute() {
		//登陆
		String formPageContens = loginWiki();
		if(formPageContens == null || "".equals(formPageContens)){
			return false;
		}
		//执行wiki上的导出操作
		String archiveLocation = null;
		try {
			archiveLocation = exportSpace(formPageContens);
		} catch (Exception e) {
			LOG.error("在wiki上执行导出操作时出错.", e);
		}
		if(archiveLocation == null || "".equals(archiveLocation)){
			return false;
		}
		//下载导出的文件到本地
		return downLoad(archiveLocation);
	}
	/**
	 * 返回下载后的zip文件路径
	 * @return
	 */
	public String getSavePath(){
		return WikiInfo.getInstance().getSaveLocation();
	}
	
//	/**
//	 * 
//	 * @param args
//	 * @throws Exception
//	 */
//	public static void main(String[] args) throws Exception {
//		ExportWikiDocs exportWikiDocs = new ExportWikiDocs();
//		exportWikiDocs.excute();
//	}
	
	/**
	 * 登陆,并获取导出文档的页面，以便计算出需要提交的值
	 * @return 导出文档的页面
	 * @throws URIException 
	 */
	private String loginWiki(){
		this.httpClient = new HttpClient();
        httpClient.getHostConfiguration().setHost(WikiInfo.getInstance().getHost(), WikiInfo.getInstance().getPort(), "http");
        httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        
        //构造登陆url
        String path = WikiInfo.getInstance().getExportSpacePath();
        path = formate(path);
        
        //构造提交表单
        PostMethod method = new PostMethod(path);
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        NameValuePair[] nameValuePairs = WikiInfo.getInstance().getLoginFormValues();
        Collections.addAll(list, nameValuePairs);
        list.add(WikiInfo.getInstance().getWorkspaceIdValue());
        method.setRequestBody(list.toArray(new NameValuePair[list.size()]));
        
        try {
        	//登陆wiki服务器
			int statusCode = httpClient.executeMethod(method);
			boolean success = statusCode == HttpStatus.SC_OK;
			if(!success){
				String statusLine = method.getStatusLine().toString();
				Header header = method.getResponseHeader("location");
				String location = header == null ? "" : header.getValue();
				String msg = MessageFormat.format("登陆失败!原因 ：\n{0}:{1}", statusLine , location);
				LOG.error(msg);
				return null;
			}
			String formPage = method.getResponseBodyAsString();
			LOG.info("登陆成功!");
			return formPage;
		} catch (HttpException ex) {
			LOG.error("登陆失败!", ex);
			return null;
		} catch (IOException ex) {
			LOG.error("登陆失败!", ex);
			return null;
		} 
		finally {
			method.releaseConnection();
		}
	}
	/**
	 * 执行wiki上的导出操作
	 * @param formPageContens
	 * @return archiveLocation 
	 * @throws Exception
	 */
	private String exportSpace(String formPageContens) throws Exception{
		String contents = formPageContens;
		
		ExportFormValue form = new ExportFormValue(contents);
		NameValuePair[] nameValuePairs = form.getFormValue();
		String actionPath = form.getFormAction();
		String path = WikiInfo.getInstance().getExportSpacePath();
		int index = path.lastIndexOf('/');
		if(path != null && index > 0){
			path = path.substring(0, index);
			if(actionPath != null){
				actionPath = path + formate(actionPath);
			}
		}
		PostMethod method = new PostMethod(formate(actionPath));
		try {
			method.setRequestBody(nameValuePairs);
			int statusCode = this.httpClient.executeMethod(method);
			boolean success = statusCode == HttpStatus.SC_OK;
//			System.out.println("statusCode=" + statusCode);
			if(!success){
				
				String responseString = method.getResponseBodyAsString();
				throw new RuntimeException(responseString);
	//			System.out.println(responseString);
			}
			
			String url = null;
			long start = System.currentTimeMillis();
			GetMethod getMethod = new GetMethod(path + "/longrunningtaskxml.action");
			try {
				
				LOG.info("正在WIKI上执行导出操作...");
				String tipInfo = "exoprt:";
				do {
					Thread.sleep(3000);
					tipInfo = tipInfo + ".";
					LOG.info(tipInfo);
					this.httpClient.executeMethod(getMethod);
					String responseStr = getMethod.getResponseBodyAsString();;
					LOG.info("responseStr=" + responseStr);
					InputStream inputStream = getMethod.getResponseBodyAsStream();
					Document document = WikiXmlUtil.parser(inputStream);
					if(document == null){
						continue;
					}
					String value = WikiXmlUtil.getChildValue(document.getDocumentElement(), "currentStatus");
					if(value != null && value.indexOf(" href=\"") > -1){
						String downLoadUrl = getDownloadUrl(value);
						LOG.info("执行wiki导出操作成功");
						LOG.info("downLoadUrl=" + downLoadUrl);
						return downLoadUrl;
					}
				} while (url == null && (System.currentTimeMillis() - start) < TIME_OUT);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			finally {
				getMethod.releaseConnection();
			}
			Header header1 = method.getResponseHeader("location");
			if(header1 == null){
				String msg = "执行导出操作时出错";
				LOG.error(msg);
				return null;
			}
			return header1.getValue();
		}finally{
			method.releaseConnection();
		}
	}
	/**
	 * 
	 * @param statusValue
	 * @return
	 */
	private String getDownloadUrl(String statusValue){
		statusValue = WikiStringUtils.substringAfter(statusValue, " href=\"");
		statusValue = WikiStringUtils.substringBefore(statusValue, "\"");
		StringBuilder stringBuilder = new StringBuilder("http://");
		stringBuilder.append(WikiInfo.getInstance().getHost()).append(':');
		stringBuilder.append(WikiInfo.getInstance().getPort());
		if(!statusValue.startsWith("/")){
			stringBuilder.append('/');
		}
		stringBuilder.append(statusValue);
		return stringBuilder.toString();
	}
	/**
	 * 把服务器上的压缩包下载到本地
	 * @param archiveLocation
	 * @return 是否下载成功
	 * @throws Exception 
	 */
	private boolean downLoad(String archiveLocation){
		URL url = null;
		try {
			url = new URL(archiveLocation);
		} catch (MalformedURLException e1) {
			LOG.error("URL错误"+e1.getMessage(), e1);
			return false;
		}
		String path = url.getPath();
		path = formate(path);
		GetMethod method = new GetMethod(path);
		try {
			LOG.info("开始下载："+archiveLocation);
			requestDowloadUrl(method);
		} catch (Exception e) {
			errorDownLoad(archiveLocation, e);
			return false;
		}
		return downLoad(archiveLocation, method);
		
	}

	/**
	 * 下载压缩包
	 * @param archiveLocation
	 * @return 是否下载成功
	 * @param method
	 */
	private boolean downLoad(String archiveLocation, GetMethod method) {
		InputStream inputStream = null;
		try {
			inputStream = method.getResponseBodyAsStream();
		} catch (IOException e1) {
			errorDownLoad(archiveLocation, e1);
			return false;
		}
		String lacation = getSaveLocation();
		try {
			File lactionFile = new File(lacation);
			if(new File(lacation).exists()){
				WikiFileUtils.forceDelete(lactionFile);
			}
			if(!lactionFile.getParentFile().exists()){
				WikiFileUtils.forceMkdir(lactionFile.getParentFile());
			}
		} catch (IOException e1) {
		}
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(lacation);
			byte[] buffer = new byte[5120];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				fileOutputStream.write(buffer, 0, len);
			}
			LOG.info("下载成功,保存路径为:" + lacation);
			return true;
		} catch(IOException e){
			errorDownLoad(archiveLocation, e);
			return false;
		} finally {
			WikiIOUtils.closeQuietly(fileOutputStream);
			WikiIOUtils.closeQuietly(inputStream);
			method.releaseConnection();
		}
	}

	/**
	 * 请求下载的URL，如果
	 * @param method
	 * @return
	 * @throws Exception
	 */
	private int requestDowloadUrl(GetMethod method) throws Exception {
		long start = System.currentTimeMillis();
		int statusCode = HttpStatus.SC_BAD_REQUEST;
		do {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw e;
			}
			try {
				statusCode = this.httpClient.executeMethod(method);
			} catch (HttpException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
			long timecount = System.currentTimeMillis() - start;
			if(timecount > TIME_OUT){
				throw new TimeoutException("超时退出,超时时间:"+TIME_OUT);
			}
		} while (statusCode != HttpStatus.SC_OK);
		
		return statusCode;
	}

	/**
	 * @return
	 */
	private String getSaveLocation() {
		String lacation = WikiInfo.getInstance().getSaveLocation();
		File file = new File(lacation);
		if(file.exists()){
			file.delete();
		}
		return lacation;
	}

	/**
	 * 记录下载异常
	 * @param archiveLocation
	 * @param ex
	 */
	private void errorDownLoad(String archiveLocation, Exception ex) {
		if(ex == null){
			String msg = MessageFormat.format("未能下载成功,请求的URL为:{0}！",archiveLocation);
			LOG.error(msg);
		} else {
			String exMsg = ex.getMessage();
			if(exMsg == null){
				exMsg = "";
			}
			String msg = MessageFormat.format("未能下载成功,请求的URL为:{1},原因:", archiveLocation,exMsg);
			LOG.error(msg, ex);
		}
	}
	/**
	 * 
	 * @param path
	 * @return
	 */
	private static String formate(String path){
		if(path != null && !path.startsWith("/")){
			return "/" + path;
		}
		return path;
	}
}
