/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

 var geocoder;
  var map;
  var marker;

function codeAddress() {
                    
                    geocoder = new google.maps.Geocoder();
                    var address = document.getElementById("home:eventDetails:eventLocation:addressForEvent").value;
                    geocoder.geocode({'address': address}, function (results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            var location = results[0].geometry.location;
                            gc ([{name:'Lat',value: location.lat() }, {name:'Long',value: location.lng() }]);//
                           
                        } else {
                            alert("Geocode was not successful for the following reason: " + status);
                        }
                    });
                    }
