/**
 * Created by oleg on 11.08.2015.
 */
animalFoundModule
    .controller('AnimalFoundController',
        function AnimalFoundController($scope, AnimalFoundFactory, AnimalFoundValues) {

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);

            //spinner usability
            $scope.contentLoading = 0;

            //message with errors
            $scope.errorMessage = '';

            //Pages
            $scope.filter = AnimalFoundValues.filter;            //filter
            $scope.totalItems = AnimalFoundValues.totalItems;    //table rows count
            $scope.animals = AnimalFoundValues.animals;          //animal instance

            /**
             * Get animal list
             */
            var initList = function() {
                $scope.contentLoading++;
                AnimalFoundFactory.getListOfFoundAnimals()
                    .then(
                    function(){},

                    //fail
                    function(error){
                        $scope.errorMessage = error;
                    }
                )
                    .finally(function() {
                        $scope.contentLoading--;
                    });
            };

            /**
             * @return count of rows for pagination.
             */
            $scope.contentLoading++;
            AnimalFoundFactory.getAmountRecords()
                .then(
                    function(){},

                    //fail
                    function(error){
                        $scope.errorMessage = error;
                        $scope.totalItems.count = 0;
                    }
                )
                .finally(function() {
                    $scope.contentLoading--;
                });

            initList();

            /**
             * @return next page.
             */
            $scope.pageChanged = function() {
                //scroll to top of the page
                jQuery('html, body').animate({ scrollTop: 0 }, 500);

                initList();
            };

            /**
             * @return list of animals with given count of rows.
             */
            $scope.countChanged = function(count) {
                $scope.filter.limit = count;
                initList();
            };

            //Dependency injection
            AnimalFoundController.$inject = ['$scope', 'AnimalFoundFactory', 'AnimalFoundValues'];

        }).controller('AnimalFoundFilterController',
            function AnimalFoundFilterController($scope, AnimalFoundFactory, AnimalFoundValues, $window) {

                //locale
                $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

                $scope.filter = AnimalFoundValues.filter;                  //filter
                $scope.animalTypes = AnimalFoundValues.animalTypes;        //list of animal types

                /**
                 * Get animal list
                 */
                var initList = function() {
                    $scope.contentLoading++;
                    AnimalFoundFactory.getListOfFoundAnimals()
                        .then(
                        function(result){},

                        //fail
                        function(error){
                            $scope.errorsFlag = true;
                            $scope.errorMessage = error;
                        }
                    )
                        .finally(function() {
                            $scope.contentLoading--;
                        });
                };

                /**
                 * @return list of animal types.
                 */
                AnimalFoundFactory.getAnimalTypes()
                    .then(
                    function(){},
                    function(error){
                        $scope.errorMessage = error;
                    }
                    )
                    .finally(function() {
                    });

                /**
                 * @return list of animal breeds according to animal type.
                 */
                $scope.getAnimalBreeds = function() {
                    AnimalFoundFactory.getAnimalBreeds($scope.filter.animal.type.id)
                        .then(
                        function(data) {
                            $scope.animalBreeds = data;
                        },
                        function(error){
                            $scope.errorMessage = error;
                        }
                        )
                        .finally(function() {
                        });
                };

                /**
                 * reset filter values.
                 */
                $scope.reset = function() {
                    $scope.filter.animal.type = undefined;
                    $scope.filter.animal.size = undefined;
                    $scope.filter.animal.breed = undefined;
                    $scope.filter.animal.sex = undefined;
                    $scope.filter.animal.dateOfRegister = undefined;
                    $scope.filter.animal.dateOfSterilization = undefined;
                    $scope.filter.animal.image = undefined;

                    clearErrorMessage();
                    $scope.doFilter();
                    jQuery('html, body').animate({ scrollTop: 0 }, 500);
                };

                /**
                 * @return list of animals according to filter values.
                 */
                $scope.doFilter = function() {
                    AnimalFoundFactory.getAmountRecords()
                        .then(
                        function(){
                            if($scope.totalItems.count !== 0){
                                clearErrorMessage();
                            }
                        },

                        //fail
                        function(error){
                            $scope.totalItems.count = 0;
                            $scope.errorMessage = error;
                        });

                    initList();

                    jQuery('html, body').animate({ scrollTop: 0 }, 500);
                };

                /**
                 * Clear error messages
                 */
                function clearErrorMessage(){
                    $scope.errorMessage = '';
                }

                //Dependency injection
        AnimalFoundFilterController.$inject = ['$scope', 'AnimalFoundFactory', 'AnimalFoundValues', '$window'];
    });