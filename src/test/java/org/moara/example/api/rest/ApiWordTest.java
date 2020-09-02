package org.moara.example.api.rest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class ApiWordTest {
	public static void main(String [] args){
		try{
		URL url = new URL("http://52.231.76.143:33480/word/extract");			
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
		JSONObject obj = new JSONObject();
		//설정하지 않으면 기본 KO
		obj.put("langCode", "KO");
		//설정하지 않으면 SNS
		obj.put("documentType", "SNS");
		
		String sentenceValue = "청년실업률 또한 역대 최고 수준으로 치솟으면서 18년 만에 최고치를 기록했고, 취업자 수와 실업률 등 주요 고용지표가 모두 곤두박질쳤다.";
		obj.put("sentenceValue", sentenceValue);
		
		//설정하지 않으면 false
		obj.put("isRecommend", false);

		//설정하지 않으면 false
		obj.put("isOrderBy", true);
		
		//설정하지 않으면 true
		obj.put("isCompoundIn", true);

		//설정하지 않으면 true
		obj.put("isCompound", true);

		//설정하지 않으면 null
		obj.put("inWordClass", "NOUN,VERB");
		
		//설정하지 않으면 null
		obj.put("outWordClass", "SYMBOL");

		
		String param =  obj.toString();
		
//		System.out.println(param);
//		System.out.println(param);
		OutputStream opstrm = conn.getOutputStream();
		opstrm.write(param.getBytes());
		opstrm.flush();
		opstrm.close();
		
		  String charSet = "UTF-8";
	        StringBuilder message = new StringBuilder(); 
	   		BufferedReader br;
	   		
	   		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	   					
	   			br = new BufferedReader(
	   					new InputStreamReader(conn.getInputStream(), charSet));
	   					
	   			for (;;) {
	   				String line = br.readLine();
	   				if (line == null) break;
	   				message.append(line).append('\n');
	   			}
	   			
	   		}
       
	   		String result = message.toString();
	   		System.out.println(result);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
