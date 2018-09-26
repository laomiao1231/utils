package com.miao.mina.server;

import com.miao.mina.dto.PhoneMessageDto;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class PhoneDtoServerHandler extends IoHandlerAdapter {
    private static Logger logger = Logger.getLogger(PhoneDtoServerHandler.class);

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("服务端发送异常...", cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        PhoneMessageDto phoneMessageDto = (PhoneMessageDto) message;
        String sendPhone = phoneMessageDto.getSendPhone();
        String receivePhone = phoneMessageDto.getReceivePhone();
        String msg = phoneMessageDto.getMessage();
        logger.info("发送人手机号码：" + sendPhone);
        logger.info("接受人手机号码：" + receivePhone);
        logger.info("发送信息：" + msg);
        session.write("发送成功");
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        session.closeOnFlush();
    }
}
