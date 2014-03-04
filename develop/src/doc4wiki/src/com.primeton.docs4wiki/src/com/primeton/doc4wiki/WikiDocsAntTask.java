/*******************************************************************************
 * $Header: /cvsroot/BPS6/develop/build/dailybuild/doc4wiki/src/com.primeton.docs4wiki/src/com/primeton/doc4wiki/WikiDocsAntTask.java,v 1.1 2013/06/06 01:44:58 liuxiang Exp $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

import com.primeton.doc4wiki.util.WikiInfo;

/**
 * 
 * @author liuxiang(mailto:liuxiang@primeton.com)
 * 2013-6-6 上午8:11:03
 */
public class WikiDocsAntTask extends Task {
	private Vector<Property> properties = new Vector<Property>();
	/**
	 * 
	 */
	public WikiDocsAntTask() {
		super();
	}
	
	/* (non-Javadoc)
	 * @see org.apache.tools.ant.Task#execute()
	 */
	@Override
	public void execute() throws BuildException {
		Map<String, String> map = new HashMap<String, String>();
		if(this.properties.size() > 0){			
			for (Iterator<Property> iterator = this.properties.iterator(); iterator.hasNext();) {
				Property property = iterator.next();
				map.put(property.getName(), property.getValue());
			}
			Properties properties = new Properties();
			properties.putAll(map);
			WikiInfo.setProperties(properties);
		}
		
		boolean success = new WikiDocsRun().excute();
		if(!success){
			throw new BuildException("未能执行成功");
		}
	}
	/**
	 * 
	 * @return
	 */
	public Property createProperty(){
		Property property = new Property();
		this.properties.add(property);
		return property;
	}
}
