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
                    var address = document.getElementById("eventDetails:addressForEvent").value;
                    geocoder.geocode({'address': address}, function (results, status) {
                        if (status == google.maps.GeocoderStatus.OK) {
                            //var location = results[0].geometry.location;
                           
                            for (var i=0;i<results[0].address_components.length;i++){
                              if (results[0].address_components[i].types[0]==='locality'){
                                    var city= results[0].address_components[i].long_name;
                               }
                              if (results[0].address_components[i].types[0]==='country'){
                                    var country= results[0].address_components[i].short_name;
                               } 
                            }
                            

                            document.getElementById("eventDetails:addressForEvent").style.color='green';
                            gc ([{name:'City',value: city }, {name:'Country',value: country }]);//
                           
                        } else {
                            document.getElementById("eventDetails:addressForEvent").style.color="red";
                            //alert("Geocode was not successful for the following reason: " + status);
                        }
                    });
                    }
 
function getStyleRule(name) {
  for(var i=0; i<document.styleSheets.length; i++) {
    var ix, sheet = document.styleSheets[i];
    for (ix=0; ix<sheet.cssRules.length; ix++) {
        if (sheet.cssRules[ix].selectorText === name)
            return sheet.cssRules[ix].style;
    }
  }
return null;
}

function changeStyleRule(){
var rule = getStyleRule('.red');
rule.backgroundColor = "green";

}