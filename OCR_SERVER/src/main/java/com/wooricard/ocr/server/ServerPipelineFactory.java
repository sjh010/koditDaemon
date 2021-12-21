package com.wooricard.ocr.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

public class ServerPipelineFactory implements ChannelPipelineFactory {

	private SampleHandler sampleHandler;
	
	public ServerPipelineFactory(SampleHandler sampleHandler) {
		this.sampleHandler = sampleHandler;
	}
	
	@Override
	public ChannelPipeline getPipeline() throws Exception {
		
		ChannelPipeline p = Channels.pipeline();
		p.addLast("decoder", new SampleDecoder());
		p.addLast("handler", sampleHandler);
		
		return p;
	}

}
