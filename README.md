sendRtest
=========

Simplified version of sendR for load testing with dummy data.

This sendR-test version fires a configurable amount of SOAP requests at a configured webservice. The contents of the SOAP request can be configured in a SOAP templated, where an ID and a timestamp can be dynamically inserted.
The standard configuration uses 16 actors to perform the requests in parallel.

###Why this fork?

This specialized version of sendR has been created to perform load tests on a Mendix webservice.

Applications build with Mendix 5.3.0 - 5.3.2. can no longer handle large amounts of webservice requests [Mendix ticket CC0000000100548]. After approx. 140.000 request (90 minutes), the application is no longer responding. 

Applications created with Mendix 5.2.0 didn't have this problem and could handle millions of requests.


###Usage

1. Download and extract the latest release
2. Goto the bin directory
3. Start the application from the command-line: ./sendrtest -DapplyEvolutions.default=true -Dhttp.port=2908
4. Open the url http://localhost:2908
5. Configure the 'tester' by pressing the wrench icon
6. Save the changes
7. Use the play icon to start the load test



