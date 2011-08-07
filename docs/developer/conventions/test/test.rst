Testing
=======

.. toctree::
   :maxdepth: 1
   
   junit
   online
   data

Use of Maven Test Profiles
--------------------------

Install with all normal tests::
    
    mvn install

We have isolated long running tests so they are not part of the day to day grind. You can use a
maven profile to increase the amount of testing performed.

*  Install with online tests (will connect to servers around the world)::
     
     mvn -P online install

*  Install with stress tests (long running tests that try to break the code using many threads)::
     
     mvn -P stress install

* To run interactive tests that will display dialogs on screen::
     
     mvn -P interactive install

You can combine profiles as needed::
   
   mvn -P online,stress install

Build Time Targets
------------------

To keep build times down (so tests are run at all) we ask you to stay in the following time limits.

* 20 seconds for a module
* 5 seconds	 for a plugin
* 5 seconds	 for an extension

Tests on unsupported modules are not subject to a time limit, to run your tests in an unsupported
module you will need to make use of the provided profile::
   
   mvn -P unsupported install

Code Coverage vs Regression Testing
------------------------------------

Code Coverage reports are available via::
   
   mvn site

The percentage reported is based on the lines of code your test cases manage to test, please limit
this to "real" tests - although we demand 40% test coverage for supported modules we would much
rather this is produced honestly.

Creating boiler plate tests that just call assertEquals against every method, and cutting and
pasting the current result is an example of a regression test. While this will catch changes in our
codebase it is not nearly as useful as actually testing for what you expect.
