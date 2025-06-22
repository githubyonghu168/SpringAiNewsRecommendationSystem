package edu.whut.SpringAINewsRecommendationSystem.utils;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.whut.SpringAINewsRecommendationSystem.data.Interests;
import edu.whut.SpringAINewsRecommendationSystem.data.NewsPOJO;

@Service
public class NewsTools {
    @Autowired
    VectorStore vectorStore;

    @Autowired
    Interests interests;

    // 原先在向Client添加工具就会报错，
    // 原因是OpenAI对Tool的name有要求，
    // 似乎是只能有字母，数字，下划线，连空格都不行（空格不行已验证）
    // OpenAI的文档只有Python和JS代码，难看。
    @Tool(name = "get_news_recommendations", description = "根据用户提供的关键词向用户推荐新闻")
    public List<NewsPOJO> recommendNews(@ToolParam(description = "用户提供的新闻关键词")String keyWords){
        List<Document> b = vectorStore.similaritySearch(SearchRequest.builder()
                                            .query(keyWords)
                                            .topK(3)
                                            .build()); 
        List<NewsPOJO> c = new ArrayList<>();
        for(Document i: b){
            c.add(new NewsPOJO(i.getText(), (String)i.getMetadata().get("link"), (String)i.getMetadata().get("time")));
        }
        return c;
    }

    @Tool(name = "get_news_recommendations_from_local_interests", description = "当用户没有提供关键词时，根据本地记录的用户历史兴趣词向用户推荐新闻")
    public List<NewsPOJO> recommendNewsFromLocalInterests(){
        List<String> allInterests = interests.getInterests();
        List<String> first5Interests;
        if(allInterests.size() < 5){
            first5Interests = allInterests;
        }else{
            first5Interests = new ArrayList<>(5);
            for(int i=0;i<5;i++){
                first5Interests.add(allInterests.get(i));
            }
        }

        String interestsString = "";
        for(String i: first5Interests){
            interestsString += i;
            interestsString += ",";
        }

        List<Document> b = vectorStore.similaritySearch(SearchRequest.builder()
                                            .query(interestsString)
                                            .topK(3)
                                            .build()); 
        List<NewsPOJO> c = new ArrayList<>();
        for(Document i: b){
            c.add(new NewsPOJO(i.getText(), (String)i.getMetadata().get("link"), (String)i.getMetadata().get("time")));
        }
        return c;
    }

    @Tool(name = "get_user_interests", description = "获取用户的历史兴趣词")
    public List<String> getInterests(){
        return interests.getInterests();
    }

    @Tool(name = "add_user_interests", description = "用户要求添加自己的兴趣词")
    public void addInterests(@ToolParam(description = "用户要求添加的兴趣词，以列表传入")List<String> interestWords){
        for(String i: interestWords){
            interests.addInterests(i);
        }
    }
}
