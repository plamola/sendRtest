'use strict';

/** Controllers */
angular.module('sseFeed.controllers',[]).
    controller('FeedCtrl', function ($scope, $http, $log, $resource,localStorageService) {
        $scope.msgs = [];
        $scope.inputText = "";
        $scope.categories = [];


        $scope.$watchCollection('transformers', function () {
            $log.debug('watch $scope.transformers triggered');
            for(var c=0;c < $scope.transformers.length;c++) {
                var transformer  = $scope.transformers[c];
                // make sure to process every tranformer only once
                if (typeof transformer.processed == "undefined" ) {
                    var categories = $scope.categories;
                    if (transformer.category != "" && transformer.category != null) {
                        categories.push(transformer.category);
                    } else {
                        transformer.category = "No category";
                        categories.push("No category");
                    }
                    $scope.categories=categories.getUnique();
                    //console.dir($scope.categories);
                    transformer.processed = true;
                    $scope.transformers[c] = transformer;
                }
            }
        });

        var storedCategory = localStorageService.get('activeCategory');
        if (storedCategory == null) {
            $scope.activeCategory = "No category";
        } else {
            $scope.activeCategory = storedCategory;
        }

        $scope.transformers = $resource('/sendr/transformers').query();

        $scope.setActiveCategory = function(cat) {
            $scope.activeCategory = cat;
            localStorageService.set('activeCategory',$scope.activeCategory);
        }



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

        Array.prototype.getUnique = function(){
           var u = {}, a = [];
           for(var i = 0, l = this.length; i < l; ++i){
              if(u.hasOwnProperty(this[i])) {
                 continue;
              }
              a.push(this[i]);
              u[this[i]] = 1;
           }
           return a;
        }




    });