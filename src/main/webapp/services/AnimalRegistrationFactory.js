/**
 * Created by oleg on 13.08.2015.
 */
animalRegistrationModule
    .factory('AnimalRegistrationFactory',
        function AnimalRegistrationFactory ($http, $q, RESOURCES, HttpErrorHandlerFactory) {

            //Create instance of current factory
            var factory = {};

            //Insert new homeless animal
            factory.insertHomelessAnimal = function(animal){
                var def = $q.defer();

                $http.post(RESOURCES.ANIMAL_REGISTRATION, animal)
                    .success(function (data) {
                        def.resolve(data);
                    })
                    .error(function (data, status) {

                        if(status === 400) {
                            var resp = [];
                            data.forEach(function (error) {
                                resp.push(error.message);
                            });
                            def.reject(resp);
                        }
                        else
                            def.reject(HttpErrorHandlerFactory.returnError(status));
                    });

                return def.promise;
            };

            //Inject dependencies
            AnimalRegistrationFactory.$inject = ['$q', 'RESOURCES', 'HttpErrorHandlerFactory'];

            return factory;
        }
    );

animalRegistrationModule
    .factory('AnimalDetailFactory',
        function AnimalDetailFactory ($http, $q, RESOURCES, HttpErrorHandlerFactory) {

            //Create instance of current factory
            var factory = {};

            factory.getAnimalTypes = function() {
                var def = $q.defer();

                $http.get(RESOURCES.ANIMAL_TYPES)
                    .success(function(data) {
                        def.resolve(data);
                    })
                    .error(function(data, status) {
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
                    .error(function(data, status) {
                        def.reject(HttpErrorHandlerFactory.returnError(status));
                    });

                return def.promise;
            };

            //Inject dependencies
            AnimalDetailFactory.$inject = ['$q', 'RESOURCES', 'HttpErrorHandlerFactory'];

            return factory;
        }
    );