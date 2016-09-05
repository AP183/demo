var app = angular.module('app', ['ngRoute','ngResource', 'app.directives',  'ngTable', 'ui.router', 'ngCookies', 'ui.bootstrap']);
app.config(function($routeProvider){
    $routeProvider
	    .when('/login',{
	        templateUrl: '/view/login.html',
	        controller: 'homeController'
	    })
	    .when('/adminAdministration',{
	        templateUrl: '/view/adminAdministration.html',
	        controller: 'homeController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Admin') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
	    })
	    .when('/teacherAdministration',{
	        templateUrl: '/view/teacherAdministration.html',
	        controller: 'homeController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Teacher') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
	    })
	    .when('/studentAdministration',{
	        templateUrl: '/view/studentAdministration.html',
	        controller: 'homeController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Student') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
	    })
	    .when('/createSolution',{
	        templateUrl: '/view/solved.html',
	        controller: 'solutionController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Student') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
	    })
	    .when('/mycart',{
	        templateUrl: '/view/mycart.html',
	        controller: 'solutionController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Student') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
	    })
	    .when('/home',{
	        templateUrl: '/view/home.html',
	        controller: 'homeController'
	    })
	     .when('/display-xml',{
	        templateUrl: '/view/display-xml.html',
	        controller: 'xmlController'
	    })
        .when('/createAccount',{
            templateUrl: '/view/addUser.html',
            controller: 'usersController'
        })
        .when('/resetPassword',{
            templateUrl: '/view/resetPassword.html',
            controller: 'homeController'
        })
         .when('/reset/:email',{
            templateUrl: '/view/reset.html',
            controller: 'homeController'
        })
        .when('/addAssignment',{
            templateUrl: '/view/addAssignment.html',
            controller: 'assignmentsController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Teacher') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
        }).when('/assignments',{
            templateUrl: '/view/assignments.html',
            controller: 'assignmentsController'
        }).when('/solutions',{
            templateUrl: '/view/solutions.html',
            controller: 'solutionController'
        }) .when('/users',{
            templateUrl: '/view/users.html',
            controller: 'usersController',
            resolve: {
                authorize: ["userPersistenceService", "$location", function(userService, $location) {
                   var authentication = userService.getCookieData();
                                   
                                      if (!authentication)
                                      {
                                        $location.path("/");
                                        
                                      } else if (authentication.role != 'Admin') {
                                    	  $location.path("/home");
                                      }
                                    	  
                                      return authentication;
                }]}
        })
        
        .otherwise(
            { redirectTo: '/'}
        );
});

var directives = angular.module('app.directives', []);