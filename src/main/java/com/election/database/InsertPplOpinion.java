/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.database;

import com.election.model.CustomTweet;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author deadcode
 */
public class InsertPplOpinion {
    
    
    private  enum DatabaseFields  {
 
       cand_id, post_src,  created_at, post_type, sum_positive, sum_negative, sum_mixed, sum_neutral, tweet_keywords

}
    
    public static void master(ArrayList<CustomTweet> tweetList){
         
        
          Document masterDoc = new Document();
          
          
          // #0 Initialize the document
          
           initializeDoc(masterDoc, tweetList);
          
         // #1 Aggregate all the tweets based on sentiments
   
          aggreateTweetsBasedOnSentiments(masterDoc, tweetList);
          
          // #2 Aggreate all the unique keywords
          
          aggregateKeywords(masterDoc, tweetList);
        
          // All the tweets are aggragated, let's insert into DB
          
              
          
          if(!masterDoc.isEmpty()){
          
              
              System.out.println(masterDoc.toJson());
                          
              MongoStatic.getRequiredCollection(MongoStatic.CollectionNames.CandidatePosts.name()).insertOne(masterDoc);
          }
          
    }
    
    private static void initializeDoc(Document masterDoc, ArrayList<CustomTweet> tweetList){
        
           try {
             
             CustomTweet firstTweet = tweetList.get(0); // first tweet
             
             Date lastDate = new SimpleDateFormat("yyMMddHHmmssZ").parse(firstTweet.getCreatedAt());
             
             for(CustomTweet tweet : tweetList) {
                 
                   Date currentDate = new SimpleDateFormat("yyMMddHHmmssZ").parse(tweet.getCreatedAt());
                  
                    if(lastDate.after(currentDate))
                {
                     lastDate = currentDate;
                }
                    
                    System.out.println(currentDate.toString());
                  
             }
             
             masterDoc.append(DatabaseFields.cand_id.name(), firstTweet.getId());
             masterDoc.append(DatabaseFields.created_at.name(), lastDate.toString());
             masterDoc.append(DatabaseFields.post_type.name(), MongoStatic.TweetType.Ppl_Opinion_Abt_Cand.name());
             masterDoc.append(DatabaseFields.post_src.name(), firstTweet.getSource());
             
         } catch (ParseException ex) {
             Logger.getLogger(InsertPplOpinion.class.getName()).log(Level.SEVERE, null, ex);
         }
         
    }
    
    private static void aggregateKeywords(Document masterDoc, ArrayList<CustomTweet> tweetList){
                
          HashSet<String> uniqueKeywords = new HashSet<>();
        
         for(CustomTweet tweet : tweetList) {
             
             if(tweet.getTweetKeywords().size() > 0){
                 
                 uniqueKeywords.addAll(tweet.getTweetKeywords());
             }
              
         }
        
         masterDoc.append(DatabaseFields.tweet_keywords.name(), uniqueKeywords);
    }
    
    private static void aggreateTweetsBasedOnSentiments(Document masterDoc, ArrayList<CustomTweet> tweetList){
        
         int positive = 0, negative  = 0, mixed = 0, neutral = 0;
        
        for(CustomTweet tweet : tweetList) {
             
                     // aggregate tweets as count on
                
                   System.out.println(tweet.getType());
                     
                        if(tweet.getType().equals(MongoStatic.SentimentType.positive.name()))
                     {
                         ++positive;
                          System.out.println(positive);
                     }
                       else if(tweet.getType().equals(MongoStatic.SentimentType.negative.name()))
                     {
                         ++negative;                         
                          System.out.println(negative);
                     }
                      else if(tweet.getType().equals(MongoStatic.SentimentType.neutral.name()))
                     {
                         ++neutral;
                     }
                      else if(tweet.getMixed().equals("1"))
                     {
                         ++mixed;
                          System.out.println(mixed);
                     }
                        
        } // for ends here 
        
        // Set all aggreagated opinions
        
        masterDoc.append(DatabaseFields.sum_positive.name(), String.valueOf(positive));
        masterDoc.append(DatabaseFields.sum_negative.name(), negative);
        masterDoc.append(DatabaseFields.sum_mixed.name(), mixed);
        masterDoc.append(DatabaseFields.sum_neutral.name(), neutral);
        
    }
    
}
