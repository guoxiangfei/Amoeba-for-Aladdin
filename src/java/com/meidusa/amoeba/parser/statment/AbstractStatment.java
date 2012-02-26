package com.meidusa.amoeba.parser.statment;


public abstract class AbstractStatment implements Statment {

	private int parameterCount;
	
	/**
	 * @return parameterCount
	 */
	public int getParameterCount() {
		return parameterCount;
	}
	/**
	 * ÉèÖÃparameterCountµÄÖµ
	 * @param count 
	 */
	public void setParameterCount(int count){
		this.parameterCount = count;
	}
}
