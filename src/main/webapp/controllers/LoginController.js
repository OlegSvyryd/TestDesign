var animalAppControllers = angular.module('LoginController', []);

animalApp.controller('LoginController', ['$scope', '$location', '$route', 'AuthenticationService', 'hashPassword', 'localStorageService', 'OauthAuthenticationService',
                                         function($scope, $location, $route, AuthenticationService, hashPassword, localStorageService, OauthAuthenticationService) {
	
	var locale = localStorage.getItem("NG_TRANSLATE_LANG_KEY");
	
	$scope.IsHidden = true;
	$scope.showRenewBlock = function () {$scope.IsHidden =  false;} 
	
	$scope.restorePasword=function (){
		
		AuthenticationService.restorePassword($scope.mailToRestore, locale).then(
			function(result){
				if(result.userId==1){
			        $scope.confirmRestoreMessage=true;
			        console.log("success");	
				} else if (result.userId==0){
					$scope.errorRestoreMessage1=true;
					console.log("not found");						        
			    } else if (result.userId==-1){
					$scope.errorRestoreMessage2=true;
					console.log("serverEroor");			    	 
			    } else if (result.userId==-2){
					$scope.errorRestoreMessage3=true;
					console.log("userNotActive");			    	 
			    } else {
			        console.log("error");			        
			    }
			}
	
		);
	}
	
	$scope.login=function(){			
		
		var userLogin = $scope.login.userLogin;
		var password = hashPassword($scope.login.password);
		
		var memoryMe ="OFF"
		if($scope.login.memoryMe) memoryMe ="ON";
		
		localStorageService.set("memoryMe", memoryMe);
		
		
		AuthenticationService.login(userLogin, password).then(
				function(result){					
					if(result.userId==0){
		        		$scope.errorConfirmMessage=true;		        		  		
		        	} else {			        	
				        $location.path("/ua/user/profile");	
				        $route.reload();
		        	}					
				},
				function(error){
					$scope.errorMessage=true;
				}
		);
		
	}	
	
	$scope.loginGoogle=function(){		
		
		if (localStorageService.cookie.get("refreshGoogleToken")) {
			OauthAuthenticationService.loginDirectGoogle();			
		} else {
			OauthAuthenticationService.loginGoogle();
		}
		
	}
	
	$scope.loginFacebook=function(){		
		
		OauthAuthenticationService.loginFacebook();		
		
	}
	
	$scope.loginTwitter=function(){		
		
		if (localStorageService.cookie.get("twitterToken") && localStorageService.cookie.get("twitterSecret")) {
			OauthAuthenticationService.loginDirectTwitter();			
		} else {
			OauthAuthenticationService.loginTwitter();		
		}
	}
	
	
	
}]);