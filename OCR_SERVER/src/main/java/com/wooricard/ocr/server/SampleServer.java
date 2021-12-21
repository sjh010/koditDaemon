package com.wooricard.ocr.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.AdaptiveReceiveBufferSizePredictor;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SampleServer {

	@Autowired
	private SampleHandler sampleHandler;
	
	public SampleServer() {
		start();
	}
	
	public void start() {
		Executor executor = null;
		
		NioServerSocketChannelFactory factory = null;
		
		ServerBootstrap bootstrap = null;
		
		try {
			executor = Executors.newCachedThreadPool();
			
			factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), executor);
			
			bootstrap = new ServerBootstrap(factory);
			
			bootstrap.setPipelineFactory(new ServerPipelineFactory(sampleHandler));
			
			bootstrap.setOption("child.receiveBufferSizePredictor", new AdaptiveReceiveBufferSizePredictor(1024*50, 1024*50, 10485760));
			bootstrap.setOption("child.tcpNoDelay", "true");
			bootstrap.setOption("chil.keepAlive", "true");
			
			bootstrap.bind(new InetSocketAddress(12345));
			
			System.out.println("BIND");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
