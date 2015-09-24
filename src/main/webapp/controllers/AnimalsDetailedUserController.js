var animalAppControllers = angular.module('AnimalsDetailedUserController', [ ]);

animalApp.controller('AnimalsDetailedUserController', ['$scope', 'UserDataService', '$routeParams', '$window', 'UserAnimalsValues',
                                                       'localStorageService', '$filter',
                                               function($scope, UserDataService, $routeParams, $window, UserAnimalsValues, 
                                            		   localStorageService, $filter) {
	
	if (!localStorageService.get('userRole')){
		$location.path("#ua");	
	}
	
	 //initialize loading spinner
     var targetContent = document.getElementById('loading-block');
     new Spinner(opts).spin(targetContent);
     //This variable decides when spinner loading for contentis closed.
     $scope.contentLoading = 1;

     $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

     var animalId = $routeParams.animalId;       //animal id
     $scope.animal = UserAnimalsValues.animal;  //animal
     $scope.animalImage = undefined;	
	
     UserDataService.getAnimal(animalId).then(
			function(result){				
				angular.copy(result, UserAnimalsValues.animal);
				$scope.animalImage = "resources/img/noimg.png";
				if (UserAnimalsValues.animal.image != undefined) {
                    if (UserAnimalsValues.animal.image.length > 0) {
                        $scope.animalImage = UserAnimalsValues.animal.image;
                    }
                }
				$scope.contentLoading--;
			},
			function(error){				
				console.log(error)
				$scope.contentLoading--;
			}
		);
	
     $scope.deleteAnimal = function() {
    	 if (!confirm($filter('translate')("ANIMAL_DETAILED_CONFIRM_DELETE"))) {
             return;
         }
    	 
    	 UserDataService.deleteAnimal($scope.animal.id)
            .then(function(data) {
                $window.location.href = "#/ua/user/profile";
            },
            function(data) {
                $window.alert("Animal delete failed.");
            });
    }
	
}]);