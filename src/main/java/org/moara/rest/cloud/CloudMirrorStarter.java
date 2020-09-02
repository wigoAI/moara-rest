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
package org.moara.rest.cloud;

import org.moara.Moara;
import org.moara.common.annotation.Priority;
import org.moara.common.config.Config;
import org.moara.common.config.ConfigSet;
import org.moara.common.sort.QuickSortArray;
import org.moara.common.util.ExceptionUtil;
import org.moara.engine.cloud.mirror.CloudMirrorEngineInitializer;
import org.moara.open.api.server.ApiServer_Daemon;
import org.moara.open.api.server.receive.ReceiveServerDaemon;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
/**
 * cloud 환경에서 사용되는 api
 * @author macle
 */
@SpringBootApplication(scanBasePackages = {"org.moara", "com.wigoai"})
public class CloudMirrorStarter {
	private static final Logger logger = LoggerFactory.getLogger(CloudMirrorStarter.class); 
	

	public static void main(String[] args) {
		if(args == null || args.length < 1){
			logger.error("args is null, config path set");
			return ;
		}
		
		try{
			ConfigSet.setPath(args[0]);
			
			Moara.init();
			
			
			int engineApiPort = Config.getInteger("cloud.mirror.engine.api.port", 33402);
			ApiServer_Daemon apiDaemon  =  new ApiServer_Daemon(engineApiPort);
			apiDaemon.start();
			
			Integer receivePort = Config.getInteger("cloud.mirror.engine.receive.port");			
			if(receivePort != null){
				ReceiveServerDaemon receiveServerDaemon= new ReceiveServerDaemon(receivePort);
				receiveServerDaemon.start();
			}
			
			String restApiUseFlag = Config.getConfig("cloud.mirror.rest.api.use.flag", "Y");
			
			restApiUseFlag = restApiUseFlag.trim().toUpperCase();
				
			if(restApiUseFlag.equals("Y")) {
					
				int restPort =  Config.getInteger("cloud.mirror.rest.api.port", 33480);
				HashMap<String, Object> props = new HashMap<>();
				props.put("server.port", restPort);
					
				String [] springbootArgs = new String[0];
					
				new SpringApplicationBuilder()
				.sources(CloudMirrorStarter.class)                
				.properties(props)
				.run(springbootArgs);
					
				Config.logConfigLoad();
			}


			new Thread(() -> {
				try{

					Collection<URL> collections = ClasspathHelper.forJavaClassPath();

					List<URL> urlList = new ArrayList<>();
					for(URL url : collections){

						String path = url.getPath();
						try{
							File file = new File(path);
							if(file.isDirectory()){
								urlList.add(url);
							}else if(file.getName().contains("moara")){
								urlList.add(url);
							}
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}

					}

					Reflections  ref = new Reflections (new ConfigurationBuilder().setUrls(urlList));

					List<CloudMirrorEngineInitializer> initializerList = new ArrayList<>();
					for (Class<?> cl : ref.getSubTypesOf(CloudMirrorEngineInitializer.class)) {
						try{
							CloudMirrorEngineInitializer initializer = (CloudMirrorEngineInitializer)cl.newInstance();
							initializerList.add(initializer);
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
					}

					if(initializerList.size() == 0){
						return;
					}


					//noinspection ToArrayCallWithZeroLengthArrayArgument
					CloudMirrorEngineInitializer [] initializerArray = initializerList.toArray(new CloudMirrorEngineInitializer[initializerList.size()]);

					int [] numArray = new int[initializerArray.length];

					for(int i=0 ; i<numArray.length ; i++){

						int seq = 1000;
						try{

							Priority priority =initializerArray[i].getClass().getAnnotation(Priority.class);
							if(priority != null){
								seq = priority.seq();
							}



						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}


						numArray[i] = seq;

					}


					QuickSortArray<CloudMirrorEngineInitializer> sort = new QuickSortArray<>(initializerArray);
					sort.sortAsc(numArray);
					//noinspection ForLoopReplaceableByForEach
					for(int i=0 ; i<initializerArray.length ; i++){
						try{
							initializerArray[i].init();
						}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
					}


				}catch(Exception e){
					logger.error(ExceptionUtil.getStackTrace(e));
				}

			}).start();
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	
	}
}
