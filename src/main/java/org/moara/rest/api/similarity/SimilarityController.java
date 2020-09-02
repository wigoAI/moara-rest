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
package org.moara.rest.api.similarity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.moara.ara.cluster.DocumentSimilarity;
import org.moara.ara.datamining.textmining.TextMining;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.ara.datamining.textmining.similarity.cluster.grouping.SimilarityDocumentGroup;
import org.moara.ara.datamining.textmining.similarity.cluster.grouping.SimilarityDocumentGrouping;
import org.moara.ara.datamining.textmining.similarity.compare.SimilarityDocumentCompare;
import org.moara.ara.datamining.textmining.similarity.compare.SimilarityDocumentResult;
import org.moara.ara.datamining.textmining.similarity.document.SimilarityDocument;
import org.moara.ara.datamining.textmining.similarity.learn.SimilarityLearnType;
import org.moara.common.callback.ObjectCallback;
import org.moara.common.code.LangCode;
import org.moara.common.config.Config;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


/**
 * 유사도 api 정의
 * @author macle
 */
@RestController
public class SimilarityController {


    private static final Logger logger = LoggerFactory.getLogger(SimilarityController.class);

    private static final long WAIT_TIME = 1000L*60L*20;

    /**
     * grouping
     * @param data String json object
     * @return String json object
     */
    @RequestMapping(value = "/similarity/cluster/grouping" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String grouping(@RequestBody String data) {

        JSONObject param = new JSONObject(data);

        JSONObject messageObj = new JSONObject();

        try {

            final CallbackFlag callbackFlag = new CallbackFlag();
            SimilarityDocumentGrouping similarityDocumentGrouping = new SimilarityDocumentGrouping();

            if(param.has("min_percent")) {
                similarityDocumentGrouping.setMinPercent(param.getDouble("min_percent"));
            }else{
                similarityDocumentGrouping.setMinPercent(0.5);
            }
            if(param.has("group_min_percent")) {
                similarityDocumentGrouping.setGroupMinPercent(param.getDouble("group_min_percent"));
            }else{
                similarityDocumentGrouping.setGroupMinPercent(0.30);
            }

            if(param.has("thread_count")) {
                int count = param.getInt("thread_count");
                if(count == -2){
                    similarityDocumentGrouping.setThreadCount(Config.getInteger("similarity.cluster.grouping.thread.count", -1));
                }else{
                    similarityDocumentGrouping.setThreadCount(count);
                }


            }else{
                similarityDocumentGrouping.setThreadCount(Config.getInteger("similarity.cluster.grouping.thread.count", -1));
            }


            similarityDocumentGrouping.setGroupRelation(true);

            final Thread waitThread = Thread.currentThread();

            ObjectCallback endCallback = o -> {


                JSONArray jsonArray = new JSONArray();

                SimilarityDocumentGroup[] groups = (SimilarityDocumentGroup []) o;

                for(SimilarityDocumentGroup group : groups){
                    SimilarityDocument[] documents = group.getDocumentArray();

                    for(SimilarityDocument simDoc : documents){
                        JSONObject obj = new JSONObject();
                        obj.put("relation_group", group.getRelationGroupNumber());
                        obj.put("group", group.getGroupId());
                        obj.put("id",simDoc.getDocument().getId());

                        jsonArray.put(obj);
                    }
                }

                callbackFlag.isEnd = true;
                if(jsonArray.length() > 0){
                    messageObj.put("message","success");
                    messageObj.put("group_sort", jsonArray);
                }else{
                    messageObj.put("message","document length 0");
                }
                waitThread.interrupt();
            };

            similarityDocumentGrouping.setEndCallback(endCallback);
            similarityDocumentGrouping.groupingJsonData(param.getString("data"));

            try{
                Thread.sleep(WAIT_TIME);
            }catch(Exception ignored){ }

            if(callbackFlag.isEnd){
                return messageObj.toString();
            }else{
                similarityDocumentGrouping.stopGrouping();
                messageObj.put("message","time over");
                return messageObj.toString();
            }

        }catch(Exception e) {

            String errorMessage =ExceptionUtil.getStackTrace(e);
            messageObj.put("message","error: -> " +errorMessage);
            logger.error(ExceptionUtil.getStackTrace(e));
            return messageObj.toString();
        }
    }



    private static class CallbackFlag {
        boolean isEnd = false;

    }

    /**
     *
     * @param jsonValue String json object
     * @return Double
     */
    @RequestMapping(value = "/similarity/equals/max" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Double similarityEqualsMax(@RequestBody String jsonValue) {
        try {
            JSONObject obj = new JSONObject(jsonValue);
            String contents = obj.getString("source");
            String compare = obj.getString("target");

            SimilarityDocument similarityDocument = new SimilarityDocument();
            Document document = new Document();
            document.setDocType(Document.SNS);
            document.setLangCode(LangCode.KO);
            document.setContents(contents);
            TextMining.mining(document);
            similarityDocument.setLearnType(SimilarityLearnType.DATA_EQUALS);
            similarityDocument.learn(document);
            similarityDocument.setMinPercent(0.2);

            Document compareDocument = new Document();
            compareDocument.setDocType(Document.SNS);
            compareDocument.setLangCode(LangCode.KO);
            compareDocument.setContents(compare);
            TextMining.mining(compareDocument);
            SimilarityDocumentResult result = SimilarityDocumentCompare.compare(similarityDocument, compareDocument);
            if(result.isResult()){
                return result.getMaxPercent().getPercent();
            }else{
                return 0.0;
            }
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return -1.0;
        }
    }

    /**
     *
     * @param jsonValue String json object
     * @return Double
     */
    @RequestMapping(value = "/similarity/cosine" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Double similarityCosine(@RequestBody String jsonValue) {
        try {
            JSONObject obj = new JSONObject(jsonValue);
            String contents = obj.getString("source");
            String compare = obj.getString("target");
            return DocumentSimilarity.compareDocument(contents, compare);
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return -1.0;
        }
    }



}
