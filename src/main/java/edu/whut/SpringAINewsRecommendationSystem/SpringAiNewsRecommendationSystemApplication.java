package edu.whut.SpringAINewsRecommendationSystem;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import edu.whut.SpringAINewsRecommendationSystem.utils.NewsCrawl;

@SpringBootApplication
@EnableScheduling
public class SpringAiNewsRecommendationSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiNewsRecommendationSystemApplication.class, args);
	}

	/*
		经debug查看变量内容，发现不必写EmbeddingModel的Bean对象，
		因为SpringBoot会自动产生，若要进行个性化调整可重新声明Bean对象；
		也不必在.properties配置文件中写需要使用的嵌入模型的链接、key之类的，
		因为会通过OpenAI的链接和key去调用嵌入模型，对于其他公司的模型，不清楚是否有同样的特性；
		也不必声明向量数据库的本地信息，CharMemory会自动保存对话，VectorStore会自动将对话嵌入后保存。
	 * 
	*/
	//@Bean
	//public EmbeddingModel embeddingClient() {	
	//}
	@Bean
	VectorStore setVectorStore(EmbeddingModel embeddingModel){
		return SimpleVectorStore.builder(embeddingModel).build();
	}

	@Bean
	public ChatMemory chatMemory() {
		return MessageWindowChatMemory.builder().build();
	}

	@Scheduled(cron ="0 0 * * *  ?")
	public void autoCrawl() {
		NewsCrawl.getNewsByCrawl("https://www.chinanews.com.cn/importnews.html");
	}
}
