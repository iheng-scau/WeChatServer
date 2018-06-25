package org.excode.wechat.server.test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class Client extends Thread{
	public static final Logger log=Logger.getLogger(Client.class);
	public static final String HOST_IP="169.254.24.42";
	public static final int PORT=8888;
	
	public void run(){
		log.info("client lauched");
		try {
			Socket socket=new Socket(HOST_IP,PORT);
			DataInputStream in=new DataInputStream(socket.getInputStream());
			DataOutputStream out=new DataOutputStream(socket.getOutputStream());
			
			Scanner scanner=new Scanner(System.in);
			String msg="";
			
			while(true){
				msg=scanner.nextLine();
				out.writeUTF(msg);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
