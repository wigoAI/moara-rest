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
package org.moara.rest.api.nlp;

import org.json.JSONObject;
import org.moara.ara.datamining.textmining.api.word.WordExtracts;
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
 * 단어추출 api 정의
 * @author macle
 */
@RestController
public class WordApiController {
	private static final Logger logger   = LoggerFactory.getLogger(WordApiController.class);



	@RequestMapping(value = "/document/words" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String documentWords(@RequestBody String jsonValue){

        try {
            return WordExtracts.extractDocumentWord(new JSONObject(jsonValue)).toString();
        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));
            return ApiMessageCode.FAIL;
        }
    }


	@RequestMapping(value = "/document/word/syllable" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String documentWordSyllable(@RequestBody String jsonValue){

		try {
			return WordExtracts.extractDocumentWordSyllable(new JSONObject(jsonValue));
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiMessageCode.FAIL;
		}
	}

	@RequestMapping(value = "/document/word/simple" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String documentWordSimple(@RequestBody String jsonValue){

		try {
			return WordExtracts.extractDocumentWordSimple(new JSONObject(jsonValue));
		}catch(Exception e){
			logger.error(ExceptionUtil.getStackTrace(e));
			return ApiMessageCode.FAIL;
		}
	}


}


