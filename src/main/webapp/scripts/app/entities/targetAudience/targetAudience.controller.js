'use strict';

angular.module('daepoolApp')
    .controller('TargetAudienceController', function ($scope, $state, $modal, TargetAudience, TargetAudienceSearch, ParseLinks) {
      
        $scope.targetAudiences = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            TargetAudience.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.targetAudiences.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.targetAudiences = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            TargetAudienceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.targetAudiences = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.targetAudience = {
                name: null,
                description: null,
                size: null,
                createtime: null,
                refreshtime: null,
                criteria: null,
                id: null
            };
        };
    });
