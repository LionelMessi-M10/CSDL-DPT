package com.csdldpt.btl.utils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.multipart.MultipartFile;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

public class TFIDF {
    public static Map<String, Double> calculateTFIDFFromMultipartFile(MultipartFile file) {
        // Khởi tạo StanfordCoreNLP với các tác vụ cần thiết
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        String document = getFileContent(file);
        List<String> words = extractKeywords(document, pipeline);
        Map<String, Integer> tf = calculateTermFrequency(words);
        // Tính toán IDF với một tập hợp các tài liệu, trong trường hợp này là chỉ một tài liệu
        Map<String, Double> idf = calculateInverseDocumentFrequency(Collections.singletonList(tf));
        return calculateTFIDF(tf, idf);
    }

    private static String getFileContent(MultipartFile file) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content.toString();
    }

    private static List<String> extractKeywords(String document, StanfordCoreNLP pipeline) {
        List<String> keywords = new ArrayList<>();
        Annotation annotation = new Annotation(document);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                if (word.matches("[\\p{L}\\p{M}]+")) {  // Lọc chỉ các từ chứa ký tự và dấu điểm
                    keywords.add(word.toLowerCase()); // Chuyển đổi sang chữ thường
                }
            }
        }
        return keywords;
    }

    private static Map<String, Integer> calculateTermFrequency(List<String> words) {
        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String word : words) {
            frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
        }
        return frequencyMap;
    }

    private static Map<String, Double> calculateInverseDocumentFrequency(List<Map<String, Integer>> documents) {
        Map<String, Double> idfMap = new HashMap<>();
        int totalDocuments = documents.size();
        for (Map<String, Integer> document : documents) {
            for (String word : document.keySet()) {
                idfMap.put(word, idfMap.getOrDefault(word, 0.0) + 1);
            }
        }
        for (String word : idfMap.keySet()) {
            double idf = Math.log((double) totalDocuments / (idfMap.get(word) + 1)) + 1;
            idfMap.put(word, idf);
        }
        return idfMap;
    }

    private static Map<String, Double> calculateTFIDF(Map<String, Integer> tf, Map<String, Double> idf) {
        Map<String, Double> tfidf = new HashMap<>();
        for (String word : tf.keySet()) {
            double tfidfValue = tf.get(word) * idf.getOrDefault(word, 0.0);
            tfidf.put(word, tfidfValue);
        }
        return tfidf;
    }
}
