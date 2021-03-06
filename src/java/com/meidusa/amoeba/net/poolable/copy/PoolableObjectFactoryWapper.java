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
package com.meidusa.amoeba.net.poolable.copy;

/**
 * 采用该factory Wrapper，则object 必须实现{@link PoolableObject}
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 */
public class PoolableObjectFactoryWapper implements PoolableObjectFactory{
	private PoolableObjectFactory factory;
	private ObjectPool pool;
	public PoolableObjectFactoryWapper(ObjectPool pool,PoolableObjectFactory factory){
		this.factory = factory;
		this.pool = pool;
	}
	
	public void activateObject(Object obj) throws Exception {
		PoolableObject object = (PoolableObject)obj;
		object.setActive(true);
		factory.activateObject(obj);
	}
	
	public void destroyObject(Object obj) throws Exception {
		PoolableObject object = (PoolableObject)obj;
		if(object.getObjectPool() != null){
			object.setObjectPool(null);
		}
		factory.destroyObject(obj);
	}
	
	/**
	 * 采用该factory Wrapper，则object 必须实现PoolableObject
	 */
	public Object makeObject() throws Exception {
		PoolableObject object = (PoolableObject)factory.makeObject();
		object.setObjectPool(pool);
		return object;
	}
	
	public void passivateObject(Object obj) throws Exception {
		PoolableObject object = (PoolableObject)obj;
		object.setActive(false);
		factory.passivateObject(obj);
	}
	public boolean validateObject(Object obj) {
		return factory.validateObject(obj);
	}
}
