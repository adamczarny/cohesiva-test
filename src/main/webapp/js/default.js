/**
 * Created by adam on 3/6/14.
 */
var app = angular.module("app",[]);

app.controller("defaultController", function($scope,$http){
    $scope.getIds= function (id){$http.get("/servlet/rest/"+id).success(function(data){
        $scope.things =data;
   }).error(function(error){
        $scope.things = '{"status"="error","id"=1}';
    })};
});