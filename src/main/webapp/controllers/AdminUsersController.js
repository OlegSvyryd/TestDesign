adminUsersModule
    .controller('AdminUsersController',
    function AdminUsersController($scope, AdminUsersFactory, AdminUsersValues) {

        //initialize loading spinner
        var targetContent = document.getElementById('loading-block');
        new Spinner(opts).spin(targetContent);

        $scope.contentLoading = 0;
        $scope.errorMessage = '';

        //Pages
        $scope.filter = AdminUsersValues.filter;            //filter
        $scope.totalItems = AdminUsersValues.totalItems;    //table rows count
        $scope.users = AdminUsersValues.users;          //animal instance

        /**
         * @return count of rows for pagination.
         */
        $scope.contentLoading++;
        AdminUsersFactory.getAmountRecords()
            .then(
            function(result){},

            //fail
            function(error){
                $scope.totalItems.count = 0;
                $scope.errorMessage = error;
            }
        )
            .finally(function() {
                $scope.contentLoading--;
            });

        $scope.contentLoading++;
        AdminUsersFactory.getListOfAdminUsers()
            .then(
            function(result){},

            //fail
            function(error){
                $scope.errorMessage = error;
            }
        )
            .finally(function() {
                $scope.contentLoading--;
            });

        /**
         * @return next page.
         */
        $scope.pageChanged = function() {
            $scope.contentLoading++;

            //scroll to top of the page
            jQuery('html, body').animate({ scrollTop: 0 }, 500);

            AdminUsersFactory.getListOfAdminUsers()
                .finally(function() {
                    $scope.contentLoading--;
            });
        };

        /**
         * @return list of users with given count of rows.
         */
        $scope.countChanged = function(count) {
            $scope.filter.limit = count;
            AdminUsersFactory.getListOfAdminUsers();
        };

        //Dependency injection
        AdminUsersController.$inject = ['$scope', 'AdminUsersFactory', 'AdminUsersValues'];

    }).controller('AdminUsersFilter',
    function AdminUsersFilter($scope, AdminUsersFactory, AdminUsersValues, $window) {

        //locale
        $scope.currentLanguage = $window.localStorage.getItem('NG_TRANSLATE_LANG_KEY');

        $scope.filter = AdminUsersValues.filter;    //filter

        /**
         * reset filter values.
         */
        $scope.reset = function() {
            $scope.filter.user.isActive = undefined;

            $scope.doFilter();
            jQuery('html, body').animate({ scrollTop: 0 }, 500);
        };

        /**
         * @return list of users according to filter values.
         */
        $scope.doFilter = function() {
            $scope.$parent.contentLoading++;

            AdminUsersFactory.getAmountRecords()
                .then(
                function(result){},

                //fail
                function(error){
                    $scope.totalItems.count = 0;
                    $scope.$parent.errorMessage = error.toString();
                });

            AdminUsersFactory.getListOfAdminUsers().finally(
                function(){
                    $scope.$parent.contentLoading--;
                }
            );
            console.log($scope.totalItems.count);

            jQuery('html, body').animate({ scrollTop: 0 }, 500);
        };

        //Dependency injection
        AdminUsersFilter.$inject = ['$scope', 'AdminUsersFactory', 'AdminUsersValues', '$window'];
    }
);