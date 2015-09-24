angular.module('animalApp').factory('hashPassword',function (){
	
	return function (pass){
		return CryptoJS.MD5(pass).toString();		
	};	
	
});