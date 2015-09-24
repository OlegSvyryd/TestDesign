angular.module('AnimalShortInfoController', ['AnimalShortInfoService', 'vcRecaptcha','ContactsValues'])
	.controller('AnimalShortInfoController', ['$scope', 'AnimalShortInfoService', 'AdoptionFactory', '$routeParams', '$filter', 'vcRecaptchaService', 'ContactsValues',
		function($scope, AnimalShortInfoService, AdoptionFactory, $routeParams, $filter,  vcRecaptchaService, ContactsValues) {

			var service = $routeParams.service;

			var animalId = $routeParams.animalId;

			$scope.service = service;
			$scope.animalId = animalId;
			$scope.animalImage = "resources/img/noimg.png";

			if(service === "found"){
				$scope.detail=false;
				$scope.textButton= $filter('translate')('CONTACT_AUTHOR');
				$scope.linkBack="#/ua/animal/found"
				$scope.textTitle = $filter('translate')('ANIMAL_SHORT_INFO_FIND');
			};
			if(service === "adoption"){
				$scope.detail=true;
				$scope.textButton= $filter('translate')('ADOPT');
				$scope.linkBack="#/ua/animal/adoption";
				$scope.textTitle = $filter('translate')('ANIMAL_SHORT_INFO_ADOPTION');
			};
			if(service === "lost") {
				$scope.detail=true;
				$scope.textButton= $filter('translate')('CONTACT_OWNER');
				$scope.linkBack="#/ua/animal/lost";
				$scope.textTitle = $filter('translate')('ANIMAL_SHORT_INFO_LOST');
			};

			$scope.openDialog = function(){
				$scope.showPopUpDialog = true;
			}

			this.getAnimal = function(animalId) {
				AnimalShortInfoService.getAnimal(animalId)
					.then(function(data) {
						$scope.animal = data;
						if ($scope.animal.image != undefined) {
							if ($scope.animal.image.length > 0) {
								$scope.animalImage = $scope.animal.image;
							}
						}

                            AdoptionFactory.getListOfAnimalStatuses(animalId)
                                .then(
                                function(result){
                                    for(var i = 0; i < result.length; i++){
                                        if(result[i].animalStatus.id === 14) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status catched"></p>');
                                            $(".sprite-status.catched")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CAUGHT'));
                                        }
                                        if(result[i].animalStatus.id === 13) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status released"></p>');
                                            $(".sprite-status.released")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_RELEASED'));
                                        }
                                        if(result[i].animalStatus.id === 20) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status newborn"></p>');
                                            $(".sprite-status.newborn")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_NEWBORN'));
                                        }
                                        if(result[i].animalStatus.id === 12) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status disinfected"></p>');
                                            $(".sprite-status.disinfected")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_DISINFECTED'));
                                        }
                                        if(result[i].animalStatus.id === 16) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status vaccinated"></p>');
                                            $(".sprite-status.vaccinated")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_VACCINATED'));
                                        }
                                        if(result[i].animalStatus.id === 10) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status cured"></p>');
                                            $(".sprite-status.cured")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CURED'));
                                        }
                                        if(result[i].animalStatus.id === 19) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status sick"></p>');
                                            $(".sprite-status.sick")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_SICK'));
                                        }
                                        if(result[i].animalStatus.id === 22) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status injured"></p>');
                                            $(".sprite-status.injured")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_INJURED'));
                                        }
                                        if(result[i].animalStatus.id === 17) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status adopt"></p>');
                                            $(".sprite-status.adopt")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_ADOPT'));
                                        }
                                        if(result[i].animalStatus.id === 18) {
                                            $("#status-sprite-" + result[i].animal.id)
                                                .append('<p class="center-block sprite-status custody"></p>');
                                            $(".sprite-status.custody")
                                                .attr('title', $filter('translate')('ANIMAL_STATUS_CUSTODY'));
                                        }
                                    }
                                },

                                function(error){
                                    $scope.errorMessage = error;
                                }
                            );

                    },
					function(data) {
						console.log('Animal retrieval failed.');
					});
			};
			this.getAnimal(animalId);


//initialize loading spinner
			var target = document.getElementById('loading-block')
			new Spinner(ContactsValues.spinneropts).spin(target);

			$scope.spinnerloading = 0;
			$scope.startupmessage = 1;
			$scope.okmessage = 0;
			$scope.errormessage = 0;

			$scope.response = null;
			$scope.widgetId = null;

			$scope.setResponse = function (response) {
				$scope.response = response;
			};
			$scope.setWidgetId = function (widgetId) {
				$scope.widgetId = widgetId;
			};
			$scope.cbExpiration = function () {
				$scope.response = null;
			};

			this.sendMessage = function (message) {
				AnimalShortInfoService.sendMessage(message)
					.then(function(response){
						console.log(response);
						if (response.success === true) {
							$scope.spinnerloading = 0;
							$scope.startupmessage = 0;
							$scope.okmessage = 1;
							$scope.errormessage = 0;

							$scope.message.signup='';
							$scope.message.email='';
							$scope.message.text='';
							vcRecaptchaService.reload($scope.widgetId);

						} else {
							$scope.spinnerloading = 0;
							$scope.startupmessage = 0;
							$scope.okmessage = 0;
							$scope.errormessage = 1;

							vcRecaptchaService.reload($scope.widgetId);
						}
					},

					function(error){
						$scope.spinnerloading = 0;
						$scope.startupmessage = 0;
						$scope.okmessage = 0;
						$scope.errormessage = 1;

						// In case of a failed validation you need to reload the captcha
						// because each response can be checked just once
						vcRecaptchaService.reload($scope.widgetId);
					});


				$scope.submit = function () {
					if (vcRecaptchaService.getResponse === "") { //if answer from Google is empty
						$scope.spinnerloading = 0;
						$scope.startupmessage = 1;
						$scope.okmessage = 0;
						$scope.errormessage = 0;

					} else {
						$scope.spinnerloading = 1;
						$scope.startupmessage = 0;
						$scope.okmessage = 0;
						$scope.errormessage = 0;

						$scope.message.gRecaptchaResponse = $scope.response ;                //g-captcah-reponse for server
						console.log($scope.message);

						$scope.closeDialog();

						/* MAKE AJAX REQUEST to our server with g-captcha-string */
						this.sendMessage($scope.message);



					}
				}
			}
		}])
	.directive('popUpDialog', function(){
		return{
			restrict: 'E',
			scope: false,
			controller: function($scope){
				$scope.showPopUpDialog = false;
				$scope.closeDialog = function(){
					$scope.showPopUpDialog = false;
				}
			}
		}

	});