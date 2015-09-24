var animalAppControllers = angular.module('ContactsController', ['vcRecaptcha','ContactsValues']);

animalApp.controller('ContactsController', function ($http, $scope, vcRecaptchaService, ContactsValues) {

    var myLatlng = new google.maps.LatLng(ContactsValues.googlemap.latitude, ContactsValues.googlemap.longitude),
        mapOptions = {
            zoom: ContactsValues.googlemap.zoom,
            center: myLatlng
        },
        map = new google.maps.Map(document.getElementById('map'), mapOptions),
        marker = new google.maps.Marker({
            position: myLatlng,
            draggable: true,
            map: map,
            animation: google.maps.Animation.DROP,
            title: ContactsValues.googlemap.markerhint
        });

    google.maps.event.addListener(marker, 'click', toggleBounce);

    function toggleBounce() {

        if (marker.getAnimation() !== null) {
            marker.setAnimation(null);
        } else {
            marker.setAnimation(google.maps.Animation.BOUNCE);
        }
    }


        //initialize loading spinner
        var target = document.getElementById('loading-block')
        new Spinner(ContactsValues.spinneropts).spin(target);

        $scope.spinnerloading = 0;
        $scope.startupmessage = 1;
        $scope.okmessage = 0;
        $scope.errormessage = 0;

    $scope.response = null;
    $scope.widgetId = null;
    $scope.model = ContactsValues.recaptcha;

    $scope.setResponse = function (response) {
        $scope.response = response;
    };
    $scope.setWidgetId = function (widgetId) {
        $scope.widgetId = widgetId;
    };
    $scope.cbExpiration = function () {
        $scope.response = null;
    };
    $scope.submit = function () {
       if (vcRecaptchaService.getResponse() === "") { //if answer from Google is empty
           $scope.spinnerloading = 0;
           $scope.startupmessage = 1;
           $scope.okmessage = 0;
           $scope.errormessage = 0;

        } else {
           $scope.spinnerloading = 1;
           $scope.startupmessage = 0;
           $scope.okmessage = 0;
           $scope.errormessage = 0;

           $scope.feedback.gRecaptchaResponse = $scope.response ;                //g-captcah-reponse for server
            console.log($scope.feedback);
            /* MAKE AJAX REQUEST to our server with g-captcha-string */

            $http.post('/webapi/contacts/mail', $scope.feedback).success(function (response) {
				console.log(response);
                if (response.success === true) {
                    $scope.spinnerloading = 0;
                    $scope.startupmessage = 0;
                    $scope.okmessage = 1;
                    $scope.errormessage = 0;

                    $scope.feedback.signup='';
                    $scope.feedback.email='';
                    $scope.feedback.text='';
                    vcRecaptchaService.reload($scope.widgetId);

                } else {
                    $scope.spinnerloading = 0;
                    $scope.startupmessage = 0;
                    $scope.okmessage = 0;
                    $scope.errormessage = 1;

                    vcRecaptchaService.reload($scope.widgetId);
                }
            })
                        .error(function (error) {
                    $scope.spinnerloading = 0;
                    $scope.startupmessage = 0;
                    $scope.okmessage = 0;
                    $scope.errormessage = 1;

                        // In case of a failed validation you need to reload the captcha
                        // because each response can be checked just once
                    vcRecaptchaService.reload($scope.widgetId);

                }
                    );
        }
    }
}
)