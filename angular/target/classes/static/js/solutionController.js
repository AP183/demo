app.controller('solutionController', function($scope, $http, $location, ngTableParams, $uibModal,  $filter, $stateParams, userPersistenceService, logger, $cookies) {

	$scope.assignment = {};
	$scope.assignments = [];
	
	$scope.solutions = [];
	
	var userService = userPersistenceService.authentication;
	$scope.isAuth = userService.isAuth;
	$scope.email = userService.email;
	$scope.role = userService.role;
	getAssignments();

	
	function initCart() {
		if ($cookies.getObject($scope.email)) {
			$scope.myCart = $cookies.getObject($scope.email);
			//$scope.totals=parseFloat('0');
			//for (var i = 0; i < $scope.myCart.length; i ++) {
				//var xx = parseFloat($scope.myCart[i].price);
				//$scope.totals = parseFloat($scope.totals + parseFloat($scope.myCart[i].price));
			//}
			//$scope.totals = $scope.totals.toFixed(1);
		} else {
			$scope.myCart = [];
			//$scope.totals = parseFloat('0');
		}
	}
	
	initCart();
	
	var userService = userPersistenceService.authentication;
	$scope.isAuth = userService.isAuth;
	$scope.email = userService.email;
	$scope.role = userService.role;
	getAssignments();
	getSolutions();

        function configureNgTable () {
        	$scope.ngTableFilter = {
                    'id': '' 
                    ,'title': '' 
                    ,'subject': '' 
                    ,'description': '' 
                };
        	
            $scope.ngTableParams = new ngTableParams({
            	page: 1
                , count: 10
                , sorting: {
                    'name': 'desc' 
                }
                , filter: $scope.ngTableFilter
                

            }
            ,{
                total: $scope.assignments.length
                , getData: function ($defer, $params) {
                    var filter = $params.filter();
           
                    var data = $scope.assignments;

                    var filteredData = $params.filter() ?
                                    $filter('filter')(data, filter, false) :
                                    data;

                    var orderedData = $params.sorting() ?
                                    $filter('orderBy')(filteredData, $params.orderBy()) :
                                    filteredData;

                    var pageData = $(orderedData).slice(($params.page() - 1) * $params.count(), $params.page() * $params.count());
                    $params.total(orderedData.length);
                    $defer.resolve(pageData);


                   
                }
                , $scope: { $data: {} }
            });

            
        }
	
	function getAssignments()  {
		$http.get ("http://localhost:8080/getAssignments")
				.success(function(data) {
					$scope.assignments = data;
					configureNgTable();
				})
				.error (function() {
					alert("failed to retrieve assignments");
				})	
	}
	
	function getSolutions()  {
		 
		$http.get ("http://localhost:8080/getSolution")
		.success(function(data) {
					$scope.solutions = data;
					configureNgTable();
				})
				.error (function() {
					alert("failed to retrieve solutions");
				})	
	}
	
	$scope.addToCart = function (item) {
		initCart();
		
		$scope.myCart.push(item);
		
		if ($cookies.getObject($scope.email)) {
			$cookies.remove($scope.email);
			$cookies.putObject($scope.email, $scope.myCart);
		} else {
			$cookies.putObject($scope.email, $scope.myCart);
		}
		
		logger.logSuccess("You have succesfully added the assignment to solved");
	}
	
	$scope.removeFromCart = function (item) {
		$scope.myCart.pop(item);
		
		if ($cookies.getObject($scope.email)) {
			$cookies.remove($scope.email);
			$cookies.putObject($scope.email, $scope.myCart);
		} else {
			$cookies.putObject($scope.email, $scope.myCart);
		}
		
		logger.logSuccess("You have succesfully removed the solution");
	}
	
	$scope.sendSolution = function() {
		var model = {
				list: $cookies.getObject($scope.email),
				email : $scope.email
		};
		
		$http.post ("http://localhost:8080/sendSolution", model)
			.success(function(data) {
				//$cookies.remove($scope.email);
				
//				if ($cookies.getObject($scope.email)) {
//					$scope.myCart = $cookies.getObject($scope.email);
//					$scope.totals=0;
//					for (var i = 0; i < $scope.myCart.length; i ++) {
//						$scope.totals += $scope.myCart[i].price;
//					}
//				} else {
					$scope.myCart = [];
					//$scope.totals = parseFloat('0');
				//}
				
				getAssignments();
				
				configureNgTable();
				$scope.ngTableParams.reload();
				
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
			.error (function() {
				BootstrapDialog.show({
	                title: 'Error',
	                message: "The solution could not be sent",
	                type: BootstrapDialog.TYPE_DANGER,
	                buttons: [{
	                    label: 'OK',
	                    action: function (dialog) {
	                    	$scope.ngTableParams.reload();
	                        dialog.close();
	                    }
	                }]
	            });
			})	
	}
	
	$scope.preview = function (content) {
		
		
		 var modalInstance = $uibModal.open({
             templateUrl: '../view/display-xml.html'
            , controller: 'xmlController'
            , backdrop: 'static'
            , resolve: {
                content: function () {
                    return content;
                }
            }
            , $scope: { $data: {} }
         });

	}
	
});
