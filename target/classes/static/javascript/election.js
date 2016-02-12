
(function (){

      var app = angular.module("Election", []);

      app.controller("IssueController", ['$http', function($http){

            var currentInstance = this;

            currentInstance.name = "";
            currentInstance.description = "";
            currentInstance.topic = "";

            currentInstance.data = {};

            currentInstance.issues = [];


         // Get all the issues
        $http.get('http://localhost:8080/getAll').then(function successCallback(response) {

            currentInstance.issues = response.data;

             console.log(response.data);

          }, function errorCallback(response) {

             console.log(response.data);

          });


         currentInstance.call = function (){

         $http.post('http://localhost:8080/issue_handler/add',  {

                "name": currentInstance.name,
                "description" : currentInstance.description

            }).then(function successCallback(response) {

             console.log(response);

          }, function errorCallback(response) {

             console.log(response);

          });

           };

    }]);
})();
