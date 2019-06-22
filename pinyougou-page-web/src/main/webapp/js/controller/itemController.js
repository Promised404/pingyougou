app.controller('itemController', function ($scope, $http) {

	$scope.specificationItem = {};// 存储用户选的的规格列表

	$scope.addNum = function(x) {
		$scope.num += x;
		if ($scope.num < 1) {
			$scope.num = 1;
		}
	};

	//选择规格列表
	$scope.selectSpecification = function (key, value) {
        $scope.specificationItem[key] = value;
        searchSku();
    };

	// 判断某规格是否被选中
	$scope.isSelected = function (key, value) {
		if ($scope.specificationItem[key] == value) {
			return true;
		} else {
			return false;
		}
    };

	$scope.sku = {};//当前选择的SKU

	// 加载默认的SKU
	$scope.loadSku = function () {
		$scope.sku = skuList[0];
        $scope.specificationItem = JSON.parse(JSON.stringify(skuList[0].spec));
    };

    // 对象值是否相等
    matchObject = function (map1, map2) {

		for (var k in map1) {
			if (map1[k] != map2[k]) {
				return false;
			}
		}
		for (var k in map2) {
			if (map2[k] != map1[k]) {
				return false;
			}
		}
		return true;

    }

    // 根据规格查询sku
	searchSku = function () {
		for (var i = 0; i < skuList.length; i++) {
			if (matchObject(skuList[i].spec, $scope.specificationItem)) {
				$scope.sku = skuList[i];
				return;
			}
		}
    };

    $scope.addCart = function () {
		$http.get("http://localhost:9107/cart/addGoodsToCartList.do?itemId=" +
			$scope.sku.id + "&num=" + $scope.num, {'withCredentials':true}).success(
			function (response) {
				if (response.success) {
					location.href = "http://localhost:9107/cart.html"
				} else {
					alert(response.message);
				}
            }
		)
    }

});

