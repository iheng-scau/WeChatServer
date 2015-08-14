package org.excode.wechat.server.prototype;

public class Broadcast extends Thread {
	ClientThread clientThread;
	ServerMainThread serverThread;
	String str;
	
	public Broadcast(ServerMainThread thread){
		this.serverThread=thread;
	}

	@Override
	public void run() {
	// TODO Auto-generated method stub
		while(true){
			try{
				Thread.sleep(200);			
			}catch(Exception e){
				e.printStackTrace();
			}
	
			synchronized(serverThread.messages){
				if(serverThread.messages.isEmpty()){
					continue;
				}
				str=(String)serverThread.messages.firstElement();
			}
			
			synchronized(serverThread.clients){
				for(ClientThread thread:serverThread.clients){
					try{
						thread.out.writeUTF(str);
					}catch(Exception e){
						e.printStackTrace();
					}
				}
				this.serverThread.messages.removeElement(str);
			}
		}	
	}
}
