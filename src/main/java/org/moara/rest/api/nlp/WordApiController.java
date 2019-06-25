



package org.moara.rest.api.nlp;

import org.json.JSONObject;
import org.moara.ara.datamining.textmining.api.word.WordExtractApi;
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
 *  파 일 명 : WordApiController.java
 *  설    명 : 예제용 단어 추출 api
 *
 *  작 성 자 : malce(김용수)
 *  작 성 일 : 2018.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */
@RestController
public class WordApiController {
	private static final Logger logger   = LoggerFactory.getLogger(WordApiController.class);
	
	
	@RequestMapping(value = "/word/extract" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extract(@RequestBody String jsonValue) {
		try {
			JSONObject obj = new JSONObject(jsonValue);
			WordExtractApi wordExtractApi = new WordExtractApi(obj);
			
			return wordExtractApi.extract();
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}

	@RequestMapping(value = "/document/word/extract" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extractDocumentWord(@RequestBody String jsonValue) {
		try {
			JSONObject obj = new JSONObject(jsonValue);
			return WordExtractApi.extractDocumentWord(obj);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
}


