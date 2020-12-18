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

package com.wigoai.rest.dev;

import com.wigoai.rest.RestCall;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author macle
 */
public class WordAddRestCall {
    public static void main(String[] args) {

        JSONObject param = new JSONObject();


        JSONArray words = new JSONArray();

        JSONObject word = new JSONObject();
        word.put("text","김용수");
        word.put("pos", "NNP");
        word.put("is_compound", false);

        JSONObject word2 = new JSONObject();
        word2.put("text","허령회");
        word2.put("pos", "NNP");
        word2.put("is_compound", false);
        words.put(word);
        words.put(word2);

        param.put("user_id", "macle");
        param.put("dept_id", "AiLab");

        param.put("words", words);

        String responseMessage = RestCall.postJson("http://127.0.0.1:33377/word/v1/dic/add",param.toString());

        System.out.println(responseMessage);
    }
}
