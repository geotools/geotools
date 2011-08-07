Supporting your module
==========================

The GeoTools library plays host to several "unsupported" components, here is the process for formally including your work in the as a core part of the GeoTools library.

* Why Support your Module
  
  You do get a couple of benefits from having a supported module:
  
  * your work is bundled up as part of the GeoTools release process
  * You can have a couple of seconds JUnit test time to verify your module works
  * You can create Online-Tests for cruise control to run

* Picking up a Module
  
  If you are interested in picking up an unsupported module (perhaps it was abandoned?)
  have a look into the section on Module Maintainers - you can volunteer as Module
  Maintainer if you interested.

====== ============================= ==========================================
Check  Step                          Notes
====== ============================= ==========================================
x      Visibility                    Communicate module status to users
x      Intellectual Property Check   Build user trust, OSGeo policy
x      Follow the Developers Guide   Project policies
x      User Documentation            Ensure work is accessible to users
x      Ask                           Ask to be included in the next release
====== ============================= ==========================================

Visibility / Module Status
^^^^^^^^^^^^^^^^^^^^^^^^^^

The GeoTools Module Matrix defines a couple quick QA tests allowing you to rate your module according to the number of gold stars it has earned.

1. The first step is to rate your module:
   
   :doc:`check`

2. GeoTools 2.5 and onward expects four stars for a supported module.

3. Create a Module Matrix page:
   
   * show project status
   * list jira issues (from as many projects as possible)
   
4. The goal here is to make your module status visible to end users.
   
   * Blog posts
   * Email on the user list

Intellectual Property Check
^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Your module must have a file presenting the providence review of the contents of your module
   including the copyright and licenses which apply to your module.

2. The file must be named and located as 'src/site/apt/review.apt'
   
   The file must be in the "Almost Plain Text" format and should follow the standard layout of
   the file in the unsupported/example module.

3. This file describes any contents which do not follow the copyright and license of the
   project as a whole.
   
   * Deviations which require later fixes should have a link to a JIRA task explaining the
     issue and describing the plan to resolve the issue or remove the offending resource.
   
   * The goal here is to show that each and every file has been looked at, by hand, and any
     issues of copyright or licensing are known, and if possible addressed.
   
   * To make it easier to start you may wish to organise your review.apt file by
     package.

4. Fixing some of these issues in the past has required "stubbing" some of the jar dependencies
   with dummy code of the same signature from a third party JAR which we cannot distribute.

Follow the Developers Guide
^^^^^^^^^^^^^^^^^^^^^^^^^^^

The developers guide lists a number of coding conventions, we would like to ensure you
line up with the following for a consistent project.

1. Coding Style: This is easy to check, load up the code formatting in eclipse and hit auto format.

2. Do not return null.
   
   A simple search for "return null" will often find a few things to think about.

3. Logging
   
   A few searches for "System.out.println" will often find content that needs to be logged.

4. Exception handling
   
   This one is harder to test, particularly with gobbling exceptions.
   Searching for "catch (Throwable" often leads to results.

5. Avoid Assumptions

6. Converting URLs to Files

7. Use of Assertions, IllegalArgumentException and NPE

8. Naming Conventions
   
   Running "FindBugs" will often catch inconsistent names.

9. Module Directory Structure

10. Testing
    
    Careful attention to use of "OnlineTest" for anything involving the internet. Running a
    build without your machine to the internet is an easy way to identify online tests.
    
    Maven indicates how long each test takes to run, use of "StressTest" can allow you to
    cut down on testing time as needed.

Test Coverage and Maven Profiles
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The one most likely to cause grief is the JUnit testing requirement.

1. Please be aware that the tests may be performed by a build box such as hudson.
   Especially if they are on-line tests requiring the use of web services.

2. The test coverage expectation for the GeoTools library is 40%.
       
   * Test Coverage measured and published to Module Matrix page
   * Coverage of 40% measured by a hudson control profile.
     You can supply a test fixture so the nightly builds can run against your database.
   * Plugins can extend provided "conformance test" if available to quickly reach 40%.

3. For help setting up your test fixture and maven profile for the nightly build box please
   contact the geotools-devel list.

How to measure test coverage:

1. Test coverage measured with cobertura or clover.
2. Run the following for your plugin::
     
     run mvn site

How to use a conformance test:

1. The author of an interface or abstract class may have provided a sample "conformance" test
   used to verify plug-in completeness and correctness.
2. For JDBC-NG datastore implementations
   
   * When implementing a new JDBC datastore please extend the provided abstract test class
     and customise it with your own test fixture.
   * For an example review the jdbc-hsql plugin

3. DataStore conformance in general can be based on MemoryDataStore example
   
   * Verify concurrency and event notification
   * Verify constant time performance of metadata queries

User Documentation
^^^^^^^^^^^^^^^^^^

1. Currently have a very simple requirement for user documentation.
   
   * Please make something (anything!) available in GeoTools User Guide
   
2. Link to the documentation page from your module matrix page.
3. It is recommended that you make a single example showing how to use your module
   or plugin (you can use a a code example if you must).
4. You may wish to refer to the writing guidelines for the user guide
   
   * :doc:`/docs/index`

Ask to be included in the next release
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

1. Finally you can ask to be included in the next release
2. Send an email to the list, indicating your module is ready
3. Chances are there will be questions and the occasional code review
4. Congratulations and welcome to GeoTools!
