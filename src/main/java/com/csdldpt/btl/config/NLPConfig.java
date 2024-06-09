package com.csdldpt.btl.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;


@Configuration
public class NLPConfig {

    @Bean
    public StanfordCoreNLP stanfordCoreNLP() {
        Properties props = new Properties();
        // Cấu hình các tác vụ cần thiết cho VNCoreNLP
        props.setProperty("annotators", "tokenize, ssplit");
        // Khởi tạo và trả về một StanfordCoreNLP instance
        return new StanfordCoreNLP(props);
    }
}

