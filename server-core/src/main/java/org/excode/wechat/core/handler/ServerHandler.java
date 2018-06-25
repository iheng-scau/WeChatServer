package org.excode.wechat.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.log4j.Log4j2;

import java.net.InetAddress;
import java.util.Date;

/**
 * @author zhangdh@jpush.cn
 * @date 6/25/18
 */
@Log4j2
public class ServerHandler extends SimpleChannelInboundHandler<String>{
    private final static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush("Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        channels.add(ctx.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        log.info("msg receive:{}", msg);
        channels.forEach(channel -> {
            if (channel == ctx.channel())
                return;
            channel.writeAndFlush("{timestamp}:{msg}\r\n".replace("{timestamp}", new Date().toString()).replace("{msg}", msg));
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
