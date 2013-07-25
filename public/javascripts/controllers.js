'use strict';

/** Controllers */
angular.module('sseFeed.controllers',[]).
    controller('FeedCtrl', function ($scope, $http) {
        $scope.msgs = [];
        $scope.inputText = "";

        $scope.init = function(channel)
          {
            $scope.listen(channel);
          };

//        /** change current room, restart EventSource connection */
//        $scope.setCurrentRoom = function (room) {
//            $scope.chatFeed.close();
//            $scope.msgs = [];
//            $scope.listen();
//        };

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

        /** start listening on messages from selected channel */
        $scope.listen = function (channel) {
            $scope.chatFeed = new EventSource("/statusFeed/channel" + channel);
            $scope.chatFeed.addEventListener("message", $scope.addMsg, false);
        };


    });