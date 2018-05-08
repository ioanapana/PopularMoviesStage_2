package com.example.work_pana.popularmoviesstage_one.utils;

public class JsonUtils {
    private String movieListBaseURL = "http://api.themoviedb.org/3";
    private String popularMoviesEndpoint = "/movie/popular?";
    private String topRatedMoviesEndpoint = "/movie/top_rated?";
    private String apiKey = "api_key=df27df00b2916dd211edca6241aea543";
    public String popularMoviesURL = movieListBaseURL + popularMoviesEndpoint + apiKey;
    public String topRatedMoviesURL = movieListBaseURL + topRatedMoviesEndpoint + apiKey;
    private String baseImageUrl = "http://image.tmdb.org/t/p/w185";


}
