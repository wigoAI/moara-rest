package org.moara.rest.api.nlp;

import org.json.JSONObject;
import org.moara.ara.cluster.DocumentSimilarity;
import org.moara.ara.datamining.textmining.TextMining;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.ara.datamining.textmining.similarity.compare.SimilarityDocumentCompare;
import org.moara.ara.datamining.textmining.similarity.compare.SimilarityDocumentResult;
import org.moara.ara.datamining.textmining.similarity.document.SimilarityDocument;
import org.moara.ara.datamining.textmining.similarity.learn.SimilarityLearnType;
import org.moara.common.code.LangCode;
import org.moara.common.util.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
/**
 * <pre>
 *  파 일 명 : SimilarityApiController.java
 *  설    명 : 유사도 api
 *
 *
 *  작 성 자 : macle(김용수)
 *  작 성 일 : 2019.11.20
 *  버    전 : 1.0
 *  수정이력 :
 *  기타사항 :
 * </pre>
 * @author Copyrights 2019 by ㈜모아라. All right reserved.
 */
public class SimilarityApiController {
    private static final Logger logger   = LoggerFactory.getLogger(SimilarityApiController.class);

    @RequestMapping(value = "/similarity/equals/max" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Double similarityEqualsMax(@RequestBody String jsonValue) {
        try {
            JSONObject obj = new JSONObject(jsonValue);
            String contents = obj.getString("source");
            String compare = obj.getString("target");

            SimilarityDocument similarityDocument = new SimilarityDocument();
            Document document = new Document();
            document.setDocType(Document.SNS);
            document.setLangCode(LangCode.KO);
            document.setContents(contents);
            TextMining.mining(document);
            similarityDocument.setLearnType(SimilarityLearnType.DATA_EQUALS);
            similarityDocument.learn(document);
            similarityDocument.setMinPercent(0.2);

            Document compareDocument = new Document();
            compareDocument.setDocType(Document.SNS);
            compareDocument.setLangCode(LangCode.KO);
            compareDocument.setContents(compare);
            TextMining.mining(compareDocument);
            SimilarityDocumentResult result = SimilarityDocumentCompare.compare(similarityDocument, compareDocument);
            if(result.isResult()){
                return result.getMaxPercent().getPercent();
            }else{
                return 0.0;
            }
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return -1.0;
        }
    }

    @RequestMapping(value = "/similarity/cosine" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public Double similarityCosine(@RequestBody String jsonValue) {
        try {
            JSONObject obj = new JSONObject(jsonValue);
            String contents = obj.getString("source");
            String compare = obj.getString("target");
            return DocumentSimilarity.compareDocument(contents, compare);
        }catch(Exception e) {
            logger.error(ExceptionUtil.getStackTrace(e));
            return -1.0;
        }
    }

}
