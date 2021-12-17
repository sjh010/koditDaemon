package com.kodit.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import com.kodit.config.SpringConfigration;
import com.kodit.server.KoditInbondServer;

@Component("koditDaemon")
public class KoditDaemon {

	private static final Logger logger = LoggerFactory.getLogger(KoditDaemon.class);

//	@Autowired
//	private ThreadPoolTaskExecutor taskExecutor;

	public static void main(String[] args) {
		@SuppressWarnings({ "unused", "resource" })
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(SpringConfigration.class);
		
		ctx.getBean(KoditInbondServer.class).start();
	}
}
