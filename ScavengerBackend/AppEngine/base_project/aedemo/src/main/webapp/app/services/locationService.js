
// Provides a Service for requesting the current location.
var LocationService = angular.module('LocationService', [])

LocationService.factory('LocationDataOp', ['$http', function ($http) {

    var LocationDataOp = {};

    LocationDataOp.getLocations = function() {
        return $http.get('rest/ds');
    };

    LocationDataOp.addLocation = function(location) {
        return $http.post('rest/ds', location);
    };
    return LocationDataOp;
}]);
