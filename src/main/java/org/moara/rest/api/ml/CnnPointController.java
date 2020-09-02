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
package org.moara.rest.api.ml;

import org.moara.common.util.ExceptionUtil;
import org.moara.ml.cnn.SttCnnPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * cnn point
 * @author macle
 */
@RestController
public class CnnPointController {
	
	private static final Logger logger   = LoggerFactory.getLogger(CnnPointController.class);

	/**
	 * @param jsonObjectValue String json object
	 * @return String  score(double)
(	 */
	@RequestMapping(value = "/ml/cnn/P0001" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getCnnPointValue(@RequestBody String jsonObjectValue) {
		try {

			return SttCnnPoint.getPointValue(jsonObjectValue);
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "-1";
		}
	}

	/**
	 *
 	 * @param jsonObjectValue String json object
	 * @return String  score(double)
	 */
	@RequestMapping(value = "/ml/cnn/P0002" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getCnnPoint0002(@RequestBody String jsonObjectValue) {
		return SttCnnPoint.getAllPointValue(jsonObjectValue);
		
	}

	/**
	 * 응답체크
	 * @return String
	 */
	@RequestMapping(value = "/ml/cnn/health" , method = RequestMethod.GET)
	public String health() {
		try {
			return "";
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
}
