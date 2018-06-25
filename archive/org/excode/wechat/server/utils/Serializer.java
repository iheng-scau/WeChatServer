package org.excode.wechat.server.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

/**
 * ���ڴ�����������л��ͷ����л�
 * @author iheng
 * @version 2016-01-08
 */
public class Serializer {
	
	/**
	 * �Դ���Ķ���������л�Ϊ�ֽ�����
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
	 * �Դ�����ֽ�������з����л�
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
