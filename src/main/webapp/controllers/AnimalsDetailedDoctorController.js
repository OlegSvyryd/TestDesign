angular.module('AnimalsDetailedDoctorController', ['AnimalsDoctorModule', 'AnimalsDoctorValues'])
    .controller('AnimalsDetailedDoctorController', ['$scope', '$routeParams', 'AnimalsDoctorService', 'AnimalsDoctorValues', '$filter', '$window',
        function($scope, $routeParams, AnimalsDoctorService, AnimalsDoctorValues, $filter, $window) {

            AnimalsDoctorService.rolesAllowed('doctor');

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            //This variable decides when spinner loading for contentis closed.
            $scope.contentLoading = 0;

            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            var animalId = $routeParams.animalId;       //animal id
            $scope.animal = AnimalsDoctorValues.animal;  //animal
            $scope.animalImage = undefined;
            $scope.errors = [];

            /**
             * @param animalId id of animal used for lookup.
             * @return animal instance.
             */
            var getAnimal = function() {
                $scope.contentLoading++;

                AnimalsDoctorService.getAnimal(animalId)
                    .then(function (respounce) {
                        $scope.animalImage = "resources/img/no_img.png";
                        if (AnimalsDoctorValues.animal.image != undefined) {
                            if (AnimalsDoctorValues.animal.image.length > 0) {
                                $scope.animalImage = AnimalsDoctorValues.animal.image;
                            }
                        }

                        if ($scope.animal.user == undefined) {
                            $scope.error = $filter('translate')("ERROR_NO_RECORDS");
                        }
                    }, function (respounce) {
                        $scope.errors.push({msg: $filter('translate')("ERROR_ANIMAL_NOT_FOUND")});
                    })
                    .finally(function () {
                        $scope.contentLoading--;
                    });
            }

            getAnimal();
        }]);