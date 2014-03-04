/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/ExportFormValue.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-5
 *******************************************************************************/

package com.primeton.doc4wiki.impl;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.NameValuePair;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:11:14
 */
public class ExportFormValue {
	private static final String CONFIRM_VALUE = "%E5%AF%BC%E5%87%BA";	//提交
	private static final String FALSE = "false";
	private static final String TRUE = "true";
	private static final String CONFIRM = "confirm";
	private static final String SYNCHRONOUS = "synchronous";
	private static final String INCLUDE_COMMENTS = "includeComments";
	private static final String TYPE_HTML = "TYPE_HTML";
	private static final String EXPORT_TYPE = "exportType";
	private static final String ATL_TOKEN = "atl_token";
	private static final String HIDDEN = "hidden";
	/**
	 * Comment for <code>NAME</code>
	 */
	private static final String NAME = "name";
	/**
	 * Comment for <code>VALUE</code>
	 */
	private static final String VALUE = "value";
	/**
	 * Comment for <code>CHECKBOX</code>
	 */
	private static final String CHECKBOX = "checkbox";
	/**
	 * Comment for <code>INPUT_TYPE</code>
	 */
	private static final String INPUT_TYPE = "input";
	/**
	 * Comment for <code>TRUE_VALUE</code>
	 */
	/**
	 * Comment for <code>TYPE</code>
	 */
	private static final String TYPE = "type";
	private Tag formTag;
	private NodeChildrenFilter nodeChildrenFilter = new NodeChildrenFilter();
	/**
	 * 
	 * @param resource
	 * @throws ParserException 
	 */
	public ExportFormValue(String resource) throws ParserException {
		Parser parser = new Parser(resource);
		parser(parser);
	}
	/**
	 * 
	 * @param inputStream
	 * @param charSet
	 * @throws UnsupportedEncodingException
	 * @throws ParserException
	 */
	public ExportFormValue(InputStream inputStream,String charSet) throws UnsupportedEncodingException, ParserException{
		Page page = new Page(inputStream,charSet);
		Lexer lexer = new Lexer(page);
		Parser parser = new Parser(lexer);
		parser(parser);
	}
	/**
	 * 
	 * @param parser
	 * @throws ParserException
	 */
	private void parser(Parser parser) throws ParserException{
		ExportSpaceFormNodeVistor vistor = new ExportSpaceFormNodeVistor();
		parser.visitAllNodesWith(vistor);
		formTag = vistor.getFormTag();
	}
	/**
	 * 获取需要提交的数据
	 * @return
	 * @throws ParserException 
	 */
	public NameValuePair[] getFormValue() throws ParserException {
		final List<NameValuePair> list = new ArrayList<NameValuePair>();
		if(this.formTag == null){
			return list.toArray(new NameValuePair[list.size()]);
		}
		
		list.add(new NameValuePair(EXPORT_TYPE, TYPE_HTML));
		list.add(new NameValuePair(INCLUDE_COMMENTS, TRUE));
		list.add(new NameValuePair(SYNCHRONOUS, FALSE));
		list.add(new NameValuePair(CONFIRM, CONFIRM_VALUE));
		
		formTag.accept(new NodeVisitor(true,true){
			@Override
			public void visitTag(Tag tag) {
				String tagName = tag.getTagName();
				
				if(INPUT_TYPE.equalsIgnoreCase(tagName)){
					String type = tag.getAttribute(TYPE);
					String name = tag.getAttribute(NAME);					
					if(CHECKBOX.equalsIgnoreCase(type)){
						String value = tag.getAttribute(VALUE);
						if(NodeChildrenFilter.FilterValue.equals(value)){
							nodeChildrenFilter.setParent(tag.getParent());
						} else {
							if(nodeChildrenFilter.accept(tag)){								
								list.add(new NameValuePair(name,value));
							}
						}
					}else if(HIDDEN.equalsIgnoreCase(type)){
						if(ATL_TOKEN.equalsIgnoreCase(name)){
							String value = tag.getAttribute(VALUE);
							list.add(new NameValuePair(name,value));
						}
					}
				}
			}
		});
		return list.toArray(new NameValuePair[list.size()]);
	}
	
	/**
	 * 
	 * @return
	 */
	public String getFormAction(){
		if(formTag == null){
			return null;
		}
		return formTag.getAttribute("action");
	}
}
