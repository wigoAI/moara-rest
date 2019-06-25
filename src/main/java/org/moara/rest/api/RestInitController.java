/** 
 * <pre>
 *  파 일 명 : RestInitController.java
 *  설    명 : page init 성공테스트용
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2018.07
 *  버    전 : 1.0
 *  수정이력 : 
 *  기타사항 :
 * </pre>
 * @author Copyrights 2018 by ㈜모아라. All right reserved.
 */

package org.moara.rest.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestInitController {
	
	@RequestMapping(value = "/init" )
	public String init() {
		return "init sucess";
	}
}
