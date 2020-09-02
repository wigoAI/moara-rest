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
 * cnn point
 * @author macle
 */
@RestController
public class VocabController {


    private static final Logger logger = LoggerFactory.getLogger(VocabController.class);

    /**
     * @param jsonValue String json object
     * @return String json array
     */
    @RequestMapping(value = "/ml/vocab/tokenize" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String tokenize(@RequestBody(required = false) String jsonValue){

        if(jsonValue == null){
            return "[]";
        }

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
