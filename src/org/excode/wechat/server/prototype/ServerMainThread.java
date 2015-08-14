package org.excode.wechat.server.prototype;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import org.apache.log4j.Logger;

public class ServerMainThread extends Thread{
	ServerSocket serverSocket;
	public static final int PORT=80;
	public static Logger log=Logger.getLogger(ServerMainThread.class);
	
	Vector<ClientThread> clients;
	Vector<Object> messages;
	
	Broadcast broadcast;
	
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
			
			broadcast=new Broadcast(this);
			broadcast.start();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			try{
				log.info("server main thread running");
				Socket socket=serverSocket.accept();
				log.info(socket.getInetAddress().getHostAddress());
				ClientThread clientThread=new ClientThread(socket,this);
				clientThread.start();
				if(socket!=null){
					clients.addElement(clientThread);
				}
			}catch(Exception e){
				log.info("exception caught"+e);
				log.info("establish connection failed");
				System.exit(0);
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
		serverSocket.close();
	}
	
}

