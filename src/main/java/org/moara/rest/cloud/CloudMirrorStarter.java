
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
 * <pre>
 *  파 일 명 : CloudMirrorStarter.java
 *  설    명 : 클라우드 미러용 스타트
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */
@SpringBootApplication(scanBasePackages = {"org.moara"})
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


			//noinspection AnonymousHasLambdaAlternative
			new Thread(){
				@Override
				public void run(){
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
								//noinspection deprecation
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
					
				}
				
			}.start();
			
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
		}
	
	}
}
