package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;
import com.meidusa.amoeba.sqljep.ParseException;

public class Instr extends AbstractFunction {

	/**
	 * ��list�еĵ�һ��Ԫ��list[0]�������parameters�����м���õ�String���ͱ���param1
	 * ��list�еĵ�һ��Ԫ��list[1]�������parameters�����м���õ�String���ͱ���param2
	 * ����param2��param1�е�λ��+1
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
