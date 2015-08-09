angular.module('SkyScraperApp.controllers', []).
controller('itemsController', function($scope, itemsService, $http) {

    $scope.items = {};
    $scope.loading = false;

    /*$scope.getMore = $http({
                method: 'POST',
                url: '/getMore'
              });*/

    console.log("here");

    itemsService.getItems().success(function (response) {

        $scope.items = response.items;

    });

    /*itemsService.getMore().success(function (response) {

            $scope.items = response.items;

        });*/

    $scope.loadMore = function(){

        $scope.items.loading = true;

        console.log("Loading more for you");

        // get last item in items.
        // grab it's id.
        // send this id on so that only items lower than this are selected.
        var item = $scope.items[$scope.items.length-1];
        var pubDate = item.pubDate
        //console.log($scope.items);
        //console.log(item);
        //console.log(id);
        //itemsService.getMore(pubDate);

        var req = {
         method: 'GET',
         url: '/getMore/'+pubDate,
         /*
         //?pubDate='+pubDate
         headers: {
           'Content-Type': undefined
         },*/
         data: { pubDate: pubDate }
        };

        //.then();

        $http(req).then($scope.loadMoreSucessful,$scope.loadMoreIssue);




    }

    $scope.loadMoreSucessful = function(response) {
        //$scope.items = response.items;

        //console.log($scope.items.length);
        //$scope.items = $scope.items.concat(response.items);
        //console.log($scope.items.length);

//console.log(response.data);

        var items = response.data.items;

        for(itemKey in items){
            var item = items[itemKey];
            $scope.items.push(item)
            console.log(item);
        }

        $scope.items.loading = false;


    }


    $scope.loadMoreIssue = function(response) {

        alert("There was an issue with loading more for you. Try again if you'd like.");
        $scope.items.loading = false;

    }

    $scope.search = function(){

        console.log($scope.query);

    }


    $scope.formatDate = function(date){
          var dateOut = new Date(date);
          return dateOut;
    };

});

/*
angular.module('SkyScraperApp').filter('time', function($filter)
{
 return function(pubDate)
 {
  if(input == null){ return ""; }

  var _date = $filter('date')(new Date(pubDate), 'HH:mm:ss');

  return _date.toUpperCase();

 };
});*/