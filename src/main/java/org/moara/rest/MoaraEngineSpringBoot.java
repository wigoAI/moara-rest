/*
 * Copyright (C) 2020 Wigo Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.moara.rest;

import org.moara.MoaraInitializer;
import org.moara.common.annotation.Priority;
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
 *  버    전 : 1.1
 *  수정이력 :  2019.11
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 ~ 2019by ㈜모아라. All right reserved.
 */

//다른서비스들보다는 빨리실행
@Priority(seq = Integer.MAX_VALUE-100)
@SpringBootApplication(scanBasePackages = {"org.moara", "com.wigoai"})
public class MoaraEngineSpringBoot implements MoaraInitializer {
	
	private static final Logger logger = LoggerFactory.getLogger(MoaraEngineSpringBoot.class);

	@Override
	public void init() {
		start();
	}

	public static void start(){
		MoaraEngine moaraEngine = MoaraEngine.getInstance();

		String restApiUseFlag = moaraEngine.getConfig("rest.api.use.flag");

		logger.debug("restApiUseFlag " + restApiUseFlag);

		if(restApiUseFlag != null) {
			restApiUseFlag = restApiUseFlag.trim().toUpperCase();

			if(restApiUseFlag.equals("Y")) {

				String restPortValue =  moaraEngine.getConfig("rest.api.port");

				int restPort = Integer.parseInt(restPortValue);

				HashMap<String, Object> props = new HashMap<>();
				props.put("server.port", restPort);
				if(ConfigSet.getLogbackConfigPath() != null) {
					props.put("logging.config", ConfigSet.getLogbackConfigPath());
				}

//					props.put("logging.config", new File(Config.getLogConfigPath()).getParentFile().getAbsolutePath()+"/logback_spring.xml");


				String [] springbootArgs = new String[0];

				new SpringApplicationBuilder()
						.sources(MoaraEngineSpringBoot.class)
						.properties(props)
						.run(springbootArgs);


				logger.debug("spring boot start");

			}

		}
	}


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
			start();

		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}

	}


}
