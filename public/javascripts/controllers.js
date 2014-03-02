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
                    transformer.percentageDone = 0;
//                    transformer.msgs = [];
                    $scope.categories=categories.getUnique();
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
        $scope.addMsg = function (payload) {
            $scope.$apply(function () {
                var msg = JSON.parse(payload.data);
                //console.dir(msg);
                for(var t=0; t < $scope.transformers.length;t++) {
                    var transformer = $scope.transformers[t];
                    if (msg.channelName == transformer.name) {
                        transformer.channelId     = msg.channelId;
                        transformer.channelName   = msg.channelName;
                        transformer.successes     = msg.successes;
                        transformer.failures      = msg.failures;
                        transformer.timeouts      = msg.timeouts;
                        transformer.activeworkers = msg.activeworkers;
                        transformer.starttime     = msg.starttime;
                        transformer.stoptime      = msg.stoptime;
                        transformer.status        = msg.status;
                        transformer.currentFile   = msg.currentFile;
                        transformer.nrOfLines     = msg.nrOfLines;
                        transformer.startTime     = msg.startTime;
                        transformer.stopTime      = msg.stopTime;
                        transformer.time          = msg.time;
                        transformer.percentageDone = truncateDecimals((msg.successes / msg.nrOfLines)*100,1);
//                        if (msg.text != "") {
//                            transformer.msgs.push('<span class="muted">' + msg.time + '</span>&nbsp;' + msg.text);
//                        }
                        $scope.transformers[t] = transformer;
                    }
                    //console.dir(transformer);
                }
                if (msg.text != "") {
                    $scope.msgs.push(JSON.parse(payload.data));
                }
            });
        };


        function truncateDecimals (num, digits) {
            var numS = num.toString(),
                decPos = numS.indexOf('.'),
                substrLength = decPos == -1 ? numS.length : 1 + decPos + digits,
                trimmedResult = numS.substr(0, substrLength),
                finalResult = isNaN(trimmedResult) ? 0 : trimmedResult;
            return parseFloat(finalResult);
        }


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