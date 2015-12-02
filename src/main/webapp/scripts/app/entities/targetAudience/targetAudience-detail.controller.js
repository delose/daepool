'use strict';

angular.module('daepoolApp')
    .controller('TargetAudienceDetailController', function ($scope, $rootScope, $stateParams, entity, TargetAudience, User) {
        $scope.targetAudience = entity;
        $scope.load = function (id) {
            TargetAudience.get({id: id}, function(result) {
                $scope.targetAudience = result;
            });
        };
        var unsubscribe = $rootScope.$on('daepoolApp:targetAudienceUpdate', function(event, result) {
            $scope.targetAudience = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
