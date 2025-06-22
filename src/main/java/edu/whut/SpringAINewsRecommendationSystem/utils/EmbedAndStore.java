package edu.whut.SpringAINewsRecommendationSystem.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class EmbedAndStore {
    @Autowired
    VectorStore vectorStore;

    public void embedAndStore(){
        ClassPathResource classPathResource = new ClassPathResource("/rag/news");
        File file = null;
        try {
            file = classPathResource.getFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        BufferedReader bufferedReader;
        List<Document> documents = new ArrayList<>();
        try {
            bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            String line = null;
            while (true) {
                line = bufferedReader.readLine();
                if(line == null){
                    break;
                }
                String[] words = line.split("#");
                for(int i=0;i<words.length;i+=3){
                    Document a = Document.builder()
                                        .text(words[i])
                                        .metadata("link",words[i+1])
                                        .metadata("time", words[i+2])
                                        .build();
                    documents.add(a);
                }
            }
            
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        vectorStore.write(new TokenTextSplitter().transform(documents));
    }
}
