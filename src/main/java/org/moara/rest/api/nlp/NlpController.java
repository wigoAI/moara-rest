package org.moara.rest.api.nlp;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.moara.ara.classification.model.ClassificationModel;
import org.moara.ara.classification.model.ClassificationModelMetaManager;
import org.moara.ara.classification.model.SimpleClassificationResults;
import org.moara.ara.datamining.data.CodeNameScore;
import org.moara.ara.datamining.textmining.TextMining;
import org.moara.ara.datamining.textmining.api.document.JsonDocument;
import org.moara.ara.datamining.textmining.dictionary.ontology.OntologyResult;
import org.moara.ara.datamining.textmining.dictionary.ontology.element.OntologyAnalysisResult;
import org.moara.ara.datamining.textmining.dictionary.ontology.pattern.Interest.InterestPattern;
import org.moara.ara.datamining.textmining.dictionary.ontology.pattern.Interest.PatternDataCombination;
import org.moara.ara.datamining.textmining.document.Document;
import org.moara.ara.datamining.textmining.document.DocumentWord;
import org.moara.common.util.ExceptionUtil;
import org.moara.open.api.ApiMessageCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author macle
 */
@Slf4j
@RestController
public class NlpController {

    @RequestMapping(value = "/document/wcp" , method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    public String documentSentences(@RequestBody String jsonValue){
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            Document document = JsonDocument.makeDocument(jsonValue);
            document.setOntologyDetailResult();
            TextMining.mining(document);
            Map<String, OntologyResult> ontologyMap = document.getOntologyResultMap();
            JsonArray keywords = new JsonArray();

            Collection<DocumentWord> wordColl = document.getDocumentWordCollection();
            if(wordColl.size() > 0){
                DocumentWord [] words = wordColl.toArray(new DocumentWord[0]);
                Arrays.sort(words, DocumentWord.COUNT_SORT_DESC);
                for(DocumentWord word : words){
                    JsonObject keyword = new JsonObject();
                    keyword.addProperty("code", word.getWordCode());
                    keyword.addProperty("text", word.getSyllableValue());
                    keyword.addProperty("count", word.getCount());
                    keywords.add(keyword);
                }
                wordColl.clear();
            }

            List<CodeNameScore> classifyList = new ArrayList<>();

            JsonArray classifies = new JsonArray();

            Collection<ClassificationModel<?>> classificationModels = ClassificationModelMetaManager.getInstance().getClassificationModels();

            for (ClassificationModel<?> model : classificationModels) {
                if (!model.autoClassification()) continue;

                SimpleClassificationResults results = new SimpleClassificationResults(model);
                Map<String, JsonObject> predictResult = results.predictResult(document);

                for (Map.Entry<String, JsonObject> entry : predictResult.entrySet()) {
                    CodeNameScore classify= new CodeNameScore();
                    classify.setCode(entry.getKey());
                    classify.setName(entry.getValue().get("name").getAsString());
                    JsonElement jsonElement = entry.getValue().get("probability");
                    if(jsonElement != null){
                        classify.setScore(jsonElement.getAsDouble());
                    }else{
                        classify.setScore(0);
                    }

                    classifyList.add(classify);
                }

            }

            if(classifyList.size() > 0){
                CodeNameScore [] values = classifyList.toArray(new CodeNameScore [0]);
                Arrays.sort(values, CodeNameScore.SORT_DESC);

                for(CodeNameScore codeNameScore :  values){
                    JsonObject object = new JsonObject();
                    object.addProperty("code", codeNameScore.getCode());
                    object.addProperty("name", codeNameScore.getName());
                    object.addProperty("score", codeNameScore.getScore());
                    classifies.add(object);
                }
                classifyList.clear();
            }


            Collection<OntologyResult> ontologyResultCollection = ontologyMap.values();

            List<PatternCount> patternList = new ArrayList<>();

            for(OntologyResult ontologyResult : ontologyResultCollection){
                if(!ontologyResult.isOntology()){
                    continue;
                }

                List<OntologyAnalysisResult> analysisResultList =  ontologyResult.getOntologyAnalysisResultList();
                for(OntologyAnalysisResult ontologyAnalysisResult : analysisResultList){

                    List<PatternDataCombination> list = InterestPattern.analysis(ontologyAnalysisResult, document);
                    if(list.size() == 0){
                        continue;
                    }


                    PatternCount patternCount = new PatternCount();
                    patternCount.ontologyResult = ontologyResult;
                    patternCount.ontologyAnalysisResult = ontologyAnalysisResult;
                    patternCount.count = list.size();
                    patternList.add(patternCount);
                }
            }

            JsonArray patterns = new JsonArray();
            if(patternList.size() > 0){
                PatternCount [] array = patternList.toArray(new PatternCount[0]);
                Arrays.sort(array, PatternCount.SORT_DESC);
                for(PatternCount patternCount : array){
                    JsonObject object = new JsonObject();

                    object.addProperty("category_code",  patternCount.ontologyResult.getOntologyCode());
                    object.addProperty("category_name",  patternCount.ontologyResult.getOntologyName());
                    object.addProperty("code",  patternCount.ontologyAnalysisResult.getOntologyAnalysisCode());
                    object.addProperty("expression",  patternCount.ontologyAnalysisResult.getExpressionValue());
                    object.addProperty("count",  patternCount.count);

                    patterns.add(object);
                }
                patternList.clear();
            }


            JsonObject result = new JsonObject();
            result.add("keywords", keywords);
            result.add("classifies", classifies);
            result.add("patterns", patterns);
            return gson.toJson(result);
        }catch(Exception e){
            log.error(ExceptionUtil.getStackTrace(e));
            return ApiMessageCode.FAIL;
        }
    }
}
