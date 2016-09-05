app.controller('homeController', function($scope, $http, $location, userPersistenceService, $routeParams, $sce, $cookies) {

//	$scope.isAuth = authService.authentication.isAuth;
//	$scope.email = authService.authentication.currentUser.email;
//	$scope.role = authService.authentication.currentUser.role;
	var userService = userPersistenceService.authentication;
	$scope.isAuth = userService.isAuth;
	$scope.email = userService.email;
	$scope.role = userService.role;
	
	$scope.user = {};
	$scope.credentials = {};
	$scope.reset = {};
	$scope.form = {};
	$scope.form.email = '';
	$scope.form.email = $routeParams.email;
	$scope.form.oldpassword = '';
	$scope.form.newpassword = '';
	$scope.form.newpasswordconfirmed = '';
	
	if ($cookies.getObject($scope.email)) {
		$scope.myCart = $cookies.getObject($scope.email);
	} else {
		$scope.myCart = [];
	}
	

	//the login function is put on scope in order to be visible in the html pages which use this controller
	$scope.login = function () {
		var model = {'email': $scope.credentials.email, 'password': $scope.credentials.password};//see login.html
		
		$http.post("http://localhost:8080/searchUser", model)
			.success(function(data) {
				if (data != "Invalid password" && data != "Unregistered user" && data != "Your account is blocked") {
					
					$scope.credentials.role = data;
					//authService.login($scope.credentials);
					userPersistenceService.setCookieData($scope.credentials);
					$scope.$emit('logOn');
					//$location.path("/home");
					if (userService.role == "Admin")
						$location.path("/adminAdministration");
					else if (userService.role == "Supplier")
						$location.path("/supplierAdministration");
					else
						$location.path("/clientAdministration");
					
				} else {
					
					BootstrapDialog.show({
                        title: 'Error',
                        message: data,
                        type: BootstrapDialog.TYPE_DANGER,
                        buttons: [{
                            label: 'OK',
                            action: function (dialog) {
                                dialog.close();
                            }
                        }]
                    });
				}
			})
			.error (function() {
				BootstrapDialog.show({
                    title: 'Error',
                    message: "Incorect route",
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
	
	$scope.logout = function () {
		//authService.logout();
		userPersistenceService.clearCookieData();
		$scope.$emit('logOut');
		$location.path("/login");
	}
	
	
	$scope.resetPassword = function () {
		$scope.form.email = $scope.reset.email;
		$http.post("http://localhost:8080/reset-password-check", $scope.reset)
		.success(function(data) {
			if (data == "Exist") {
				$location.path("/reset/" +  $scope.reset.email);
			} else {
				BootstrapDialog.show({
	                title: 'Error',
	                message: "The account does not exist",
	                type: BootstrapDialog.TYPE_DANGER,
	                buttons: [{
	                    label: 'OK',
	                    action: function (dialog) {
	                        dialog.close();
	                    }
	                }]
	            });
			}
		})
		.error (function() {
			BootstrapDialog.show({
                title: 'Error',
                message: "The mail could not be found",
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
	
	$scope.resetPass = function () {
		var params = $location.search();
		$scope.form.email = $routeParams.email;
		$http.post("http://localhost:8080/reset-password", $scope.form)
		.success(function(data) {
			if (data == "The password was changed") {
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
				
				$location.path("/login");
			} else {
				BootstrapDialog.show({
	                title: 'Error',
	                message: data,
	                type: BootstrapDialog.TYPE_DANGER,
	                buttons: [{
	                    label: 'OK',
	                    action: function (dialog) {
	                        dialog.close();
	                    }
	                }]
	            });
			}
		})
		.error (function() {
			BootstrapDialog.show({
                title: 'Error',
                message: "The password could not be changed",
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
