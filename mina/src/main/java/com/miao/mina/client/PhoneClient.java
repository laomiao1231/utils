package com.miao.mina.client;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class PhoneClient {
    private static Logger logger = Logger.getLogger(PhoneClient.class);
    private static String HOST = "127.0.0.1";
    private static int PORT = 3005;

    public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(20000);
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"),
                        LineDelimiter.WINDOWS.getValue(),
                        LineDelimiter.WINDOWS.getValue()))
        );
        connector.setHandler(new PhoneClientHandler());
        IoSession session = null;
        try {
            ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
            future.awaitUninterruptibly();
            session = future.getSession();

            String sendPhone = "13681803609"; // 当前发送人的手机号码
            String receivePhone = "13721427169"; // 接收人手机号码
            String message = "测试发送短信，这个是短信信息哦，当然长度是有限制的哦....";
            String msg = sendPhone + ";" + receivePhone + ";" + message;
            session.write(msg);
        } catch (Exception e) {
            logger.error("客户端链接异常...", e);
        }
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
