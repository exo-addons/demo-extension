/**
 * Demo Extension class
 * @constructor
 */
function DemoExtension() {

}

DemoExtension.prototype.headerCtrl = function ($scope, $http) {
  $scope.startPopulating = function() {

    console.log("startPopulating");
    $http.get('/demo-extension/start').success(function(data) {
      console.log(data);
    });

  }
}

DemoExtension.prototype.populateCtrl = function ($scope, $http, $timeout) {

  $scope.onElementsTimeout = function(){

    $http.get('/demo-extension/elements.json').success(function(data) {
      $scope.elements = data;
    });

    popto = $timeout($scope.onElementsTimeout,3000);
  }
  var popto = $timeout($scope.onElementsTimeout,100);

  $scope.stop = function(){
    $timeout.cancel(popto);
  }
}
