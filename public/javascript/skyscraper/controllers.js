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

        var item = $scope.items[$scope.items.length-1];
        var pubDate = item.pubDate

        var req = {
         method: 'GET',
         url: '/getMore/'+pubDate,
         data: { pubDate: pubDate }
        };

        $http(req).then($scope.loadMoreSucessful,$scope.loadMoreIssue);

    }

    $scope.loadMoreSucessful = function(response) {

        var items = response.data.items;

        for(itemKey in items){
            var item = items[itemKey];
            $scope.items.push(item)
            console.log(item);
        }

        $scope.items.loading = false;

    }


    $scope.loadMoreIssue = function(response) {

        alert("There was an issue with loading more items. Try again if you'd like. Could be network'ish");
        $scope.items.loading = false;

    }

    $scope.formatDate = function(date){
          var dateOut = new Date(date);
          return dateOut;
    };

    $scope.searchSubmit = function(){

        // we're passing in the event from the DOM / js method call
        // so we can then grab the original event ( a mouseclick or keyboard action )
        // and then preventDefault on it, which stops the form submission
        // from a regular event.
        //
        // return false; then prevents this submission from occuring
        try{
            $scope.search();
        }catch(exception){
            console.error(exception)
        }
        console.log("IN searchSubmit()");
        //event.originalEvent.preventDefault();
        return false;

    }


    $scope.search = function(){

        $scope.items.loading = true;
        console.log("Searching for you");

        //var item = $scope.items[$scope.items.length-1];
        //var pubDate = item.pubDate



        var req = {
         method: 'GET',
         url: '/search/'+$scope.searchQuery
         /*,
         data: { pubDate: pubDate }*/
        };

        $http(req).then($scope.searchSucessful,$scope.searchIssue);

    }

    $scope.searchSucessful = function(response) {

        $scope.searchItems = response.data.items;

        $scope.items.loading = false;

    }


    $scope.loadMoreIssue = function(response) {

        alert("There was an issue with searching for items. Try again if you'd like. Could be network'ish");
        $scope.items.loading = false;

    }

    $scope.killSearch = function(){

        $scope.searchItems = null;
        $scope.searchQuery = "";

    }



});

