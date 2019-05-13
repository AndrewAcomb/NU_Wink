const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


const maxmeterdist = 100;
const maxdegbearing = 60;

exports.recognizeBuilding = functions.https.onCall((data, context) => {
    
    const userlat = data.userlat;
    const userlon = data.userlon;
    const userheading = data.userheading;

    var best_cost = Number.MAX_VALUE;
    var best_index = 0;

   var best_bearingdiff;

    var ref = admin.database().ref('Buildings');

      return ref.once('value').then(function(snapshot) {

        snapshot.forEach(function(childSnapshot) {
            
            var templat = childSnapshot.child('lat').val();
            var templon = childSnapshot.child('lon').val();
            var tempindex = childSnapshot.key;

                var temp_bearingdiff = calcBearingDifference(userheading, radToDegree(calcRadBearing(userlat, userlon, templat, templon)));

                //if (temp_bearingdiff < 60) {

                    var temp_dist = calcMeterDistance(userlat, userlon, templat, templon);

                    if (temp_dist < 100) {

                        var temp_cost = findCost(temp_dist, temp_bearingdiff);
                        //console.info(temp_cost);

                        if (temp_cost < best_cost) {
                            best_bearingdiff = temp_bearingdiff;
                            best_index = tempindex;
                            best_cost = temp_cost;
                        }
                    }
                //}
        }); 

        console.log("User Location: " + userlat + ", "+ userlon)
        console.log("Heading Difference" + best_bearingdiff)
        console.log("Best Cost: " + best_cost)
        console.log("Best Index: " + best_index)
        //var ret = snapshot.child(''+best_index).val();
        var ret = snapshot.child(''+ Math.floor(Math.random()*89)).val();
        console.log(ret);
        return ret;

      });



    /* var ref = admin.database().ref("users");
    return ref.once("value")
    .then(function(snapshot) {

        var message = snapshot.child("1").child("username").val(); \
        
        console.log(message)
        console.log(t)
        console.log('step1');
        return{
            foo: "bar"
        };
        

    }); */


  });


  testFunction = function (test) {
    
    var ref = admin.database().ref("users");
    ref.once("value")
    .then(function(snapshot) {

        var message = snapshot.child("1").child("username").val(); // "Lovelace"
        
        snapshot.child

        var foo = "not";
        console.log(foo)

        foo += message;
        console.log(foo)

        test = foo;
        console.log(test)

        admin.database().ref('users/5').set({
            username: test
          });

    });

    /* main = function (args) {
        var temp_dist;
        var temp_bearingdiff;
        var temp_coords;
        var temp_cost;
        var best_index = 0;
        var best_cost = 999999999.0;
        var data = Main.getRandomCoords();
        for (var i = 0; i < data.length; i++) {
            {
                temp_coords = data[i];
                console.info(temp_coords);
                temp_bearingdiff = Main.calcBearingDifference(Main.userheading, Main.radToDegree(Main.calcRadBearing(Main.userlat, Main.userlon, temp_coords[0], temp_coords[1])));
                if (temp_bearingdiff < Main.maxdegbearing) {
                    temp_dist = Main.calcMeterDistance(Main.userlat, Main.userlon, temp_coords[0], temp_coords[1]);
                    if (temp_dist < Main.maxmeterdist) {
                        temp_cost = Main.findCost(temp_dist, temp_bearingdiff);
                        console.info(temp_cost);
                        if (temp_cost < best_cost) {
                            best_index = i;
                            best_cost = temp_cost;
                        }
                    }
                }
            }
            ;
        }
        console.info(best_index);
        console.info(best_cost);
    }; */
    

    /* const users = admin.database().ref().child('users');

    console.log(users);

    const query = users.child('1/username').val();

    console.log(query);

    
    test += query;
    return test; */
  }

  buildingTest =  function() {
      var ref = admin.database().ref('Buildings');

      ref.once('value', function(snapshot) {
        snapshot.forEach(function(childSnapshot) {
          var childKey = childSnapshot.key;

          var values = childSnapshot.child('lat').val();
          console.log(values);

        });
      });
  }

 

