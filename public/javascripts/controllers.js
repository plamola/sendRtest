'use strict';

/** Controllers */
angular.module('sseFeed.controllers',[]).
    controller('FeedCtrl', function ($scope, $http, $resource) {
        $scope.msgs = [];
        $scope.inputText = "";

        $scope.transformers = $resource('/sendr/transformers').query();

        $scope.init = function()
          {
            $scope.listen();
          };

        $scope.sendStart = function (id) {
            $http.post("/start/" +  id );
        };


        $scope.sendPause = function (id) {
            $http.post("/pause/" +  id );
        };


        $scope.sendStop = function (id) {
            $http.post("/stop/" +  id );
        };

        /** handle incoming messages: add to messages array */
        $scope.addMsg = function (msg) {
            $scope.$apply(function () { $scope.msgs.push(JSON.parse(msg.data)); });
        };

        /** start listening on messages */
        $scope.listen = function () {
            $scope.chatFeed = new EventSource("/statusFeedAll");
            $scope.chatFeed.addEventListener("message", $scope.addMsg, false);
        };


    });