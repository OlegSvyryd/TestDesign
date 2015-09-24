angular.module('AnimalMedicalHistoryEditorController', ['DPController', 'AnimalsDoctorModule', 'AnimalsDoctorValues', 'AnimalMedicalHistoryValues'])
    .controller('AnimalMedicalHistoryEditorController', ['$scope', '$routeParams', '$window', '$filter', 'AnimalsDoctorService', 'AnimalsDoctorValues', 'AnimalMedicalHistoryValues',
        function($scope, $routeParams, $window, $filter, AnimalsDoctorService, AnimalsDoctorValues, AnimalMedicalHistoryValues) {

            AnimalsDoctorService.rolesAllowed('doctor');

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            //This variable decides when spinner loading for contentis closed.
            $scope.contentLoading = 2;

            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            var animalId = $routeParams.animalId;       //animal id
            $scope.animal = AnimalsDoctorValues.animal;  //animal
            $scope.animalImage = undefined;
            $scope.itemTypes = AnimalMedicalHistoryValues.itemTypes;
            $scope.item = {};
            $scope.item.animalId = animalId;
            $scope.errors = [];

            /**
             * @param animalId id of animal used for lookup.
             * @return animal instance.
             */
            AnimalsDoctorService.getAnimal(animalId)
                .catch(function(respounce) {
                    $scope.errors.push({msg: $filter('translate')("ERROR_ANIMAL_NOT_FOUND")});
                })
                .finally(function() {
                    $scope.animalImage = "resources/img/no_img.png";
                    if (AnimalsDoctorValues.animal.image != undefined) {
                        if (AnimalsDoctorValues.animal.image.length > 0) {
                            $scope.animalImage = AnimalsDoctorValues.animal.image;
                        }
                    }

                    $scope.contentLoading--;
                });

            AnimalsDoctorService.getAnimalMedicalHistoryTypes()
                .catch(function(respounce) {
                    $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMAL_MH_TYPES")});
                })
                .finally(function() {
                    $scope.contentLoading--;
                });

            $scope.submit = function(isValid) {
                if(!isValid){
                    return;
                }

                $scope.item.date = $filter('date')($scope.item.date, 'yyyy-MM-dd');

                AnimalsDoctorService.updateAnimalMedicalHistoryItem($scope.item)
                    .then(function(){
                        $window.location.href = "#/ua/user/doctor/animals/medical_history/" + animalId;
                    }
                    , function() {
                        $window.alert("Не вдалося добавити запис.");
                    });
            }
        }]);
