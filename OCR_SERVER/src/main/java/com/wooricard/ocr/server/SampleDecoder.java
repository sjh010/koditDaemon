package com.wooricard.ocr.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

import com.wooricard.ocr.model.request.EdmsRequest;

public class SampleDecoder extends FrameDecoder {

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		EdmsRequest request = new EdmsRequest();
		request.setDataKey("test");
		
		return request;
	}

}
