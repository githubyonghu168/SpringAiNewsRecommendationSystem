package edu.whut.SpringAINewsRecommendationSystem.client;

import java.time.LocalDate;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import edu.whut.SpringAINewsRecommendationSystem.utils.NewsTools;
import reactor.core.publisher.Flux;

@Service
public class NewsAssistantClient {

	private final ChatClient chatClient;

	public NewsAssistantClient(ChatClient.Builder builder, VectorStore vectorStore, ChatMemory chatMemory, NewsTools newsTools){
		ChatClient chatClient = builder.defaultSystem("""
										你是一位新闻阅读助手，目的是帮助用户获取他感兴趣的新闻，并提供对新闻内容的总结。
										如果需要，你可以调用相应函数辅助完成。
										请以中文进行输出。
										今天的日期是 {current_date}。
										""")
										.defaultAdvisors(PromptChatMemoryAdvisor.builder(chatMemory).build(),
                                                        new QuestionAnswerAdvisor(vectorStore),
                                                        SimpleLoggerAdvisor.builder().build()
										            )
										.defaultTools(newsTools)
										.build();
		this.chatClient = chatClient;
	}

	public Flux<String> chat(String input){
        return chatClient.prompt()
                        .system(s -> s.param("current_date", LocalDate.now().toString()))
                        .user(input)
						.stream()
                        .content();
    }
}
