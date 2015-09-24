//Path for reegistration
var root = 'http://localhost:8080/oauth/webapi/login/';
var root2 = 'http://localhost:8080/AnimalWebApp/webapi/users/user';

function FBLog() {
$('.btn-facebook').click(function() {
		console.log("ping");
	   window.location = root + "facebook";

	});
} 

function googleLog() {
$('.btn-google').click(function() {
		console.log("ping");
	   window.location = root + "google";

	});
}

function gitLog() {
$('.btn-github').click(function() {
		console.log("ping");
	   window.location = root + "github";

	});
}

function twitterLog() {
$('.btn-twitter').click(function() {
		console.log("ping");
	   window.location = root + "twitter";

	});
} 

function usualLog() {
	$('#usualReg').click(function() {

		console.log("ping");
		
		var userLogin=$("#userLogin").val(); 
		var userName=$("#userName").val(); 
		var userSurname=$("#userSurname").val(); 
		var userEmail=$("#userEmail").val(); 
		var password=$("#password").val(); 
		var userPhone=$("#userPhone").val(); 
		var userAdress=$("#userAdress").val(); 
		var userCompany=$("#userCompany").val(); 
		var userCompanyInfo=$("#userCompanyInfo").val(); 
		var userRole = "user";

		// var userJson = ('{"id" :' + petId +', "type" : ' + petType + ', "owner":{"name":' + petOwName + ', "id" : ' + petOwId + ', "adress" : ' + petOwAd + '}, "size" : ' + petSize + '}' );

		var userJson = ('{"active": true, "address": {"id": 30}, "email":"' + userEmail + '", "name" : "' + userName + '", "password" : "'+ password + '", "surname" : "' +
				userSurname + '", "phone" : "'+ userPhone	+ '", "userCompany" : "'+	userCompany + '", "userAdress" : "'+ userAdress+'", "userCompanyInfo" : "' +
				userCompanyInfo+'","userRole": {"id": 3},"userType": {"id": 1},'+ '"registrationDate": "2015-07-22"}'); 
		
		console.log(userJson);

		
		$.ajax({			
			method: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			url: root2,
			data: userJson			
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				console.log(str);
				// $('#result').html(str);
				// highlightResult();
         	});			
		});
} 

function switch1(){
	$('#editProfile').click(function() {
		console.log("ping");
		$('#editUser').css("display", "block");
});
}  


window.onload = function() {
	googleLog();
	FBLog();
	gitLog();
	twitterLog();
	usualLog();
	switch1();
}