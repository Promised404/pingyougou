app.service('payService', function ($http) {

    // 本地支付
    this.createNative = function () {
        return $http.get('pay/createNative.do');
    };

    // 查询订单情况
    this.queryPayStatus =function (out_trade_no) {
        return $http.get('pay/queryPayStatus.do?out_trade_no=' + out_trade_no);
    };

});