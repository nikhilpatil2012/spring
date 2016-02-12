/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.database;

import com.election.model.Candidate;
import com.election.model.CustomTweet;
import com.election.model.Issue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.MongoWriteConcernException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bson.Document;

/**
 *
 * @author deadcode
 */
public class MongoStatic {
    
   public static enum SentimentType {
        
       positive, negative, neutral
   } 
    
    public static enum CollectionNames  {
        
        Candidates, CandidatePosts, Issues
    }
    
    public static enum TweetType {
    
         Ppl_Opinion_Abt_Cand, Cand_Opinion_Abt_Issue
}
    
    public static String[] documentKeys = {"id","name","occupation","party_type", "twitter_handle", "quote"};
    
    public static String[] postsKeys = {"cand_id", "post_src",  "created_at", "post_type", "sum_positive", "sum_negative", "sum_mixed", "sum_neutral", "tweet_keywords"};
    
    public static MongoCollection<Document> getRequiredCollection(String name){
          
         
        return new MongoClient("localhost").getDatabase("election").getCollection(name);
    }
    
    private static ExecutorService getExecuter(){
        return Executors.newSingleThreadExecutor();
    }
    
    public static void addIssue(Issue issue){
        
        ExecutorService executor = getExecuter();
        executor.submit(() -> {
        
         try {
         
            getRequiredCollection(CollectionNames.Issues.name()).insertOne(new Document()
                            .append("name", issue.getName())
                            .append("description", issue.getDescription()));            
         }
         
           catch(MongoWriteException | MongoWriteConcernException mex){
           
                System.out.println("Mongo Exception "+mex.getMessage());
           } 
        
           executor.shutdown(); 
        
        });
                  
    }
    
    public static void updateTweet(CustomTweet customTweet){
      
         MongoCollection<Document> collection = getRequiredCollection(CollectionNames.CandidatePosts.name());
          
         // inserting posts
         
         try {
         
            collection.insertOne(new Document()
                 .append(postsKeys[0], customTweet.getId())
                 .append(postsKeys[1], customTweet.getSource())
                 .append(postsKeys[2], customTweet.getCreatedAt())
                 .append(postsKeys[3], customTweet.getText())                 
                 .append(postsKeys[4], customTweet.getTweetKeywords())
                 .append(postsKeys[5], customTweet.getEntities())
                 .append(postsKeys[6], customTweet.getRelations())
                 .append(postsKeys[7], customTweet.getScore())
                 .append(postsKeys[8], customTweet.getMixed())
                 .append(postsKeys[9], customTweet.getType()));
         
         }
           catch(MongoWriteException | MongoWriteConcernException mex){
           
                System.out.println("Mongo Exception "+mex.getMessage());
           }
    }
    
    public static List<Issue> getAllIssues(){
        
        
        MongoCollection<Document> collection = getRequiredCollection(CollectionNames.Issues.name());
        
        List<Issue> issues = new ArrayList<>();
        
        FindIterable<Document> data =  collection.find();
        
        ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
       
          
          for(Document d : data){
         
            try {
                
                Issue issue = mapper.readValue(d.toJson(), Issue.class);   
                
                
                issues.add(issue);
            } catch (IOException ex) {
                Logger.getLogger(MongoStatic.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
        
            return issues;
    }
    
    public static List<Candidate> getAllCandidates(){
        
        MongoCollection<Document> collection = getRequiredCollection(CollectionNames.Candidates.name());
        
        List<Candidate> candidates = new ArrayList<>();
        
        FindIterable<Document> data =  collection.find();
        
        ObjectMapper mapper = new ObjectMapper();
        
         for(Document d : data){
         
            try {
                
                Candidate candidate = mapper.readValue(d.toJson(), Candidate.class);
                                              
                candidates.add(candidate);
            } catch (IOException ex) {
                Logger.getLogger(MongoStatic.class.getName()).log(Level.SEVERE, null, ex);
            }
         }
         
         return candidates;
    }
    
        public static void InsertCandidatesDocument()
     {
        MongoCollection<Document> collection = getRequiredCollection(CollectionNames.Candidates.name());
        
        
        // Inserting Democrates
        
        collection.insertOne(new Document()
                .append(documentKeys[0], "hillary_clinton_2016")
                .append(documentKeys[1], "Hillary Clinton")
                .append(documentKeys[2],"FORMER SECRETARY OF STATE")
                .append(documentKeys[3],"DEMOCRAT")
                .append(documentKeys[4],"@HillaryClinton")
                .append(documentKeys[5], "We're going to fight for real solutions that make a real difference in people's lives"));
                
        
        
        collection.insertOne(new Document()
                .append(documentKeys[0], "bernie_sanders_2016")
                .append(documentKeys[1], "Bernie Sanders")
                .append(documentKeys[2], "UNITED STATES SENATOR") 
                .append(documentKeys[3], "DEMOCRAT")
                .append(documentKeys[4],"@BernieSanders")
                .append(documentKeys[5], "We need trade policies that are designed for the American worker, not for the multinational corporations who are richer than ever before."));
        
        collection.insertOne(new Document()
                .append(documentKeys[0], "martin_o'malley_2016")
                .append(documentKeys[1], "Martin O’Malley")
                .append(documentKeys[2], "FORMER MARYLAND GOVERNOR")
                .append(documentKeys[3], "DEMOCRAT")
                .append(documentKeys[4],"@MartinOMalley")
                .append(documentKeys[5],"We need to expand—not cut or “enhance”—Social Security so our seniors can retire in dignity, not poverty. "));
        
        // Inserting Republicans
      
        collection.insertOne(new Document()
                .append(documentKeys[0], "donald_j_trump_2016")
                .append(documentKeys[1], "Donald J. Trump")
                .append(documentKeys[2], "REAL ESTATE MOGUL")
                .append(documentKeys[3], "REPUBLICAN")        
                .append(documentKeys[4],"@realDonaldTrump")
                .append(documentKeys[5],"ISIS is making big threats today - no respect for U.S.A. or our \"leader\" - If I win it will be a very different story,with very fast results"));

        collection.insertOne(new Document()
                .append(documentKeys[0], "ted_cruz_2016")
                .append(documentKeys[1], "Ted Cruz")
                .append(documentKeys[2], "UNITED STATES SENATOR")
                .append(documentKeys[3], "REPUBLICAN")
                .append(documentKeys[4], "@tedcruz")
                .append(documentKeys[5],"Every year we spend roughly $500 billion on tax compliance. That is roughly the budget of our entire military, entirely wasted on tax compliance."));

/*          
        collection.insertOne(new Document()
                .append(documentKeys[0], "")
                .append(documentKeys[1], "")
                .append(documentKeys[2], "")
                .append(documentKeys[3], ""));
*/
                    
     }
        
        public static boolean renameValueById(String id, String field, String value, String collectionName){
             
            MongoCollection<Document> collection = getRequiredCollection(collectionName);
            
            FindIterable<Document> data =  collection.find();
            
            Document requiredDoc = null;
        
         for(Document d : data){
          
             if(d.get("_id").toString().equals(id)) {
                  
                   // found the required id
                   
                   requiredDoc = d;                   
                   break;
             }             
         }
         
           if(requiredDoc != null){
          
                 System.out.println(requiredDoc.get(field));
               
               return requiredDoc.remove(field, requiredDoc.get(field));
               //requiredDoc.remove(field);
               
           // return requiredDoc.append(field, value).containsValue(value);
                                
             }
            
            return false;
        }

}

