'use strict'

app.controller('xmlController', ['$uibModalInstance', '$scope', 'content',
function ($uibModalInstance, $scope, content) {

    $scope.content = content;

    $scope.ok = function () {
    	$uibModalInstance.close();
    }

    $scope.cancel = function () {
        $uibModalInstance.dismiss();
    }

}]);

