'use strict';

directives.directive('cstMenu', ['$rootScope', 'userPersistenceService', 
function ($rootScope, userPersistenceService) {
    return {
        restrict: 'E',
        replace: true,
        controller: 'homeController',
        templateUrl: '../view/menu.html',
        link: function (scope, elementk, attrs) {
            var refreshLogin = function () {
                var authData = userPersistenceService.authentication;

                if (authData.isAuth) {
                    scope.isAuth = true;
                    scope.email = authData.email;
                    scope.role = authData.role;
                }
                else {
                    scope.isAuth = false;
                    scope.email = '';
                    scope.role = '';
                }
            }

            scope.$on('logOut', function () {
                refreshLogin();
            });

            scope.$on('logOn', function () {
                refreshLogin();
            });

            refreshLogin();
        }
    };
}]);