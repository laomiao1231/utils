package com.miao.mina.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class MyCodecDecoder01 implements ProtocolDecoder {
    private Charset charset; // 编码格式
    private String delimiter; // 文本分隔符
    private IoBuffer delimBuf; // 文本分割符匹配的变量
    // 定义常量值，作为每个IoSession中保存解码任务的key值
    private static String CONTEXT = MyTextLineCodecDecoder.class.getName()
            +".context";

    // 构造函数，必须指定Charset和文本分隔符
    public MyCodecDecoder01(Charset charset, String delimiter) {
        this.charset = charset;
        this.delimiter = delimiter;
    }

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput)
            throws Exception {
        Context context = getContext(ioSession);
        if(delimiter == null || "".equals(delimiter)) {
            delimiter = "\r\n";
        }
        if(charset == null) {
            charset = Charset.forName("UTF-8");
        }
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }

    private class Context {
        private CharsetDecoder decoder;
        private IoBuffer buf; // 保存真实解码内容
        private int matchCount = 0; // 匹配到的文本换行符个数
        private Context() {
            decoder = charset.newDecoder();
            buf = IoBuffer.allocate(80).setAutoExpand(true);
        }
        // 重置
        public void reset() {
            matchCount = 0;
            decoder.reset();
        }
        // 追加数据
        public void append(IoBuffer in) {
            getBuf().put(in);
        }
        // ======get/set方法=====================
        public CharsetDecoder getDecoder() {
            return decoder;
        }
        public IoBuffer getBuf() {
            return buf;
        }
        public int getMatchCount() {
            return matchCount;
        }
        public void setMatchCount(int matchCount) {
            this.matchCount = matchCount;
        }
    }

    private Context getContext(IoSession session) {
        Context context = (Context) session.getAttribute(CONTEXT);
        if(context == null) {
            context = new Context();
            session.setAttribute(CONTEXT, context);
        }
        return context;
    }

    private void decodeNormal(Context context, IoBuffer buffer, ProtocolDecoderOutput output)
            throws CharacterCodingException{
        int matchCount = context.getMatchCount();

    }
}
