package org.excode.wechat.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

/**
 * 用于传出对象的序列化和反序列化
 * @author iheng
 * @version 2016-01-08
 */
public class Serializer {
	
	/**
	 * 对传入的对象进行序列化为字节数组
	 * @param object
	 * @return
	 * @throws IOException
	 */
	public static byte[] serialize(Object object) throws IOException{
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		ObjectOutputStream oos=new ObjectOutputStream(baos);
		oos.writeObject(object);
		return baos.toByteArray();
	}
	
	/**
	 * 对传入的字节数组进行反序列化
	 * @param bytes
	 * @return
	 * @throws Exception
	 */
	public static Object deserialize(byte[] bytes) throws Exception{
		ByteArrayInputStream bias=new ByteArrayInputStream(bytes);
		ObjectInputStream ois=new ObjectInputStream(bias);
		return ois.readObject();
	}
}
