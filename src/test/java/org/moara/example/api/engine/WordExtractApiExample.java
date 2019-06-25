
/** 
 * <pre>
 *  파 일 명 : WordExtractApiExample.java
 *  설    명 : 예제용 단어 추출 api
 *         
 *  작 성 자 : malce(김용수)
 *  작 성 일 : 2018.06
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */

package org.moara.example.api.engine;

import org.json.JSONArray;
import org.json.JSONObject;
import org.moara.open.api.client.ApiRequest;

public class WordExtractApiExample {

	public static void main(String [] args) {
		
		String result = null;
		try{
			//엔진을 활용한 api가이드
			//엔진의 ip address, port를 입력
			ApiRequest request = new ApiRequest( "127.0.0.1", 33316);
			request.setPackageName("org.moara.ara.datamining.textmining.api");
			
			//연결
			request.connect();
			//여기부터 요청 반복반복가능	
			
			JSONObject obj = new JSONObject();
			//설정하지 않으면 기본 KO
//			obj.put("langCode", "KO");
			//설정하지 않으면 SNS
//			obj.put("documentType", "SNS");
			
			String sentenceValue = "청년실업률 또한 역대 최고 수준으로 치솟으면서 18년 만에 최고치를 기록했고, 취업자 수와 실업률 등 주요 고용지표가 모두 곤두박질쳤다.";
			obj.put("sentenceValue", sentenceValue);
			
			//설정하지 않으면 false
//			obj.put("isRecommend", false);

			//설정하지 않으면 false
			obj.put("isOrderBy", true);
			
			//설정하지 않으면 true
//			obj.put("isCompoundIn", true);

			//설정하지 않으면 true
//			obj.put("isCompound", true);

			//설정하지 않으면 null
//			obj.put("inWordClass", "NOUN,VERB");
			
			//설정하지 않으면 null
//			obj.put("outWordClass", "SYMBOL");

			result = request.sendToReceiveMessage("WordExtract", obj.toString());
			
			JSONArray jsonArray = new JSONArray(result);
			int length = jsonArray.length();
			
			for(int i=0 ; i<length ; i++){
				JSONObject jSONObject =jsonArray.getJSONObject(i);
				
				//code, extractCode, syllable, extractType, wordClass, wordClassDetail, startIndex, endIndex, analysisGroup, element

				System.out.println(jSONObject.getString("syllable"));
			}
			
			
			//연결종료 (반드시 호출)
			request.disConnect();
		
		}catch(Exception e){
			System.out.println(result);
			
			e.printStackTrace();
		}
		
	}
}
