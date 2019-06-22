app.controller('payController', function ($scope, $location, payService) {

    $scope.createNative = function () {
        payService.createNative().success(
            function (response) {
                // 显示订单号和金额
                $scope.out_trade_no = response.out_trade_no;
                $scope.money = (response.total_fee / 100).toFixed(2);

                var qr = new QRious({
                    element:document.getElementById('qrious'),
                    size:200,
                    /*value:response.code_url,  微信支付没有注册成商家*/
                    value:"www.waityou99.com",
                    level:'H'
                });
                queryPayStatus(); //调用查询
            }
        );
    }

    queryPayStatus = function () {
        payService.queryPayStatus($scope.out_trade_no).success(
            function (response) {
                if (response.success) {
                    location = "paysuccess.html#?money=" + $scope.money;
                } else {
                    if (response.message == '二维码超时') {
                        $scope.createNative();
                    }else {
                        location = "payfail.html";
                    }
                }
            }
        )
    };

    $scope.getMoney = function () {
        return $location.search()['money'];
    }
    
});