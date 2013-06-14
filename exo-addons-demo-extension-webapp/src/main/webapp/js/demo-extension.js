/**
 * Demo Extension class
 * @constructor
 */
function DemoExtension() {

}

DemoExtension.prototype.phoneListCtrl = function ($scope, $http) {
  $http.get('/demo-extension/phones.json').success(function(data) {
    $scope.phones = data;
  });
}
