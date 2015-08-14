package org.excode.wechat.server.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

public class Client extends Thread{
	public static final Logger log=Logger.getLogger(Client.class);
	public static final String HOST_IP="192.168.56.1";
	public static final int PORT=80;
	
	public void run(){
		log.info("client lauched");
		try {
			Socket socket=new Socket(HOST_IP,PORT);
			DataInputStream in=new DataInputStream(socket.getInputStream());
			DataOutputStream out=new DataOutputStream(socket.getOutputStream());
			
			out.writeUTF("text msg");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
