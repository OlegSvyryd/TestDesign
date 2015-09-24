angular.module('AnimalsAdminModule', ['AnimalsAdminValues', 'AnimalsModule'])
    .service('AnimalsAdminService', ['$http', '$q', '$window', 'AnimalsAdminValues', 'AnimalsService',
        function($http, $q, $window, AnimalsAdminValues, AnimalsService) {

            this.rolesAllowed = function(role) {
                if ( !AnimalsService.rolesAllowed(role) ) {
                    $window.location.href = "#ua";
                }
            }

            /**
             * filter instance used for lookup.
             * @return list of animals.
             */
            this.getAnimals = function() {
                AnimalsAdminValues.animals.values = [];

                return AnimalsService.getAnimalsForAdmin(AnimalsAdminValues.filter)
                    .then(function(response) {
                        AnimalsAdminValues.animals.values = response.data;
                        return response;
                    });
            }

            /**
             * filter instance used for lookup.
             * @return count of rows for pagination.
             */
            this.getPagesCount = function() {

                return AnimalsService.getAnimalsPaginatorForAdmin(AnimalsAdminValues.filter)
                    .then(function(response) {
                        AnimalsAdminValues.totalItems.count = response.data.rowsCount;
                        return response;
                    });
            }

            /**
             * @param animalId id of animal used for lookup.
             * @return animal instance.
             */
            this.getAnimal = function(animalId) {

                if (AnimalsAdminValues.animal != undefined) {
                    if (AnimalsAdminValues.animal.id != undefined) {
                        if (AnimalsAdminValues.animal.id == animalId) {
                            var def = $q.defer();
                            def.resolve(AnimalsAdminValues.animal);
                            return def.promise;
                        }
                    }
                }

                return AnimalsService.getAnimalForAdmin(animalId)
                    .then(function(response) {
                        angular.copy(response.data, AnimalsAdminValues.animal);
                        AnimalsAdminValues.animal.active = AnimalsAdminValues.animal.active.toString();
                        return response;
                    });
            }

            /**
             * @param animalId id of animal used for lookup.
             * delete animal.
             */
            this.deleteAnimal = function(animalId) {

                return AnimalsService.deleteAnimalForAdmin(animalId);
            }

            /**
             * @param animal instance to be updated
             * update animal.
             */
            this.updateAnimal = function(animal) {

                return AnimalsService.updateAnimalForAdmin(animal);
            }

            /**
             * @return list of animal types.
             */
            this.getAnimalTypes = function() {

                if (AnimalsAdminValues.animalTypes.values.length !== 0) {
                    var def = $q.defer();
                    def.resolve(AnimalsAdminValues.animalTypes.values);
                    return def.promise;
                }

                return AnimalsService.getAnimalTypes()
                    .then(function(response) {
                        AnimalsAdminValues.animalTypes.values = response.data;
                        return response;
                    });
            }

            /**
             * @return list of animal services.
             */
            this.getAnimalServices = function() {

                if (AnimalsAdminValues.animalServices.values.length !== 0) {
                    var def = $q.defer();
                    def.resolve(AnimalsAdminValues.animalServices.values);
                    return def.promise;
                }

                return AnimalsService.getAnimalServices()
                    .then(function(response) {
                        AnimalsAdminValues.animalServices.values = response.data;
                        return response;
                    });
            }

            /**
             * @return list of animal breeds according to animal type.
             */
            this.getAnimalBreeds = function(animalTypeId) {

                return AnimalsService.getAnimalBreeds(animalTypeId);
            }

            /**
             * @param id
             * sending message to Twitter.
             */
            this.sendTwitter = function (id) {

                return AnimalsService.sendTwitter(id)
                    .then(function(data) {

                    });
            };

            /**
             * @param id
             * sending message to Facebook.
             */
            this.sendFacebook = function (id) {

                return AnimalsService.sendFacebook(id)
                    .then(function(data) {

                    });
            };

            /**
             * @param animalId id of animal used for lookup.
             * delete animal image.
             */
            this.deleteAnimalImage = function(animalId) {

                return AnimalsService.deleteAnimalImageForAdmin(animalId)
                    .then(function(response) {
                        AnimalsAdminValues.animal.image = undefined;
                        return response;
                    });
            }
    }]);