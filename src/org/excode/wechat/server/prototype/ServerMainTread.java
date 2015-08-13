package org.excode.wechat.server.prototype;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ServerMainTread extends Thread{
	ServerSocket serverSocket;
	public static final int PORT=80;
	public static Logger log=Logger.getLogger(ServerMainTread.class);
	
	Vector<ClientThread> clients;
	Vector<Object> messages;
	
	BroadCast broadcast;
	
	String ip;
	InetAddress ip_address=null;
	
	public ServerMainThread(){
		//init the main thread
		clients=new Vector<ClientThread>();
		messages=new Vector<Object>();
		
		try{
			serverSocket=new ServerSocket(PORT);
			ip_address=InetAddress.getLocalHost();
			ip=ip_address.getHostAddress();
			log.info("server address:"+ip+",port:"+String.valueOf(serverSocket.getLocalPort()));
			
			broadcast=new BroadCast(this);
			broadcast.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			try{
				serverSocket.accept();
			}
		}
	}
}
