package com.miao.mina;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Date;

public class MinaDemo1Handler extends IoHandlerAdapter{
    private static Logger logger = Logger.getLogger(MinaDemo1Handler.class);

    @Override
    public void sessionCreated(IoSession session) throws Exception {
        logger.info("服务端与客户端创建连接...");
    }

    @Override
    public void sessionOpened(IoSession session) throws Exception {
        logger.info("服务端与客户端连接打开...");
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
        logger.info("服务端进入空闲状态...");
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        logger.error("服务端发送异常...", cause);
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String msg = message.toString();
        logger.info("服务端接收到的数据为：" + msg);
        if ("bye".equals(msg)) { // 服务端断开连接的条件
            session.closeNow();
        }
        Date date = new Date();
        //返回给客户端的信息
        session.write(date);
    }

    @Override
    public void messageSent(IoSession session, Object message) throws Exception {
        logger.info("服务端发送信息成功...");
        session.closeNow();
    }
}
