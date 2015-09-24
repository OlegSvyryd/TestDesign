/**
 * Created by oleg on 11.08.2015.
 */
animalLostModule
    .factory('AnimalLostFactory',
        function AnimalLostFactory($http, $q, RESOURCES, HttpErrorHandlerFactory, AnimalLostValues){

        //Create instance of current factory
        var factory = {};

        //Get amount animals for adoption
        factory.getAmountRecords = function() {
            var def = $q.defer();

            $http.post(RESOURCES.ANIMALS_LOST_PAGINATOR, AnimalLostValues.filter)
                .success(function (data) {
                    if(data.rowsCount === 0)
                        def.reject(HttpErrorHandlerFactory.returnError(404));

                    AnimalLostValues.totalItems.count = data.rowsCount;
                    def.resolve(data);
                })
                .error(function (data, status) {
                    AnimalLostValues.totalItems.count = 0;
                    def.reject(HttpErrorHandlerFactory.returnError(status));
                });

            return def.promise;
        };

        //Get list of animals for adoption
        factory.getListOfLostAnimals = function(){
            var def = $q.defer();

            $http.post(RESOURCES.ANIMALS_LOST, AnimalLostValues.filter)
                .success(function (data) {
                    AnimalLostValues.animals.values = data;
                    def.resolve(data);
                })
                .error(function () {
                    def.reject(HttpErrorHandlerFactory.returnError(status));
                });

            return def.promise;
        };

            factory.getAnimalTypes = function() {
                var def = $q.defer();

                if (AnimalLostValues.animalTypes.values.length !== 0) {
                    def.resolve(AnimalLostValues.animalTypes.values);
                    return def.promise;
                }

                $http.get(RESOURCES.ANIMAL_TYPES)
                    .success(function(data) {
                        AnimalLostValues.animalTypes.values = data;
                        def.resolve(data);
                    })
                    .error(function() {
                        def.reject(HttpErrorHandlerFactory.returnError(status));
                    });

                return def.promise;
            };

            factory.getAnimalBreeds = function(animalTypeId) {
                var def = $q.defer();

                $http.get(RESOURCES.ANIMAL_BREEDS + animalTypeId)
                    .success(function(data) {
                        def.resolve(data);
                    })
                    .error(function() {
                        def.reject(HttpErrorHandlerFactory.returnError(status));
                    });

                return def.promise;
            };

        //Inject dependencies
        AnimalLostFactory.$inject = ['$q', 'RESOURCES', 'HttpErrorHandlerFactory', 'AnimalLostValues'];

        return factory;
        }
    );