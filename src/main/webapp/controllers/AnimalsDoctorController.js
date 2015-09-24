angular.module('AnimalsDoctorController', ['nya.bootstrap.select', 'DPController', 'AnimalsDoctorValues', 'AnimalsDoctorModule'])
    .controller('AnimalsDoctorController', ['$scope', 'AnimalsDoctorService', 'AnimalsDoctorValues', '$filter', '$window',
        function($scope, AnimalsDoctorService, AnimalsDoctorValues, $filter, $window) {

            AnimalsDoctorService.rolesAllowed('doctor');

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            $scope.contentLoading = 0;

            $scope.filter = AnimalsDoctorValues.filter;            //filter
            $scope.totalItems = AnimalsDoctorValues.totalItems;    //table rows count
            $scope.animals = AnimalsDoctorValues.animals;          //animal instance
            $scope.errors = [];

            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            /**
             * @return count of rows for pagination.
             */
            var getPagesCount = function() {
                $scope.contentLoading++;

                AnimalsDoctorService.getPagesCount()
                    .catch(function() {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ROWS_COUNT")});
                    })
                    .finally(function () {
                        $scope.contentLoading--;
                    });
            }

            /**
             * @return list of animals.
             */
            var getAnimals = function() {
                $scope.error = undefined;
                $scope.contentLoading++;

                AnimalsDoctorService.getAnimals()
                    .then(function (response) {
                        if ($scope.animals.values.length == 0) {
                            $scope.error = $filter('translate')("ERROR_NO_ANIMALS");
                        }
                    }, function(response) {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS")});
                    })
                    .finally(function () {
                        $scope.contentLoading--;
                    });
            }

            $scope.getData = function() {
                getPagesCount();
                getAnimals();
            }

            $scope.getData();

            /**
             * @return next page.
             */
            $scope.pageChanged = function() {
                getAnimals();
            };

            /**
             * @return list of animals with given count of rows.
             */
            $scope.countChanged = function(count) {
                $scope.filter.limit = count;
                $scope.filter.page = 1;
                getAnimals();
            };
    }])
    .controller('AnimalsFilterDoctorController', ['$scope', '$filter', 'AnimalsDoctorService', 'AnimalsDoctorValues', '$window',
        function($scope, $filter, AnimalsDoctorService, AnimalsDoctorValues, $window) {

            $scope.filter = AnimalsDoctorValues.filter;                  //filter
            $scope.animalTypes = AnimalsDoctorValues.animalTypes;        //list of animal types
            $scope.animalServices = AnimalsDoctorValues.animalServices;  //list of animal services
            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            /**
             * @return list of animal types.
             */
            var getAnimalType = function() {
                $scope.$parent.contentLoading++;

                AnimalsDoctorService.getAnimalTypes()
                    .catch(function (response) {
                        $scope.$parent.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_TYPES")});
                    })
                    .finally(function () {
                        $scope.$parent.contentLoading--;
                    });
            }

            getAnimalType();

            /**
             * @return list of animal types.
             */
            var getAnimalServices = function() {
                $scope.$parent.contentLoading++;

                AnimalsDoctorService.getAnimalServices()
                    .catch(function (response) {
                        $scope.$parent.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_SERVICES")});
                    })
                    .finally(function () {
                        $scope.$parent.contentLoading--;
                    });
            }

            getAnimalServices();

            /**
             * @return list of animal breeds according to animal type.
             */
            $scope.getAnimalBreeds = function() {
                $scope.filterAnimalBreedFlag = true;

                AnimalsDoctorService.getAnimalBreeds($scope.filter.animal.type.id)
                    .then(function(response) {
                        $scope.animalBreeds = response.data;
                    }, function(response) {
                        $scope.$parent.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_BREEDS")});
                    })
                    .finally(function() {
                        $scope.filterAnimalBreedFlag = false;
                    });
            }

            /**
             * reset filter values.
             */
            $scope.reset = function() {
                $scope.filter.animal.transpNumber = undefined;
                $scope.filter.animal.service = undefined;
                $scope.filter.animal.type = undefined;
                $scope.filter.animal.breed = undefined;
                $scope.filter.animal.sex = undefined;
                $scope.filter.animal.dateOfRegister = undefined;

                $scope.submit(true);
            }

            /**
             * @return list of animals according to filter values.
             */
            $scope.submit = function(isValid) {
                if(!isValid){
                    return;
                }

                $scope.filter.animal.dateOfRegister = $filter('date')($scope.filter.animal.dateOfRegister, 'yyyy-MM-dd');
                $scope.filter.page = 1;

                $scope.$parent.getData();
            };
    }]);