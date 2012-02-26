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

import java.sql.Timestamp;
import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 * 
 */
public class Now extends AbstractFunction implements RealtimeCalculator {
	/*
	 * 返回当前的时间，精确到微妙，譬如 2012-02-26 18:43:46.187
	 * (non-Javadoc)
	 * @see com.meidusa.amoeba.parser.function.Function#evaluate(java.util.List, java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public Comparable evaluate(List<Expression> list, Object[] parameters) {
		return new Timestamp(System.currentTimeMillis());
	}

//	public static void main(String args[]) {
//		Comparable num = new Timestamp(System.currentTimeMillis());
//		System.out.println(num.toString());
//	}
}
