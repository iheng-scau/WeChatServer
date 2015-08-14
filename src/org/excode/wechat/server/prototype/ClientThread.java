package org.excode.wechat.server.prototype;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import org.apache.log4j.Logger;

public class ClientThread extends Thread {
	public static final Logger log=Logger.getLogger(ClientThread.class);
	
	Socket clientSocket;
	
	DataInputStream in=null;
	DataOutputStream out=null;
	
	ServerMainThread serverThread;
	
	public ClientThread(Socket socket,ServerMainThread thread){
		this.clientSocket=socket;
		this.serverThread=thread;
		
		try{
			in=new DataInputStream(clientSocket.getInputStream());
			out=new DataOutputStream(clientSocket.getOutputStream());
		}catch(Exception e){
			log.info("exception caught");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try{
				String msg=in.readUTF();
				log.info("server recieve:"+msg);
				synchronized(serverThread.messages){
					if(msg!=null){
						serverThread.messages.addElement(msg);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	
}
