package org.moara.rest.api.crawling;

import org.json.JSONObject;
import org.moara.common.util.ExceptionUtil;
import org.moara.moa.crawling.core.http.HttpUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * <pre>
 *  파 일 명 : CrawlingController.java
 *  설    명 :
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2020.01
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2020 by ㈜WIGO. All right reserved.
 */
@RestController
public class CrawlingController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlingController.class);


    @RequestMapping(value = "/crawling/http/script" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String extractSentence(@RequestBody String message) {
        try {
            JSONObject messageObj = new JSONObject(message);
            JSONObject optionData = null;
            if(!messageObj.isNull("option_data")) {
                Object obj = messageObj.get("option_data");
                if(obj.getClass() == String.class){
                    optionData = new JSONObject((String) obj);
                }else{
                    optionData = (JSONObject)obj;
                }
            }

            return HttpUrl.getScript(messageObj.getString("url"), optionData);
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return "{}";
        }
    }

}
