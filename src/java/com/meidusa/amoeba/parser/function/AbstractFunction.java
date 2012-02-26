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
package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public abstract class AbstractFunction implements Function {
	
	protected String name;
	/**
	 * 构造函数，给name赋值
	 * @param functionName
	 */
	public AbstractFunction(String functionName){
		this.name = functionName;
	}
	
	public AbstractFunction(){}
	/**
	 * 返回name的值
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设定name的值
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 把变量list中的所有元素用","隔开，并用"("和")"把这些字符串包含起来
	 * "(list[0],list[1],....,list[n-1])"
	 */
	public void toString(List<Expression> list,StringBuilder builder) {
		if(list == null){
			builder.append(getName());
			builder.append("(");
			builder.append(")");
		}else{
			int current = 0;//这个变量好像没什么用
			builder.append(getName());
			builder.append("(");
			for(Expression e:list){
				builder.append(e);
				current ++;
				if(current != list.size()){
					builder.append(",");
				}
			}
			builder.append(")");
		}
	}
	/**
	 * 返回"()"
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder();
		toString(null,builder);
		return builder.toString();
	}
}
