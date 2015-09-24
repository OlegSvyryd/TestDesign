angular.module('animalApp').factory('UserDataService',function ($q, $http, $rootScope, $location, localStorageService, UserAnimalsValues, RESOURCES){
	
	return {
		
		getUser:function (id) {	
			
			var def = $q.defer();
			
			$http.defaults.headers.common['AccessToken'] = localStorageService.get("accessToken");
			$http.get(RESOURCES.USER_FOR_USER + id)
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to get user");
            });
			return def.promise;
		},		
	
		getUserAnimals: function (id) {
			
			var def = $q.defer();
			
			$http.get(RESOURCES.ANIMALS_FOR_USER + id + "/animals")
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to get animals");
            });
			return def.promise;
		},
		
		getPaginator: function(id){
			var def = $q.defer();
			$http.get(RESOURCES.PAGINATOR_FOR_USER + id + "/animals/paginator")
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to get animals");
            });
			return def.promise;
		},
		
		getUserAnimalsWithFilter: function(id, filter){
			var def = $q.defer();
			
			$http.post(RESOURCES.ANIMALS_FOR_USER_WITH_FILTER + id + "/animals", filter)
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to get animals");
            });
			return def.promise;
		},
		

		updateUser:function (user , id){
			
			var def = $q.defer();
			
			$http.put(RESOURCES.USER_FOR_USER + id, user)
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to update user");
            });
			return def.promise;			
		},
		
		getAnimal: function (animalId) {
			
			var def = $q.defer();
			var id = localStorageService.get("userId");
			
			$http.defaults.headers.common['AccessToken'] = localStorageService.get("accessToken");
			$http.get(RESOURCES.USER_FOR_USER + id + "/animals/" + animalId)
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to get animal");
            });
			return def.promise;
		},	
		
		
		deleteAnimal: function (animalId) {
			
			var def = $q.defer();
			var id = localStorageService.get("userId");
						
			$http.delete(RESOURCES.USER_FOR_USER + id + "/animals/" + animalId)
			.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to delete animal");
            });
			return def.promise;
		},
		
		updateAnimal: function(animal) {
            var def = $q.defer();
            var id = localStorageService.get("userId");
            
            $http.defaults.headers.common['AccessToken'] = localStorageService.get("accessToken");
            $http.post(RESOURCES.USER_FOR_USER + id + "/animals/animal", animal)
                .success(function(data) {
                    def.resolve(data);
                })
                .error(function() {
                    def.reject("Failed to update animal.");
                });

            return def.promise;
        },
        
        deleteAnimalImage: function (animalId){
        	
        	var def = $q.defer();
        	$http.defaults.headers.common['AccessToken'] = localStorageService.get("accessToken");
        	var id = localStorageService.get("userId");
        	
        	$http.delete(RESOURCES.USER_FOR_USER + id + "/animals/" + animalId + "/image")
        	.success(function (data) {
                def.resolve(data);
            })
            .error(function (error) {
                def.reject("Failed to delete animal image");
            });
			return def.promise;
        }      
		
	};	
	
});