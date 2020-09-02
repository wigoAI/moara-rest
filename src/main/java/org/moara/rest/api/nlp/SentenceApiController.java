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

import org.moara.ara.datamining.textmining.api.sentence.SentenceApi;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 문장구분 api 정의
 * @author macle
 */
@RestController
public class SentenceApiController {

	private static final Logger logger   = LoggerFactory.getLogger(SentenceApiController.class);
	
	
	@RequestMapping(value = "/sentence/extract" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String extract(@RequestBody String documentJson) {
		try {
			return SentenceApi.extractSetence(documentJson);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
	
}
