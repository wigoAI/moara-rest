package org.moara.rest.api.ml;

import org.json.JSONObject;
import org.moara.ara.datamining.textmining.dictionary.word.embedding.vocabulary.KoreaVocabWords;
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
 *  파 일 명 : VocabController.java
 *  설    명 : 보케블러리용 컨트롤러
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2020.05
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 *
 * @author Copyrights 2020 by ㈜ WIGO. All right reserved.
 */
@RestController
public class VocabController {


    private static final Logger logger = LoggerFactory.getLogger(VocabController.class);

    @RequestMapping(value = "/ml/vocab/tokenize" , method = RequestMethod.POST, produces= MediaType.ALL_VALUE)
    public String documentWordSimple(@RequestBody String jsonValue){


        if("".equals(jsonValue.trim())){
            return "[]";
        }
        try {
            return KoreaVocabWords.getVocabWords(new JSONObject(jsonValue).getString("text"));
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return "[]";
        }
    }

}
