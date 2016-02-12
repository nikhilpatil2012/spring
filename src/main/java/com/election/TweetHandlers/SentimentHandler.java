 
// This class will fetech the sentiments and keywords from the given tweet.
 
package com.election.TweetHandlers;

import com.election.JsonParser.PplOpinionParser;
import com.election.model.Candidate;
import com.election.model.CustomTweet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import twitter4j.Status;

/**
 *
 * @author deadcode
 */


public class SentimentHandler extends Thread{
    
    private TweetStorageHandler tweetStorageHandler;
    private Status tweet;
    private CyclicBarrier cyclicBarrier; 
    private CustomTweet customTweet;
    private final Candidate candidate;
    
     public SentimentHandler(CyclicBarrier cyclicBarrier, TweetStorageHandler tweetStorageHandler, Status tweet, Candidate candidate){
          
          this.cyclicBarrier = cyclicBarrier;
          this.tweetStorageHandler = tweetStorageHandler;
          this.tweet = tweet;
          this.candidate = candidate;
          
          customTweet = tweetStorageHandler.createNewCustomTweet(tweet, candidate.getId());          
     }

    @Override
    public void run() {
     
        try {
        
            String url = "http://gateway-a.watsonplatform.net/calls/text/TextGetCombinedData";
            
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost post = new HttpPost(url);
            
            // add header
            post.setHeader("content-type", "application/x-www-form-urlencoded");
            
            List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
            urlParameters.add(new BasicNameValuePair("outputMode", "json"));
            urlParameters.add(new BasicNameValuePair("apikey", "3bd40a92a64d5750303bd6a159a20ff576f48c13"));
            urlParameters.add(new BasicNameValuePair("extract", "keyword,doc-sentiment,relation,entity,concept"));
            urlParameters.add(new BasicNameValuePair("text", customTweet.getText()));
            
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            
            HttpResponse response = client.execute(post);
            System.out.println("Response Code : "
                    + response.getStatusLine().getStatusCode());
            
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            
            rd.close();
            
            PplOpinionParser.ParseJson(result.toString(), customTweet);
            
            tweetStorageHandler.updateCustomTweet(customTweet);
            
            cyclicBarrier.await();
            
             System.out.println("Thread Over");
            
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SentimentHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SentimentHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(SentimentHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BrokenBarrierException ex) {
                
              System.out.println("Barrier Broken "+ex.getMessage());
        } 
       
    }
     
      public void parseJson(String data)
     {
            JsonParser parse = new JsonParser();
            JsonElement jsonElement = parse.parse(data);
            JsonObject master = jsonElement.getAsJsonObject();
            
            System.out.println(master.toString());
            
            
              if(master.has("docSentiment")) {
                  
                JsonObject sentimentObject = master.getAsJsonObject("docSentiment");

                         if(sentimentObject.has("type")){
                            customTweet.setType(sentimentObject.get("type").toString());
                        }
                        
                          if(sentimentObject.has("score")){
                            customTweet.setScore(sentimentObject.get("score").getAsDouble());
                        }
                          
                          if(sentimentObject.has("mixed")){
                              customTweet.setMixed(sentimentObject.get("mixed").toString());
                          }
                          
            } // docSentiment ends here
              
              if(master.has("entities")){
                   
                   JsonArray entities = master.getAsJsonArray("entities");
                    
                       if(entities.size() > 0)
                    {
                        ArrayList<String> entitiesList = new ArrayList<String>();
                        
                        
                        for(int i=0; i<entities.size(); i++){
                         
                            JsonObject entity = entities.get(i).getAsJsonObject();
                             
                              if(entity.has("text")){
                                  
                                   entitiesList.add(entity.get("text").toString());
                              }
                        }
                          
                         customTweet.setEntities(entitiesList);
                    }                  
              } // entities ends here
              
                 if(master.has("keywords"))
               {
                   JsonArray keywords = master.getAsJsonArray("keywords");
                    
                       if(keywords.size() > 0)
                    {
                        ArrayList<String> tweetKeywords = new ArrayList<String>();
                        
                        for(int i=0; i<keywords.size(); i++){
                         
                            JsonObject word = keywords.get(i).getAsJsonObject();
                             
                              if(word.has("text")){
                                  
                                   tweetKeywords.add(word.get("text").toString());
                              }
                        }
                        
                         customTweet.setTweetKeywords(tweetKeywords);
                    }
                   
               } // keywords ends here
              
              
                     if(master.has("relations")){
                   
                   JsonArray relations = master.getAsJsonArray("relations");
                    
                       if(relations.size() > 0)
                    {
                        ArrayList<String> relationsList = new ArrayList<String>();
                   
                        
                        
                        for(int i=0; i<relations.size(); i++){
                         
                            JsonObject relation = relations.get(i).getAsJsonObject().getAsJsonObject("object");
                             
                              if(relation.has("text")){
                                  
                                   relationsList.add(relation.get("text").toString());
                              }
                        }
                          
                         customTweet.setRelations(relationsList);
                    }                  
                       
              } // relation ends here
                            
               tweetStorageHandler.updateCustomTweet(customTweet);
            }
      
}
