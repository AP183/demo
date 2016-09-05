app.controller('assignmentsController', function($scope, $http, $location, ngTableParams, $filter, $stateParams, userPersistenceService) {

	$scope.assignments = {};
	$scope.assignments = [];
	
	var userService = userPersistenceService.authentication;
	$scope.isAuth = userService.isAuth;
	$scope.email = userService.email;
	$scope.role = userService.role;
	getAssignments();

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
                    'title': 'desc' 
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
    

	
	
	$scope.addAssignment = function () {
		
		var model = { 'id': $scope.assignment.id, 'title': $scope.assignment.title,  'subject': $scope.assignment.subject, 'description': $scope.assignment.description};
		
		$http.post("http://localhost:8080/addAssignment", model)
			.success(function(data) {
				if (data == "The assignment was added successfully") {
					$scope.product={};
				
					BootstrapDialog.show({
	                    title: 'Succes',
	                    message: data,
	                    type: BootstrapDialog.TYPE_SUCCESS,
	                    buttons: [{
	                        label: 'OK',
	                        action: function (dialog) {
	                            dialog.close();
	                        }
	                    }]
	                });
				} else {
					BootstrapDialog.show({
	                    title: 'Warning',
	                    message: data,
	                    type: BootstrapDialog.TYPE_WARNING,
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
                    message: "The assignment could not be added",
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
	
});