findCost = function (dist, bearing) {

    return Math.pow(dist / maxmeterdist, 2) + Math.pow(bearing / maxdegbearing, 2);

};

calcRadBearing = function (lat1, lon1, lat2, lon2) {
    var y = Math.sin(lon2 - lon1) * Math.cos(lat2);
    var x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);

    var radbearing = Math.atan2(y, x);
    if (radbearing < 0) {
        radbearing = 2 * Math.PI - radbearing;
    }

    return radbearing;
};

calcMeterDistance = function (lat1, lon1, lat2, lon2) {

var a = 6378137;
var b = 6356752.314245;
var f = 1 / 298.257223563;
var L = (function (x) { return x * Math.PI / 180; })(lon2 - lon1);
var U1 = Math.atan((1 - f) * Math.tan(/* toRadians */ (function (x) { return x * Math.PI / 180; })(lat1)));
var U2 = Math.atan((1 - f) * Math.tan(/* toRadians */ (function (x) { return x * Math.PI / 180; })(lat2)));
var sinU1 = Math.sin(U1);
var cosU1 = Math.cos(U1);
var sinU2 = Math.sin(U2);
var cosU2 = Math.cos(U2);
var sinLambda;
var cosLambda;
var sinSigma;
var cosSigma;
var sigma;
var sinAlpha;
var cosSqAlpha;
var cos2SigmaM;
var lambda = L;
var lambdaP;
var iterLimit = 100;

do {
    {
        sinLambda = Math.sin(lambda);
        cosLambda = Math.cos(lambda);
        sinSigma = Math.sqrt((cosU2 * sinLambda) * (cosU2 * sinLambda) + (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda) * (cosU1 * sinU2 - sinU1 * cosU2 * cosLambda));
        if (sinSigma === 0)
            return 0;
        cosSigma = sinU1 * sinU2 + cosU1 * cosU2 * cosLambda;
        sigma = Math.atan2(sinSigma, cosSigma);
        sinAlpha = cosU1 * cosU2 * sinLambda / sinSigma;
        cosSqAlpha = 1 - sinAlpha * sinAlpha;
        cos2SigmaM = cosSigma - 2 * sinU1 * sinU2 / cosSqAlpha;
        if (isNaN(cos2SigmaM))
            cos2SigmaM = 0;
        var C = f / 16 * cosSqAlpha * (4 + f * (4 - 3 * cosSqAlpha));
        lambdaP = lambda;
        lambda = L + (1 - C) * f * sinAlpha * (sigma + C * sinSigma * (cos2SigmaM + C * cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM)));
    }
} while ((Math.abs(lambda - lambdaP) > 1.0E-12 && --iterLimit > 0));
  if (iterLimit === 0)
    return NaN;
var uSq = cosSqAlpha * (a * a - b * b) / (b * b);
var A = 1 + uSq / 16384 * (4096 + uSq * (-768 + uSq * (320 - 175 * uSq)));
var B = uSq / 1024 * (256 + uSq * (-128 + uSq * (74 - 47 * uSq)));
var deltaSigma = B * sinSigma * (cos2SigmaM + B / 4 * (cosSigma * (-1 + 2 * cos2SigmaM * cos2SigmaM) - B / 6 * cos2SigmaM * (-3 + 4 * sinSigma * sinSigma) * (-3 + 4 * cos2SigmaM * cos2SigmaM)));
var dist = b * A * (sigma - deltaSigma);
return dist;
};

shiftToValidDegree = function (deg){

    return ((deg % 360) + 360) % 360;

}

calcBearingDifference = function (userheadingdeg, degree) {
    var degdifference = shiftToValidDegree(userheadingdeg - degree);
    return degdifference;
};

radToDegree = function (rad) {
  return rad * (180 / Math.PI);
};



 // Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original

/* exports.addMessage = functions.https.onRequest(async (req, res) => {
    // Grab the text parameter.
    const original = req.query.text;
    // Push the new message into the Realtime Database using the Firebase Admin SDK.
    const snapshot = await admin.database().ref('/Users').push({original: original});
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    res.redirect(303, snapshot.ref.toString());
  }); */