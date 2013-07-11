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
}

DemoExtension.prototype.populateCtrl = function ($scope, $http, $timeout) {
  $scope.startPopulating = function() {
    $(".btn-start").addClass("disabled");

    $http.get(demoExtension.jzStart).success(function(data) {
      console.log("Status :: "+data.status);
      $(".btn-start").removeClass("disabled");
      $timeout.cancel(popto);
      $scope.onElementsTimeout();
    });

  }

  $scope.onElementsTimeout = function(){

    $http.get(demoExtension.jzElements).success(function(data) {
      $scope.elements = data;
    });

    popto = $timeout($scope.onElementsTimeout,500);
  }
  var popto = $timeout($scope.onElementsTimeout,100);

  $scope.stop = function(){
    $timeout.cancel(popto);
  }
}
