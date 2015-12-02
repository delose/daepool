'use strict';

describe('TargetAudience Detail Controller', function() {
    var $scope, $rootScope;
    var MockEntity, MockTargetAudience, MockUser;
    var createController;

    beforeEach(inject(function($injector) {
        $rootScope = $injector.get('$rootScope');
        $scope = $rootScope.$new();
        MockEntity = jasmine.createSpy('MockEntity');
        MockTargetAudience = jasmine.createSpy('MockTargetAudience');
        MockUser = jasmine.createSpy('MockUser');
        

        var locals = {
            '$scope': $scope,
            '$rootScope': $rootScope,
            'entity': MockEntity ,
            'TargetAudience': MockTargetAudience,
            'User': MockUser
        };
        createController = function() {
            $injector.get('$controller')("TargetAudienceDetailController", locals);
        };
    }));


    describe('Root Scope Listening', function() {
        it('Unregisters root scope listener upon scope destruction', function() {
            var eventType = 'daepoolApp:targetAudienceUpdate';

            createController();
            expect($rootScope.$$listenerCount[eventType]).toEqual(1);

            $scope.$destroy();
            expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
        });
    });
});
