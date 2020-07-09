package org.moara.rest.api.similarity;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * <pre>
 *  파 일 명 : JsonDataMake.java
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

public class JsonDataMake {
    public static void main(String[] args) {

        JSONArray array = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id","1");
        jsonObject.put("title", "제목1");
        jsonObject.put("contents","본문1");
        array.put(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("id","2");
        jsonObject.put("title", "제목2");
        jsonObject.put("contents","본문2");
        array.put(jsonObject);
        jsonObject = new JSONObject();
        jsonObject.put("id","3");
        jsonObject.put("title", "제목3");
        jsonObject.put("contents","본문3");
        array.put(jsonObject);

        System.out.println(array.toString());

    }
}
