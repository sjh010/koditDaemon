package com.kodit.server;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kodit.util.KoditEdmsUploadUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

@Component
@Sharable
public class RequestHandler extends ChannelInboundHandlerAdapter {	
	
	private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

	@Autowired
	private KoditEdmsUploadUtil koditEdmsUploadUtil;
	
	@Value("${edms.url}")
	private String edmsUrl;
	
	/**
	 * 클라이언트로 부터 메시지 수신
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		logger.info("==== Message receive from client ====");
		
		ChannelFuture channelFuture = ctx.write(Unpooled.EMPTY_BUFFER);
		
		ByteBuf in = (ByteBuf) msg;
		
		String receivedMessage = in.toString(CharsetUtil.UTF_8);
		
		logger.info("received : {}", receivedMessage);
		
		String[] fileList = receivedMessage.split("\\|");
		
		StringBuilder sb;

		String coverFilePath 	= fileList[0];	// 커버 이미지 경로
		String originFilePath	= fileList[1];	// 본문 이미지 경로
		
		//////////////////////////////////////////////////////////////////////////////
		// 이미지 병합 start
		
		sb = new StringBuilder();
		sb.append(FilenameUtils.getFullPath(originFilePath));
		sb.append(FilenameUtils.getBaseName(originFilePath));
		sb.append("_Merge.tif");
		
		String mergeFilePath = koditEdmsUploadUtil.mergeTiff(fileList, sb.toString());
		
		// 이미지 병합 end
		//////////////////////////////////////////////////////////////////////////////
		
		//////////////////////////////////////////////////////////////////////////////
		// 이미지 암호화 start
		sb = new StringBuilder(); 
		sb.append(FilenameUtils.getFullPath(originFilePath));
		sb.append(FilenameUtils.getBaseName(originFilePath));
		sb.append("_Secu.tif");
		
		String encFilePath = koditEdmsUploadUtil.encryptImageFile(mergeFilePath, sb.toString());

		
		String response = koditEdmsUploadUtil.sendEdms(edmsUrl, encFilePath);
		
		logger.info("response : {}", response);
		
		channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(response, CharsetUtil.UTF_8));	
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.writeAndFlush(null).addListener(ChannelFutureListener.CLOSE);
		super.exceptionCaught(ctx, cause);
	}
	
}
