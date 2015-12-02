'use strict';

angular.module('daepoolApp')
	.controller('TargetAudienceDeleteController', function($scope, $modalInstance, entity, TargetAudience) {

        $scope.targetAudience = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            TargetAudience.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });