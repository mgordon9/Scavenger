/**
 * Controller for Seek View. Includes a form for submitting a new request.
 */
app.controller('SeekViewController', function($scope, $http) {

    // Init Form.
    $scope.myForm = {};

    // Name
    $scope.myForm.name = '';

    // Item
    $scope.helpItems = {
        selectedItem: null,
        availableItems: [
            {name: 'drink', description: 'Desperately in need for a Drink.'},
            {name: 'fuel', description: 'Ran out of fuel.'},
            {name: 'grocery', description: 'Cannot make it to the grocery store.'},
            {name: 'photo', description: 'Need someone to take a photo.'},
            {name: 'player', description: 'Another player needed.'},
            {name: 'ride', description: 'Need a ride.'},
            {name: 'takeoutdog', description: 'Dog needs to go out.'},
            {name: 'tutoring', description: 'Need help for homework.'}
        ]
    }
    $scope.onHelpItemSelected = function(item) {
        $scope.helpItems.selectedItem = item;
    }

    // Description
    $scope.myForm.description = '';

    // Location
    $scope.myForm.latitude = '';
    $scope.myForm.longitude = '';
    // Update Minimap.
    var mapOptions = {
        zoom: 4,
        center: new google.maps.LatLng(40.0000, -98.0000),
    }

    // Do HTML5 geolocation
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
            $scope.myForm.latitude = position.coords.latitude;
            $scope.myForm.longitude = position.coords.longitude;

            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
        });
    }

    $scope.myForm.onDropPinBtnClicked = function($event) {
        console.log("DropPinBtn clicked.");

        $scope.minimap = new google.maps.Map(document.getElementById('minimap'), mapOptions);

        var pos = {
            lat: position.coords.latitude,
            lng: position.coords.longitude
        };

        $scope.minimap.setCenter(pos);
        $scope.minimap.setZoom(15);
    }


    // Submit
    $scope.myForm.onSubmitBtnClicked = function($event) {
        console.log("Submit btn clicked.");

        var requestbody = {
            keyname: $scope.myForm.name,
            latitude: $scope.myForm.latitude,
            longitude: $scope.myForm.longitude,
            activity: $scope.helpItems.selectedItem
        };

        $http.post('rest/ds', requestbody).
        then(function () {
            console.log("POST request successful. Data: " + requestbody);
        });
    };
});