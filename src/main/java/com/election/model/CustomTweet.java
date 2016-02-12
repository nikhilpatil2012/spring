/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.model;

import java.util.ArrayList;

/**
 *
 * @author deadcode
 */
public class CustomTweet {
     
     private String id = "";
     private String createdAt = "";
     private String text = "";
     private ArrayList<String> tweetKeywords = new ArrayList<String>();
     private ArrayList<String> entities = new ArrayList<String>();    
     private ArrayList<String> relations = new ArrayList<String>();    
     private Double score = 0.0;
     private String mixed = "";     
     private String type = "";
     private String source = "";

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
     
     

    public String getCreatedAt() {
        return createdAt;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Double getScore() {
        return score;
    }

    public String getMixed(){
        return mixed;
    }
    

    public String getType() {
        return type;
    }

    public ArrayList<String> getRelations() {
        return relations;
    }

    
    
    public ArrayList<String> getTweetKeywords() {
        return tweetKeywords;
    }

    public ArrayList<String> getEntities() {
        return entities;
    }
    
    
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMixed(String mixed) {
        this.mixed = mixed;
    }

    
    public void setScore(Double score) {
        this.score = score;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setTweetKeywords(ArrayList<String> tweetKeywords) {
        this.tweetKeywords = tweetKeywords;
    }

    public void setEntities(ArrayList<String> entities) {
        this.entities = entities;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public void setRelations(ArrayList<String> relations) {
        this.relations = relations;
    }
 
    
    
}
