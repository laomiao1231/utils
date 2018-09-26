package com.miao.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSessionConfig;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class PhoneServer {
    private static Logger logger = Logger.getLogger(PhoneServer.class);
    private static int PORT = 3005;

    public static void main(String[] args) {
        IoAcceptor acceptor = null;
        try {
            acceptor = new NioSocketAcceptor();
            acceptor.getFilterChain().addLast(
                    "codec",
                    new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                            LineDelimiter.WINDOWS.getValue(),
                            LineDelimiter.WINDOWS.getValue()))
            );
            IoSessionConfig cfg = acceptor.getSessionConfig();
            cfg.setIdleTime(IdleStatus.BOTH_IDLE, 30);
            acceptor.setHandler(new PhoneServerHandler());
            acceptor.bind(new InetSocketAddress(PORT));
            logger.info("服务端启动成功...     端口号为：" + PORT);
        } catch (Exception e) {
            logger.error("服务端启动异常....", e);
            e.printStackTrace();
        }
    }
}
