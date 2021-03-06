/*****************************************************************************
      SQLJEP - Java SQL Expression Parser 0.2
      November 1 2006
         (c) Copyright 2006, Alexey Gaidukov
      SQLJEP Author: Alexey Gaidukov

      SQLJEP is based on JEP 2.24 (http://www.singularsys.com/jep/)
           (c) Copyright 2002, Nathan Funk
 
      See LICENSE.txt for license information.
*****************************************************************************/

package com.meidusa.amoeba.sqljep.function;

import java.math.BigDecimal;

import com.meidusa.amoeba.sqljep.function.PostfixCommand;
import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;
/**
 * 求绝对值类
 * @author Li Hui
 *
 */
public class Abs extends PostfixCommand {
	/**
	 * 返回参数的个数，因为这个类是用于求绝对值，所以参数个数为1
	 */
	final public int getNumberOfParameters() {
		return 1;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}

	/**
	 * 根据参数param的类型，求param的绝对值
	 * @param param
	 * @return
	 * @throws ParseException
	 */
	public static Comparable<?>  abs(Comparable<?>  param) throws ParseException {
		if (param == null) {
			return null;
		}
		if (param instanceof String) {
			param = parse((String)param);
		}
		if (param instanceof BigDecimal) {		// BigInteger is not supported
			return ((BigDecimal)param).abs();
		}
		if (param instanceof Double || param instanceof Float) {
			return new Double(Math.abs(((Number)param).doubleValue()));
		}
		if (param instanceof Number) {		// Long, Integer, Short, Byte 
			return new Long(Math.abs(((Number)param).longValue()));
		}
		throw new ParseException(WRONG_TYPE+" abs("+param.getClass()+")");
	}

	public Comparable<?> getResult(Comparable<?>... comparables) throws ParseException {
		return abs(comparables[0]);
	}
}

