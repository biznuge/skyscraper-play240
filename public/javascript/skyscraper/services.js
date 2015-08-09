angular.module('SkyScraperApp.services', []).
  factory('itemsService', function($http) {

    var itemsPlayController = {};

    itemsPlayController.getItems = function() {
      return $http({
        method: 'GET',
        url: '/getItems'
      });
    }

    return itemsPlayController;
  });