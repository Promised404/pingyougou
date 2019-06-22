app.controller('searchController', function ($scope, $location, searchService) {

    $scope.searchMap = {'keywords':'', 'category':'', 'brand':'', 'spec':{}, 'price':'', 'pageNo':1, 'pageSize':40, 'sort':'', 'sortField':''};

    // 搜索
    $scope.search = function () {
        $scope.searchMap.pageNo = parseInt($scope.searchMap.pageNo);
        searchService.search($scope.searchMap).success(
            function (response) {
                $scope.resultMap = response;
                buildPageLable();// 构建分页栏
                //$scope.searchMap.pageNo = 1; //查询后显示第一页
            }
        );
    };

    // 构建分页栏
    buildPageLable = function() {
        $scope.pageLable = [];
        var firstPage = 1;//开始页码
        var lastPage = $scope.resultMap.totalPages;
        $scope.firstDot = true;
        $scope.lastDot = true;

        if ($scope.resultMap.totalPages > 5) {
            if ($scope.searchMap.pageNo <= 3) {
                lastPage = 5;
                $scope.firstDot = false;
            } else if ($scope.searchMap.pageNo >= $scope.resultMap.totalPages - 2) {
                firstPage = $scope.resultMap.totalPages - 4;
                $scope.lastDot = false;
            } else { // 显示以当前页为中心页
                firstPage = $scope.searchMap.pageNo - 2;
                lastPage = $scope.searchMap.pageNo + 2;
            }
        } else {
            $scope.firstDot = false;//没点
            $scope.lastDot = false;//没点
        }

        for (var i = firstPage; i <= lastPage; i++) {
            $scope.pageLable.push(i);
        }
    };


    // 添加搜索项 改变searchMap的值
    $scope.addSearchItem = function (key, value) {
        if (key == 'category' || key == 'brand' || key == 'price') { // 如果用户点击的是分类或者品牌
            $scope.searchMap[key] = value;
        } else { // 如果点击的是规格
            $scope.searchMap.spec[key] = value;
        }
        $scope.search(); //改变条件后再查询
    };

    // 页码分页查询
    $scope.queryByPage = function(pageNo) {
        if (pageNo < 1 || pageNo > $scope.resultMap.totalPages) {
            return;
        }
        $scope.searchMap.pageNo = pageNo;
        $scope.search(); //查询
    };

    //排序查询
    $scope.queryBySort = function(sort ,sortField) {
        $scope.searchMap.sortField = sortField;
        $scope.searchMap.sort = sort;
        $scope.search();
    };

    // 样式配置
    $scope.isTopPage = function() {
        if ($scope.searchMap.pageNo == 1) {
            return true;
        } else {
            return false;
        }
    };

    $scope.isEndPage = function() {
        if ($scope.searchMap.pageNo == $scope.resultMap.totalPages) {
            return true;
        } else {
            return false;
        }
    };

    // 撤销搜索项
    $scope.removeSearchItem = function (key) {
        if (key == 'category' || key == 'brand' || key == 'price') { // 如果用户点击的是分类或者品牌
            $scope.searchMap[key] = "";
        } else { // 如果点击的是规格
            //TODO AngurlaJs delete
            delete $scope.searchMap.spec[key];
        }
        $scope.search(); //改变条件后再查询
    };

    // 判断搜索关键字是否是品牌
    $scope.keywordsIsBrand = function () {
        for (var i = 0; i < $scope.resultMap.brandList.length; i++) {
            if ($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text) >= 0) {
                return true;
            }
        }
        return false;
    };

    $scope.loadkeywords = function () {
        $scope.searchMap.keywords = $location.search()['keywords'];
        if ($scope.searchMap.keywords != null) {
            $scope.search(); //改变条件后再查询
        }
    }

});