angular.module('AnimalsModule', ['LocalStorageModule'])
    .service('AnimalsService', ['$http', '$q', 'localStorageService', 'RESOURCES',
        function($http, $q, localStorageService, RESOURCES) {

            $http.defaults.headers.common['AccessToken'] = localStorageService.get('accessToken');

            this.rolesAllowed = function(role) {
                return role == localStorageService.get('userRole');
            }

            /**
             * filter instance used for lookup.
             * @return list of animals.
             */
            this.getAnimalsForAdmin = function(filter) {

                return $http.post(RESOURCES.ANIMALS_FOR_ADMIN, filter);
            }

            /**
             * filter instance used for lookup.
             * @return count of rows for pagination.
             */
            this.getAnimalsPaginatorForAdmin = function(filter) {

                return $http.post(RESOURCES.ANIMALS_FOR_ADMIN_PAGINATOR, filter);
            }

            /**
             * filter instance used for lookup.
             * @return list of animals.
             */
            this.getAnimalsForDoctor = function(filter) {

                return $http.post(RESOURCES.ANIMALS_FOR_DOCTOR, filter);
            }

            /**
             * filter instance used for lookup.
             * @return count of rows for pagination.
             */
            this.getAnimalsPaginatorForDoctor = function(filter) {

                return $http.post(RESOURCES.ANIMALS_FOR_DOCTOR_PAGINATOR, filter);
            }

            /**
             * @param animalId id of animal used for lookup.
             * @return animal instance.
             */
            this.getAnimalForAdmin = function(animalId) {

                return $http.get(RESOURCES.ANIMAL_FOR_ADMIN + animalId);
            }

            /**
             * @param animalId id of animal used for lookup.
             * delete animal.
             */
            this.deleteAnimalForAdmin = function(animalId) {

                return $http.delete(RESOURCES.ANIMAL_FOR_ADMIN_DELETE + animalId);
            }

            /**
             * @param animal instance to be updated
             * update animal.
             */
            this.updateAnimalForAdmin = function(animal) {

                return $http.post(RESOURCES.ANIMAL_FOR_ADMIN_UPDATE, animal);
            }

            /**
             * @return list of animal types.
             */
            this.getAnimalTypes = function() {

                return $http.get(RESOURCES.ANIMAL_TYPES);
            }

            /**
             * @return list of animal services.
             */
            this.getAnimalServices = function() {

                return $http.get(RESOURCES.ANIMAL_SERVICES);
            }

            /**
             * @return list of animal breeds according to animal type.
             */
            this.getAnimalBreeds = function(animalTypeId) {

                return $http.get(RESOURCES.ANIMAL_BREEDS + animalTypeId);
            }

            /**
             * filter instance used for lookup.
             * @return list of animal medical history items.
             */
            this.getMedicalHistoryItemsForDoctor = function(filter) {

                return $http.post(RESOURCES.MEDICAL_HISTORY_ITEMS_FOR_DOCTOR, filter);
            }

            /**
             * filter instance used for lookup.
             * @return count of rows for pagination.
             */
            this.getMedicalHistoryPagesCountForDoctor = function(animalId) {

                return $http.get(RESOURCES.MEDICAL_HISTORY_ITEMS_PAGINATOR_FOR_DOCTOR + animalId);
            }

            /**
             * @param itemId id of animal medical history item used for lookup.
             * @return animal medical history item instance.
             */
            this.getMedicalHistoryItemForDoctor = function(itemId) {

                return $http.get(RESOURCES.MEDICAL_HISTORY_ITEM_FOR_DOCTOR + itemId);
            }

            /**
             * @param itemId id of animal medical history item used for lookup.
             * delete animal medical history item.
             */
            this.getMedicalHistoryItemDeleteForDoctor = function(itemId) {

                return $http.delete(RESOURCES.MEDICAL_HISTORY_ITEM_FOR_DOCTOR_DELETE + itemId);
            }

            /**
             * @return list of animal medical history types.
             */
            this.getAnimalMedicalHistoryTypesForDoctor = function() {

                return $http.get(RESOURCES.MEDICAL_HISTORY_TYPES);
            }

            /**
             * @param item - animal medical history item instance to be updated
             * update animal medical history item.
             */
            this.updateAnimalMedicalHistoryItemForDoctor = function(item) {

                return $http.post(RESOURCES.MEDICAL_HISTORY_ITEM_FOR_DOCTOR_UPDATE, item);
            }

            /**
             * @param id
             * sending message to Twitter.
             */
            this.sendTwitter = function (id) {
                var def = $q.defer();
                $http.post("/webapi/socials/twitter/" + id)
                    .success(function(data) {
                        def.resolve(data);
                    })
                    .error(function() {
                        def.reject("Failed sending message to twitter.");
                    });

                return def.promise;
            };

            /**
             * @param id
             * sending message to Facebook.
             */
            this.sendFacebook = function (id) {
                var def = $q.defer();

                $http.post("/webapi/socials/facebook/" + id)
                    .success(function(data) {
                        def.resolve(data);
                    })
                    .error(function() {
                        def.reject("Failed sending message to facebook.");
                    });

                return def.promise;
            };

            /**
             * @param animalId id of animal used for lookup.
             * delete animal image.
             */
            this.deleteAnimalImageForAdmin = function(animalId) {

                return $http.delete(RESOURCES.ANIMAL_IMAGE_DELETE_FOR_ADMIN + animalId);
            }

        }]);