'use strict';

angular.module('daepoolApp').controller('TargetAudienceDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'TargetAudience', 'User',
        function($scope, $stateParams, $modalInstance, entity, TargetAudience, User) {

        $scope.targetAudience = entity;
        $scope.users = User.query();
        $scope.load = function(id) {
            TargetAudience.get({id : id}, function(result) {
                $scope.targetAudience = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('daepoolApp:targetAudienceUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.targetAudience.id != null) {
                TargetAudience.update($scope.targetAudience, onSaveSuccess, onSaveError);
            } else {
                TargetAudience.save($scope.targetAudience, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
