/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.election.service;

import com.election.model.Issue;
import com.election.model.Candidate;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author deadcode
 */

@Service
@Transactional
public class CandidateService {
 
 
// get all the candidates

    public Candidate getTestCandidate(){
        
        Issue issue = new Issue();
        issue.setName("Maxico Border");
        issue.setDescription("This is about maxicon border");
        
        ArrayList<Issue> list = new ArrayList<>();
        list.add(issue);
        
        
        return null;
    }
    
}
