
package org.moara.rest;

import org.moara.common.config.Config;
import org.moara.common.config.ConfigSet;
import org.moara.common.util.ExceptionUtil;
import org.moara.engine.MoaraEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.HashMap;
/** 
 * <pre>
 *  파 일 명 : MoaraEngineSpringBoot.java
 *  설    명 : spring boot을 지원하는 moara engine
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.07
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */
@SpringBootApplication(scanBasePackages = {"org.moara.rest"})
public class MoaraEngineSpringBoot {
	
	private static final Logger logger = LoggerFactory.getLogger(MoaraEngineSpringBoot.class); 
	

	public static void main(String[] args) {
		if(args == null){
			logger.error("args is null, engine code in");
			return ;
		}
		
		if(args.length != 2){
			logger.error("args is engine code, confg path");
			return;
		}
		try{
			

			int index = args[1].indexOf(MoaraEngine.PATH_SPLIT);

			if(index == -1){
				ConfigSet.setPath(args[1]);
			}else{

				ConfigSet.setLogPath(args[1].substring(index+ + MoaraEngine.PATH_SPLIT.length()));

				ConfigSet.setPath(args[1].substring(0, index));
			}
			//서버 인스턴스 생성
			MoaraEngine moaraEngine = MoaraEngine.newInstance(args[0]);
			String restApiUseFlag = moaraEngine.getConfig("rest.api.use.flag");
		
			
			
			if(restApiUseFlag != null) {
				restApiUseFlag = restApiUseFlag.trim().toUpperCase();
				
				if(restApiUseFlag.equals("Y")) {
					
					String restPortValue =  moaraEngine.getConfig("rest.api.port");
					
					int restPort = Integer.parseInt(restPortValue);
					
					HashMap<String, Object> props = new HashMap<>();
					props.put("server.port", restPort);
					props.put("logging.config" , ConfigSet.getLogbackConfigPath());
					
					
//					props.put("logging.config", new File(Config.getLogConfigPath()).getParentFile().getAbsolutePath()+"/logback_spring.xml");
					
					
					String [] springbootArgs = new String[0];
					
					new SpringApplicationBuilder()
				    .sources(MoaraEngineSpringBoot.class)                
				    .properties(props)
				    .run(springbootArgs);
					
					Config.logConfigLoad();
				}
			}
		
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
		
//		HashMap<String, Object> props = new HashMap<>();
//		props.put("server.port", 8080);
//		
//		new SpringApplicationBuilder()
//	    .sources(MoaraEngineSpringBoot.class)                
//	    .properties(props)
//	    .run(args);
//	
//		
//		Moara.initMeta();
//		SpringApplication.run(MoaraEngineSpringBoot.class, args);
	}
}
