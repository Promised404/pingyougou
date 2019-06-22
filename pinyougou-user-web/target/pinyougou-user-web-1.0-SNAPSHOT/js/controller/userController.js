 //控制层 
app.controller('userController' ,function($scope,userService){

    $scope.reg = function () {
        // 前端密码效验
        if ($scope.entity.password != $scope.password) {
            alert("两次密码输入不一致，请检查");
            $scope.entity.password = "";
            $scope.password = "";
            return;
        }
        // 登录
        userService.add($scope.entity,$scope.smscode).success(
            function (response) {
                alert(response.message);
                $scope.entity={};
                $scope.password = "";
                $scope.smscode = "";
            }
        )
    };

    $scope.sendCode = function () {
        if ($scope.entity.phone == null || $scope.entity.phone == "") {
            alert("请填写手机号码！");
            return ;
        }

        userService.sendCode($scope.entity.phone).success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                } else {
                    alert(response.message);
                }
            }
        )
    }

});	
