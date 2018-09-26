package com.miao.mina;

import com.miao.mina.ProtocolCodec.MyCodecFactory;
import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.LineDelimiter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MinaDemo1 {
    private static Logger logger = Logger.getLogger(MinaDemo1.class);
    private static int PORT = 3005;

    public static void main(String[] args) {
        //创建连接
        IoAcceptor acceptor = null;
        try {
            //创建一个非阻塞server端的socket
            acceptor = new NioSocketAcceptor();
            //设置过滤器（使用mina提供的文本换行符编解码器）
            acceptor.getFilterChain().addLast(
                "codec",
                    new ProtocolCodecFilter(new MyCodecFactory())
            );
            //设置读取数据的缓冲区大小
            acceptor.getSessionConfig().setMaxReadBufferSize(2048);
            // 读写通道10秒内无操作进入空闲状态
            acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 18);
            // 绑定逻辑处理器
            acceptor.setHandler(new MinaDemo1Handler());
            // 绑定端口
            acceptor.bind(new InetSocketAddress(PORT));
            logger.info("服务端启动成功...     端口号为：" + PORT);
        }catch (Exception e){
            logger.error("服务端启动异常....", e);
            e.printStackTrace();
        }
    }
}
