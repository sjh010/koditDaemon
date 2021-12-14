package com.kodit.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

@Component
public class NettyChannelInitializer extends ChannelInitializer<SocketChannel> {

	@Autowired
	private RequestHandler jsonHandler;
	
	/**
	 * 클라이언트 소켓 채널이 생성쇨 때 호출됨
	 */
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		
		// decoder는 @Sharable이 안 됨, Bean 객체 주입이 안되고, 매번 새로운 객체 생성해야함
		
		// 뒤이어 처리할 디코더 및 핸들러 추가
		pipeline.addLast(jsonHandler);
	
	}

}
