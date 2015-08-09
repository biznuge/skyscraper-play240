angular.module('SkyScraperApp.services', []).
  factory('itemsService', function($http) {

    var itemsPlayController = {};

    itemsPlayController.getItems = function() {
      return $http({
        method: 'GET',
        url: '/getItems'
      });
    }

    /*itemsPlayController.getMore = function(previousPubDate) {
          return $http({
            method: 'GET',
            url: '/getMore',
            data: {pubDate:previousPubDate}
          });
    }*/

    return itemsPlayController;
  });