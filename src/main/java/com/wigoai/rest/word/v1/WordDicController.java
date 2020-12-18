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

package com.wigoai.rest.word.v1;

import org.json.JSONArray;
import org.json.JSONObject;
import org.moara.ara.datamining.textmining.dictionary.word.element.WordClass;
import org.moara.ara.datamining.textmining.dictionary.word.element.WordClassDetail;
import org.moara.ara.datamining.textmining.dictionary.word.management.NewWord;
import org.moara.common.data.database.jdbc.JDBCUtil;
import org.moara.common.network.socket.HostAddrPort;
import org.moara.common.util.ExceptionUtil;
import org.moara.engine.EngineApiRequests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 단어관련 컨트롤러 
 * @author macle
 */
@RestController
public class WordDicController {


    private static final Logger logger = LoggerFactory.getLogger(WordDicController.class);

    /**
     * 사전추가 메타동기화
     * @param jsonValue 사전 데이터정보
     * @return 추가된 데이터 정보
     */
    @RequestMapping(value = "/word/v1/dic/add" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String add(@RequestBody final String jsonValue){

        JSONObject response = new JSONObject();


        JSONArray success = new JSONArray();
        JSONArray overlap = new JSONArray();
        JSONArray error = new JSONArray();

        try {

            JSONObject request = new JSONObject(jsonValue);
            String userId = null;
            if(request.has("user_id")){
                userId = request.getString("user_id");
            }
            String deptId = null;
            if(request.has("dept_id")){
                deptId = request.getString("dept_id");
            }

            NewWord newWord = new NewWord();
            newWord.setUserId(userId);
            newWord.setDeptId(deptId);

            JSONArray array = request.getJSONArray("words");
            for (int i = 0; i < array.length(); i++) {

                try {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String text = jsonObject.getString("text");

                    text = text.replaceAll("\\s" ,"");

                    String partOfSpeech = jsonObject.getString("pos");

                    WordClassDetail wordClassDetail = WordClassDetail.valueOf(partOfSpeech);
                    WordClass wordClass = wordClassDetail.wordClass();

                    String query = "SELECT W.CD_WORD FROM TB_ARA_WORD W, TB_ARA_WORD_DIC D\n" +
                            "WHERE W.CD_WORD = D.CD_WORD\n" +
                            "AND D.WORD_SYLLABLE = '%s'\n" +
                            "AND W.WORD_CLASS ='%s'\n" +
                            "AND W.FG_DEL = 'N'\n" +
                            "AND D.FG_DEL = 'N'";
                    query = String.format(query, text, wordClass.toString());

                    if(JDBCUtil.isRowData(query)){
                        overlap.put(jsonObject);
                        continue;
                    }

                    newWord.setWordClass(wordClass.toString());
                    newWord.setWordClassDetail(wordClassDetail.toString());
                    newWord.setCompound(jsonObject.has("is_compound") && jsonObject.getBoolean("is_compound"));
                    newWord.add(text);


                    success.put(jsonObject);

                }catch(Exception e){
                    error.put(array.get(i));
                    logger.error(ExceptionUtil.getStackTrace(e));
                }
            }

            response.put("message" , "success: " + success.length() + ", overlap: " + overlap.length() +", error: " + error.length());
            response.put("success", success);
            response.put("overlap", overlap);
            response.put("error", error);


            if(success.length() > 0) {
                List<String> engineCodeList = JDBCUtil.getList("SELECT CD_ENGINE FROM TB_MOARA_ENGINE WHERE FG_DIC_META_USE='Y' AND FG_DEL='N'");

                for (String engineCode : engineCodeList) {
                    try {
                        EngineApiRequests.sendToReceiveMessage(engineCode, "META0005");
                    } catch (Exception e) {
                        logger.error(ExceptionUtil.getStackTrace(e));
                    }
                }


            }
            return response.toString();

        }catch(Exception e){
            logger.error(ExceptionUtil.getStackTrace(e));

            response.put("message", "error");
            return response.toString();
        }

    }


}
