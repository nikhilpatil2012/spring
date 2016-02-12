/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.election.controller;

import com.election.database.MongoStatic;
import java.util.List;
import com.election.model.Candidate;
import com.election.model.Issue;
import com.election.service.CandidateService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author deadcode
 */

@Controller
public class ElectionRestController {

    @Autowired
     CandidateService candidateService;


    @RequestMapping(value = "/user/")
    public ResponseEntity<Candidate> getUser() {

        Candidate candidate = candidateService.getTestCandidate();

        return new ResponseEntity<>(candidate, HttpStatus.OK);
    }

    @RequestMapping("/issue_handler")
    public String paddy()
    {
        return "issue_handler";
    }

   @RequestMapping("/home")
     public String home()
   {
       return "home";
   }

    @RequestMapping("/candidate_page")
     public String candidatePage()
   {       
       return "candidate_page";
   }
   
     @RequestMapping(value = "/candidates", method = RequestMethod.GET)
     public ResponseEntity< List<Candidate>> getAllCandidates(){
         
          List<Candidate> candidateList = MongoStatic.getAllCandidates();
         
           return new ResponseEntity<>(candidateList, HttpStatus.OK);
     }
     
     // add a new issue into the database
    @RequestMapping("/issue_handler/{id}")
     public ResponseEntity<Issue> update(@PathVariable("id") String id, @RequestBody Issue issue) {

            if(id.equals("add")) 
          {
            MongoStatic.addIssue(issue);         
            System.out.println("Done");
         }

     return new ResponseEntity<>(issue, HttpStatus.OK);
    }

          // Get all the issues from the database
    @RequestMapping("getAll")
     public ResponseEntity<List<Issue>> getAllIssues() {

        List<Issue> issuesList  = MongoStatic.getAllIssues();

        return new ResponseEntity<>(issuesList, HttpStatus.OK);
     }
     
}
