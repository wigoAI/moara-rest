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

package org.moara.rest.api.crawling;

import org.json.JSONObject;
import org.moara.common.util.ExceptionUtil;
import org.moara.moa.crawling.core.http.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * crawling rest
 * @author macle
 */
@RestController
public class CrawlingController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlingController.class);

    /**
     * 문장결과 얻기
     * @param message String json object
     * @return String json object
     */
    @RequestMapping(value = "/crawling/http/script" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String extractSentence(@RequestBody String message) {
        try {
            JSONObject messageObj = new JSONObject(message);
            JSONObject optionData = null;
            if(!messageObj.isNull("option_data")) {
                Object obj = messageObj.get("option_data");
                if(obj.getClass() == String.class){
                    optionData = new JSONObject((String) obj);
                }else{
                    optionData = (JSONObject)obj;
                }
            }

            return HttpUrl.getScript(messageObj.getString("url"), optionData);
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return "{}";
        }
    }

}
