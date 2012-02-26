package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;
import com.meidusa.amoeba.sqljep.ParseException;

public class Instr extends AbstractFunction {

	/**
	 * 把list中的第一个元素list[0]传入参数parameters，进行计算得到String类型变量param1
	 * 把list中的第一个元素list[1]传入参数parameters，进行计算得到String类型变量param2
	 * 返回param2在param1中的位置+1
	 */
	@SuppressWarnings("unchecked")
    public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		if(list.size() != 2){
			return null;
		}
		
		String param1 = (String)list.get(0).evaluate(parameters);
		String param2 = (String)list.get(1).evaluate(parameters);
		
		if(param1 == null || param2 == null){
			return 0;
		}
		
		return param1.indexOf(param2) +1;
	}

}
