// 购物车服务层
app.service('cartService', function ($http) {

    // 查找购物车列表
    this.findCartList = function () {
        return $http.get('../cart/findCartList.do')
    };

    //新增
    this.addGoodsToCartList = function (itemId, num) {
        return $http.get('../cart/addGoodsToCartList.do?itemId=' + itemId + '&num=' + num);
    }

    this.sum = function (cartList) {
        var totalValue = {totalNum:0, totalMoney:0};

        for (var i = 0; i < cartList.length; i++) {
            for (var j = 0; j < cartList[i].orderItemList.length; j++) {
                totalValue.totalNum += cartList[i].orderItemList[j].num;
                totalValue.totalMoney += cartList[i].orderItemList[j].totalFee;
            }
        }
        return totalValue;
    };

    // 判断是否登录
    this.isLogin = function () {
      return $http.get('../cart/isLogin.do');
    };

    // 获取当前登录账号的地址
    this.findAddressList = function () {
        return $http.get('address/findListByLoginUser.do');
    };

    this.update = function (address) {
        return $http.post('../address/update.do', address);
    };

    this.add = function (address) {
        return $http.post('../address/add.do', address);
    };

    this.del = function (ids) {
        return $http.get('../address/delete.do?ids=' + ids);
    };

    this.submitOrder = function (order) {
        return $http.post('../order/add.do', order);
    }
});