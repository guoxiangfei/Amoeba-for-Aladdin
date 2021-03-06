/**
 * <pre>
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 * </pre>
 */
package com.meidusa.amoeba.mysql.net.packet;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.mysql.io.MySqlPacketConstant;
import com.meidusa.amoeba.net.packet.AbstractPacketBuffer;
import com.meidusa.amoeba.util.StringFillFormat;

/**
 * 数据包 抽象类
 * 猜测：这个应该是用于Aladdin Proxy Server与MySQL之间的数据包
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 * @author hexianmao
 */
public class AbstractPacket extends com.meidusa.amoeba.net.packet.AbstractPacket implements MySqlPacketConstant {

    protected static Logger logger = Logger.getLogger(AbstractPacket.class);

    /** 只表示数据长度，不包含包头长度 */
    public int              packetLength;

    /** 当前的数据包序列数 */
    public byte             packetId;

    @Override
    protected void init(AbstractPacketBuffer buffer) {
        buffer.setPosition(0);
        packetLength = (buffer.readByte() & 0xff) + ((buffer.readByte() & 0xff) << 8) + ((buffer.readByte() & 0xff) << 16);
        packetId = buffer.readByte();
    }

    /**
     * 估算packet的大小，估算的太大浪费内存，估算的太小会影响性能
     * 这里假设数据包头的长度是4+1
     */
    protected int calculatePacketSize() {
        return HEADER_SIZE + 1;
    }

    @Override
    protected void write2Buffer(AbstractPacketBuffer buffer) throws UnsupportedEncodingException {
        // TODO Auto-generated method stub
    }

    @Override
    protected void afterPacketWritten(AbstractPacketBuffer buffer) {
        int position = buffer.getPosition();
        packetLength = position - HEADER_SIZE;
        buffer.setPosition(0);
        buffer.writeByte((byte) (packetLength & 0xff));
        buffer.writeByte((byte) (packetLength >>> 8));
        buffer.writeByte((byte) (packetLength >>> 16));
        buffer.writeByte((byte) (packetId & 0xff));// packet id
        buffer.setPosition(position);
    }

    @Override
    protected Class<? extends AbstractPacketBuffer> getPacketBufferClass() {
        return MysqlPacketBuffer.class;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("[Length=").append(StringFillFormat.format(packetLength, 4));
        s.append(",PacketId=").append(StringFillFormat.format(packetId, 2)).append("]");
        return s.toString();
    }

}
