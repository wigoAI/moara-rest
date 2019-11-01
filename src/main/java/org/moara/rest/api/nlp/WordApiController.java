



package org.moara.rest.api.nlp;

import org.json.JSONObject;
import org.moara.ara.datamining.textmining.TextMining;
import org.moara.ara.datamining.textmining.api.document.DocumentStandardKey;
import org.moara.ara.datamining.textmining.api.sentence.Sentences;
import org.moara.ara.datamining.textmining.api.util.ApiInOutUtil;
import org.moara.ara.datamining.textmining.api.word.WordExtractApi;
import org.moara.ara.datamining.textmining.api.word.WordExtracts;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.common.util.ExceptionUtil;
import org.moara.open.api.ApiMessageCode;
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

	@RequestMapping(value = "/document/words" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String documentWords(String jsonValue){

        try {
            return WordExtracts.extractDocumentWord(new JSONObject(jsonValue)).toString();
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiMessageCode.FAIL;
        }
    }

    @RequestMapping(value = "/document/sentences" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
    public String documentSentences(String jsonValue){
        try {
            JSONObject obj = new JSONObject(jsonValue);
            Document document = ApiInOutUtil.makeDocument(obj);
            TextMining.mining(document);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put(DocumentStandardKey.ANALYSIS_CONTENTS.key(), document.getAnalysisContents());
            jsonObject.put(DocumentStandardKey.SENTENCE_ARRAY.key(), Sentences.extractSentence(document, obj));

            return jsonObject.toString();
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiMessageCode.FAIL;
        }

    }

}


