/* global google */

(function(){
     
    var app = angular.module("Mappy",[]);
    
    app.controller("MappyController", ['$http', function($http){
       
       var map = new google.maps.Map(document.getElementById('map'));
       
           var currentInstance = this;
            
            currentInstance.data = {};
            currentInstance.currentLocation = {};
           
           var infowindow;
           
     if (navigator.geolocation) {              
        navigator.geolocation.getCurrentPosition(function (position) {            
         
         currentLocation = {lat: position.coords.latitude,lng: position.coords.longitude};
            
         var marker = new google.maps.Marker({            
                map: map,
                position: currentLocation
            
            });
            
              var mapOptions = {
                  zoom: 15,
                  center: currentLocation,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
              };
            
          map.setOptions(mapOptions);  
          
           infowindow = new google.maps.InfoWindow();
           
            var service = new google.maps.places.PlacesService(map);
            service.nearbySearch({
              location: currentLocation,
              radius: 500,
              types: ['store']
            }, callback);

        $http({
          method: 'GET',
          url: 'http://mysafeinfo.com/api/data?list=englishmonarchs&format=json'
        }).then(function successCallback(response) {
            currentInstance.data = response;
                 
          }, function errorCallback(response) {
            currentInstance.data = response ;    
          });
    
});
    } else { 
        //$scope.currentLocation.innerHTML = "Geolocation is not supported by this browser.";
    }
    
            function callback(results, status) {
              if (status === google.maps.places.PlacesServiceStatus.OK) {
                for (var i = 0; i < results.length; i++) {
                  createMarker(results[i]);
                }
              }
            }

            function createMarker(place) {
              var marker = new google.maps.Marker({
                map: map,
                position: place.geometry.location
              });

              google.maps.event.addListener(marker, 'click', function() {
                infowindow.setContent(place.name);
                infowindow.open(map, marker);
              });
            }
       
    }]);
     
    app.controller("HomeFormController", function(){
         
         this.country = "UK";
         this.city = "Dublin";
         this.suburb = "Rathmines";
         
         this.search = {};
         
         this.addSearch = function(){        
             
             localStorage.setItem("country", this.country);
             localStorage.setItem("city", this.city);
             localStorage.setItem("suburb", this.suburb);                               
             
         };
    });
    
    app.controller("MapController", function(){
        
        var map = new google.maps.Map(document.getElementById('map'));
       
           var currentInstance = this;
            
            currentInstance.data = {};
            currentInstance.currentLocation = {};
            
           var infowindow;
           
     if (navigator.geolocation) {              
        navigator.geolocation.getCurrentPosition(function (position) {            
         
         currentLocation = {lat: position.coords.latitude,lng: position.coords.longitude};
            
         var marker = new google.maps.Marker({            
                map: map,
                position: currentLocation
            
            });
            
              var mapOptions = {
                  zoom: 15,
                  center: currentLocation,
                  mapTypeId: google.maps.MapTypeId.ROADMAP
              };
            
          map.setOptions(mapOptions);  
          
           infowindow = new google.maps.InfoWindow();
           
            var service = new google.maps.places.PlacesService(map);
            service.nearbySearch({
              location: currentLocation,
              radius: 500,
              types: ['store']
            }, callback);

        $http({
          method: 'GET',
          url: 'http://mysafeinfo.com/api/data?list=englishmonarchs&format=json'
        }).then(function successCallback(response) {
            currentInstance.data = response;
                 
          }, function errorCallback(response) {
            currentInstance.data = response ;    
          });
    
});
    } else { 
        //$scope.currentLocation.innerHTML = "Geolocation is not supported by this browser.";
    }
    
            function callback(results, status) {
              if (status === google.maps.places.PlacesServiceStatus.OK) {
                for (var i = 0; i < results.length; i++) {
                  createMarker(results[i]);
                }
              }
            }

            function createMarker(place) {
              var marker = new google.maps.Marker({
                map: map,
                position: place.geometry.location
              });

              google.maps.event.addListener(marker, 'click', function() {
                infowindow.setContent(place.name);
                infowindow.open(map, marker);
              });
            }
        
    });
    
})();