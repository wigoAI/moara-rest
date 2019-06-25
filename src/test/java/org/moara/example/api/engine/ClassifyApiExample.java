
/** 
 * <pre>
 *  파 일 명 : ClassifyApiExample.java
 *  설    명 : 예제용 분류결과 api
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

public class ClassifyApiExample {
	
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
			
			//제목 입력
			obj.put("title","테스트 5월 고용 쇼크 정부도 매우 충격적");
			
			//본문입력 제목이나 본문 둘중에 하나는 입력해야 결과가 돌아옴.
			String contents = "테스트 지난달 취업자 증가수가 7만2000여명에 그치면서 고용쇼크가 연속 이어지고 있다. 8년4개월 만에 최악의 수준이라는 분석이다. \n "+
					" 청년실업률 또한 역대 최고 수준으로 치솟으면서 18년 만에 최고치를 기록했고, 취업자 수와 실업률 등 주요 고용지표가 모두 곤두박질쳤다. 인구 감소폭에 비해 취업자 수 감소폭이 더 큰 상황이다. \n "+
					" 15일 통계청이 발표한 5월 고용동향에 따르면 지난달 취업자 수는 2706만4000명으로 1년 전보다 7만2000명 증가하는 데 그쳤다. \n "+
					" 이는 지난 2010년 1월 1만 명이 줄어든 이후 8년 4개월 만에 가장 저조한 성적표로, 올 들어서도 2월부터 4월까지 3개월 연속 10만명 대에 머물던 취업자 증가 폭이 5월에는 7만명 대까지 추락한 것이다. \n "+
					" 취업자 증가 폭이 넉 달 연속 20만명 대를 밑도는 것은 글로벌 금융위기 이후 처음이다. \n "+
					" 정부는 이 같은 고용쇼크를 생산가능 인구 감소가 확대되는 가운데, 제조·서비스·건설 분야의 고용 부진과 기저효과 등의 영향이라고 풀이했다. \n "+
					" 특히 한국GM 사태로 촉발된 자동차산업 구조조정 여파로 제조업 취업자가 큰 폭으로 줄었고, 자동차 판매부진과 산업경쟁력 약화 등에 따른 주력산업 부진 확대 등과 함께 도·소매업 등에서의 취업자 수도 줄면서 취업자 증가 폭은 결국 10만 명 밑까지 떨어진 것이다. \n "+
					" 업종별로는 교육 서비스업(-9만8000명), 제조업(-7만9000명), 도매 및 소매업(-5만9000명), 숙박 및 음식점업(-4만3000명) 취업자가 전년 같은기간 보다 급감했다. 특히 제조업은 4월에 이어 2개월 연속 감소세를 이어갔으며, 조선·자동차 분야에서 취업자가 급감했다. \n "+
					" 지난달 청년실업률(15~29세)은 4.0%로 0.4%p 상승했다. 역대 최고로 올라 실업률이 2000년 5월(4.1%) 이후 18년 만에 최고치를 기록했다. 전년 같은 기간(9.2%)에 비해서는 10.5%로 1.3% 올랐다. 이는 1999년 6월 관련 통계를 작성한 이후 5월 기준으로 최고치다. \n "+
					" 이 같은 일자리 정부를 표방하고 있는 '문재인 정부'의 일자리 성적표가 최악의 상황이 지속되자 정부도 적지 않은 충격을 받은 것으로 알려졌다. 김동연 경제부총리 겸 기획재정부 장관은 이에 대해 “5월 고용동향 내용은 매우 충격적이다”라고 언급했다. \n "+
					" 김 부총리는 “일자리 상황이 단기에 호전되기 어려운 상황이지만 그동안 정부가 많은 노력했음에도 크게 나아지 않고 있다. 저를 포함한 경제팀 모두가 무거운 책임 느낀다”고 말한 것으로 전해졌다. \n "+
					" 이에 정부는 청년 일자리대책 주요과제와 추경 집행에 만전을 기하고, 산업 경쟁력 강화와 규제 개선 등 혁신성장을 속도감 있게 추진하겠다는 계획이다. \n "+
					" 데일리안 이소희 기자 (aswith@naver.com) \n "+
					" ⓒ (주)데일리안 - 무단전재, 변형, 무단배포 금지";
			
			obj.put("contents",contents);
			
			result = request.sendToReceiveMessage("ClassifyResult", obj.toString());
			
			
			
			JSONObject resultObj = new JSONObject(result); 
			
			System.out.println("category start");
			
			JSONArray categoryArray = resultObj.getJSONArray("category");
			for(int i=0 ; i<categoryArray.length() ; i++){
				
				//code, name, level
				//코드, 이름, 레벨(카테고리)
				JSONObject jSONObject = categoryArray.getJSONObject(i);
					
				System.out.println(jSONObject.getString("code"));
				System.out.println(jSONObject.getString("name"));
				System.out.println(jSONObject.getInt("level"));

			}
			

			System.out.println("\n\n");
			System.out.println("ontology start");			
			JSONArray ontologyArray = resultObj.getJSONArray("ontology");
			for(int i=0 ; i < ontologyArray.length() ; i++){
				
				//ontologyCode, analysisCode, expression
				//온톨로지코드, 분석코드, 표현식
				JSONObject jSONObject = ontologyArray.getJSONObject(i);
				System.out.println(jSONObject.getString("ontologyCode"));
				System.out.println(jSONObject.getString("analysisCode"));
				System.out.println(jSONObject.getString("expression"));
				
			}
			
			System.out.println("\n\n");
			System.out.println("taxonomy start");		
			JSONArray taxonomyArray = resultObj.getJSONArray("taxonomy");
			for(int i=0 ; i < taxonomyArray.length() ; i++){
				
				//code, name, point
				//코드, 이름, 점수
				JSONObject jSONObject = taxonomyArray.getJSONObject(i);
				System.out.println(jSONObject.getString("code"));
				System.out.println(jSONObject.getString("name"));
				System.out.println(jSONObject.getDouble("point"));
				
			}
	
			
			
			//연결종료 (반드시 호출)
			request.disConnect();
		
		}catch(Exception e){
			System.out.println(result);
			
			e.printStackTrace();
		}
		
	}
}
