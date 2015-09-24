var root = 'http://localhost:8080/AnimalWebApp/webapi/animals/';
var root2 = 'http://localhost:8080/AnimalWebApp/webapi/users/';

function highlightResult() {
	$('#result').each(function(i, block) {
		hljs.highlightBlock(block);
	});
}

function prepareJson(str) {
	var newstr =  str.replace(/\n/g, '<br/>')
					.replace(/\\n/g, ' ')
					.replace(/\t/g, '&nbsp;&nbsp;');
	return newstr
}

function getAllPets() {
	$('#getAll').click(function() {

		$.ajax({
			url: root,
			method: 'GET'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
        });
    });
}        


function getPet() {
	$('#getPet').click(function() {		
		
		var petId=$('#petIdGet').val();
		console.log(petId);

		$.ajax({
			url: root + petId,
			method: 'GET'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });
    });	
}

// POST with JSON
function postPet() {
	$('#postPet').click(function() {
		var animalJson=$("#petJSON").val(); 
		console.log(animalJson);

		//var string = ('{"id" :' + petId +', "type" : ' + petType + ', "owner":{"name":' + petOwName + ', "id" : ' + petOwId + ', "adress" : ' + petOwAd + '}, "size" : ' + petSize + '}' );
	
		$.ajax({			
			method: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			url: root + "animal",
			data: animalJson			
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });			
	});
}


//PUT with JSON
function putPet() {
	$('#putPet').click(function() {
		var animalJson=$("#petJSON").val(); 
		console.log(animalJson);

		//var string = ('{"id" :' + petId +', "type" : ' + petType + ', "owner":{"name":' + petOwName + ', "id" : ' + petOwId + ', "adress" : ' + petOwAd + '}, "size" : ' + petSize + '}' );
	
		$.ajax({			
			method: 'PUT',
			contentType: 'application/json',
			dataType: 'json',
			url: root + "animal",
			data: animalJson			
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });			
	});
}


function delPet() {
	$('#delPet').click(function() {		
		
		var petId=$('#petDelId').val();
		console.log(petId);

		$.ajax({
			url: root + petId,
			method: 'DELETE'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
		});
	});	
}


function getAllUsers() {
	$('#getAllUs').click(function() {

		$.ajax({
			url: root2,
			method: 'GET'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
        });
    });
}        


function getUser() {
	$('#getUs').click(function() {		
		
		var petId=$('#UsIdGet').val();
		console.log(petId);

		$.ajax({
			url: root2 + petId,
			method: 'GET'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });
    });	
}

// POST with JSON
function postUser() {
	$('#postUs').click(function() {
		var animalJson=$("#UsJson").val(); 
		console.log(animalJson);

		
		$.ajax({			
			method: 'POST',
			contentType: 'application/json',
			dataType: 'json',
			url: root2 + "user",
			data: animalJson			
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });			
	});
}


//PUT with JSON
function putUser() {
	$('#putUs').click(function() {
		var animalJson=$("#UsJson").val(); 
		console.log(animalJson);
		
		$.ajax({			
			method: 'PUT',
			contentType: 'application/json',
			dataType: 'json',
			url: root2 + "user",
			data: animalJson			
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
         });			
	});
}


function delUser() {
	$('#delUs').click(function() {		
		
		var petId=$('#UsDelId').val();
		console.log(petId);

		$.ajax({
			url: root2 + petId,
			method: 'DELETE'
			}).then(function(data) {
				var str = JSON.stringify(data, null, '\t');
				str = prepareJson(str);
				$('#result').html(str);
				highlightResult();
		});
	});	
}



window.onload = function() {
	getAllPets();
	getPet();
	postPet();
	putPet();
	delPet();

	getAllUsers();
	getUser();
	postUser();
	putUser();
	delUser();
	
}




