'use strict';

angular.module('daepoolApp')
    .factory('TargetAudienceSearch', function ($resource) {
        return $resource('api/_search/targetAudiences/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
