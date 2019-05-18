package com.m.utils;

import com.jcraft.jsch.*;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource(value = "classpath:sftp.properties")
@ConfigurationProperties(prefix = "sftp.client")
public class SftpUtil {
    private String host;

    private Integer port;

    private String protocol;

    private String username;

    private String password;

    private String root;

    private String privateKey;

    private String sessionStrictHostKeyChecking;

    private Integer sessionConnectTimeout;

    private Integer channelConnectedTimeout;

    // 设置第一次登陆的时候提示，可选值：(ask | yes | no)
    private static final String SESSION_CONFIG_STRICT_HOST_KEY_CHECKING = "StrictHostKeyChecking";

    /**
     * 创建sftp连接
     * @return
     * @throws Exception
     */
    public ChannelSftp connectByKey() throws Exception {
        JSch jSch = new JSch();
        if (StringUtils.isNoneBlank(privateKey)) {
            jSch.addIdentity(privateKey);
        }
        Session session = jSch.getSession(username, host, port);
        session.setConfig(SESSION_CONFIG_STRICT_HOST_KEY_CHECKING, sessionStrictHostKeyChecking);
        // 设置登录超时
        session.connect(sessionConnectTimeout);
        // 创建sftp信道
        Channel channel = session.openChannel(protocol);
        channel.connect(channelConnectedTimeout);
        return (ChannelSftp) channel;
    }

    /**
     * 关闭sftp连接
     * @param sftp
     */
    public void disconnect(ChannelSftp sftp) {
        try {
            if (sftp != null) {
                if (sftp.isConnected()) {
                    sftp.disconnect();
                }
                if (null != sftp.getSession()) {
                    sftp.getSession().disconnect();
                }
            }
        } catch (JSchException e) {
            e.printStackTrace();
        }
    }
}
