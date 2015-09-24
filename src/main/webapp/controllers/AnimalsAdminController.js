angular.module('AnimalsAdminController', ['AnimalsAdminModule', 'nya.bootstrap.select', 'DPController', 'AnimalsAdminValues'])
    .controller('AnimalsAdminController', ['$scope', 'AnimalsAdminService', 'AnimalsAdminValues', '$filter', '$window', 'angularPopupBoxes',
        function($scope, AnimalsAdminService, AnimalsAdminValues, $filter, $window, angularPopupBoxes) {

            AnimalsAdminService.rolesAllowed("moderator");

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);
            $scope.contentLoading = 0;

            $scope.filter = AnimalsAdminValues.filter;            //filter
            $scope.totalItems = AnimalsAdminValues.totalItems;    //table rows count
            $scope.animals = AnimalsAdminValues.animals;          //animal instance
            $scope.errors = [];

            var successMessage = $filter('translate')('SOCIALS_SENDED');
            //alert success registration
            $scope.alertSample = function() {
                angularPopupBoxes.alert(successMessage).result.then(function() {
                    location.href="#/ua/user/home/animals";
                });
            };

            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            /**
             * @return count of rows for pagination.
             */
            var getPagesCount = function() {
                $scope.contentLoading++;

                AnimalsAdminService.getPagesCount()
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

                AnimalsAdminService.getAnimals()
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

            /**
             * @param id
             * sending message to Twitter.
             */
            $scope.sendTwitter = function (id) {
                AnimalsAdminService.sendTwitter(id);
                $scope.alertSample();
                $filter('filter')(AnimalsAdminValues.animals.values,{id: id})[0].dateOfTwitter = new Date();
            };

            /**
             * @param id
             * sending message to Facebook.
             */
            $scope.sendFacebook = function (id) {
                AnimalsAdminService.sendFacebook(id);
                $scope.alertSample();
                $filter('filter')(AnimalsAdminValues.animals.values,{id: id})[0].dateOfFacebook = 1;
            };

            $scope.closeAlert = function(index) {
                $scope.errors.splice(index, 1);
            };
    }])
    .controller('AnimalsFilterAdminController', ['$scope', '$filter', 'AnimalsAdminService', 'AnimalsAdminValues', '$window',
        function($scope, $filter, AnimalsAdminService, AnimalsAdminValues, $window) {

            $scope.filter = AnimalsAdminValues.filter;                  //filter
            $scope.animalTypes = AnimalsAdminValues.animalTypes;        //list of animal types
            $scope.animalServices = AnimalsAdminValues.animalServices;  //list of animal services
            $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

            /**
             * @return list of animal types.
             */
            var getAnimalTypes = function() {
                $scope.$parent.contentLoading++;

                AnimalsAdminService.getAnimalTypes()
                    .catch(function() {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_TYPES")});
                    })
                    .finally(function() {
                        $scope.$parent.contentLoading--;
                    });
            }

            /**
             * @return list of animal types.
             */
            var getAnimalServices = function() {
                $scope.$parent.contentLoading++;

                AnimalsAdminService.getAnimalServices()
                    .catch(function() {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_SERVICES")});
                    })
                    .finally(function() {
                        $scope.$parent.contentLoading--;
                    });
            }

            var getFilterData = function() {
                getAnimalTypes();
                getAnimalServices();
            }

            getFilterData();

            /**
             * @return list of animal breeds according to animal type.
             */
            $scope.getAnimalBreeds = function() {
                $scope.filterAnimalBreedFlag = true;
                AnimalsAdminService.getAnimalBreeds($scope.filter.animal.type.id)
                    .then(function(response) {
                        $scope.animalBreeds = response.data;
                    }, function(response) {
                        $scope.errors.push({msg: $filter('translate')("ERROR_FAILED_TO_GET_ANIMALS_BREEDS")});
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
                $scope.filter.animal.active = undefined;

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