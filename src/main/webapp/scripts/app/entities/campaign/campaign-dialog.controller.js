'use strict';

angular.module('daepoolApp').controller('CampaignDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Campaign', 'User',
        function($scope, $stateParams, $modalInstance, entity, Campaign, User) {

        $scope.campaign = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            Campaign.get({id : id}, function(result) {
                $scope.campaign = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('daepoolApp:campaignUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.campaign.id != null) {
                Campaign.update($scope.campaign, onSaveSuccess, onSaveError);
            } else {
                Campaign.save($scope.campaign, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
