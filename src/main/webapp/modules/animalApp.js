var animalApp = angular.module('animalApp', [
    'ngRoute',
    'ngResource',
    'AdoptionModule',
    'AnimalFoundModule',
    'AnimalLostModule',
    'AnimalRegistrationModule',
    'LoginController',
    'ContactsController',
    'StarterPageController',
    'AnimalsAdminController',
    'AnimalsDetailedAdminController',
    'AnimalsEditorAdminController',
    'AnimalsDoctorController',
    'AnimalsDetailedDoctorController',
    'AnimalMedicalHistoryController',
    'AnimalMedicalHistoryEditorController',
    'AnimalMedicalHistoryDetailedController',
    'RegistrationController',
    'UserProfileController',
    'MainController',
    'AnimalShortInfoController',
    'UserConfirmRegistrationController',
    'LocalStorageModule',
    'pascalprecht.translate',
    'ngCookies',
    'AdminUsersModule',
    'HttpErrorHandlerModule',
    'ui.bootstrap'
]);

animalApp .config(['$routeProvider', '$translateProvider',
    function($routeProvider, $translateProvider) {
        $routeProvider
            .when('/ua', {
                templateUrl: 'views/main_view.html',
                controller: 'StarterPageController'
            })
            .when('/ua/animal/adoption', {
                templateUrl: 'views/animal_adoption.html',
                controller: 'AnimalAdoptionController'
            })
            .when('/ua/animal/found', {
                templateUrl: 'views/animal_found.html',
                controller: 'AnimalFoundController'
            })
            .when('/ua/animal/lost', {
                templateUrl: 'views/animal_lost.html',
                controller: 'AnimalLostController'
            })
            .when('/ua/animal/homeless', {
                templateUrl: 'views/reg_homeless.html',
                controller: 'HomelessController'
            })
            .when('/ua/animal/detail', {
                templateUrl: 'views/admin_animals_detailed.html',
                controller: 'AnimalsDetailController'
            })
            .when('/ua/contacts', {
                templateUrl: 'views/contacts.html',
                controller: 'ContactsController'
            })
            .when('/ua/user/login', {
                templateUrl: 'views/login.html',
                controller: 'LoginController'
            })
            .when('/ua/user/home/animals', {
                templateUrl: 'views/admin_animals.html',
                controller: 'AnimalsAdminController'
            })
            .when('/ua/user/home/animals/:animalId', {
                templateUrl: 'views/admin_animals_detailed.html',
                controller: 'AnimalsDetailedAdminController'
            })
            .when('/ua/user/home/animals/editor/:animalId', {
                templateUrl: 'views/admin_animals_editor.html',
                controller: 'AnimalsEditorAdminController'
            })
            .when('/ua/user/doctor/animals', {
                templateUrl: 'views/doctor_animals.html',
                controller: 'AnimalsDoctorController'
            })
            .when('/ua/user/doctor/animals/:animalId', {
                templateUrl: 'views/doctor_animals_detailed.html',
                controller: 'AnimalsDetailedDoctorController'
            })
            .when('/ua/user/doctor/animals/medical_history/:animalId', {
                templateUrl: 'views/animal_medical_history.html',
                controller: 'AnimalMedicalHistoryController'
            })
            .when('/ua/user/doctor/animals/medical_history/:animalId/editor', {
                templateUrl: 'views/animal_medical_history_editor.html',
                controller: 'AnimalMedicalHistoryEditorController'
            })
            .when('/ua/user/doctor/animals/medical_history/:animalId/detailed/:itemId', {
                templateUrl: 'views/animal_medical_history_detailed.html',
                controller: 'AnimalMedicalHistoryDetailedController'
            })
            .when('/ua/user/registration', {
                templateUrl: 'views/registration.html',
                controller: 'RegistrationController'
            })
            .when('/ua/user/confirmRegistration', {
                templateUrl: 'views/confirm_registration.html',
                controller: 'UserConfirmRegistrationController'
            })
            .when('/ua/user/profile', {
                templateUrl: 'views/user_profile.html',
                controller: 'UserProfileController'
            })
            .when('/ua/user/profile/animals/:animalId', {
                templateUrl: 'views/user_animals_detailed.html',
                controller: 'AnimalsDetailedUserController'
            })
            .when('/ua/user/profile/animals/editor/:animalId', {
                templateUrl: 'views/user_animals_editor.html',
                controller: 'AnimalsEditorUserController'
            })
            .when('/ua/animal/:service/:animalId', {
                templateUrl: 'views/animal_short_info.html',
                controller: 'AnimalShortInfoController'
            })
            .when('/ua/animal/registration_homeless', {
                templateUrl: 'views/reg_homeless.html',
                controller: 'AnimalHomelessRegController'
            })
	        .when('/ua/animal/registration_owned', {
        	    templateUrl: 'views/reg_owned.html',
        	    controller: 'AnimalOwnedRegController'
      	    })
            .when('/ua/user/admin/users', {
                templateUrl: 'views/admin_users.html',
                controller: 'AdminUsersController'
            })
            .otherwise({
                redirectTo: '/ua'
            });

        //secure lang
        $translateProvider.useSanitizeValueStrategy('escapeParameters');

        //load json files with lang
        $translateProvider.useStaticFilesLoader({
            prefix: 'resources/json/lang-',
            suffix: '.json'
        });

        /**
         * This method define a user locale language
         */
        $translateProvider.determinePreferredLanguage(function () {
            /**
             * @const Set default value of language
             */
            DEFAULT_VALUE = 'en';

            /**
             * @const Get a user locale language
             */
            PREFERRED_LANGUAGE = navigator.language || navigator.userLanguage ||
            navigator.browserLanguage || navigator.systemLanguage || DEFAULT_VALUE;

            //check if we have user locale
            if(PREFERRED_LANGUAGE !== 'en' && PREFERRED_LANGUAGE !== 'uk')
                return DEFAULT_VALUE;

            return PREFERRED_LANGUAGE;
        });

        //work in async
        $translateProvider.forceAsyncReload(true);

        //save lang in storage
        $translateProvider.useLocalStorage();
    }]);

