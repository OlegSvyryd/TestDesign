var animalAppControllers = angular.module('MainController', ['ui.bootstrap']);

animalApp.controller('MainController', ['$scope', '$rootScope', 'localStorageService', 'AuthenticationService',
    function ($scope, $rootScope, localStorageService, AuthenticationService) {

        if (!localStorageService.cookie.get("accessToken")) {
            localStorageService.clearAll();
        } else {
            if (localStorageService.get("memoryMe") == "ON")
                localStorageService.cookie.set("accessToken", localStorageService.get("accessToken"), 30);
            if (localStorageService.get("memoryMe") == "OFF")
                localStorageService.cookie.set("accessToken", localStorageService.get("accessToken"), 0.065);
        }


        $scope.logout = function () {
            AuthenticationService.logout();
        };

        $scope.session = function (value) {

            if (!localStorageService.get("userName")) {
                return false;
            } else {
                $scope.userRole = localStorageService.get("userRole");
                $rootScope.userName = localStorageService.get("userName");
                return true;
            }

        };

        //carousel
        $scope.myInterval = 3000;
        $scope.slides = [
            {
                image: 'resources/img/carousel/car-1.jpg'
            },
            {
                image: 'resources/img/carousel/car-2.jpg'
            },
            {
                image: 'resources/img/carousel/car-3.jpg'
            }
        ];
    }])

    //directive for password matching (StackOverFlow)
    .directive("passwordVerify", function () {
        return {
            require: "ngModel",
            scope: {
                passwordVerify: '='
            },
            link: function (scope, element, attrs, ctrl) {
                scope.$watch(function () {
                    var combined;

                    if (scope.passwordVerify || ctrl.$viewValue) {
                        combined = scope.passwordVerify + '_' + ctrl.$viewValue;
                    }
                    return combined;
                }, function (value) {
                    if (value) {
                        ctrl.$parsers.unshift(function (viewValue) {
                            var origin = scope.passwordVerify;
                            if (origin !== viewValue) {
                                ctrl.$setValidity("passwordVerify", false);
                                return undefined;
                            } else {
                                ctrl.$setValidity("passwordVerify", true);
                                return viewValue;
                            }
                        });
                    }
                });
            }
        };
    });