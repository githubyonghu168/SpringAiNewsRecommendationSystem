package edu.whut.SpringAINewsRecommendationSystem.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.whut.SpringAINewsRecommendationSystem.client.NewsAssistantClient;
import edu.whut.SpringAINewsRecommendationSystem.data.RequestData;
import edu.whut.SpringAINewsRecommendationSystem.utils.EmbedAndStore;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/")
public class MyController {
    Logger log = LoggerFactory.getLogger(MyController.class);
    static boolean embeded = false;
    @Autowired
    NewsAssistantClient newsAssistantClient;

    @Autowired
    EmbedAndStore embedAndStore;

    @PostMapping("/")
    public Flux<String> streamOutput(@RequestBody RequestData requestData){
        if(!embeded){
            embedAndStore.embedAndStore();
            embeded = true;
        }
        return newsAssistantClient.chat(requestData.input);
    }                
}
