/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/RootNodeVisitor.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
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

package com.primeton.doc4wiki.impl;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

import com.primeton.doc4wiki.util.WikiInfo;


/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 ÉÏÎç8:11:41
 */
public class RootNodeVisitor extends NodeVisitor {
	private static final String A_HREF = "a";
	private static final String LI = "li";
	private static final String HREF = "href";
	/**
	 * Comment for <code>liTag</code>
	 */
	private Tag liTag;
	
	/**
	 * @param recurseChildren
	 * @param recurseSelf
	 */
	public RootNodeVisitor(boolean recurseChildren, boolean recurseSelf) {
		super(recurseChildren, recurseSelf);
	}
	
	@Override
	public void visitTag(Tag tag) {
		if(liTag != null){
			return;
		}
		if(!A_HREF.equalsIgnoreCase(tag.getTagName())){
			return;
		}
		String href = tag.getAttribute(HREF);
		if(WikiInfo.getInstance().getRootIndex().equals(href)){
			Node node = tag.getParent();
			if(node instanceof Tag){
				String name = ((Tag)node).getTagName();
				if(name.equalsIgnoreCase(LI)){
					liTag = (Tag)node;
				}
			}
		}
	}
	/**
	 * 
	 * @return
	 */
	public Tag getRootNode(){
		return this.liTag;
	}

}
