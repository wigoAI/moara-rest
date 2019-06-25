/** 
 * <pre>
 *  파 일 명 : CloudMirrorStarter.java
 *  설    명 : moara를 cloud에서 mirror 형태로 사용할 경우의 시작 클래스
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.08
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */

package org.moara.rest.cloud;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.moara.rest"})
public class CloudMirrorStarter {
	private static final Logger logger = LoggerFactory.getLogger(CloudMirrorStarter.class); 
	
	@Bean
	public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
	    return new BeanFactoryPostProcessor() {

	        @Override
	        public void postProcessBeanFactory(
	                ConfigurableListableBeanFactory beanFactory) throws BeansException {
	            BeanDefinition bean = beanFactory.getBeanDefinition(
	                    DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

	            bean.getPropertyValues().add("loadOnStartup", 1);
	        }
	    };
	}
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
								CloudMirrorEngineInitializer initializer = (CloudMirrorEngineInitializer)cl.newInstance();
								initializerList.add(initializer);
							}catch(Exception e){logger.error(ExceptionUtil.getStackTrace(e));}
						}
						
						if(initializerList.size() == 0){
							return;
						}
						
						
						
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
