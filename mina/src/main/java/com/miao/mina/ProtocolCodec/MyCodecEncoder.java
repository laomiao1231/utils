package com.miao.mina.ProtocolCodec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoder;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

//编码器
public class MyCodecEncoder implements ProtocolEncoder {
    private Charset charset = Charset.forName("UTF-8");

    @Override
    public void encode(IoSession ioSession, Object o, ProtocolEncoderOutput protocolEncoderOutput)
            throws Exception {
        IoBuffer buffer = IoBuffer.allocate(100).setAutoExpand(true);
        CharsetEncoder encoder = charset.newEncoder();
        buffer.putString(o.toString(), encoder);
        buffer.put((byte)'\r');
        buffer.put((byte)'\n');
        buffer.flip();
        protocolEncoderOutput.write(buffer);
    }

    @Override
    public void dispose(IoSession ioSession) throws Exception {

    }
}
