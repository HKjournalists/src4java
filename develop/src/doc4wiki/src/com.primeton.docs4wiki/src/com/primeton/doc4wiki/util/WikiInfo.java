/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/util/WikiInfo.java,v 1.1 2013/06/06 01:44:59 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:59 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-6
 *******************************************************************************/

package com.primeton.doc4wiki.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:12:19
 */

public class WikiInfo implements WikiConstant{

	private static String host = "";
	private static String path = "";
	private static int port = 80;
	private static String userName = "";
	private static String userPassword = "";
	private static String workspace_id = "";
	private static String location = "";
	
	private static final String NAME_KEY = "os_username";
	private static final String PASSWORD_KEY = "os_password";
	private static final String KEY_KEY = "key";
	
	private static String rootIndex = "";
	
    private static final Log LOG = LogFactory.getLog(WikiInfo.class);
    private static WikiInfo wikiInfo;
    private static Properties properties = null;
	/**
	 * 
	 */
	private WikiInfo() {
		//首先从上下文中查找配置，如果没有找到，使用配置文件
		if(properties == null){			
			properties = new Properties();
			InputStream inputStream = WikiInfo.class.getResourceAsStream(CONFIG_FILE);
			try {
				properties.load(inputStream);
			} catch (IOException ex) {
				String msg = MessageFormat.format("初始化数据时发生错误，请查看配置文件({0})配置是否正确.", CONFIG_FILE);
				LOG.error(msg, ex);
			} finally {
				WikiIOUtils.closeQuietly(inputStream);
			}
		}
		init(properties);
	}
	/**
	 * @param properties
	 */
	private void init(Properties properties) {
		host = properties.getProperty(WIKI_HOST, host);
		host = host.trim();
		LOG.info("WIKI_HOST:" + host);
		
		path = properties.getProperty(WIKI_PATH, path);
		path = path.trim();
		if(!path.startsWith("/")){
			path = "/" + path;
		}
		LOG.info("WIKI_PATH:" + path);
		
		userName = properties.getProperty(USER_NAME, userName);
		LOG.info("USER_NAME:" + userName);
		
		userPassword = properties.getProperty(USER_PASSWORD, userPassword);
		LOG.info("USER_PASSWORD:" + userPassword);
		
		workspace_id = properties.getProperty(WORKSPACE_ID, workspace_id);
		workspace_id = workspace_id.trim();
		LOG.info("WORKSPACE_ID:" + workspace_id);
		
		String tempPort = properties.getProperty(WIKI_PORT, String.valueOf(port));
		tempPort = tempPort.trim();
		port = Integer.valueOf(tempPort);
		LOG.info("WIKI_PORT:" + port);
		
		location = properties.getProperty(RESOURCE_SAVE_LOCATION, location);
		location = location.trim();
		LOG.info("RESOURCE_SAVE_LOCATION:" + location);
		
		rootIndex = properties.getProperty(ROOT_INDEX,rootIndex);
		rootIndex = rootIndex.trim();
		LOG.info("rootIndex:" + rootIndex);
	}
	/**
	 * 
	 * @return
	 */
	public static WikiInfo getInstance(){
		if(wikiInfo == null){
			wikiInfo = new WikiInfo();
		}
		return wikiInfo;
	}
	/**
	 * 
	 * @return
	 */
	public NameValuePair[] getLoginFormValues(){
		NameValuePair[] nameValuePairs = new NameValuePair[2];
		nameValuePairs[0] = new NameValuePair(NAME_KEY,userName);
		nameValuePairs[1] = new NameValuePair(PASSWORD_KEY,userPassword);
		return nameValuePairs;
	}
	/**
	 * 
	 * @return
	 */
	public NameValuePair getWorkspaceIdValue(){
		return new NameValuePair(KEY_KEY,workspace_id);
	}
	/**
	 * 
	 * @return
	 */
	public String getHost(){
		return host;
	}
	/**
	 * 
	 * @return
	 */
	public int getPort(){
		return port;
	}
	/**
	 * 
	 * @return
	 */
	public String getExportSpacePath(){
		return path;
	}
	/**
	 * 
	 * @return
	 */
	public String getSaveLocation(){
		return location;
	}
	/**
	 * 
	 * @return
	 */
	public String getRootIndex(){
		return rootIndex;
	}
	/**
	 * 不使用缺省的 config.properties，使用外部的配置
	 * @param properties
	 */
	public static void setProperties(Properties properties){
		WikiInfo.properties = properties;
	}
	/**
	 * 
	 * @return
	 */
	public static Properties getProperties(){
		return WikiInfo.properties;
	}
}
