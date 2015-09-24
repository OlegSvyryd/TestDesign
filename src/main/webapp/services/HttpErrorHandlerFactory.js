/**
 * Created by oleg on 17.09.2015.
 */
httpErrorHandlerModule
    .factory('HttpErrorHandlerFactory',
        function HttpErrorHandlerFactory($filter) {

            //Create instance of current factory
            var factory = {};

            factory.returnError = function(status){
                if(status === 400)
                    return $filter('translate')('HTTP_400');
                else if(status === 401)
                    return $filter('translate')('HTTP_401');
                else if(status === 404)
                    return $filter('translate')('HTTP_404');
                else if(status === 500)
                    return $filter('translate')('HTTP_500');
            };

            //Inject dependencies
            HttpErrorHandlerFactory.$inject = ['$filter'];

            return factory;
        }
    );
