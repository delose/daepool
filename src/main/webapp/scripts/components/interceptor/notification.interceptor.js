 'use strict';

angular.module('daepoolApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-daepoolApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-daepoolApp-params')});
                }
                return response;
            }
        };
    });
