angular.module('AnimalShortInfoService', [])
    .service('AnimalShortInfoService', ['$http', '$q',  function($http, $q) {

        this.getAnimal = function(animalId) {
            var def = $q.defer();

            $http.get("/webapi/animals/service/" + animalId)
                .success(function(data) {
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to get animal");
                });

            return def.promise;
        };

        this.sendMessage = function (message) {
            var def = $q.defer();

            $http.post("/webapi/service/message", message)
                .success(function (data) {
                    def.resolve(data);
                })
                .error(function (data) {
                    def.reject("Failed to send message");
                });

            return def.promise;
        }

    }]);