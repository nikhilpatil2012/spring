/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author deadcode
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Candidate {
    
    private String id;
    private String name;
    private String occupation;
    private String party_type;
    private String twitter_handle;
    private String quote;

    public String getQuote() {
        return quote;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getTwitter_handle() {
        return twitter_handle;
    }

    public String getParty_type() {
        return party_type;
    }

     
}
