'use strict';

angular.module('daepoolApp')
    .factory('CampaignSearch', function ($resource) {
        return $resource('api/_search/campaigns/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
