package com.miao.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;


public class PhoneServerHandler extends IoHandlerAdapter {
    private static Logger logger = Logger.getLogger(PhoneServerHandler.class);

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("服务端发送异常...", cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String phoneMsg = message.toString();
        String[] msgs = phoneMsg.split(";");
        String sendPhone = msgs[0];
        String receivePhone = msgs[1];
        String msg = msgs[2];
        logger.info("发送人手机号码： "+sendPhone);
        logger.info("接收人手机号码： "+receivePhone);
        logger.info("发送内容： "+msg);
        session.write("发送成功");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        session.closeOnFlush();
    }
}
