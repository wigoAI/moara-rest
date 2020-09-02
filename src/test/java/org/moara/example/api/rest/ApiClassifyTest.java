
/** 
 * <pre>
 *  파 일 명 : ApiMessageTest.java
 *  설    명 : api메시지 예제
 *                    
 *  작 성 자 : yeonie(김용수)
 *  작 성 일 : 2015.03
 *  버    전 : 1.2
 *  수정이력 : 2015.12
 *  기타사항 :
 * </pre>
 * @author Copyrights 2015 ㈜모아라. All right reserved.
 */


package org.moara.example.api.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.common.code.LangCode;

public class ApiClassifyTest {
	
	public static void main(String [] args){
		try{
			URL url = new URL("http://52.231.76.143:33480/classify/result");			
			HttpURLConnection conn;
	        conn = (HttpURLConnection) url.openConnection(); 
	        conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setDoInput( true ) ;
			conn.setDoOutput( true ) ;
			conn.setInstanceFollowRedirects( false );
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			
	    	conn.setConnectTimeout(30000);
	    	conn.setReadTimeout(30000);
			conn.setUseCaches(false);
			JSONObject object = new JSONObject();
			
			object.put("langCode", LangCode.KO);

			object.put("documentType", Document.SNS);
			object.put("title", "\"회식, 업무 연장이라더니\"…주 52시간 지침에도 혼란 불가피");
			object.put("contents", "(세종=연합뉴스) 이영재 기자 = 고용노동부가 오는 7월1일 노동시간 단축(주 최대 52시간제) 시행을 앞두고 11일 발표한 가이드라인은 관련법과 판례 등을 토대로 마련됐다.\n\n그러나 제도 시행 20일 전에야 내놓은 가이드라인이 추상적인 데다 노사가 합의를 통해 알아서 결정할 부분이 많아 일선 산업 현장의 혼란이 불가피해 보인다.\n\n노동부는 다양한 사업장에 일률적으로 적용할 수 있는 가이드라인을 내놓는 데는 한계가 있고 지나치게 구체적일 경우 오히려 역효과를 낼 수도 있다는 입장이다.\n\n◇ 대기시간은 노동시간 인정 \n\n노동자가 특정 업무를 하지 않아도 사용자의 지휘·감독 아래 있어 자유롭게 이용할 수 없는 '대기시간'은 노동시간으로 인정된다.");
//			object.put("title", "title");
//			object.put("contents", "contents");
			
			String param =  object.toString();
				
			System.out.println(param);
			OutputStream opstrm = conn.getOutputStream();
			opstrm.write(param.getBytes());
			opstrm.flush();
			opstrm.close();
			
			  String charSet = "UTF-8";
		        StringBuilder message = new StringBuilder(); 
		   		BufferedReader br= null;
		   		
		   		if (conn != null && conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
		   					
		   			br = new BufferedReader(
		   					new InputStreamReader(conn.getInputStream(), charSet));
		   					
		   			for (;;) {
		   				String line = br.readLine();
		   				if (line == null) break;
		   				message.append(line + '\n'); 
		   			}
		   			
		   		}
	       
		   		String result = message.toString();
		   		System.out.println(result);
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
}
