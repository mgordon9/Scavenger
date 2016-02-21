/**
 * Controller for Map View. Handles issuing and parsing results of
 * a REST request; fills a map with markers then.
 */
app.controller('MapViewController', function($scope, $http) {

    // Variables
    var mapOptions = {
        zoom: 4,
        center: new google.maps.LatLng(40.0000, -98.0000),
    }
    $scope.map = new google.maps.Map(document.getElementById('map'), mapOptions);
    $scope.markers = [];

    var infoWindow = new google.maps.InfoWindow();

    // Helper function for parsing markers
    var createMarker = function(info) {
        var image = {
            url: 'img/' + info.activity + '.png',
            size: new google.maps.Size(24, 24),
            origin: new google.maps.Point(0, 0),
            anchor: new google.maps.Point(12, 12)
        };

        var marker = new google.maps.Marker({
            map: $scope.map,
            icon: image,
            position: new google.maps.LatLng(info.latitude, info.longitude),
            title: info.keyname
        });
        marker.content = '<div class="infoWindowContent">' + "Date: " + info.date + '</div>';

        google.maps.event.addListener(marker, 'click', function(){
            infoWindow.setContent('<h2>' + marker.title + '</h2>' + marker.content);
            infoWindow.open($scope.map, marker);
        });

        $scope.markers.push(marker);
    }

    // Do HTML5 geolocation
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
            var pos = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };

            $scope.map.setCenter(pos);
            $scope.map.setZoom(15);

            // Set Marker for current location
            var currentLocationMarker = {
                keyname: "currentlocation",
                date: "rightnow",
                latitude: position.coords.latitude,
                longitude: position.coords.longitude,
                activity: "currentlocation"
            };
            createMarker(currentLocationMarker);


        }, function() {
            handleLocationError(true, infoWindow, map.getCenter());
        });
    } else {
        handleLocationError(false, infoWindow, map.getCenter());
    }
    var handleLocationError = function(browserHasGeolocation, infoWindow, pos) {
        infoWindow.setPosition(pos);
        infoWindow.setContent(browserHasGeolocation ?
            'Error: The Geolocation service failed.' :
            'Error: Your browser doesn\'t support geolocation.');
    };

    // Update Markers only when map drag is finished.
    google.maps.event.addListenerOnce($scope.map, 'bounds_changed', function() {
        updateMarkers();
        google.maps.event.addListener($scope.map, "idle", updateMarkers);
    });

    var updateMarkers = function() {
        var bounds = $scope.map.getBounds();
        console.log("NE: " + bounds.getNorthEast() + ", SW: " + bounds.getSouthWest());

        // Build&Execute REST request and initiate parsing of markers
        var requri = 'rest/ds?'
                        + 'latitude1=' + bounds.getNorthEast().lat()
                        + '&longitude1=' + bounds.getNorthEast().lng()
                        + '&latitude2=' + bounds.getSouthWest().lat()
                        + '&longitude2=' + bounds.getSouthWest().lng();

        $http.get(requri).
        then(function(response) {
            console.log("Parsing request with "
                + "status:" + response.status
                + ", statusText:" + response.statusText
                + ", response.data.length:" + response.data.length);

            for (i = 0; i < response.data.length; i++){
                createMarker(response.data[i]);
            }
        });
    };

    // Open custom tooltip on click
    $scope.openInfoWindow = function(e, selectedMarker){
        e.preventDefault();
        google.maps.event.trigger(selectedMarker, 'click');
    }

});