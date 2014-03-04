/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/TocRegenerator.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-7
 *******************************************************************************/

package com.primeton.doc4wiki;

import java.io.File;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.ParserException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.primeton.doc4wiki.impl.RootNodeVisitor;
import com.primeton.doc4wiki.util.WikiStringUtils;
import com.primeton.doc4wiki.util.WikiXmlUtil;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:11:47
 */
public class TocRegenerator {
	/**
	 * Comment for <code>ROOT_LABEL</code>
	 */
	private static final String ROOT_LABEL = "BPS 帮助文档";
	/**
	 * Comment for <code>ROOT_HREF</code>
	 */
	private static final String A_HREF = "a";
	private static final String DOCS = "docs/";
	private static final String TOPIC = "topic";
	private static final String HREF = "href";
	private static final String UL = "ul";
	private static final String LI = "li";
	private static final String TOC = "toc";
	private static final String LABEL = "label";
	
	private static final Log LOG = LogFactory.getLog(TocRegenerator.class);
	
	/**
	 * 
	 * @param tocXmlLocation
	 * @param indexHtmlLocation
	 * @return
	 */
	public boolean updateToc(String tocXmlLocation, String indexHtmlLocation){
		Tag tag = null;
		try {
			tag = getRootLiTag(indexHtmlLocation);
		} catch (ParserException e) {
			LOG.error("解析html出现错误!", e);
			return false;
		}
		if(tag == null){
			LOG.error("解析html出现错误，未能正确解析出html！");
			return false;
		}
		Document document = WikiXmlUtil.newDocument();
		
		Element outElement = document.createElement(TOC);
		//把html中的连接信息转换为toc.xml中对应的信息
		appendToc(tag, outElement,true);
		outElement.setAttribute(LABEL, ROOT_LABEL);
		document.appendChild(outElement);
		File tocFile = getTocFile(tocXmlLocation);
		
		TransformerFactory tFactory = TransformerFactory.newInstance();  
		try {
			Transformer transformer = tFactory.newTransformer(); 
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(document);   
			StreamResult result = new StreamResult(tocFile);  
			transformer.transform(source, result);
		} catch (Exception e) {
			LOG.error("保存toc文件出错!", e);
			return false;
		}
		LOG.info("完成TOC文件的更新!");
		return true;
	}

	/**
	 * @param tocXmlLocation
	 * @return
	 */
	private File getTocFile(String tocXmlLocation) {
		File tocFile = new File(tocXmlLocation);
		if(!tocFile.exists()){
			File parent = tocFile.getParentFile();
			if(!parent.exists()){
				parent.mkdirs();
			}
		} else {
			tocFile.delete();
		}
		return tocFile;
	}
	
	/**
	 * 
	 * @param tag
	 * @param outElement
	 * @param isRoot
	 */
	private void appendToc(Tag tag, Element outElement, boolean isRoot) {
		if(!LI.equalsIgnoreCase(tag.getTagName())){
			return;
		}
		Element topicElement = outElement;
		if(!isRoot){
			Document document = outElement.getOwnerDocument();
			topicElement = document.createElement(TOPIC);
			outElement.appendChild(topicElement);			
		}
		Node[] nodes = tag.getChildren().toNodeArray();
		for (int i = 0; i < nodes.length; i++) {
			Node node = nodes[i];
			if(!(node instanceof Tag)){
				continue;
			}
			Tag ulTag = (Tag)node;
			String name = ulTag.getTagName();
			if(A_HREF.equalsIgnoreCase(name)){
				setTocValue((LinkTag) ulTag, topicElement);
			} 
			else if(UL.equalsIgnoreCase(name)){
				appendToc(topicElement, node);
			}
			
		}
	}

	/**
	 * @param linkTag
	 * @param topicElement
	 */
	private void setTocValue(LinkTag linkTag, Element topicElement) {
		try {
			String href = linkTag.getAttribute(HREF);
			String value = linkTag.getLinkText();
			value = value == null? "":value.trim();
			href = href == null?"":href.trim();
			value = WikiStringUtils.remove(value, '\n');
			value = WikiStringUtils.remove(value, '\r');
			value = fomate(value);
			topicElement.setAttribute(LABEL, value);
			topicElement.setAttribute(HREF, DOCS + href);
		} catch (Exception e) {
		}
	}
	/**
	 * 
	 * @param label
	 * @return
	 */
	private String fomate(String label){
		if(label == null){
			return null;
		}
		String reg = "(?:\\d+\\.*|\\s*)*";
		label = label.replaceFirst(reg, "");
		return label.trim();
	}
	
	/**
	 * @param outElement
	 * @param node
	 */
	private void appendToc(Element outElement, Node node) {
		Node[] nodesUl = node.getChildren().toNodeArray();
		for (int j = 0; j < nodesUl.length; j++) {
			Node node2 = nodesUl[j];
			if(!(node2 instanceof Tag)){
				continue;
			}
			appendToc((Tag) node2, outElement,false);
		}
	}

	/**
	 * @param htmlPath
	 * @return
	 * @throws ParserException
	 */
	private Tag getRootLiTag(String htmlPath) throws ParserException {
		Parser parser = new Parser(htmlPath);
		parser.setEncoding("UTF-8");
		RootNodeVisitor visitor = new RootNodeVisitor(true, true);
		parser.visitAllNodesWith(visitor);
		return visitor.getRootNode();
	}
	
	 public static void main(String[] args) {
		  TocRegenerator ge = new TocRegenerator();
		  ge.updateToc("G:/workspace_BPS/test.plugin.docs4wiki/src/test/toc.xml", "G:/workspace_BPS/test.plugin.docs4wiki/src/test/docs/index.html");
		  System.out.println("ok");
		  //
	}
	
}
