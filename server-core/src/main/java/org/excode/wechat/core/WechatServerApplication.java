package org.excode.wechat.core;

import lombok.extern.log4j.Log4j2;
import org.excode.wechat.core.conf.WechatServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author zhangdh@jpush.cn
 * @date 6/25/18
 */
@Log4j2
@SpringBootApplication
@EnableConfigurationProperties(WechatServerConfiguration.class)
public class WechatServerApplication {

    public static void main(String[] args){
        SpringApplication.run(WechatServerApplication.class,args);
    }
}
