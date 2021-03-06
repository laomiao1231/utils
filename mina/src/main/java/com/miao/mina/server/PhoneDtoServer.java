package com.miao.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;

public class PhoneDtoServer {
    private static Logger logger = Logger.getLogger(PhoneDtoServer.class);
    private static int PORT = 3005;

    public static void main(String[] args) {
        IoAcceptor acceptor = null;
        try{
            acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(new ObjectSerializationCodecFactory())
            );
            IoSessionConfig cfg = acceptor.getSessionConfig();
            cfg.setIdleTime(IdleStatus.BOTH_IDLE, 10);
            acceptor.setHandler(new PhoneDtoServerHandler());
            acceptor.bind(new InetSocketAddress(PORT));
            logger.info("服务端启动成功...     端口号为：" + PORT);
        } catch (Exception e) {
            logger.error("服务端启动异常....", e);
            e.printStackTrace();
        }
    }
}
