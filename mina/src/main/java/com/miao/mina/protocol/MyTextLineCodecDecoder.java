package com.miao.mina.protocol;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;

public class MyTextLineCodecDecoder implements ProtocolDecoder {
    private Charset charset = Charset.forName("UTF-8");
    // 定义常量值，作为每个IoSession中保存解码内容的key值
    private static String CONTEXT = MyTextLineCodecDecoder.class.getName() + ".context";

    @Override
    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput)
            throws Exception {
        Context context = getContext(ioSession);
        decodeAuto(context, ioBuffer, protocolDecoderOutput);
    }

    @Override
    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {
        Context context = (Context) ioSession.getAttribute(CONTEXT);
        if(context != null) {
            ioSession.removeAttribute(CONTEXT);
        }
    }

    private class Context {
        private IoBuffer buffer;
        public Context() {
            buffer = IoBuffer.allocate(100).setAutoExpand(true);
        }
        public IoBuffer getBuffer() {
            return buffer;
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

    private void decodeAuto(Context context, IoBuffer buffer, ProtocolDecoderOutput output)
             throws CharacterCodingException {
        boolean mark = false;
        while (buffer.hasRemaining()) {
            byte b = buffer.get();
            switch (b) {
                case '\r': break;
                case '\n': break;
                default:
                    context.getBuffer().put(b);
            }
            if(mark) {
                IoBuffer ioBuffer = context.getBuffer();
                ioBuffer.flip();
                try{
                    output.write(ioBuffer.getString(charset.newDecoder()));
                }finally {
                    ioBuffer.clear();
                }
            }
        }
    }
}
