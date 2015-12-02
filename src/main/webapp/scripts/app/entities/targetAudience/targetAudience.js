'use strict';

angular.module('daepoolApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('targetAudience', {
                parent: 'entity',
                url: '/targetAudiences',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'daepoolApp.targetAudience.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/targetAudience/targetAudiences.html',
                        controller: 'TargetAudienceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('targetAudience');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('targetAudience.detail', {
                parent: 'entity',
                url: '/targetAudience/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'daepoolApp.targetAudience.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/targetAudience/targetAudience-detail.html',
                        controller: 'TargetAudienceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('targetAudience');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TargetAudience', function($stateParams, TargetAudience) {
                        return TargetAudience.get({id : $stateParams.id});
                    }]
                }
            })
            .state('targetAudience.new', {
                parent: 'targetAudience',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/targetAudience/targetAudience-dialog.html',
                        controller: 'TargetAudienceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    size: null,
                                    createtime: null,
                                    refreshtime: null,
                                    criteria: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('targetAudience', null, { reload: true });
                    }, function() {
                        $state.go('targetAudience');
                    })
                }]
            })
            .state('targetAudience.edit', {
                parent: 'targetAudience',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/targetAudience/targetAudience-dialog.html',
                        controller: 'TargetAudienceDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['TargetAudience', function(TargetAudience) {
                                return TargetAudience.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('targetAudience', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('targetAudience.delete', {
                parent: 'targetAudience',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/targetAudience/targetAudience-delete-dialog.html',
                        controller: 'TargetAudienceDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['TargetAudience', function(TargetAudience) {
                                return TargetAudience.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('targetAudience', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
