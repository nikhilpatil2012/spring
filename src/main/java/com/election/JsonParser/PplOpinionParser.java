/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.JsonParser;

import com.election.model.CustomTweet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.ArrayList;

/**
 *
 * @author deadcode
 */
public class PplOpinionParser {
  
     public static void ParseJson(String data, CustomTweet customTweet){
         
            JsonParser parse = new JsonParser();
            JsonElement jsonElement = parse.parse(data);
            JsonObject masterObject = jsonElement.getAsJsonObject();
         
            
            // #1 Parse Document Sentiments from the json
            
             ParseSentiments(masterObject, customTweet);
             
           //  #2 Parse Keywords from the json
           
            ParseKeywords(masterObject, customTweet);
           
     }
     
     private static void ParseSentiments(JsonObject masterObject, CustomTweet customTweet){
         
               
              if(masterObject.has("docSentiment")) {
                  
                JsonObject sentimentObject = masterObject.getAsJsonObject("docSentiment");

                         if(sentimentObject.has("type")){
                            customTweet.setType(sentimentObject.get("type").getAsString());
                            
                        }
                        
                          if(sentimentObject.has("score")){
                            customTweet.setScore(sentimentObject.get("score").getAsDouble());
                        }
                          
                          if(sentimentObject.has("mixed")){
                              customTweet.setMixed(sentimentObject.get("mixed").getAsString());
                          }
                          
            } // docSentiment ends here
          
     }
 
     private static void ParseKeywords(JsonObject masterObject, CustomTweet customTweet){
          
                if(masterObject.has("keywords"))
               {
                   JsonArray keywords = masterObject.getAsJsonArray("keywords");
                    
                       if(keywords.size() > 0)
                    {
                        ArrayList<String> tweetKeywords = new ArrayList<String>();
                        
                        for(int i=0; i<keywords.size(); i++){
                         
                            JsonObject word = keywords.get(i).getAsJsonObject();
                             
                              if(word.has("text")){
                                  
                                   tweetKeywords.add(word.get("text").getAsString());
                              }
                        }
                        
                         customTweet.setTweetKeywords(tweetKeywords);
                    }
                   
               } // keywords ends here
          
         
     }
     
}
