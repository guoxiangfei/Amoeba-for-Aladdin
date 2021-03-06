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
package com.meidusa.amoeba.mysql.filter;

import com.meidusa.amoeba.mysql.filter.FilterInvocation.Result;

/**
 * 
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public abstract class AbstractIOFilter implements IOFilter {
	
	private byte[] resultBuffer;
	public void startFiltrate(){
	}
	
	protected void setResultBuffer(byte[] resultBuffer) {
		this.resultBuffer = resultBuffer;
	}

	public void finishFiltrate(){
		resultBuffer = null;
	}
	
	public Result doFilter(PacketFilterInvocation invocation) {
		return packetFilter(invocation.getByteBuffer());
	}

	protected abstract Result packetFilter(byte[] packetBuffer);
	
	public byte[] getFiltedResult(){
		return resultBuffer;
	}
}
