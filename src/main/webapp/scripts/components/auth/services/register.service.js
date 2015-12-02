'use strict';

angular.module('daepoolApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


