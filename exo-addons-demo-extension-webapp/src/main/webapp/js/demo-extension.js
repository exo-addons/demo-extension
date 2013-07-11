/**
 * Demo Extension class
 * @constructor
 */
function DemoExtension() {
  var $demoApplication = $("#populator_div");
  this.jzStart = $demoApplication.jzURL("PopulatorApplication.start");
  this.jzElements = $demoApplication.jzURL("PopulatorApplication.elements");
}

DemoExtension.prototype.headerCtrl = function ($scope, $http) {
  $scope.startPopulating = function() {

    $http.get(demoExtension.jzStart).success(function(data) {
      console.log("Status :: "+data.status);
    });

  }
}

DemoExtension.prototype.populateCtrl = function ($scope, $http, $timeout) {

  $scope.onElementsTimeout = function(){

    $http.get(demoExtension.jzElements).success(function(data) {
      $scope.elements = data;
    });

    //popto = $timeout($scope.onElementsTimeout,3000);
  }
  var popto = $timeout($scope.onElementsTimeout,100);

  $scope.stop = function(){
    $timeout.cancel(popto);
  }
}
