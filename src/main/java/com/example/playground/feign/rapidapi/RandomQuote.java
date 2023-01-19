package com.example.playground.feign.rapidapi;

import java.util.ArrayList;
import java.util.List;

public class RandomQuote{
    public int id;
    public String language_code;
    public String content;
    public String url;
    public Originator originator;
    public List<String> tags = new ArrayList<>();
}
