var animalAppControllers = angular.module('UserProfileController', ['UserAnimalsValues']);

animalApp.controller('UserProfileController', ['$scope', 'UserDataService', 'AuthenticationService', 'hashPassword', 'localStorageService', '$route',
                                               '$location', 'UserAnimalsValues', 'OauthAuthenticationService',
                                               function($scope, UserDataService, AuthenticationService, hashPassword, localStorageService, $route,
                                            		   $location, UserAnimalsValues, OauthAuthenticationService) {
		
	
	$scope.currentLanguage = localStorage.getItem("NG_TRANSLATE_LANG_KEY");
	//initialize loading spinner
    var targetContent = document.getElementById('loading-block');
    var targetContent2 = document.getElementById('loading-block2');
    new Spinner(opts).spin(targetContent);
    new Spinner(opts).spin(targetContent2);
    $scope.contentLoading = 0;
    $scope.contentLoading2 = 0;
    
    $scope.filter = UserAnimalsValues.filter;            //filter
    $scope.totalItems = UserAnimalsValues.totalItems;    //table rows count
    $scope.animals = UserAnimalsValues.animals;          //animal instance
	
    if(localStorageService.get("disableGoogleButton")){
    	$scope.disableGoogle=true;
    }
    if(localStorageService.get("disableTwitterButton")){
    	$scope.disableTwitter=true;
    }
    if(localStorageService.get("disableFacebookButton")){
    	$scope.disableFacebook=true;
    }
	
	$scope.IsHidden = true;
	$scope.showPopup = function () {$scope.IsHidden =  false;}    
	$scope.closePopup = function () {$scope.IsHidden =  true; $route.reload();}
	
	$scope.userInfo = null;
	$scope.fields = null;
	
	if(!localStorageService.get("userId")){
		AuthenticationService.refreshSession();  		
	} else {
			
		if (localStorageService.get('userRole')!=="moderator" && 
				localStorageService.get('userRole')!=="doctor" && 
				localStorageService.get('userRole')!=="guest"){
			$location.path("#ua");	
		}
		
		
		var id = localStorageService.get("userId");		

		$scope.contentLoading++;
		UserDataService.getUser(id).then(
				function(result){
					$scope.userInfo=result;
					$scope.fields = $scope.userInfo;
					$scope.contentLoading--;
				},
				function(error){					
					console.log(error)
					$scope.contentLoading--;
				}
			);		
		
		$scope.contentLoading2++;		
		UserDataService.getPaginator(id).then(
			function(result){				
				UserAnimalsValues.totalItems.count = result.rowsCount;
			},
			function(error){
				UserAnimalsValues.totalItems.count = 0;
				console.log(error)				
			}
		);		
		
		
		var getAnimals = function() { 
			UserDataService.getUserAnimalsWithFilter(id, UserAnimalsValues.filter).then(		
				function(result){					
					UserAnimalsValues.animals.values=result;
					$scope.contentLoading2--;
					if ($scope.animals.values.length == 0) {						
                        $scope.errorNoAnimals = true;
					}
				},
				function(error){					
					$scope.contentLoading2--;
				}
			);
		}
		
		$scope.getData = function() {
            getAnimals();
        };
        
        $scope.getData();

		
		/**
         * @return next page.
         */
        $scope.pageChanged = function() {
            $scope.contentLoading2 = 1;
            getAnimals();
        };

        /**
         * @return list of animals with given count of rows.
         */
        $scope.countChanged = function(count) {
            $scope.filter.limit = count;          
            if(UserAnimalsValues.totalItems.count<(UserAnimalsValues.filter.page*count)){            	
            	UserAnimalsValues.filter.page=Math.ceil(UserAnimalsValues.totalItems.count/count);            	
            }
            $scope.contentLoading2 = 1;
            getAnimals();    		
        };
	}	
	
	
    $scope.submitUpdatedForm=function(){  
    	  	
    	if($scope.passwordNew){    		
    		$scope.fields.password=hashPassword($scope.passwordNew);     		
    	}     	
    	UserDataService.updateUser($scope.fields, $scope.fields.id).then(
				function(result){
					console.log("user updated");				
				},
				function(error){
					console.log(error)
				}
		);		
		$scope.IsHidden =  true;
	};	  
	
	$scope.AddOwnAnimal=function(){    	
    	    	
    	$location.path("/ua/animal/registration_owned");	    		
    	
	};	
	
	$scope.JoinGoogle=function(){	
		
		OauthAuthenticationService.loginGoogle();
		
		
	};
	
	$scope.JoinFacebook=function(){	
		
		OauthAuthenticationService.loginFacebook();
		
		
	};
	
	$scope.JoinTwitter=function(){	
		
		OauthAuthenticationService.loginTwitter();
		
		
	};
	
	if($location.search().join){
		$scope.errorJoinMessage="Помилка об'єднання акаунтів. Даний соціальний акаунт вже використовується!"; 
	} else {
		$scope.errorJoinMessage=null;
	}
	
	
	
}]);