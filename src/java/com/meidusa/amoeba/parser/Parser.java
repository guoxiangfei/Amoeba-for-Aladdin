/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.meidusa.amoeba.parser;

import java.util.Map;

import com.meidusa.amoeba.parser.function.Function;
import com.meidusa.amoeba.parser.dbobject.Schema;
import com.meidusa.amoeba.parser.statment.Statment;

/**
 * 定义一个分析器的接口
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public interface Parser {
	/***
	 * 
	 * @return
	 * @throws ParseException
	 */
	public Statment doParse() throws ParseException;
	/***
	 * 一个schema通常是一组为了描述一类给定的XML文档而预先定好的规则。
	 * 它定义了可以在指定XML文档中出现的各个元素以及和某个元素相关的若干属性。
	 * 它同时定义了关于XML文档的结构化信息，比如哪几个元素是其他元素的子元素，子元素出现的顺序和他们的数量。
	 * 它还可以定义一个元素是否为空，能否包含文本或者属性是否有默认值
	 * @param schema
	 */
	public void setDefaultSchema(Schema schema);
	/**
	 * 
	 * @param funMap
	 */
	public void setFunctionMap(Map<String,Function> funMap);
}
