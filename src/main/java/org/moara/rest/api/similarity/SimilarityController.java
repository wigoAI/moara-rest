package org.moara.rest.api.similarity;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import org.moara.ara.datamining.textmining.similarity.cluster.grouping.SimilarityDocumentGroup;
import org.moara.ara.datamining.textmining.similarity.cluster.grouping.SimilarityDocumentGrouping;
import org.moara.ara.datamining.textmining.similarity.document.SimilarityDocument;
import org.moara.common.callback.ObjectCallback;
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
 * <pre>
 *  파 일 명 : SimilarityGroupController.java
 *  설    명 :
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2020.07
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 *
 * @author Copyrights 2020 by ㈜ WIGO. All right reserved.
 */
@RestController
public class SimilarityController {


    private static final Logger logger = LoggerFactory.getLogger(SimilarityController.class);

    private static final long WAIT_TIME = 1000L*60L*20;

    @RequestMapping(value = "/similarity/cluster/grouping" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String result(@RequestBody String data) {

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



    private class CallbackFlag {
        boolean isEnd = false;

    }

    public static void main(String[] args) {

        String jsonData = "[{\"contents\":\"본문1\",\"id\":\"1\",\"title\":\"제목1\"},{\"contents\":\"본문2\",\"id\":\"2\",\"title\":\"제목2\"},{\"contents\":\"본문3\",\"id\":\"3\",\"title\":\"제목3\"}]";

        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonData, JsonArray.class);

        int size = jsonArray.size();


        for (int i = 0; i < size ; i++) {
            System.out.println(jsonArray.get(i).getAsString());

        }

    }

}
