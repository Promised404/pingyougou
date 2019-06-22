app.controller('indexController', function ($scope,$controller, contentService) {

    $controller('baseController',{$scope:$scope});//继承

    $scope.contentList = [];

    // 通过
    $scope.findContent = function (id) {
        contentService.findByCategoryId(id).success(
            function (response) {
                $scope.contentList[id] = response;
            }
        )
    };

    // 搜索 （传递参数）
    $scope.search = function () {
        location.href = "http://localhost:9104/search.html#?keywords=" + $scope.keywords;
    }
});