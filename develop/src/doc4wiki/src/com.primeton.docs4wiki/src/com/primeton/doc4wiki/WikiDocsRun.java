/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/WikiDocsRun.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-13
 *******************************************************************************/

package com.primeton.doc4wiki;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.primeton.doc4wiki.impl.ExportWikiDocs;
import com.primeton.doc4wiki.util.WikiFileUtils;
import com.primeton.doc4wiki.util.WikiInfo;
import com.primeton.doc4wiki.util.WikiStringUtils;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:11:09
 */
public class WikiDocsRun {
	private static final Log LOG = LogFactory.getLog(WikiDocsRun.class);
	/**
	 * 
	 */
	public WikiDocsRun() {
		super();
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		if(args == null || args.length < 8){
			LOG.warn("没有设置参数，将会使用默认参数！参数格式如下："+
					"\"wikidocs_resource_save_location=c:/userdoc/userDoC.zip \"\n"+
					"\"wikidocs_path=/spaces/exportspace.action  \"\n"+
					"\"wikidocs_host=192.168.1.31  \"\n"+
					"\"wikidocs_port=8080  \"\n"+
					"\"wikidocs_user_name=liuxiang  \"\n"+
					"\"wikidocs_user_password=000000  \"\n"+
					"\"wikidocs_workspace_id=UserDoc  \"\n"+
					"\"wikidocs_root_index=Home.html  \"\n");
		} else {
			Properties properties = new Properties();
			for (int i = 0; i < args.length; i++) {
				String string = args[i];
				if(string.indexOf('=') > 0){
					String key = WikiStringUtils.substringBefore(string, "=");
					String value = WikiStringUtils.substringAfter(string, "=");
					key = key.trim();
					properties.setProperty(key, value);
				}
			}
			WikiInfo.setProperties(properties);
		}
		
		boolean success = new WikiDocsRun().excute();
		if(!success){
			throw new Exception("未能执行成功!");
		}
	}
	
	/**
	 * 
	 */
	public boolean excute(){
		//导出space，并下载
		ExportWikiDocs exportWikiDocs = new ExportWikiDocs();
		boolean success = exportWikiDocs.excute();
		if(!success){
			return false;
		}
		String path = exportWikiDocs.getSavePath();
		
		File file = new File(path);
		String name = file.getName();
		name = WikiStringUtils.substringBeforeLast(name, ".");
		String workDir = file.getParent();
		String des = workDir + "/" + "docs";
		File desDir = new File(des);
		try {
			if(desDir.exists()){
				WikiFileUtils.forceDelete(desDir);
			}
			WikiFileUtils.forceMkdir(desDir);
		} catch (IOException e1) {
			
		}
		//解压下来的zip
		try {
			WikiFileUtils.unpack(file, desDir,true);
		} catch (Exception e) {
			LOG.error("解压出错!", e);
			return false;
		} 
		//更新toc.xml文件
		TocRegenerator gRegenerator = new TocRegenerator();
		gRegenerator.updateToc(workDir + "/toc.xml" , des + "/index.html");
		//删除下载的压缩文件
		new File(path).delete();

		return true;
	}
}
