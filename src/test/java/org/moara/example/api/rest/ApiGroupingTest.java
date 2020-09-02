package org.moara.example.api.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.moara.common.data.file.FileUtil;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *  파 일 명 : ApigroupingTest.java
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

public class ApiGroupingTest {

    public static void main(String[] args) {
        try{

            int maxCount = 1000;


            int count = 0;


            Map<String, String> idContentsMap = new HashMap<>();

            StringBuilder sb = new StringBuilder();

            try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/contents.txt"), StandardCharsets.UTF_8))){
                String line;
                while ((line = br.readLine()) != null) {

                    if(line.equals("")){
                        continue;
                    }

                    JSONObject jsonObject = new JSONObject(line);

                    StringBuilder contentsBuilder = new StringBuilder();

                    if(jsonObject.has("title")){
                        contentsBuilder.append(jsonObject.getString("title")).append("\n");
                    }

                    if(jsonObject.has("contents")){

                        contentsBuilder.append(jsonObject.getString("contents"));
//                        jsonObject.put("contents", jsonObject.getString("contents").substring(0,30));

                    }

                    jsonObject.remove("lang_code");
                    jsonObject.remove("doc_type");
                    jsonObject.remove("data_reg_time");

                    idContentsMap.put(jsonObject.getString("id"), contentsBuilder.toString().replace("\n", " ").replace(","," "));

                    sb.append(jsonObject.toString()).append("\n");

                    count++;

                    if(count >= maxCount){
                        break;
                    }
                }
            }

            JSONObject param = new JSONObject();
            param.put("data", sb.toString());
            param.put("min_percent", 0.5);
            param.put("group_min_percent", 0.3);
            // -1 이면 자동 최대치 설정
            param.put("thread_count", -2);

            System.out.println(param.toString());

//            URL url = new URL("http://127.0.0.1:33377/similarity/cluster/grouping");
            URL url = new URL("http://moara.org:10035/similarity/cluster/grouping");
            HttpURLConnection conn = null;
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
 //			object.put("title", "title");
//			object.put("contents", "contents");



            OutputStream opstrm = conn.getOutputStream();
            opstrm.write(param.toString().getBytes());
            opstrm.flush();
            opstrm.close();

            String charSet = "UTF-8";
            StringBuilder message = new StringBuilder();
            BufferedReader br= null;

            if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

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

            JSONObject request = new JSONObject(result);


            StringBuilder resultBuilder = new StringBuilder();

            String requestMessage = request.getString("message");
            if(requestMessage.equals("success")){
                JSONArray groupSort =  request.getJSONArray("group_sort");
                for (int i = 0; i <groupSort.length() ; i++) {
                    JSONObject obj = groupSort.getJSONObject(i);


                    resultBuilder.append(obj.getInt("relation_group") + "," + obj.getInt("group") + "," + idContentsMap.get(obj.getString("id"))).append("\n");
//                    System.out.println(obj.getInt("relation_group") + "\t" + obj.getInt("group") + "\t" + idContentsMap.get(obj.getString("id")));

                }

                FileUtil.fileOutput(resultBuilder.toString(),"EUC-KR","data/group_result.csv",false);

            }else{
                //에러 메시지 처리
                System.out.println("error: " + requestMessage );
            }


        }catch(Exception e){
            e.printStackTrace();
        }


    }

}
