'use strict';

angular.module('daepoolApp')
    .factory('TargetAudience', function ($resource, DateUtils) {
        return $resource('api/targetAudiences/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createtime = DateUtils.convertLocaleDateFromServer(data.createtime);
                    data.refreshtime = DateUtils.convertLocaleDateFromServer(data.refreshtime);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createtime = DateUtils.convertLocaleDateToServer(data.createtime);
                    data.refreshtime = DateUtils.convertLocaleDateToServer(data.refreshtime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createtime = DateUtils.convertLocaleDateToServer(data.createtime);
                    data.refreshtime = DateUtils.convertLocaleDateToServer(data.refreshtime);
                    return angular.toJson(data);
                }
            }
        });
    });
