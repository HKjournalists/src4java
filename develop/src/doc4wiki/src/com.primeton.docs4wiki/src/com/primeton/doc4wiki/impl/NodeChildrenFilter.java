/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/impl/NodeChildrenFilter.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
 * $Revision: 1.1 $
 * $Date: 2013/06/06 01:44:58 $
 *
 *==============================================================================
 *
 * Copyright (c) 2001-2006 Primeton Technologies, Ltd.
 * All rights reserved.
 * 
 * Created on 2009-5-18
 *******************************************************************************/

package com.primeton.doc4wiki.impl;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.tags.Html;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 ÉÏÎç8:11:36
 */
public class NodeChildrenFilter {
	private Node parent;
	public static final String FilterValue = "5603369";
	/**
	 * 
	 */
	public NodeChildrenFilter() {
		super();
	}
	/**
	 * 
	 * @param tag
	 * @return
	 */
	public boolean accept(Tag tag){
		if(parent != null){
			Node node = tag;
			while(node != null && !(node instanceof Html)){
				if(node.equals(parent)){
					return false;
				} else {
					node = node.getParent();
				}
			}
		}
		return true;
	}
	/**
	 * @return Returns the parent.
	 */
	public Node getParent() {
		return parent;
	}
	/**
	 * @param parent The parent to set.
	 */
	public void setParent(Node parent) {
		this.parent = parent;
	}
}
