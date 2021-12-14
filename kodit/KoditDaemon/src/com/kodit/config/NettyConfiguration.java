package com.kodit.config;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.kodit.server.NettyChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

@Configuration
public class NettyConfiguration {

	@Value("${server.port}")
	private String port;
	
	@Value("${server.netty.boss-count}")
	private String bossCount;
	
	@Value("${server.netty.worker-count}")
	private String workerCount;
	
	@Value("${server.netty.timeout}")
	private String timeoutSecond;
	
	@Bean
	public ServerBootstrap serverBootstrap(NettyChannelInitializer nettyChannelInitializer) {
		// ServerBootstrap : 서버 설정을 도와주는 class
		ServerBootstrap serverBootstrap = new ServerBootstrap();
		serverBootstrap.group(new NioEventLoopGroup(Integer.parseInt(bossCount)), new NioEventLoopGroup(Integer.parseInt(workerCount)))
					// NioServerSocketChannel : incoming connections을 수락하기 위해 새로운 Channel을 객체화할 떄 사용
					.channel(NioServerSocketChannel.class)
					.handler(new LoggingHandler(LogLevel.DEBUG))
					// ChannelInitializer : 새로운 Channel을 구성할 떄 사용되는 특별한 handler. 주로 ChannelPipeline으로 구성
					.childHandler(nettyChannelInitializer);
		
		// ServerBootstrap에 대한 다양한 Option 추가 기능
		// SO_BACKLOG : 동시에 수용 가능한 최대 incoming connection 개수
		// 이 외에도 SO_KEEPALIVE, TCP_NODELAY 등 옵션 제공
//		serverBootstrap.option(ChannelOption.SO_BACKLOG, backlog);
		
		// timeout
		serverBootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.parseInt(timeoutSecond) * 1000);
		
		return serverBootstrap;
	}
	
	// IP 소켓 주소(Port 번호)를 구현
	// 도메인 이름으로 객체 생성 가능
	@Bean
	public InetSocketAddress serverInetSocketAddress() {
		return new InetSocketAddress(Integer.parseInt(port));
	}
}
