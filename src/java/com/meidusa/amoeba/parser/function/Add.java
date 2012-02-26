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
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class Add extends AbstractFunction {

	/**
	 * 取list中的第 0个元素和list中的第1个元素，如果这两个元素都可以计算，那么？？？
	 */
	@SuppressWarnings("unchecked")
	public Comparable evaluate(List<Expression> list,Object[] parameters) throws ParseException {
		if(list.size()==2){
			Expression e1 = list.get(0);
			Expression e2 = list.get(1);
			if(e1.canEvaluate() && e2.canEvaluate()){
				//????
				Comparable comparable = com.meidusa.amoeba.sqljep.function.Add.add(e1.evaluate(parameters),e2.evaluate(parameters));
				return comparable;
			}
		}
		return null;
	}
	/**
	 * 对list中的第0个元素、" + "、第二个元素加入到builder中，表示这两个元素执行加法运算
	 * "list[0] + list[1]"
	 */
	public void toString(List<Expression> list,StringBuilder builder) {
		builder.append(list.get(0).toString());
		builder.append(" ");
		builder.append(getName());
		builder.append(" ");
		builder.append(list.get(1).toString());
	}
	/**
	 * 返回"+"
	 */
	public String getName() {
		return "+";
	}

}
