/** 
 * <pre>
 *  파 일 명 : CnnPointController.java
 *  설    명 : 머신런닝 점수 컨트롤러 
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.07
 *  버    전 : 1.1
 *  수정이력 : 2018.12
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */

package org.moara.rest.api.ml;

import org.moara.common.util.ExceptionUtil;
import org.moara.ml.cnn.SttCnnPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CnnPointController {
	
	private static final Logger logger   = LoggerFactory.getLogger(CnnPointController.class);
	
	@RequestMapping(value = "/ml/cnn/P0001" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getCnnPontValue(@RequestBody String jsonObjectValue) {
		try {
			
			
			return SttCnnPoint.getPointValue(jsonObjectValue);
//			return "0.5131235";
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "-1";
		}
	}
	
	
	@RequestMapping(value = "/ml/cnn/P0002" , method = RequestMethod.POST, produces=MediaType.APPLICATION_JSON_VALUE)
	public String getCnnPont0002(@RequestBody String jsonObjectValue) {
		return SttCnnPoint.getAllPointValue(jsonObjectValue);
		
	}
	
	
	@RequestMapping(value = "/ml/cnn/health" , method = RequestMethod.GET)
	public String health() {
		try {
			
			
			return "";
//			return "0.5131235";
		}catch(Exception e) {
			logger.error(ExceptionUtil.getStackTrace(e));
			return "";
		}
	}
}
