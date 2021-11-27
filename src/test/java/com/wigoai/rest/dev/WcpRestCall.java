package com.wigoai.rest.dev;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.wigoai.rest.RestCall;
import org.moara.ara.datamining.textmining.api.document.DocumentStandardKey;

/**
 * 단어 분류 패턴 호출
 * @author macle
 */
public class WcpRestCall {

    public static void main(String[] args) {


        Gson gson = new GsonBuilder().setPrettyPrinting().create();


        JsonObject request = new JsonObject();
        request.addProperty(DocumentStandardKey.TITLE.key(), "마스크 배부 공무원 폭행한 70대 실형.");
        request.addProperty(DocumentStandardKey.CONTENTS.key(), "장애인을 위한 공적 마스크를 배부하던 공무원을 폭행, 상처를 입힌 70대가 실형을 선고받았다.\n" +
                "\n" +
                "춘천지법 형사3단독 정수영 부장판사는 공무집행방해와 상해 혐의 등으로 재판에 넘겨진 A(70)씨에게 징역 6개월을 선고했다고 27일 밝혔다.\n" +
                "\n" +
                "A씨는 지난해 3월 강원 춘천시 한 아파트 관리사무소 앞 공원에서 장애인을 위한 공적 마스크를 나눠주던 사회복지업무 담당 공무원(여·42)에게 욕설하며 얼굴을 때리고 뒤로 넘어뜨려 다치게 한 혐의를 받고 있다.\n" +
                "\n" +
                "A씨의 변호인은 “당시 피고인의 행위는 휴대전화로 피고인을 촬영하는 피해자의 행동에 대응하기 위한 것으로 정당한 행위”라고 주장했다.\n" +
                "\n" +
                "그러나 장 부장판사는 판결문을 통해 “피고인이 욕설해 피해자의 촬영 행위를 유발하고 휴대전화를 쳐서 떨어뜨리고 때리는 등 행위는 정당한 행위라고 볼 수 없다”며 “누범기간 중에 재범하고 폭력 전과 7회 있는 점, 재판에 출석하지 않고 도주한 점 등을 참작했다”고 밝혔다.");



//        String responseMessage = RestCall.postJson("http://s2.moara.org:16015/document/wcp", gson.toJson(request));
        String responseMessage = RestCall.postJson("http://sc.wigo.ai:10015/document/wcp", gson.toJson(request));
        System.out.println( gson.toJson(request));

        System.out.println( "\n\nResponse:\n");

        System.out.println(responseMessage);
    }
}
