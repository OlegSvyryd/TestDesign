angular.module('AnimalMedicalHistoryDetailedController', ['AnimalsDoctorModule', 'AnimalsDoctorValues', 'AnimalMedicalHistoryValues'])
    .controller('AnimalMedicalHistoryDetailedController', ['$scope', '$routeParams', 'AnimalsDoctorService', 'AnimalsDoctorValues', 'AnimalMedicalHistoryValues',
        function($scope, $routeParams, AnimalsDoctorService, AnimalsDoctorValues, AnimalMedicalHistoryValues) {

            AnimalsDoctorService.rolesAllowed('doctor');

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            //This variable decides when spinner loading for contentis closed.
            $scope.contentLoading = 2;

            var animalId = $routeParams.animalId;        //animal id
            var itemId = $routeParams.itemId;            //item id
            $scope.animal = AnimalsDoctorValues.animal;  //animal
            $scope.animalImage = undefined;
            $scope.item = AnimalMedicalHistoryValues.item;

            /**
             * @param animalId id of animal used for lookup.
             * @return animal instance.
             */
            AnimalsDoctorService.getAnimal(animalId)
                .finally(function() {
                    $scope.animalImage = "resources/img/no_img.png";
                    if (AnimalsDoctorValues.animal.image != undefined) {
                        if (AnimalsDoctorValues.animal.image.length > 0) {
                            $scope.animalImage = AnimalsDoctorValues.animal.image;
                        }
                    }

                    $scope.contentLoading--;
                });

            /**
             * @param itemId id of medical history item used for lookup.
             * @return medical history item instance.
             */
            AnimalsDoctorService.getMedicalHistoryItem(itemId)
                .finally(function() {
                    $scope.contentLoading--;
                });

        }]);