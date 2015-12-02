'use strict';

angular.module('daepoolApp')
    .controller('CampaignController', function ($scope, $state, $modal, Campaign, CampaignSearch, ParseLinks) {
      
        $scope.campaigns = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Campaign.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.campaigns = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            CampaignSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.campaigns = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.campaign = {
                name: null,
                description: null,
                type: null,
                starttime: null,
                endtime: null,
                status: null,
                id: null
            };
        };
    });
