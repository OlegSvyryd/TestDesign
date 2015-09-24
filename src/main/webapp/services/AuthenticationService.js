angular.module('animalApp').factory('AuthenticationService',function (Base64, $q, $http, localStorageService, $location, $route, $window, $rootScope, RESOURCES){
	
	return {
		
		login:function (username, password){
			
			var authdata = Base64.encode(username + ':' + password);            
            
            $http.defaults.headers.common['Authorization'] = 'Basic ' + authdata; 
                       
			var memoryMe = localStorageService.get("memoryMe");
			var def = $q.defer();
			
			$http.post(RESOURCES.LOGIN_BASIC + memoryMe, {})
	        .success(function(data){	        	        	
	        		        	
		        if (localStorageService.get("memoryMe")=="ON"){
		        	localStorageService.cookie.set("accessToken",data.accessToken,30);	        		
		        } else {
		        	localStorageService.cookie.set("accessToken",data.accessToken,0.065);	//90 min
		        }		        	
		        localStorageService.set("accessToken", data.accessToken);
		        localStorageService.set("userId", data.userId);
		        localStorageService.set("userName", data.socialLogin);
		        localStorageService.set("userRole", data.userRole);
		        localStorageService.set("userRoleId", data.userRoleId);
		        	
		        def.resolve(data);		        	
	        	
	        }) 
			.error(function(data){
				if(data.userId==0){	
	        		def.resolve(data);
	        	} else {				
	        		def.reject("Failed to enter site");
	        	}
			});
			return def.promise;
		},
		
		logout:function(){			
			
            $http.get(RESOURCES.LOGOUT)
	        .success(function(data){
	        	
	        	localStorageService.clearAll();	
		        $location.path("/ua");	
		        $route.reload();		      
		        
	        }) 
			.error(function(data){				
				console.log("logout session error");
				localStorageService.clearAll();	
		        $location.path("/ua");	
		        $route.reload();
			});
            
		},
		
		
		registerUser:function(user, locale){			
		
			var def = $q.defer();
			
			$http.post(RESOURCES.REGISTRATION + locale, user)
	        .success(function(data){
	        	def.resolve(data);	        	
	        }) 
			.error(function(data, status){				
			if(status === 400){				
				var errors = [];
				for (i = 0; i < data.length; i++) {
				    errors.push(data[i].path);
				}						
				def.resolve(errors);
			} else {
				def.resolve(data);				
			}
			});
			return def.promise;
		},
		
		confirmRegistration:function(userLogin,code){
			
			var def = $q.defer();
			
			$http.post(RESOURCES.CONFIRM_REGISTRATION + userLogin + "/" + code)
	        .success(function(data){ 
	        	def.resolve(data);
	        })
	        .error(function(data){
	        	def.reject("Failed to confirm Registration");
			});
			return def.promise;
		
		},
			
		
		refreshSession:function(){
									
			$http.get(RESOURCES.REFRESH)
	        .success(function(data){	        	
	        	        		       	
		        	localStorageService.cookie.set("accessToken",data.accessToken,30);	        	
		        	localStorageService.set("accessToken", data.accessToken);
		        	localStorageService.set("userId", data.userId);
		        	localStorageService.set("userName", data.socialLogin);
		        	localStorageService.set("userRole", data.userRole);
		        	localStorageService.set("userRoleId", data.userRoleId);	
		        	
		        	if(data.refreshGoogleToken !== "null"){
		        		localStorageService.set("refreshGoogleToken", data.refreshGoogleToken);	        	
		        		localStorageService.cookie.set("refreshGoogleToken",data.refreshGoogleToken,30);		        		
		        		localStorageService.set("disableGoogleButton", true);			        				        		
		        	}
		        	if(data.twitterToken !== "null"){
		        		localStorageService.cookie.set("twitterToken",data.twitterToken,30);
		        		localStorageService.cookie.set("twitterSecret",data.twitterSecret,30);		        		
		        		localStorageService.set("disableTwitterButton", true);		        		
		        	}
		        	if(data.facebookToken !== "null"){		        		
		        		localStorageService.set("disableFacebookButton", true);		        		
		        	}
		        	
		        	$location.path("/ua/user/profile");	
			        $route.reload();
	        	
	        	
	        }) 
			.error(function(data){
				console.log("refresh session error");
				localStorageService.clearAll();
				$location.path("/ua/user/login");	
		        $route.reload();
			});			
			       	
		},
		
		restorePassword: function(email, locale){			
		
			var def = $q.defer();
			
			$http.get(RESOURCES.RESTORE_PASSWORD + email + "/" + locale)
	        .success(function(data){
	        	def.resolve(data);	        	
	        }) 
			.error(function(data){				
				def.resolve(data);				
			});			
			return def.promise;
		},
		
	};	
	
});