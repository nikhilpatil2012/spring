/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.TweetHandlers;

import com.election.database.InsertPplOpinion;
import com.election.model.Candidate;
import com.election.model.CustomTweet;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.AsyncTwitter;
import twitter4j.AsyncTwitterFactory;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterException;
import twitter4j.TwitterMethod;
import twitter4j.auth.AccessToken;

/**
 *
 * @author deadcode
 */
public class TweetHandler extends Thread{

    private final TweetStorageHandler tweetStorageHandler;
    private final Candidate candidate;
    
    private final Object lock = new Object();

    TweetHandler(Candidate candidate) {
        this.candidate = candidate;
        tweetStorageHandler = new TweetStorageHandler();
    }
    
   public void run() {
      
        // Get last 10 tweet and remove the redundant ones
        
        AsyncTwitterFactory factory = new AsyncTwitterFactory();
        final AsyncTwitter twitter = factory.getInstance();
        twitter.setOAuthConsumer("0waOxbGfCshSlz8t7QFPg", "kGcE7XZXTm0JQMbNObGzFG73QBVzYQEYUW4WQrmH88");               
        twitter.setOAuthAccessToken(new AccessToken("839606545-VAxHUWKMLB67nJnHPoHrWKvzfzFcjN1v4jMkZwr8", "pn1XRX4emt8NsmB5udXpwxmMBrPtzW06nR5mS4EoaZ6eq"));                
        
        twitter.addListener(new TwitterAdapter(){
            @Override
            public void searched(QueryResult queryResult) {
            
                
      CyclicBarrier cyclicBarrier = new CyclicBarrier(queryResult.getTweets().size(), new Runnable() {
       public void run() {
        
           // Barrier has reached, Insert all the tweet's into database with prior aggregation
           
           updateDatabase(tweetStorageHandler.getCustomTweetSet());
                
           for(CustomTweet customTweet : tweetStorageHandler.getCustomTweetSet()){
               
               if(customTweet.getType() != null){
                   System.out.println("Yuppy -- "+customTweet.getType());
               }
               
               if(customTweet.getScore() != null){
                   
                  System.out.println(customTweet.getScore());                  
               }               
               
               if(customTweet.getTweetKeywords() !=  null){
                    
                   for(String s : customTweet.getTweetKeywords()){
                         
                        System.out.println("Key "+s);
                   }
                   
               }
               
            /*   
               if(customTweet.getEntities()!=  null){
                    
                   for(String s : customTweet.getEntities()){
                         
                        System.out.println("Ent "+s);
                   }
 
               }
               
               
               if(customTweet.getRelations()!=  null){
                    
                   for(String s : customTweet.getRelations()){
                         
                        System.out.println("Rel "+s);
                   }
                 
               } */
               
           }
                 synchronized(lock){
                    lock.notify();
                  }
            
       }
   });
      
       tweetStorageHandler.setSize(queryResult.getTweets().size());
                
                   for(Status tweet : queryResult.getTweets())
                {   
                    SentimentHandler sentimentHandler = new SentimentHandler(cyclicBarrier, tweetStorageHandler, tweet, candidate);
                    sentimentHandler.start();                    
                }         
                   
            }
            
            @Override
            public void gotUserTimeline(ResponseList<Status> statuses) {
            
            }
            
            @Override
            public void onException(TwitterException te, TwitterMethod method) {
             
                    if (method == TwitterMethod.SEARCH) {
                                synchronized(lock){
                        lock.notify();
                    }
                }
                  else if(te.getErrorMessage() != null) {
                       
                      synchronized(lock){
                        lock.notify();
                    }
                      
                  }                
            }
            
        });
     
        Query query = new Query(candidate.getTwitter_handle());
        query.setCount(5);        
        
        twitter.search(query);
        
        
        synchronized(lock){
            try {
                lock.wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(TweetHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

   public void updateDatabase(ArrayList<CustomTweet> list){
        
    final ExecutorService executor = Executors.newSingleThreadExecutor(); 
    
   executor.submit(() -> {
    
           InsertPplOpinion.master(list);
           
           executor.shutdown();
    
});
   

   }
   
}
