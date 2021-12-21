package com.wooricard.ocr.sftp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import com.jcraft.jsch.SftpProgressMonitor;

public class SftpClient {

	private static final Logger logger = LoggerFactory.getLogger(SftpClient.class);
	
	private String address = "192.168.56.101";
	
	private int port = 22;
	
	private String username = "jhso";
	
	private String password = "2thwjdghks";
	
	private static Session session = null;
	
	private static Channel channel = null;
	
	private static ChannelSftp channelSftp = null;
	
	public void sshAccess() {
		JSch jSch = new JSch();
		
		try {
			session = jSch.getSession(username, address, port);
			session.setPassword(password);
			
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			
			session.setConfig(properties);
			session.connect();
		} catch (JSchException e) {
			logger.error("getSession Exception", e);
		}
	}
	
	public void sendFileToSftpServer(String srcPath, String destPath) {
		try {
			channel = session.openChannel("sftp");
			channel.connect();
			
			channelSftp = (ChannelSftp) channel;
			channelSftp.put(srcPath, destPath, new SftpProgressMonitor() {
				private long max	 = 0; // 최대	
				private long count 	 = 0; // 계산을 위해 담아두는 변수
				private long percent = 0; // 퍼센트

				@Override
				public void init(int op, String src, String dest, long max) { // 설정
					this.max = max;
				}
				
				@Override
				public void end() {
					// 종료시 할 행동
				}
				
				@Override
				public boolean count(long bytes) {
					this.count += bytes; // 전송한 바이트를 더한다
					long percnetNow = (this.count * 100) / max; // 현재값에서 최대값을 뺀 후
					if (percnetNow > this.percent) { // 퍼센트보다 크면
						this.percent = percnetNow;
						logger.info("progress : ", this.percent);
					}
					return true; // 기본값은 false이며 false인 경우 count 메소드를 호출하지 않는다.
				}
			});
		} catch (JSchException e) {
			logger.error("openChannel Exception", e);
		} catch (SftpException e) {
			logger.error("put Exception", e);
		}	
	}
	
	public void downloadFromSftpServer(String dir, String filename, String path) {
		InputStream is = null;
		FileOutputStream fos = null;
		
		try {
			channelSftp.cd(dir);
			is = channelSftp.get(filename);
		} catch (SftpException e) {
			logger.info("cd Exception", e);
		}
		
		try {
			fos = new FileOutputStream(new File(path));
			
			int i;
			
			while ((i = is.read()) != -1) {
				fos.write(i);
			}
		} catch (FileNotFoundException e) {
			logger.error("file not found", e);
		} catch (IOException e) {
			logger.error("io exception", e);
		} finally {
			try {
				fos.close();
				is.close();
			} catch (IOException e) {
				logger.error("io exception", e);
			}
		}
	}
	
	public void disconnection() {
		channelSftp.exit();
		
		channel.disconnect();
		
		session.disconnect();
	}
	
	public static void main(String[] args) {
		SftpClient sftpClient = new SftpClient();
		
		sftpClient.sshAccess();
		
		if (session != null) {
			sftpClient.sendFileToSftpServer("E:\\test.tif", "/home/jhso/result.tif");
			
			sftpClient.downloadFromSftpServer("/home/jhso", "result.tif", "E:\\download.tif");
		}
		
		sftpClient.disconnection();
	}
}
