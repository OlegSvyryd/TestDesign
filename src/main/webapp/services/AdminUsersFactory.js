adminUsersModule
    .factory('AdminUsersFactory',
    function AdminUsersFactory($http, $q, RESOURCES, AdminUsersValues, localStorageService){

        //Create instance of current factory
        var factory = {};

        //Get amount users
        factory.getAmountRecords = function() {
            var def = $q.defer();
            
            $http.defaults.headers.common['AccessToken'] = localStorageService.get("accessToken");

            $http.post(RESOURCES.ADMIN_USERS_PAGINATOR, AdminUsersValues.filter)
                .success(function (data) {
                    AdminUsersValues.totalItems.count = data.rowsCount;
                    def.resolve(data);
                })
                .error(function (error) {
                    AdminUsersValues.totalItems.count = 0;
                    def.reject("Failed to get users");
                });

            return def.promise;
        };

        //Get list of users
        factory.getListOfAdminUsers = function(){
            var def = $q.defer();
            
            $http.post(RESOURCES.ADMIN_USERS, AdminUsersValues.filter)
                .success(function (data) {
                    AdminUsersValues.users.values = data;
                    def.resolve(data);
                })
                .error(function () {
                    def.reject("Failed to get users");
                });

            return def.promise;
        };

        //Inject dependencies
        AdminUsersFactory.$inject = ['$q', 'RESOURCES', 'AdminUsersValues'];

        return factory;
    }
);