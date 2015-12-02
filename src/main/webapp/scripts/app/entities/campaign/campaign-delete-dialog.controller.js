'use strict';

angular.module('daepoolApp')
	.controller('CampaignDeleteController', function($scope, $modalInstance, entity, Campaign) {

        $scope.campaign = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Campaign.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });