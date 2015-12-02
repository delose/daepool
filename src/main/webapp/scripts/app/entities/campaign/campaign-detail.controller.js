'use strict';

angular.module('daepoolApp')
    .controller('CampaignDetailController', function ($scope, $rootScope, $stateParams, entity, Campaign, User) {
        $scope.campaign = entity;
        $scope.load = function (id) {
            Campaign.get({id: id}, function(result) {
                $scope.campaign = result;
            });
        };
        var unsubscribe = $rootScope.$on('daepoolApp:campaignUpdate', function(event, result) {
            $scope.campaign = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
