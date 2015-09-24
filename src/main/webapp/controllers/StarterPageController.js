var animalAppControllers = angular.module('StarterPageController', []);

animalApp.controller('StarterPageController', function($scope, $window) {
    $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');
});
