package com.miao.mina.ProtocolCodec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import java.nio.charset.Charset;

//解码器
public class MyCodecDecoder implements ProtocolDecoder {
    private Charset charset = Charset.forName("UTF-8");
    IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);

    public void decode(IoSession ioSession, IoBuffer ioBuffer, ProtocolDecoderOutput protocolDecoderOutput)
            throws Exception {
        while (ioBuffer.hasRemaining()){
            byte b = ioBuffer.get();
            buffer.put(b);
            if(b == '\n') {
                buffer.flip();
                byte[] msg = new byte[buffer.limit()];
                buffer.get(msg);
                String message = new String(msg, charset);
                buffer = IoBuffer.allocate(100).setAutoExpand(true);
                protocolDecoderOutput.write(message);
            }
        }
    }

    public void finishDecode(IoSession ioSession, ProtocolDecoderOutput protocolDecoderOutput) throws Exception {

    }

    public void dispose(IoSession ioSession) throws Exception {

    }
}
