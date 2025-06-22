package edu.whut.SpringAINewsRecommendationSystem.data;

public class NewsPOJO {
    private String title;
    private String time;
    private String url;
    
    public NewsPOJO() {
    }
    public NewsPOJO(String title, String time, String url) {
        this.title = title;
        this.time = time;
        this.url = url;
    }


    public String getTitle() {
        return title;
    }
    public String getTime() {
        return time;
    }
    public String getUrl() {
        return url;
    }


    public void setTitle(String title) {
        this.title = title;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setUrl(String url) {
        this.url = url;
    }  
}
