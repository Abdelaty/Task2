package com.example.task2.API;

import com.example.task2.POJO.News.NewsExample;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface NEWS_API {
    @GET("top-headlines?apiKey=a4dd44d5d0c541c6b3ad76696dc69604")
    Call<NewsExample> getNews(@Query("country") String country);
}
//"?action=get_events&from=" + from + "&to=" + toDate + "&league_id=62&APIkey=" + API_KEY
