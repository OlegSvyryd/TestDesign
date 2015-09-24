var animalAppControllers = angular.module('AnimalsEditorUserController', ['AnimalsAdminModule', 'AnimalsAdminValues']);

animalApp.controller('AnimalsEditorUserController', ['$scope', 'UserDataService', '$routeParams', '$window', '$filter', 'AnimalsAdminValues',
                                                     'AnimalsAdminService', 'UserAnimalsValues', 'localStorageService',
                                               function($scope, UserDataService, $routeParams, $window, $filter, AnimalsAdminValues, 
                                            		   AnimalsAdminService, UserAnimalsValues, localStorageService) {
	
	if (!localStorageService.get('userRole')){
		$location.path("#ua");	
	}
	
	
	//initialize loading spinner
    var targetContent = document.getElementById('loading-block');
    new Spinner(opts).spin(targetContent);
    //This variable decides when spinner loading for contentis closed.
    $scope.contentLoading = 1;

    $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

    var animalId = $routeParams.animalId;                       //animal id

    var initialize = function() {
        if (AnimalsAdminValues.animalTypes.values.length === 0  ||
            AnimalsAdminValues.animalServices.values.length === 0 ||
            UserAnimalsValues.animal.id == undefined ||
            $scope.contentLoading === 0) {
            return;
        }

        $scope.animalTypes = AnimalsAdminValues.animalTypes;        //list of animal types
        $scope.animalServices = AnimalsAdminValues.animalServices;  //list of animal services
        $scope.animal = angular.copy(UserAnimalsValues.animal);     //animal
        $scope.animalImage = "resources/img/no_img.png";

        if (UserAnimalsValues.animal.image != undefined) {
            if (UserAnimalsValues.animal.image.length > 0) {
                $scope.animalImage = UserAnimalsValues.animal.image;
            }
        }

        if ($scope.animal.type != undefined) {
            $scope.getAnimalBreeds();
        }

        $scope.contentLoading--;
    }

    /**
     * @return list of animal types.
     */
    AnimalsAdminService.getAnimalTypes()
        .finally(function() {
            initialize();
        });

    /**
     * @return list of animal services.
     */
    AnimalsAdminService.getAnimalServices()
        .finally(function() {
            initialize();
        });

    /**
     * @param animalId id of animal used for lookup.
     * @return animal instance.
     */
//    AnimalsAdminService.getAnimal(animalId)
//        .finally(function() {
//            initialize();
//        });

    /**
     * @return list of animal breeds according to animal type.
     */
    $scope.getAnimalBreeds = function() {
        $scope.filterAnimalBreedFlag = true;
        AnimalsAdminService.getAnimalBreeds($scope.animal.type.id)
            .then(function(response) {
                $scope.animalBreeds = response.data;
            })
            .finally(function() {
                $scope.filterAnimalBreedFlag = false;
            });
    }

    /**
     * @param animalId id of animal used for lookup.
     * @return animal instance.
     */
    UserDataService.getAnimal(animalId).then(
			function(result){				
				angular.copy(result, UserAnimalsValues.animal);				
//				$scope.contentLoading--;
				initialize();
			},
			function(error){				
				console.log(error)
				$scope.contentLoading--;
			}
		);

 
	$scope.updateAnimal = function(isValid) {
		if(!isValid){
            return;
        }

        $scope.animal.dateOfBirth = $filter('date')($scope.animal.dateOfBirth, 'yyyy-MM-dd');
        $scope.animal.dateOfSterilization = $filter('date')($scope.animal.dateOfSterilization, 'yyyy-MM-dd');
        $scope.animal.dateOfRegister = $filter('date')($scope.animal.dateOfRegister, 'yyyy-MM-dd');
        if (typeof $scope.animal.breed != "undefined") {
            if (typeof $scope.animal.breed.id == "undefined") {
                $scope.animal.breed = {breedUa: $scope.animal.breed};
            }
        }

        if ($scope.imageFile != undefined) {
            $scope.animal.image = $scope.imageFile['filename'] + '\n' + $scope.imageFile['base64'];
        }
        
        UserDataService.updateAnimal($scope.animal)
            .then(function(result) {
                $window.location.href = "#/ua/user/profile/animals/" + animalId;
            },
            function(data) {
                $window.alert("Animal update failed.");
            });
    }
	
	/*
     * delete image
     */
    $scope.deleteImage = function() {
        if ($scope.animalImage === "resources/img/no_img.png") {
            $window.alert($filter('translate')("DELETE_IMAGE_NO_IMAGE"));
            return;
        }

        if (!confirm($filter('translate')("DELETE_IMAGE_CONFIRM"))) {
            return;
        }

        UserDataService.deleteAnimalImage($scope.animal.id)
            .then(function(response) {
                $scope.animal.image = undefined;
                $scope.animalImage = "resources/img/no_img.png";
            }, function(response) {
                $window.alert($filter('translate')("DELETE_IMAGE_FAILED"));
            });
    }
	
}]);