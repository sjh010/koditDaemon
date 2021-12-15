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
		
		sb = new StringBuilder();
		sb.append(FilenameUtils.getFullPath(originFilePath));
		sb.append(FilenameUtils.getBaseName(originFilePath));
		sb.append("_Merge.tif");
		String mergeFilePath = sb.toString();

		String jspResponse = "FAIL";
		
		try {
			// merge tiff
			if (koditEdmsUploadUtil.mergeTiff(fileList, mergeFilePath)) {
				sb = new StringBuilder(); 
				sb.append(FilenameUtils.getFullPath(originFilePath));
				sb.append(FilenameUtils.getBaseName(originFilePath));
				sb.append("_Secu.tif");
				
				String encryptFilePath = sb.toString();

				// encrypt image
				if (koditEdmsUploadUtil.encryptImageFile(mergeFilePath, encryptFilePath)) {
					// send edms 
					jspResponse = koditEdmsUploadUtil.sendEdms(edmsUrl, encryptFilePath);
					
					logger.info("edms response : {}", jspResponse);
				}
			}
		} catch (Exception e) {
			logger.error("exception", e);
		} finally {
			channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(jspResponse, CharsetUtil.UTF_8));
		}	
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
