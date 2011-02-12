JUnit
------

As you code, you should write a unit test for each class to test the functionality and robustness of the class. This is made much easier by using JUnit.

JUnit is very good at:
* capturing a Jira bug report in a reproducible manner
* allowing you to specify exactly the behaviour your want - before you start coding
* unit testing

The general idea:
* If you are testing src/org/geotools/module/HelloWorld.java
* create a file test/org/geotools/module/HelloWorldTest.java
* any public void methods starting with test will be run by JUnit
* maven will run all tests for your module using: mvn test 
* tests can be ignored using you pom.xml file
* maven will not "release" your module into the repository while it still fails unit testing

Example TestCase
^^^^^^^^^^^^^^^^

Example JUnit 3 Test Case::
   
   package org.geotools.module;
   
   import org.geotools.map.BoundingBox;
   
   /**
    * Unit test for BoundingBox.
    */
   public class HelloWorldTest extends TestCase {
   
       /** Test suite for this test case */
       private TestSuite suite = null;
      
       /**
        * Constructor with test name.
        */
       public HelloWorldTest(String testName) {
           super(testName);
       }
   
       /**
        * Main for test runner.
        */
       public static void main(String[] args) {
           junit.textui.TestRunner.run(suite());
       }
   
       /**
        * Required suite builder.
        * @return A test suite for this unit test.
        */
       public static Test suite() {
           TestSuite suite = new TestSuite(HelloWorldTest.class);
           return suite;
       }
      
       /**
        * Initialize variables
        */
       protected void setUp() {
           // initialize variables
       }
      
       /** Test normal constuctors. */
       public void testHello(){
           assertTrue("HelloWorld should return null is true",HelloWorld.isNull());
       }
   }

Testing with JUnit is as easy as copying HelloWorldTest.java and adding more tests, and then executing it. If you want more information, look at the JUnit documentation or read one of the many JUnit tutorials.