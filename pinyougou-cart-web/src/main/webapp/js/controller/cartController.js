app.controller('cartController', function ($scope, cartService) {

    // 查询购物车列表
    $scope.findCartList = function () {
        cartService.findCartList().success(
            function (response) {
                $scope.cartList = response;
                $scope.totalValue = cartService.sum($scope.cartList);
            }
        )
    };

    //数量加减
    $scope.addGoodsToCartList = function (itemId, num) {
        cartService.addGoodsToCartList(itemId, num).success(
            function (response) {
                if (response.success) {
                    $scope.findCartList();
                } else {
                    alert(response.message);
                }
            }
        );
    };

    $scope.isLogin = function () {
        cartService.isLogin().success(
            function (response) {
                if (response.success) {
                    $scope.isLogin = true;
                } else {
                    $scope.isLogin = false;
                }
            }
        )
    };

    /*//求总和
        sum = function () {
            $scope.totalNum = 0;
            $scope.totalMoney = 0;

            for (var i = 0; i < $scope.cartList.length; i++) {
                for (var j = 0; j < $scope.cartList[i].orderItemList.length; j++) {
                    $scope.totalNum += $scope.cartList[i].orderItemList[j].num;
                    $scope.totalMoney += $scope.cartList[i].orderItemList[j].totalFee;
                }
            }
        }*/

    // 获取当前用户的列表
    $scope.findAddressList = function () {
        cartService.findAddressList().success(
            function (response) {
                $scope.addressList = response;
                for (var i = 0; i < $scope.addressList.length; i++) {
                    if ($scope.addressList[i].isDefault == '1') {
                        $scope.address = $scope.addressList[i];
                        break;
                    }
                }
            }
        )
    };

    // 选择地址，改变当前地址
    $scope.selectAddress = function (address) {
        $scope.address = address;
    };

    // 判断是否为当前地址
    $scope.isSelectAddress = function (address) {
        if ($scope.address == address) {
            return true;
        } else {
            return false;
        }
    };

    $scope.addressTemp = {};

    $scope.findOne = function() {
        $scope.addressTemp = JSON.parse(JSON.stringify($scope.address));
    };

    //保存
    $scope.save=function(){
        var serviceObject;//服务层对象
        if($scope.addressTemp.id!=null){//如果有ID
            serviceObject=cartService.update($scope.addressTemp); //修改
        }else{
            serviceObject=cartService.add($scope.addressTemp);//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    //重新查询
                    $scope.findAddressList();//重新加载
                }else{
                    alert(response.message);
                }
            }
        );
    };

    $scope.del = function () {
        cartService.del($scope.addressTemp.id).success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                    $scope.findAddressList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        )
    };

    // 提交的订单对象
    $scope.order = {paymentType:'1'};

    // 修改支付方式
    $scope.selectPayType = function (type) {
        $scope.order.paymentType = type;
    };

    // 提交订单
    $scope.submitOrder = function () {
        $scope.order.receiverAreaName = $scope.address.address;
        $scope.order.receiverMobile = $scope.address.mobile;
        $scope.order.receiver = $scope.address.contact;

        cartService.submitOrder($scope.order).success(
            function (response) {
                if (response.success) {
                    if ($scope.order.paymentType == '1') {//如果是微信支付，跳转支付页面
                        location.href = "pay.html";
                    } else {
                        location.href = "paysuccess.html";
                    }
                } else {
                    location.href = "payfail.html";
                }
            }
        )
    }

});