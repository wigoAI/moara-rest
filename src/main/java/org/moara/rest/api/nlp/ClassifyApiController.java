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

import com.google.gson.JsonObject;
import org.json.JSONObject;
import org.moara.ara.classification.model.ClassificationModel;
import org.moara.ara.classification.model.ClassificationModelMetaManager;
import org.moara.ara.classification.model.SimpleClassificationResults;
import org.moara.ara.datamining.textmining.TextMining;
import org.moara.ara.datamining.textmining.api.classify.ClassifyDefaultApi;
import org.moara.ara.datamining.textmining.api.document.JsonDocument;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.common.config.Config;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

/**
 * 분류 api 정의
 * @author macle
 */
@RestController
public class ClassifyApiController {

	private static final Logger logger   = LoggerFactory.getLogger(ClassifyApiController.class);

	/**
	 * @param documentJson String json object
	 * @return String json object
	 */
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

	@RequestMapping(value = "/classify/emotion" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String emotion(@RequestBody String documentJson) {
		try {

			JSONObject result = new JSONObject();

			Document document = JsonDocument.makeDocument(documentJson);
			TextMining.mining(document);

			String rootCode = Config.getConfig("emotion.root.code", "U716");
			Collection<ClassificationModel<?>> classificationModels = ClassificationModelMetaManager.getInstance().getClassificationModels();
			for (ClassificationModel<?> model : classificationModels) {
				if (!model.autoClassification()) continue;

				if(!model.getRootCategoryCode().equals(rootCode)){
					continue;
				}

				SimpleClassificationResults results = new SimpleClassificationResults(model);
				Map<String, JsonObject> predictResult = results.predictResult(document);
				JsonObject jsonObject = predictResult.get(Config.getConfig("emotion.positive.code", "U716001"));
				if (jsonObject == null) {
					result.put("positive", 0.0);
				}else{
					if(jsonObject.has("probability")){
						result.put("positive", jsonObject.get("probability").getAsDouble());
					}else{
						result.put("positive", 0.0);
					}

				}

				jsonObject = predictResult.get(Config.getConfig("emotion.negative.code", "U716002"));
				if (jsonObject == null) {
					result.put("negative", 0.0);
				}else{
					if(jsonObject.has("probability")){
						result.put("negative", jsonObject.get("probability").getAsDouble());
					}else{
						result.put("negative", 0.0);
					}

				}

				jsonObject = predictResult.get(Config.getConfig("emotion.neutral.code", "U716002"));
				if (jsonObject == null) {
					result.put("neutral", 0.0);
				}else{
					if(jsonObject.has("probability")){
						result.put("neutral", jsonObject.get("probability").getAsDouble());
					}else{
						result.put("neutral", 0.0);
					}

				}
				break;
			}

			return result.toString();
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "{}";
		}
	}



}
