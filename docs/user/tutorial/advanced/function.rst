:Author: Jody Garnett
:Version: |release|
:License: Creative Commons with attribution

Function Tutorial
-----------------

Adding a Function to GeoTools a very useful introduction to extending the library. This is often
used to generate values when styling that you cannot otherwise accomplish using expressions.

SnapFunction
^^^^^^^^^^^^

1. Here is a quick implementation of snapping a point to a line:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/SnapFunction.java
      :language: java

2. The mechanics of using a LocationIndexLine are covered in :doc:`/library/jts/snap`
   if you are interested.

3. One thing to notice is the definition of FunctionName used to describe valid parameters to
   user of our new function.
   
   By convention we define this as a static final SnapFunction.NAME, this is however only a
   convention (which will help in implementing the next section).

4. Create ExampleFunctionFactory implementing FunctionFactory
5. Fill in the information as shown:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/ExampleFunctionFactory.java
      :language: java

6. We make reference to the static final SnapFunction.NAME.
   
   While we mentioned this as only a convention, you are free to create a
   new new FunctionNameImpl("snap", "point", "line") as part of the getFunctionNames() method.
   This has the advantage of avoiding loading SnapFunction until a user requests it by name.

7. We can now register our factory.

   Create the file:
   
   * META_INF/services/org.geotools.filter.FunctionFactory

8. Fill in the following contents (one implementation class per line)::
   
      org.geotools.tutorial.function.ExampleFunctionFactory
    
9. That is it SnapFunction is now published!

Things to Try
^^^^^^^^^^^^^

* A fair bit of the code above is "boilerplate" and could be simplified with an appropriate
  "AbstractFunction" class.
  
  GeoTools does provide a couple Abstract classes you can extend when defining your own functions.
  Have a look **FunctionImpl** and see if you find it easier then just starting from scratch.

Function
^^^^^^^^

Normally we have a little background information on the concepts covered; in this case there is an
article on how GeoTools uses Factories; and the steps to consider when creating your own
factory system for others to use.

* :doc:`factory`