//Constants
animalApp.constant('RESOURCES', {
    ANIMALS_FOR_ADOPTING_PAGINATOR: 'webapi/animals/adoption/pagenator',
    ANIMALS_FOR_ADOPTING: 'webapi/animals/adoption',
    ANIMALS_FOR_ADOPTING_STATUSES: 'webapi/animals/adoption/',
    ANIMALS_FOUND_PAGINATOR: 'webapi/animals/found/pagenator',
    ANIMALS_FOUND: 'webapi/animals/found',
    ANIMALS_LOST_PAGINATOR: 'webapi/animals/lost/pagenator',
    ANIMALS_LOST: 'webapi/animals/lost',
    ANIMAL_TYPES: '/webapi/animals/animal_types',
    ANIMAL_BREEDS: '/webapi/animals/animal_breeds/',
    ANIMAL_SERVICES: '/webapi/animals/animal_services',
    ANIMAL_REGISTRATION: 'webapi/animals/animal',
    ANIMAL_REGISTRATION_IMAGE: 'webapi/animals/animal/image',
    ANIMALS_FOR_ADMIN: '/webapi/admin/animals',
    ANIMALS_FOR_ADMIN_PAGINATOR: '/webapi/admin/animals/paginator',
    ANIMALS_FOR_DOCTOR: '/webapi/doctor/animals',
    ANIMALS_FOR_DOCTOR_PAGINATOR: '/webapi/doctor/animals/paginator',
    ANIMAL_FOR_ADMIN: '/webapi/admin/animals/',
    ANIMAL_FOR_ADMIN_DELETE: '/webapi/admin/animals/',
    ANIMAL_FOR_ADMIN_UPDATE: '/webapi/admin/animals/editor',
    MEDICAL_HISTORY_ITEMS_FOR_DOCTOR: '/webapi/doctor/medical_history/items',
    MEDICAL_HISTORY_ITEMS_PAGINATOR_FOR_DOCTOR: '/webapi/doctor/medical_history/paginator/',
    MEDICAL_HISTORY_ITEM_FOR_DOCTOR_DELETE: '/webapi/doctor/medical_history/item/',
    MEDICAL_HISTORY_ITEM_FOR_DOCTOR_UPDATE: '/webapi/doctor/medical_history/item',
    MEDICAL_HISTORY_TYPES: '/webapi/animals/medical_history/types',
    ANIMAL_IMAGE_DELETE_FOR_ADMIN: '/webapi/admin/animals/image/',
    ADMIN_USERS_PAGINATOR: 'webapi/admin/users/paginator',
    ADMIN_USERS: 'webapi/admin/users',
    LOGIN_GOOGLE: '/webapi/account/login/google',
    LOGIN_GOOGLE_DIRECT: '/webapi/account/login/google_login_direct', 
    LOGIN_FACEBOOK: '/webapi/account/login/facebook',
    LOGIN_TWITTER: '/webapi/account/login/twitter',
    LOGIN_TWITTER_DIRECT: '/webapi/account/login/twitter_login_direct',   
    LOGIN_BASIC: '/webapi/account/login/',
    LOGOUT: '/webapi/account/logout',
    REGISTRATION: '/webapi/account/registration/',
    RESTORE_PASSWORD: '/webapi/account/restore_password/',
    CONFIRM_REGISTRATION: '/webapi/account/confirmRegistration/',
    REFRESH: '/webapi/account/refresh',
    USER_FOR_USER: '/webapi/users/user/',
    ANIMALS_FOR_USER: '/webapi/users/user/',
    PAGINATOR_FOR_USER: '/webapi/users/user/',
    ANIMALS_FOR_USER_WITH_FILTER: '/webapi/users/user/'
});

animalApp.config(function(localStorageServiceProvider){
	  localStorageServiceProvider.setPrefix('AnimalWebApp');	  
	});