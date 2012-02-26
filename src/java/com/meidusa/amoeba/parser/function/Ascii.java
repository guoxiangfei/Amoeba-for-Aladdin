package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;
import com.meidusa.amoeba.sqljep.ParseException;
import com.meidusa.amoeba.util.StringUtil;

public class Ascii extends AbstractFunction {

	/**
	 * 把parameters传给list的第0个元素进行计算，返回得到的结构的第一个元素对应的Ascii值
	 */
	@SuppressWarnings("unchecked")
    public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		if(list.size()==0){
			return null;
		}
			
		Comparable param = list.get(0).evaluate(parameters);
		if(param == null){
			return null;
		}
		String str = String.valueOf(param);
		if(StringUtil.isEmpty(str)){
			return 0;
		}else{
		}
		return (int)str.charAt(0);
	}
}
