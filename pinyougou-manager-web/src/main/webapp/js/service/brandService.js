// 品牌服务
app.service("brandService", function ($http) {

    this.findAll = function () {
        return $http.get('../brand/all.do');
    };

    this.findPage = function (page,size) {
        return $http.get('../brand/page.do?page='+ page +'&size=' + size)
    };

    this.add = function (entity) {
        return $http.post('../brand/add.do', entity);
    };

    this.update = function (entity) {
        return $http.post('../brand/update.do', entity);
    };

    this.findOne = function (id) {
        return $http.get('../brand/findOne.do?id=' + id);
    };

    this.dele = function (ids) {
        return $http.get('../brand/delete.do?ids=' + ids);
    };

    this.search = function (page, size, searchEntity) {
        return $http.post('../brand/search.do?page=' + page + '&size=' + size,searchEntity);
    };

    // 下拉列表数据
    this.selectOptionList = function () {
        return $http.get('../brand/selectOptionList.do');
    }
});