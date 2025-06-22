package edu.whut.SpringAINewsRecommendationSystem.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edu.whut.SpringAINewsRecommendationSystem.data.NewsPOJO;

public class NewsCrawl {
    private static List<NewsPOJO> newsList = new ArrayList<>();

    public static void getNewsByCrawl(String targetUrl){
        newsList.clear();

        Document document = null;
        try{
            document = Jsoup.connect(targetUrl).get();
        }catch(IOException exception){
            exception.printStackTrace();
        }
        if(document == null){

        }else{
            Elements a = document.getElementsByClass("dd_bt");
            String newsUrl = "";
            String title = "";
            for(Element a1: a){
                newsUrl = "https://www.chinanews.com.cn/" + a1.attr("href");
		        title = a1.text();
                Elements c = document.getElementsByClass("dd_time");
			    Element c1 = c.first();
			    String time = c1.text();

                newsList.add(new NewsPOJO(title, time, newsUrl));
            }	    
        }
    }
}
