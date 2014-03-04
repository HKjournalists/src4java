/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/util/WikiXmlUtil.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-8
 *******************************************************************************/

package com.primeton.doc4wiki.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 ÉÏÎç8:13:01
 */

public class WikiXmlUtil {
	private static DocumentBuilder documentBuilder = null;
	static {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setIgnoringElementContentWhitespace(true);
		try {
			documentBuilder = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public static Document newDocument(){
		if(documentBuilder == null){
			return null;
		}
		return documentBuilder.newDocument();
	}
	/**
	 * 
	 * @param inputStream
	 * @return
	 */
	public static Document parser(InputStream inputStream){
		try {
			return documentBuilder.parse(inputStream);
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
           return null;
		} finally {
			WikiIOUtils.closeQuietly(inputStream);
		}
	}
	/**
	 * 
	 * @param element
	 * @param name
	 * @return
	 */
	public static String getChildValue(Node element, String name){
		
		NodeList nodeList = element.getChildNodes();
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeList.item(i);
			if(name.equals(node.getNodeName())){
				return node.getTextContent();
			}
		}
		return null;
	}
	
	
}
