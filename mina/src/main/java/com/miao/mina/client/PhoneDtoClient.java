package com.miao.mina.client;

import com.miao.mina.dto.PhoneMessageDto;
import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class PhoneDtoClient {
    private static Logger logger = Logger.getLogger(PhoneDtoClient.class);
    private static String HOST = "127.0.0.1";
    private static int PORT = 3005;

    public static void main(String[] args) {
        IoConnector connector = new NioSocketConnector();
        connector.setConnectTimeoutMillis(20000);
        connector.getFilterChain().addLast(
                "codec",
                new ProtocolCodecFilter(new ObjectSerializationCodecFactory())
        );
        connector.setHandler(new PhoneDtoClientHandler());
        IoSession session = null;
        try{
            ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
            future.awaitUninterruptibly();
            session = future.getSession();

            PhoneMessageDto phoneMessageDto = new PhoneMessageDto();
            phoneMessageDto.setSendPhone("13681803609");
            phoneMessageDto.setReceivePhone("13721427169");
            phoneMessageDto.setMessage("测试发送短信，这个是短信信息....");

            session.write(phoneMessageDto);
        } catch (Exception e) {
            logger.error("客户端链接异常...", e);
        }
        session.getCloseFuture().awaitUninterruptibly();
        connector.dispose();
    }
}
