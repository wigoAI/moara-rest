package org.moara.example.api;

import org.moara.common.config.ConfigSet;
import org.moara.engine.MoaraEngine;

/**
 * <pre>
 *  파 일 명 : EngineStater.java
 *  설    명 : 엔진 실행기 ( 테스트옹)
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2020.02
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2020 ㈜WIGO. All right reserved.
 */
public class EngineStater {
    public static void main(String[] args) {
        ConfigSet.setPath("config/config.xml");
        MoaraEngine.newInstance("macle");
    }
}
