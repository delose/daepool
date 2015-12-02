'use strict';

angular.module('daepoolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('campaign', {
                parent: 'entity',
                url: '/campaigns',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'daepoolApp.campaign.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/campaign/campaigns.html',
                        controller: 'CampaignController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('campaign.detail', {
                parent: 'entity',
                url: '/campaign/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'daepoolApp.campaign.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/campaign/campaign-detail.html',
                        controller: 'CampaignDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('campaign');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Campaign', function($stateParams, Campaign) {
                        return Campaign.get({id : $stateParams.id});
                    }]
                }
            })
            .state('campaign.new', {
                parent: 'campaign',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/campaign/campaign-dialog.html',
                        controller: 'CampaignDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    type: null,
                                    starttime: null,
                                    endtime: null,
                                    status: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('campaign', null, { reload: true });
                    }, function() {
                        $state.go('campaign');
                    })
                }]
            })
            .state('campaign.edit', {
                parent: 'campaign',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/campaign/campaign-dialog.html',
                        controller: 'CampaignDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Campaign', function(Campaign) {
                                return Campaign.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('campaign', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('campaign.delete', {
                parent: 'campaign',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/campaign/campaign-delete-dialog.html',
                        controller: 'CampaignDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Campaign', function(Campaign) {
                                return Campaign.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('campaign', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
