package org.moara.rest;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.context.annotation.Bean;
/**
 * <pre>
 *  파 일 명 : EngineSpringBootInit.java
 *  설    명 : spirng boot을 활용하여 기동될때 필요한 항목
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2019.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜모아라. All right reserved.
 */
public class EngineSpringBootInit {
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
}
