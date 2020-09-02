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

import org.moara.ara.datamining.textmining.api.nlu.NluApi;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * nlu api 정의
 * @author macle
 */
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
