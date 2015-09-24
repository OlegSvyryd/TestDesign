/**
 * Created by Rostyslav.Viner on 20.08.2015.
 */
angular.module('DPController', ['ui.bootstrap', 'ngLocale', 'DPValues'])
    .controller('DPController', ['$scope', '$locale', 'DPValues', '$translate', function($scope, $locale, DPValues, $translate) {

        //Translate
        var setLanguage = function () {
            var language;

            if ($translate.proposedLanguage() != undefined) {
                language = $translate.proposedLanguage();
            } else {
                language = $translate.use();
            }

            angular.copy(DPValues.locales[language], $locale);
        }

        setLanguage();

        $scope.clear = function () {
            $scope.dt = null;
        };

        // Disable weekend selection
        $scope.disabled = function(date, mode) {
            return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
        };

        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $scope.opened = true;
        };

        $scope.dateOptions = {
            formatYear: 'yy',
            startingDay: 1,
            showWeeks: false,
        };

        var tomorrow = new Date();
        tomorrow.setDate(tomorrow.getDate() + 1);
        var afterTomorrow = new Date();
        afterTomorrow.setDate(tomorrow.getDate() + 2);
        $scope.events = [
            {date: tomorrow, status: 'full'},
            {date: afterTomorrow, status: 'partially'}
        ];

        $scope.getDayClass = function(date, mode) {
            if (mode === 'day') {
                var dayToCheck = new Date(date).setHours(0,0,0,0);

                for (var i=0;i<$scope.events.length;i++){
                    var currentDay = new Date($scope.events[i].date).setHours(0,0,0,0);

                    if (dayToCheck === currentDay) {
                        return $scope.events[i].status;
                    }
                }
            }

            return '';
        };

    }]);