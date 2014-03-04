/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/ExportSpaceFormNodeVistor.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-6
 *******************************************************************************/

package com.primeton.doc4wiki.impl;

import org.htmlparser.Tag;
import org.htmlparser.visitors.NodeVisitor;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 ÉÏÎç8:11:22
 */
public class ExportSpaceFormNodeVistor extends NodeVisitor {
	/**
	 * Comment for <code>NAME</code>
	 */
	private static final String NAME = "name";
	/**
	 * Comment for <code>FORM_NAME</code>
	 */
	private static final String FORM_NAME = "exportspaceform";
	/**
	 * Comment for <code>FORM_TAG</code>
	 */
	private static final String FORM_TAG = "form";
	private Tag tag;
	/**
	 * 
	 */
	public ExportSpaceFormNodeVistor() {
		super();
	}
	/*
	 * (non-Javadoc)
	 * @see org.htmlparser.visitors.NodeVisitor#visitTag(org.htmlparser.Tag)
	 */
	@Override
	public void visitTag(Tag tag) {
		String tagName = tag.getTagName();
		String name = tag.getAttribute(NAME);
		
		if(FORM_TAG.equalsIgnoreCase(tagName)
				&& FORM_NAME.equalsIgnoreCase(name)){
			this.tag = tag;
		}
	}
	/**
	 * 
	 * @return
	 */
	public Tag getFormTag(){
		return this.tag;
	}
	/*
	 * (non-Javadoc)
	 * @see org.htmlparser.visitors.NodeVisitor#shouldRecurseChildren()
	 */
	@Override
	public boolean shouldRecurseChildren() {
		return true;
	}
	/*
	 * (non-Javadoc)
	 * @see org.htmlparser.visitors.NodeVisitor#shouldRecurseSelf()
	 */
	@Override
	public boolean shouldRecurseSelf() {
		return true;
	}
}
