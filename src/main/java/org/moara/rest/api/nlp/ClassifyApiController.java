

package org.moara.rest.api.nlp;

import org.moara.ara.datamining.textmining.api.classify.ClassifyDefaultApi;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  파 일 명 : ClassifyApiController.java
 *  설    명 : 문장추출 기본적인 api정리
 *
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */
@RestController
public class ClassifyApiController {

	private static final Logger logger   = LoggerFactory.getLogger(ClassifyApiController.class);
	
	
	@RequestMapping(value = "/classify/result" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String result(@RequestBody String documentJson) {
		try {
			
			
			ClassifyDefaultApi classifyDefaultApi = new ClassifyDefaultApi(documentJson);
			
			return classifyDefaultApi.getResult();
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	
}
