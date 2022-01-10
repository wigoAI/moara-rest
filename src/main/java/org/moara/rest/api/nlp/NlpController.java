package org.moara.rest.api.nlp;

import lombok.extern.slf4j.Slf4j;
import org.moara.ara.datamining.textmining.api.wcp.DocumentWcp;
import org.moara.common.util.ExceptionUtil;
import org.moara.open.api.ApiMessageCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author macle
 */
@Slf4j
@RestController
public class NlpController {

    @RequestMapping(value = "/document/wcp" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String documentSentences(@RequestBody String jsonValue){
        try {
            return  DocumentWcp.jsonObject(jsonValue);
        }catch(Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
            return ApiMessageCode.FAIL;
        }
    }
}
