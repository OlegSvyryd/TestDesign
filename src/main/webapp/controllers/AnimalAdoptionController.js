/**
 * Created by oleg on 11.08.2015.
 */
adoptionModule
    .controller('AnimalAdoptionController',
        function AnimalAdoptionController($scope, $filter, AdoptionFactory, AnimalAdoptionValues) {

            //initialize loading spinner
            var targetContent = document.getElementById('loading-block');
            new Spinner(opts).spin(targetContent);

            //spinner usability
            $scope.contentLoading = 0;

            //message with errors
            $scope.errorMessage = '';

            //Pages
            $scope.filter = AnimalAdoptionValues.filter;            //filter
            $scope.totalItems = AnimalAdoptionValues.totalItems;    //table rows count
            $scope.animals = AnimalAdoptionValues.animals;          //animal instance

            /**
             * Get animal list and statuses
             */
            var initList = function(){
                $scope.contentLoading++;
                AdoptionFactory.getListOfAdoptionAnimals()
                    .then(
                    function(result){
                        //get statuses for each animal
                        for(var i = 0; i < result.length; i++)
                            AdoptionFactory.getListOfAnimalStatuses(result[i].id)
                                .then(
                                function(result){
                                    $scope.contentLoading++;
                                    for(var i = 0; i < result.length; i++){
                                        if(result[i].animalStatus.id === 14) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status catched"></p>');
                                            $(".sprite-status.catched")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CAUGHT'));
                                        }
                                        if(result[i].animalStatus.id === 13) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status released"></p>');
                                            $(".sprite-status.released")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_RELEASED'));
                                        }
                                        if(result[i].animalStatus.id === 20) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status newborn"></p>');
                                            $(".sprite-status.newborn")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_NEWBORN'));
                                        }
                                        if(result[i].animalStatus.id === 12) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status disinfected"></p>');
                                            $(".sprite-status.disinfected")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_DISINFECTED'));
                                        }
                                        if(result[i].animalStatus.id === 16) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status vaccinated"></p>');
                                            $(".sprite-status.vaccinated")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_VACCINATED'));
                                        }
                                        if(result[i].animalStatus.id === 10) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status cured"></p>');
                                            $(".sprite-status.cured")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CURED'));
                                        }
                                        if(result[i].animalStatus.id === 19) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status sick"></p>');
                                            $(".sprite-status.sick")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_SICK'));
                                        }
                                        if(result[i].animalStatus.id === 22) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status injured"></p>');
                                            $(".sprite-status.injured")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_INJURED'));
                                        }
                                        if(result[i].animalStatus.id === 17) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status adopt"></p>');
                                            $(".sprite-status.adopt")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_ADOPT'));
                                        }
                                        if(result[i].animalStatus.id === 18) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status custody"></p>');
                                            $(".sprite-status.custody")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CUSTODY'));
                                        }
                                    }
                                },
                                function(error){
                                    $scope.errorMessage = error;
                                }
                            ).finally(function() {
                                    $scope.contentLoading--;
                                });
                    },
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
            AdoptionFactory.getAmountRecords()
                .then(
                    function(){},

                    //fail
                    function(error){
                        $scope.totalItems.count = 0;
                        $scope.errorMessage = error;
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
            AnimalAdoptionController.$inject = ['$scope', '$filter', 'AdoptionFactory', 'AnimalAdoptionValues', '$translate'];

        }).controller('AdoptionFilterController',
            function AdoptionFilterController($scope, $filter, AdoptionFactory, AnimalAdoptionValues, $window) {

                //locale
                $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

                $scope.filter = AnimalAdoptionValues.filter;                  //filter
                $scope.animalTypes = AnimalAdoptionValues.animalTypes;        //list of animal types

                /**
                 * Get animal list and statuses
                 */
                var initList = function(){
                    $scope.contentLoading++;
                    AdoptionFactory.getListOfAdoptionAnimals()
                        .then(
                        function(result){
                            //get statuses for each animal
                            for(var i = 0; i < result.length; i++)
                                AdoptionFactory.getListOfAnimalStatuses(result[i].id)
                                    .then(
                                    function(result){
                                        for(var i = 0; i < result.length; i++){
                                            if(result[i].animalStatus.id === 14) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status catched"></p>');
                                                $(".sprite-status.catched")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_CAUGHT'));
                                            }
                                            if(result[i].animalStatus.id === 13) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status released"></p>');
                                                $(".sprite-status.released")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_RELEASED'));
                                            }
                                            if(result[i].animalStatus.id === 20) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status newborn"></p>');
                                                $(".sprite-status.newborn")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_NEWBORN'));
                                            }
                                            if(result[i].animalStatus.id === 12) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status disinfected"></p>');
                                                $(".sprite-status.disinfected")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_DISINFECTED'));
                                            }
                                            if(result[i].animalStatus.id === 16) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status vaccinated"></p>');
                                                $(".sprite-status.vaccinated")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_VACCINATED'));
                                            }
                                            if(result[i].animalStatus.id === 10) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status cured"></p>');
                                                $(".sprite-status.cured")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_CURED'));
                                            }
                                            if(result[i].animalStatus.id === 19) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status sick"></p>');
                                                $(".sprite-status.sick")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_SICK'));
                                            }
                                            if(result[i].animalStatus.id === 22) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status injured"></p>');
                                                $(".sprite-status.injured")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_INJURED'));
                                            }
                                            if(result[i].animalStatus.id === 17) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status adopt"></p>');
                                                $(".sprite-status.adopt")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_ADOPT'));
                                            }
                                            if(result[i].animalStatus.id === 18) {
                                                $("#status-sprite-" + result[i].animal.id)
                                                    .append('<p class="center-block sprite-status custody"></p>');
                                                $(".sprite-status.custody")
                                                    .attr('title', $filter('translate')('ANIMAL_STATUS_CUSTODY'));
                                            }
                                        }
                                    },
                                    function(error){
                                        $scope.errorMessage = error;
                                    }
                                );
                        },
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
                 * @return list of animal types.
                 */
                AdoptionFactory.getAnimalTypes()
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
                    AdoptionFactory.getAnimalBreeds($scope.filter.animal.type.id)
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
                    AdoptionFactory.getAmountRecords()
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
        AdoptionFilterController.$inject = ['$scope', '$filter', 'AdoptionFactory', 'AnimalAdoptionValues', '$window'];
    });