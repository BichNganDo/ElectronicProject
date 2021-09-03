var app = angular.module('myApp', ['ngRoute']); //ngPagination
app.controller('MyController', function($scope, $mdToast, $rootScope) {

})


app.config(function($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    let domain = "http://localhost:8080/admin/cate_product/partial/"
    $routeProvider

    // .when('/', {
    //     templateUrl: domain + 'cate_product.html',
    //     controller: 'listUsersController'
    // })
        .when('/listcateproduct', {
            templateUrl: domain + 'cate_product.html',
            controller: 'listCateProductsController'
        })
        .when('/addcateproduct', {
            templateUrl: domain + 'add_cate_product.html',
            controller: 'addCateProductsController'
        })

    .otherwise({ redirectTo: '/' })
})

// // trang listuser
app.controller('listCateProductsController', function($scope, $http, $rootScope) {
    $rootScope.title = "List Category Products";

})

//     $scope.selectedPageIndex = 1;
//     $scope.getListUsers = function() {
//         $http({
//             method: "GET",
//             url: "http://localhost:8080/api/user",
//             params: {
//                 action: 'getuser',
//                 page_index: $scope.selectedPageIndex
//             },
//         }).then(function(response) {
//             console.log(response);
//             $scope.users = response.data.data.allUsers;
//             $scope.totalUser = response.data.data.total;
//         })
//     }

//     $scope.pageChanged = function(pageIndex) {
//         $scope.selectedPageIndex = pageIndex;
//         $scope.getListUsers();
//     };

//     $scope.getListUsers();


//     //"changeEdit()"
//     $scope.changeEdit = function(item) { //item hay biến khác cũng được, không cần phải trùng tên với bên user
//         item.show = !item.show;
//     }

//     $scope.saveDataEdit = function(item) { //item hay biến khác cũng được, không cần phải trùng tên với bên user
//         item.show = !item.show;

//         //lấy dữ liệu về
//         var data = $.param({
//             id: item.id,
//             username: item.username,
//             password: item.password,
//             level: item.level
//         });

//         console.log(data);
//         var config = {
//             headers: {
//                 'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'
//             }
//         }
//         $http.post('http://localhost:8080/api/user?action=edit', data, config)
//             .then(function(response) {
//                 console.log(response);
//                 showNotification("success", response.data.message);
//             }, function(error) {
//                 console.log(error.data.message);
//                 showNotification("error", error.data.message);
//             })
//     }

//     $scope.delete = function(item) {
//         var data = $.param({
//             id: item.id,
//             username: item.username,
//             password: item.password,
//             level: item.level
//         });
//         var config = {
//             headers: {
//                 'content-type': 'application/x-www-form-urlencoded;charset=UTF-8'
//             }
//         }
//         $http.post('http://localhost:8080/api/user?action=delete', data, config)
//             .then(function(response) {
//                 console.log(response);
//                 showNotification("success", response.data.message);
//                 window.location.replace("http://localhost:8080/admin");
//             }, function(error) {
//                 console.log(error.data.message);
//                 showNotification("error", error.data.message);
//             })
//     }

// })


// // trang adduser
app.controller('addCateProductsController', function($scope, $http, $rootScope) {
    $rootScope.title = "Add Category Product"


})