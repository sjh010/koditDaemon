package com.wooricard.ocr.server;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wooricard.ocr.service.DmzService;

@Component
public class SampleHandler extends SimpleChannelUpstreamHandler {

	@Autowired
	private DmzService dmzService;
	
	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		e.getMessage();
		
		dmzService.test();
	}

	
}
