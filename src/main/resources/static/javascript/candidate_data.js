(function (){

 var app = angular.module("CandidateData", []);

 app.controller("CandidateCtrl",['$http', function($http){

     var instance = this;
     instance.data = [];
     instance.test = "Nikhil";
     
     
     instance.setSelected = function(candidate){    
         
         var keys = Object.keys(candidate);
         
         for(var i = 0; i<keys.length; i++){
             
             sessionStorage.setItem(keys[i], candidate[keys[i]]);
             
         }
         
       
          
     };
     
     
     instance.selectedCandidate = sessionStorage;
     
     instance.getImageUrl = function(value){
         
         return "http://localhost:8080/images/"+value+".jpg";
     };

   $http.get("http://localhost:8080/candidates").then(function successCallback(response){
       
       instance.data = response.data;
       console.log(response.data);
        
   }, function errorCallback(reponse){
               
       instance.data = response.data;
       console.log(response.data);
        
   });

 }]);

 app.controller("PanelCtrl", ['$http', function($http){

  this.test = "Nikhil";

  this.tab = 1;

  this.currentIssue = "";

  this.setTab = function (value, issue){
    this.tab = value;
    this.updateIssue(issue);
  };

  this.isSelected = function (value){

       return this.tab === value;
  };

   var currentInstance = this;

  currentInstance.issues = [];

// Get all the issues
$http.get('http://localhost:8080/getAll').then(function successCallback(response) {

  currentInstance.issues = response.data;

   console.log(response.data);

}, function errorCallback(response) {

   console.log(response.data);

}); // getAll() ends here

 currentInstance.updateIssue = function(value){

   for(var i=0; i<currentInstance.issues.length; i++){

       if(currentInstance.issues[i].name == value){
           this.currentIssue = currentInstance.issues[i].description;
       }
   }

 };

  $http.get("").then(function successCallback(response){

    console.log(response);

  }, function errorCallback(response){

    console.log(response);

  });

}]); // PanelCtrl ends here




})();
