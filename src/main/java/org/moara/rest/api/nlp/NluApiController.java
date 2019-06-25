/** 
 * <pre>
 *  파 일 명 : NluApiController.java
 *  설    명 : NLU 기본적인 api정리
 *  		  
 *  			                  
 *  작 성 자 : yh.heo(허영회)
 *  작 성 일 : 2018.06
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */

package org.moara.rest.api.nlp;

import org.moara.ara.datamining.textmining.api.nlu.NluApi;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class NluApiController {

	private static final Logger logger   = LoggerFactory.getLogger(NluApiController.class);
	
	
	@RequestMapping(value = "/nlu/sentence/result" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extractSentence(@RequestBody String documentJson) {
		try {
			return NluApi.extractNluSentence(documentJson);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	@RequestMapping(value = "/nlu/topic/result" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extractTopic(@RequestBody String documentJson) {
		try {
			return NluApi.extractNluTopic(documentJson);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	@RequestMapping(value = "/nlu/object/result" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extractObject(@RequestBody String documentJson) {
		try {
			return NluApi.extractNluSentencePhraseObject(documentJson);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	@RequestMapping(value = "/nlu/phrase/result" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extractPhrase(@RequestBody String documentJson) {
		try {
			return NluApi.extractNluPhrase(documentJson);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	
}
