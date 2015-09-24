/**
 * Created by oleg on 31.08.2015.
 */
animalApp
    .controller('TranslateController',
        function TranslateController($scope, $translate, $window) {
            $scope.changeLanguage = function (langKey) {
                $window.localStorage.setItem('NG_TRANSLATE_LANG_KEY', langKey);
                $translate.use(langKey);
                location.reload();
            };
        }
    );
