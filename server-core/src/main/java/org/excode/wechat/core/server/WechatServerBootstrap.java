package org.excode.wechat.core.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import lombok.extern.log4j.Log4j2;
import org.excode.wechat.core.conf.WechatServerConfiguration;
import org.excode.wechat.core.handler.ServerHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhangdh@jpush.cn
 * @date 6/25/2018
 */
@Log4j2
@Component
public class WechatServerBootstrap implements ApplicationRunner {
    @Autowired
    private WechatServerConfiguration configuration;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        SslContext sslContext;
        if (configuration.isSsl()) {
            SelfSignedCertificate certificate = new SelfSignedCertificate();
            sslContext = SslContextBuilder.forServer(certificate.certificate(), certificate.privateKey()).build();
        } else {
            sslContext = null;
        }

        EventLoopGroup bossGroup= new NioEventLoopGroup(1);
        EventLoopGroup workerGroup= new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap= new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline=socketChannel.pipeline();
                            if(sslContext !=null){
                                pipeline.addLast(sslContext.newHandler(socketChannel.alloc()));
                            }
                            // Add the text line codec combination first,
                            pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
                            // the encoder and decoder are static as these are sharable
                            pipeline.addLast(new StringDecoder());
                            pipeline.addLast(new StringEncoder());
                            pipeline.addLast(new ServerHandler());
                        }
                    });

            ChannelFuture future=bootstrap.bind(configuration.getPort()).sync();
            log.info("server started at port:{}", configuration.getPort());
            future.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
