app.controller('usersController', function($scope, $q, $http, $location, ngTableParams, $filter, $stateParams, userPersistenceService, $cookies) {

	$scope.user = {};
	$scope.user.role = "Student";
	$scope.users = [];
	
	var userService = userPersistenceService.authentication;
	$scope.isAuth = userService.isAuth;
	$scope.email = userService.email;
	$scope.role = userService.role;
	
	$scope.isSupplier = false;
	
	
	
	 $q.all([
	         getUsers()  
	        ])
	        .then(function (results) {
	            configureNgTable();
	        });
	
	 function configureNgTable () {
     	$scope.ngTableFilter = {
                 'firstname': '' 
                 ,'lastname': '' 
                 ,'email': '' 
                 ,'phone': '' 
                 ,'role': '' 
             };
     	
         $scope.ngTableParams = new ngTableParams({
        	 page: 1
             , count: 10
             , sorting: {
                 //'RegistrationDate': 'desc'
             }
             ,filter: $scope.ngTableFilter
         }
         , {
             total: $scope.users.length
             , getData: function ($defer, $params) {
                 var filter = $params.filter();
        
                 var data = $scope.users;

                 var filteredData = $params.filter() ?
                                 $filter('filter')(data, filter, false) :
                                 data;

//                 var orderedData = $params.sorting() ?
//                                 $filter('orderBy')(filteredData, $params.orderBy()) :
//                                 filteredData;
//
//                 var pageData = $(orderedData).slice(($params.page() - 1) * $params.count(), $params.page() * $params.count());

                 $params.total(filteredData.length);
                 $defer.resolve(filteredData);
             }
             , $scope: { $data: {} }
         });

         
     }
	
	$scope.save = function () {
		
		if($scope.isTeacher)
			$scope.user.role = "Teacher";
		
		var model = { 'firstname': $scope.user.firstname, 'lastname': $scope.user.lastname,  'email': $scope.user.email, 'phone': $scope.user.phone, 'password': $scope.user.password, 'role': $scope.user.role, 'status': "active" };
		if ($scope.user.password == $scope.user.confirm_password) {
			$http.post("http://localhost:8080/addUser", model)
				.success(function(data) {
					if (data == "The account was created")
						$scope.user={};
					
					BootstrapDialog.show({
                        title: 'Succes',
                        message: "The account was created successfully",
                        type: BootstrapDialog.TYPE_SUCCESS,
                        buttons: [{
                            label: 'OK',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    });
					
					$location.path('/login');
					
				})
				.error (function() {

					BootstrapDialog.show({
                        title: 'Error',
                        message: "The account could not be created",
                        type: BootstrapDialog.TYPE_DANGER,
                        buttons: [{
                            label: 'OK',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    });
				})
		} else {
			
			BootstrapDialog.show({
                title: 'Warning',
                message: "Password and Confirmed Password are different!",
                type: BootstrapDialog.TYPE_WARNING,
                buttons: [{
                    label: 'OK',
                    action: function (dialog) {
                        dialog.close();
                    }
                }]
            });
		}
	}
	
	 function getUsers()  {
		return $http.get ("http://localhost:8080/getUsers")
		.success(function(data) {
					$scope.users = data;
					$scope.ngTableParams.reload();
					
				})
				.error (function() {
					alert("failed to retrieve users");
				})	
	}
	 
	$scope.blockUser = function (user) {
		
		var model = {'email' : user.email, 'role' : user.role};
		
		$http.post ("http://localhost:8080/blockUser", model)
			.success(function(data) {
					getUsers().then (function() {
						
						//location.reload();
					})
					
					BootstrapDialog.show({
		                title: 'Success',
		                message: data,
		                type: BootstrapDialog.TYPE_SUCCESS,
		                buttons: [{
		                    label: 'OK',
		                    action: function (dialog) {
		                        dialog.close();
		                    }
		                }]
		            });
				})
				.error (function() {
					BootstrapDialog.show({
		                title: 'Error',
		                message: "Failed to block user",
		                type: BootstrapDialog.TYPE_DANGER,
		                buttons: [{
		                    label: 'OK',
		                    action: function (dialog) {
		                        dialog.close();
		                    }
		                }]
		            });
				})	
	}
	
	$scope.unblockUser = function (user) {
		var model = {'email' : user.email, 'role' : user.role};
		
		$http.post ("http://localhost:8080/unblockUser", model)
			.success(function(data) {
					getUsers().then (function() {
						$scope.ngTableParams.reload();
						//location.reload();
						
						BootstrapDialog.show({
			                title: 'Success',
			                message: data,
			                type: BootstrapDialog.TYPE_SUCCESS,
			                buttons: [{
			                    label: 'OK',
			                    action: function (dialog) {
			                    	$scope.ngTableParams.reload();
			                        dialog.close();
			                    }
			                }]
			            });
					})
				})
				.error (function() {
					BootstrapDialog.show({
		                title: 'Error',
		                message: "Failed to block user",
		                type: BootstrapDialog.TYPE_DANGER,
		                buttons: [{
		                    label: 'OK',
		                    action: function (dialog) {
		                        dialog.close();
		                    }
		                }]
		            });
				})
	}
	$scope.deleteUser = function (user) {
		var model = {'email' : user.email, 'role' : user.role};
		
		$http.post ("http://localhost:8080/deleteUser", model)
			.success(function(data) {
					getUsers().then (function() {
						$cookies.remove(model.email);
						$scope.ngTableParams.reload();
						//location.reload();
						
						BootstrapDialog.show({
			                title: 'Success',
			                message: "The user has been deleted",
			                type: BootstrapDialog.TYPE_SUCCESS,
			                buttons: [{
			                    label: 'OK',
			                    action: function (dialog) {
			                    	$scope.ngTableParams.reload();
			                        dialog.close();
			                    }
			                }]
			            });
					})
				})
				.error (function() {
					BootstrapDialog.show({
		                title: 'Error',
		                message: "Failed to delete user",
		                type: BootstrapDialog.TYPE_DANGER,
		                buttons: [{
		                    label: 'OK',
		                    action: function (dialog) {
		                        dialog.close();
		                    }
		                }]
		            });
				})
	}
});
