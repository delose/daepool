'use strict';

angular.module('daepoolApp')
    .factory('Campaign', function ($resource, DateUtils) {
        return $resource('api/campaigns/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.starttime = DateUtils.convertLocaleDateFromServer(data.starttime);
                    data.endtime = DateUtils.convertLocaleDateFromServer(data.endtime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.starttime = DateUtils.convertLocaleDateToServer(data.starttime);
                    data.endtime = DateUtils.convertLocaleDateToServer(data.endtime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.starttime = DateUtils.convertLocaleDateToServer(data.starttime);
                    data.endtime = DateUtils.convertLocaleDateToServer(data.endtime);
                    return angular.toJson(data);
                }
            }
        });
    });
