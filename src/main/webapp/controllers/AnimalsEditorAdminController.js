angular.module('AnimalsEditorAdminController', ['nya.bootstrap.select', 'DPController', 'naif.base64', 'AnimalsAdminModule', 'AnimalsAdminValues'])
    .controller('AnimalsEditorAdminController', ['$scope', '$window', '$filter', '$routeParams', 'AnimalsAdminService', 'AnimalsAdminValues', '$q',
        function($scope, $window, $filter, $routeParams, AnimalsAdminService, AnimalsAdminValues, $q) {

            AnimalsAdminService.rolesAllowed("moderator");

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            //This variable decides when spinner loading for contentis closed.
            $scope.errors = [];

            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            var animalId = $routeParams.animalId;                       //animal id

            var initialize = function() {
                $scope.contentLoading++;
                var promises = [];

                /**
                 * @return list of animal types.
                 */
                promises.push(AnimalsAdminService.getAnimalTypes().catch(function(respounce) {
                    $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_TYPES")});
                }));

                /**
                 * @return list of animal services.
                 */
                promises.push(AnimalsAdminService.getAnimalServices().catch(function(respounce) {
                    $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_SERVICES")});
                }));

                /**
                 * @param animalId id of animal used for lookup.
                 * @return animal instance.
                 */
                promises.push(AnimalsAdminService.getAnimal(animalId).catch(function(respounce) {
                    $scope.errors.push({msg: $filter('translate')("ERROR_ANIMAL_NOT_FOUND")});
                }));

                $q.all(promises)
                    .then(function (respounce) {
                        $scope.animalTypes = AnimalsAdminValues.animalTypes;        //list of animal types
                        $scope.animalServices = AnimalsAdminValues.animalServices;  //list of animal services
                        $scope.animal = angular.copy(AnimalsAdminValues.animal);     //animal
                        $scope.animalImage = "resources/img/no_img.png";

                        if (AnimalsAdminValues.animal.image != undefined) {
                            if (AnimalsAdminValues.animal.image.length > 0) {
                                $scope.animalImage = AnimalsAdminValues.animal.image;
                            }
                        }

                        if ($scope.animal.type != undefined) {
                            $scope.getAnimalBreeds();
                        }
                    })
                    .finally(function() {
                        $scope.contentLoading--;
                    });
            }

            initialize();

            /**
             * @return list of animal breeds according to animal type.
             */
            $scope.getAnimalBreeds = function() {
                $scope.filterAnimalBreedFlag = true;
                AnimalsAdminService.getAnimalBreeds($scope.animal.type.id)
                    .then(function(response) {
                        $scope.animalBreeds = response.data;
                    }, function() {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_BREEDS")});
                    })
                    .finally(function() {
                        $scope.filterAnimalBreedFlag = false;
                    });
            }

            /**
             * update animal.
             */
            $scope.submit = function(isValid) {
                if(!isValid){
                    return;
                }

                $scope.animal.dateOfBirth = $filter('date')($scope.animal.dateOfBirth, 'yyyy-MM-dd');
                $scope.animal.dateOfSterilization = $filter('date')($scope.animal.dateOfSterilization, 'yyyy-MM-dd');
                $scope.animal.dateOfRegister = $filter('date')($scope.animal.dateOfRegister, 'yyyy-MM-dd');
                if (typeof $scope.animal.breed != "undefined") {
                    if (typeof $scope.animal.breed.id == "undefined") {
                        if ($scope.currentLanguage == "en") {
                            $scope.animal.breed = {breedEn: $scope.animal.breed};
                        } else {
                            $scope.animal.breed = {breedUa: $scope.animal.breed};
                        }

                    }
                }

                if ($scope.imageFile != undefined) {
                    $scope.animal.image = $scope.imageFile['filename'] + '\n' + $scope.imageFile['base64'];
                }

                AnimalsAdminService.updateAnimal($scope.animal)
                    .then(function(response) {
                        angular.copy($scope.animal, AnimalsAdminValues.animal);
                        AnimalsAdminValues.animal.image = response.data.filePath;
                        $window.location.href = "#/ua/user/home/animals/" + $scope.animal.id;
                    },
                    function(response) {
                        $window.alert($filter('translate')("ERROR_ANIMAL_UPDATE"));
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

                AnimalsAdminService.deleteAnimalImage($scope.animal.id)
                    .then(function(response) {
                        $scope.animal.image = undefined;
                        $scope.animalImage = "resources/img/no_img.png";
                    }, function(response) {
                        $window.alert($filter('translate')("DELETE_IMAGE_FAILED"));
                    });
            }
        }
    ]);
