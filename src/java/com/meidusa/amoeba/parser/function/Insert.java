package com.meidusa.amoeba.parser.function;

import java.util.List;

import com.meidusa.amoeba.parser.expression.Expression;
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * ���뺯����
 * ��Ҫ�����ַ����Ĳ���
 * @author Li Hui
 *
 */
public class Insert extends AbstractFunction {

	/**
	 * ��list�еĵ�һ��Ԫ��list[0]�������parameters�����м���õ�String���ͱ���param1
	 * ��list�еĵ�һ��Ԫ��list[1]�������parameters�����м���õ�Long���ͱ���param2
	 * ��list�еĵ�һ��Ԫ��list[2]�������parameters�����м���õ�Long���ͱ���param3
	 * ��list�еĵ�һ��Ԫ��list[3]�������parameters�����м���õ�String���ͱ���param4
	 * ��param1�еĴ�param2��ʼ������param3��Ԫ���滻��param4
	 */
	@SuppressWarnings("unchecked")
    public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		if(list.size() != 4){
			return null;
		}
		String param1 = (String)list.get(0).evaluate(parameters);
		Long param2 = (Long)list.get(1).evaluate(parameters);
		Long param3 = (Long)list.get(2).evaluate(parameters);
		String param4 = (String)list.get(3).evaluate(parameters);
		if(param1 == null){
			return null;
		}
		
		return insert(param1,param2.intValue(),param3.intValue(),param4);
	}
	
	/**
	 * ���ַ���param1��pos��ʼ������length��Ԫ���滻��newStr
	 * @author Li Hui
	 * @param param1
	 * @param pos
	 * @param length
	 * @param newStr
	 * @return
	 */
	public static String insert(String param1,int pos, int length,String newStr){
		if(param1 == null){
			return null;
		}
		
		if(pos<=0) return param1;
		
		pos = pos>param1.length()?param1.length():pos;
		//length = length >= param1.length() - pos-1?param1.length() - pos-1:length;
		String str1 = param1.substring(0,pos-1);
		String str2 = null;
		if(length + pos -1 <= param1.length()){
			str2 = param1.substring(pos+length-1,param1.length());
		}
		StringBuilder builder = new StringBuilder();
		builder.append(str1);
		builder.append(newStr);
		if(str2 != null){
			builder.append(str2);
		}
		return builder.toString();
	}

	public static void main(String args[]){
		System.out.println(insert("Quadratic", 3, 3, "What"));
	}
}
