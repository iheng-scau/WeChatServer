package org.excode.wechat.core.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author zhangdh@jpush.cn
 * @date 6/25/18
 */
@Data
@ConfigurationProperties(prefix = "wechat.server")
public class WechatServerConfiguration {
    private int port = 8888;
    private boolean ssl = false;
}
