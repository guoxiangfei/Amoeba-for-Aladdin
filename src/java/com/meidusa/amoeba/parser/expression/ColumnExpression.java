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
package com.meidusa.amoeba.parser.expression;

import com.meidusa.amoeba.parser.dbobject.Column;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class ColumnExpression extends Expression {
	private Column column;
	private ComparisonExpression expression;
	
	//得到列？？
	public Column getColumn() {
		return column;
	}
	/**
	 * 给列column赋值
	 * @param column
	 */
	public void setColumn(Column column) {
		this.column = column;
	}
	/**
	 * 给变量expression赋值
	 * @param expression
	 */
	public void setExpression(ComparisonExpression expression){
		this.expression = expression;
	}
	
	/***
	 * 表达式取反
	 */
	public Expression reverse(){
		expression.reverse();
		return this;
	}
	/**
	 * 得到表达式(expression)
	 * @return
	 */
	public Expression getExpression(){
		return expression;
	}
	/**
	 * 是否需要实时计算
	 */
	public boolean isRealtime(){
		return expression.isRealtime();
	}
	/* 表达式计算
	 * (non-Javadoc)
	 * @see com.meidusa.amoeba.parser.expression.Expression#evaluate(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Comparable evaluate(Object[] parameters) {
		return expression.evaluate(parameters);
	}
	
	/**
	 * 转化成字符串
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder();
		toString(builder);
		return builder.toString();
	}

	@Override
	protected void toString(StringBuilder builder) {
		if(column == null){
			return;
		}else{
			builder.append(" ");
			builder.append(column.getSql());
			if(expression != null){
				expression.toString(builder);
			}
		}
	}

}
