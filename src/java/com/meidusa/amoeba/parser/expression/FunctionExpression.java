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

import java.util.ArrayList;
import java.util.List;

import com.meidusa.amoeba.parser.function.Function;
import com.meidusa.amoeba.parser.function.RealtimeCalculator;
import com.meidusa.amoeba.sqljep.ParseException;

/**
 * 函数表达式
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class FunctionExpression extends Expression {

    private Function         function;
    private List<Expression> argList = new ArrayList<Expression>();
    /**
     * 得到argList（List<Expression>类型）
     * @return
     */
    public List<Expression> getArgList() {
        return argList;
    }
    /**
     * 设定argList（List<Expression>类型）
     * @param argList
     */
    public void setArgList(List<Expression> argList) {
        this.argList = argList;
    }

    /**
     * 将expression添加到argList中
     * 
     * @param expression
     */
    public void addArgExpression(Expression expression) {
        argList.add(expression);
    }
    
    /**
     * 得到变量function的值（Function类型）
     * @return
     */
    public Function getFunction() {
        return function;
    }

    /**
     * 设定function的值（Function类型）
     * @param function
     */
    public void setFunction(Function function) {
        this.function = function;
    }
    /**
     * 表达式是否实时计算
     */
    public boolean isRealtime() {
        boolean isRealTime = (function instanceof RealtimeCalculator);
        if (isRealTime) return true;
        for (Expression e : argList) {
            if (e.isRealtime()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回function计算的结果
     */
    @SuppressWarnings("unchecked")
    @Override
    public Comparable evaluate(Object[] parameters) {
        try {
            return function.evaluate(argList, parameters);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 表达式取反，实际上改表达式并没有做任何变化
     */
    @Override
    public Expression reverse() {
        return this;
    }
    /**
     * ????
     */
    @Override
    protected void toString(StringBuilder builder) {
        function.toString(argList, builder);
    }
}
