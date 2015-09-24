angular.module('animalApp').factory('currentDate', function (){
	
	var d = new Date();
	var month = d.getMonth()+1;
	var day = d.getDate();
	var regDate = d.getFullYear() + '-' + (month<10 ? '0' : '') + month + '-' +    (day<10 ? '0' : '') + day;
	
	return regDate;
	
});