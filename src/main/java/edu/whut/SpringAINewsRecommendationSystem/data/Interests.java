package edu.whut.SpringAINewsRecommendationSystem.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class Interests {
    private SortedSet<String> interests;

    public Interests() {
        this.interests = new TreeSet<>();
        ClassPathResource classPathResource = new ClassPathResource("/rag/interests");
        File file = null;
        try {
            file = classPathResource.getFile();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //File file = new File("/resources/rag/interests");
        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8));
            String line = null;
            while (true) {
                line = bufferedReader.readLine();
                if(line == null){
                    break;
                }
                String[] words = line.split(",");
                for(String i: words){
                    interests.add(i);
                }
            }
            bufferedReader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public List<String> getInterests() {
        List<String> a = new ArrayList<>();
        for(String i: interests){
            a.add(i);
        }
        return a;
    }

    public void addInterests(String newInterest){
        interests.add(newInterest);
    }

    public int size(){
        return this.interests.size();
    }
}
