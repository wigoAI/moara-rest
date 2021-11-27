package org.moara.rest.api.nlp;

import org.moara.ara.datamining.textmining.dictionary.ontology.OntologyResult;
import org.moara.ara.datamining.textmining.dictionary.ontology.element.OntologyAnalysisResult;

import java.util.Comparator;

/**
 * @author macle
 */
public class PatternCount {

    public final static Comparator<PatternCount> SORT_DESC = (c1, c2) -> Integer.compare(c2.count, c1.count);

    OntologyResult ontologyResult;
    OntologyAnalysisResult ontologyAnalysisResult;


    int count;
}
