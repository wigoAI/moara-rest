/** 
 * <pre>
 *  파 일 명 : MoaraRestApiServer.java
 *  설    명 : 테스트 클래스 엔진없이 구동용
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.07
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */
package org.moara.rest.api;

import java.util.HashMap;

import org.moara.Moara;
import org.moara.common.config.Config;
import org.moara.rest.MoaraEngineSpringBoot;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = {"org.moara.rest"})
public class MoaraRestApiServer extends  SpringBootServletInitializer {

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
		Config.getConfig("");
//		
		
		System.out.println("Config.getLogConfigPath() " + Config.getLogConfigPath());
		HashMap<String, Object> props = new HashMap<>();
		props.put("server.port", 8050);
		props.put("logging.config", Config.getLogConfigPath());
		String [] springbootArgs = new String[0];
		
		new SpringApplicationBuilder()
	    .sources(MoaraEngineSpringBoot.class)                
	    .properties(props)
	    .run(springbootArgs);
		
		Moara.initMeta();
		
//		SpringApplication.run(MoaraRestApiServer.class, args);
//		Config.logConfigLoad();
//		Moara.initMeta();
		
	}

}
