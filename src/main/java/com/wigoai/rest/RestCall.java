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
package com.wigoai.rest;

import org.moara.common.util.ExceptionUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * rest util
 * @author macle
 */
@SuppressWarnings("unused")
public class RestCall {


    /**
     * rest 호출후 결과 받기
     * post Json 으로 호출
     * 연결시간 30초
     * @param address String address
     * @param value String value
     * @param timeOut millisecond
     * @return String result
     */
    public static String postJson(String address, String value, int timeOut){

        try {
            URL url = new URL(address );
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");

            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            conn.setUseCaches(false);
            if(value != null && value.length() >0) {
                OutputStream stream = conn.getOutputStream();
                stream.write(value.getBytes());
                stream.flush();
                stream.close();
            }
            String charSet = "UTF-8";
            StringBuilder message = new StringBuilder();
            BufferedReader br;

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

                br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream(), charSet));

                for (;;) {
                    String line = br.readLine();
                    if (line == null) break;
                    message.append('\n').append(line);
                }
                br.close();

            }else{
                throw new RuntimeException("http response fail: " + conn.getResponseCode());
            }

            return message.substring(1);
        }catch(Exception e){
            throw new RuntimeException(ExceptionUtil.getStackTrace(e));
        }
    }

}
