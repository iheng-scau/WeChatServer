package org.excode.wechat.server.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.excode.wechat.server.utils.Serializer;
import org.iheng.wechat.entities.WeChatMessage;

public class NIOServer {
	private int port=8001;
	private Charset cs=Charset.forName("gb2312");
	
	private static ByteBuffer sBuffer=ByteBuffer.allocate(1024);
	private static ByteBuffer rBuffer=ByteBuffer.allocate(1024);
	
	private Map<String,SocketChannel> clientMap=new HashMap<String,SocketChannel>();
	private static Selector selector;
	
	public static void main(String[] args){
		NIOServer server=new NIOServer(8001);
		server.listen();
	}
	
	public NIOServer(int port){
		this.port=port;
		try{
			init();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	private void init() throws IOException{
   		ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
		serverSocketChannel.configureBlocking(false);
		serverSocketChannel.bind(new InetSocketAddress(port));
		selector=Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		System.out.println("server start on port:"+port);
	}
	
	private void listen(){
		while(true){
			try{
				selector.select();
				Set<SelectionKey> selectionKeys=selector.selectedKeys();
				for(SelectionKey key:selectionKeys){
					handleKey(key);
				}
				
				selectionKeys.clear();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	private void handleKey(SelectionKey key) throws Exception{
		ServerSocketChannel serverChannel=null;
		SocketChannel clientChannel=null;
		String rContent=null;
		int count=0;
		if(key.isAcceptable()){
			serverChannel=(ServerSocketChannel)key.channel();
			clientChannel=serverChannel.accept();
			clientChannel.configureBlocking(false);
			clientChannel.register(selector, SelectionKey.OP_READ);
		}else if(key.isReadable()){
			clientChannel=(SocketChannel)key.channel();
			rBuffer.clear();
			count=clientChannel.read(rBuffer);
			if(count>0){
				rBuffer.flip();
				byte[] bytes=rBuffer.array();
				WeChatMessage msg=(WeChatMessage)Serializer.deserialize(bytes);
				rContent=msg.getMsgContent();
				System.out.println(clientChannel.toString()+":"+rContent);
				dispatch(clientChannel,rContent);
				clientChannel=(SocketChannel)key.channel();
				clientChannel.register(selector, SelectionKey.OP_READ);
			}
		}
	}
	
	private void dispatch(SocketChannel client,String content) throws IOException{
		Socket socket=client.socket();
		String name="["+socket.getInetAddress().toString().substring(1)+":"+Integer.toHexString(client.hashCode())+"]";
		if(!clientMap.isEmpty()){
			for(Map.Entry<String, SocketChannel> entry : clientMap.entrySet()){
				SocketChannel temp=entry.getValue();
				if(!client.equals(temp)){
					sBuffer.clear();
					sBuffer.put((name+":"+content).getBytes());
					sBuffer.flip();
					temp.write(sBuffer);
				}
			}
		}
		clientMap.put(name,client);
	}
}
