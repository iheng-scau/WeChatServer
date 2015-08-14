package org.excode.wechat.server.test;

import org.excode.wechat.server.prototype.ServerMainThread;

public class RunTest {
	public static void main(String[] args) throws InterruptedException{
		new ServerMainThread().run();
	}
}
