/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.TweetHandlers;

import com.election.model.CustomTweet;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import twitter4j.Status;

/**
 *
 * @author deadcode
 */
public class TweetStorageHandler {
    
    private ArrayList<CustomTweet> customTweetSet = new ArrayList<CustomTweet>();     
    private int counter = 0;
    private int size = 0;
    
    public ArrayList<CustomTweet> getCustomTweetSet() {
        return customTweetSet;
    }

    public void setSize(int size) {
        this.size = size;
    }
    
    
    public synchronized void updateCustomTweet(CustomTweet customTweet){
        
         customTweetSet.add(customTweet);
         counter++;
         
         System.out.println(counter);
         
           if(counter == size)
          {
          }
    }
    
    public CustomTweet createNewCustomTweet(Status tweet, String id){
        
        CustomTweet customTweet = new CustomTweet();
        customTweet.setId(id);
        customTweet.setSource("twitter");
        customTweet.setText(tweet.getText());
        customTweet.setCreatedAt(new SimpleDateFormat("yyMMddHHmmssZ").format(tweet.getCreatedAt()));
     
        return customTweet;
    }
     
}
