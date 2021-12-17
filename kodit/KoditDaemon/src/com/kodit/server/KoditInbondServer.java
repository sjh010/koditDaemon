package com.kodit.server;

import java.net.InetSocketAddress;

import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

@Component
public class KoditInbondServer {

	private static final Logger logger = LoggerFactory.getLogger(KoditInbondServer.class);
	
	@Autowired
	private ServerBootstrap serverBootstrap;
	
	@Autowired
	private InetSocketAddress serverInetSocketAddress;
	
	private Channel serverChannel;
	
	public void start() {
		try {
			logger.info("koditDaemon Start....");
			// ChannelFuture : I/O operation의 결과나 상태를 제공하는 객체
			// 지정한 host, port로 소켓을 바인딩하고 incoming connection을 받도록 준비함
			ChannelFuture serverChannelFuture = serverBootstrap.bind(serverInetSocketAddress).sync();
			
			// 서버 소켓이 닫힐 때까지 기다림
			serverChannel = serverChannelFuture.channel().closeFuture().sync().channel();
		} catch (InterruptedException e) {
			logger.error("", e);
		}
	}
	
	// Bean을 제거하기 전에 해야할 작업이 있을 때 설정
	@PreDestroy
	public void stop() {
		if (serverChannel != null) {
			serverChannel.close();
			serverChannel.parent().closeFuture();
		}
	}
}
