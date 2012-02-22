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
package com.meidusa.amoeba.parser.dbobject;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class Column implements DBObjectBase {
	
	private Table table;
	private String name;
	/***
	 * 得到表（table）的值
	 * @return
	 */
	public Table getTable() {
		return table;
	}
	/**
	 * 给变量table赋值
	 * @param table
	 */
	public void setTable(Table table) {
		this.table = table;
	}
	/**
	 * 得到name的值
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**
	 * 给变量name赋值
	 * @param name
	 */
	public void setName(String name) {
		this.name = name.toUpperCase();
	}
	/***
	 * 
	 */
	public String getSql() {
		return (table == null?name:table.getSql()+"."+name);
	}
	
	/**
	 * 判断两个表名是否相同，不管大小写
	 */
	public boolean equals(Object o){
		if(o instanceof Column){
			Column other = (Column)o;
			if(table.equals(other.getTable()) && name.equalsIgnoreCase(other.getName())){
				return true;
			}
		}
		return false;
	}
	
	/***
	 * ？？？
	 * 目前不知道这个hashCode有什么用，还有前面为什么加311
	 */
	public int hashCode(){
		return 311+ (name==null?0:name.hashCode())+(table == null?0:table.hashCode());
	}

	public String toString(){
		return getSql();
	}
}
