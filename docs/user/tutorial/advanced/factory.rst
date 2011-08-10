:Author: Jody Garnett
:Version: |release|
:License: Creative Commons with attribution

Factory Introduction
--------------------

We are going to use FunctionFactory as an introduction to the GeoTools plug-in system.
   
In the :doc:`/welcome/architecture` we saw the difference between the core GeoTools library and plug-ins that
contribute functionality. One thing that makes a plug-in work is the "Factory SPI" plug-ins system
(this is actually a part of Java not something we made up).

Reference:

* http://download.oracle.com/javase/tutorial/sound/SPI-intro.html
* http://c2.com/cgi/wiki?ObserverPattern
* http://gsraj.tripod.com/design/creational/factory/factory.html
* http://www.eclipse.org/articles/Article-Plug-in-architecture/plugin_architecture.html

Review
''''''

Each plugin jar has:

* META-INF/services folder
  
  The folder contains a list of files (one for each interface name)
* The files contain a list of classes that implement that interface

This page is where we explain how this all works.

GeoTools is extended using the **Factory** and **FactoryRegistry** classes. The standard Factory
Pattern gives us a clue about what is about to happen:

FACTORY PATTERN
   a Factory is an object that creates other objects.

Here is where the fun begins ...

Step 1 Interface
^^^^^^^^^^^^^^^^

The first step is to define your interface; in this case we are going to use the Function
interface already provided by GeoTools.

.. image:: /images/filter_function.PNG

GeoTools loves to work with interfaces, and this is the reason why (it is the first step
of making a plug-in based library).

We are going to use **Function** as an example for this discussion::

  public interface Function extends Expression {
      String getName();
      List<Expression> getParameters();
      Literal getFallbackValue();
  }
  public interface Expression {
      Object evaluate(Object object);
      <T> T evaluate( Object object, Class<T> context );
      Object accept(ExpressionVisitor visitor, Object extraData);
  }

1. Here is a quick implementation of snapping a point to a line:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/SnapFunction.java
      :language: java

2. The mechanics of using a LocationIndexLine are covered in :doc:`/library/jts/snap`
   if you are interested.

3. One thing to notice is the definition of FunctionName used to describe valid parameters to
   user of our new function.
   
   By convention we define this as a static final SnapFunction.NAME, this is however only a
   convention (which will help in implementing the next section).

Step 2 Factory
^^^^^^^^^^^^^^

Interfaces are not allowed to have constructors, so anything that would been a Constructor is
defined as a Factory interface.

To continue with our example::

  public interface FunctionFactory {
      List<FunctionName> getFunctionNames();
      Function function(String name, List<Expression> args, Literal fallback);
  }

The factory above describes what functions are available, and allows for Function
creation. So far everything looks normal. The above is exactly how the plain "Factory Pattern"
usually works - hopefully it is familiar to you?

.. note::
   
   Factories are named after the interface they are responsible for; thus FunctionFactory.
   
Variations:

* Many factories just have a single create method (this is called the factory method)  
  We have a couple of examples of this in GeoTools including DataStoreFactorySpi
  
* Some factories have several create method allowing a compatible set of objects to be created
  together.  We have a couple of examples of these abstract factories, such as FeatureTypeFactory.

.. note:: 
   
   Some GeoTools factories extend the Factory interface, but this is optional. This Factory
   interface is useful only for factories that can be configured through a set of Hints.

To continue with our implementation we will define ExampleFunctionFactory:

1. Create ExampleFunctionFactory implementing FunctionFactory
2. Fill in the information as shown:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/ExampleFunctionFactory.java
      :language: java

3. We make reference to the static final SnapFunction.NAME.
   
   While we mentioned this as only a convention, you are free to create a
   new new FunctionNameImpl("snap", "point", "line") as part of the getFunctionNames() method.
   This has the advantage of avoiding loading SnapFunction until a user requests it by name.

4. We can now register our factory.

   Create the file:
   
   * META_INF/services/org.geotools.filter.FunctionFactory

5. Fill in the following contents (one implementation class per line)::
   
      or.geotools.tutorial.function.ExampleFunctionFactory
    
6. That is it SnapFunction is now published!

Step 3 FactoryRegistery
^^^^^^^^^^^^^^^^^^^^^^^

Geotools 2.2 uses javax.imageio.ServiceRegistry magic (where this plug-in system originated from).
Please note that the FactoryRegistry will cache the factories already found. Since factories
are stateless this should not be a problem.

Direct use of FactoryRegistry
'''''''''''''''''''''''''''''

1. You can directly use FactoryRegistery in your own code::
    
      Set categories = Collections.singleton(new Class[] {FunctionFactory.class,});
      FactoryRegistry registry = new FactoryRegistry(categories);
      
      Iterator iterator = registry.getProviders(FunctionFactory.class);
   
2. Internally The **FactoryRegistry** will look up key in System properties.
   
   * If key doesn't exist or a SecurityException is thrown, fall through.
   *  Otherwise attempt to instantiate the given class.
   
3. Then FactoryRegistry will search the resource paths for the key in META-INF/services.
   
   * If the resource is found, the file is read and the class is instantiated.
   * If the resource does not exist, fall through.
   
4. This means that FactoryRegistry will be able find any FunctionFactory that is provied on
   the CLASSPATH.

.. note::
   
   GeoTools already has a FactoryRegistry for handling FunctionFactory, as part of
   CommonFactory finder. There is however nothing stopping you from using your
   own FactoryRegistry (other than wasing resources).

Defining your own FactoryFinder
'''''''''''''''''''''''''''''''

It is noted that FactoryRegistry is not synchronized, to protect for this you can wrap the
direct use up in a FactoryFinder, which also provide type-safety.

.. note::
   
   Finders are named after the interface they are responsible for; thus FunctionFinder.

Here is an use of FactoryRegistry as part of FactoryFinder:

1. Create the FactoryRegistry in a lazy fashion, listing the interfaces you are interested
   in obtaining (known as categories).

2. GeoTools traditionally holds a FactoryRegistry in a "Finder" class:
   
   * Create ExampleFinder

3. Fill in the following details:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/ExampleFinder.java
      :language: java
      
4. The above is an example only, please use FunctionFinder

Tips for implementing your own FactoryFinder:

* The code example makes use of LazySet, this keeps us from having to check the classpath each time.
* The utility method addDefaultHints is used to apply the global GeoTools configuration to the hints
  supplied by the user.
* As shown above you can add some helper methods for client code. Often this is used to perform
  searches based on some criteria, or used to locate the "best" factory for a given task.

FactoryIteratorProviders
''''''''''''''''''''''''

FactoryIteratorProviders is used to support other Plugin mechanisms.

By default the "Factory SPI" mechanism is used to locate the Factories provided by a
FactoryFinder (and FactoryRegistry). However in order to support other plugin mechanisms
the Factories has a method addFactoryIteratorProvider(...). This method allows a developer
to add an iterator that knows how to process another extension mechanism. For example, in
eclipse one would add a FactoryIteratorProvider that returns a provider that knows how to
process eclipse extension points and can create factories from the eclipse extensions.

Abstract
^^^^^^^^

Now that we have helped client code make use of our interface, the next step is to provide
an abstract class to help those developing an implementation.

Most Geotools Factories are kind enough to give you an Abstract superclass to start your
implementation efforts from. When making your own Factories this is a good example to follow.

.. note::
   
   By asking developers to extend an abstract class you can help protect them from any
   additional methods that are added to the interface in the future.

1. Here is an example **AbstractFunction** to get a feel for what is involved.

   This is not part of GeoTools (yet) - it just shows the approach used:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/AbstractFunction.java
      :language: java

2. Here is a sample use.
   
   Note we have cut down on the number of methods the developer needs to fill in, and we have
   provided a helper method to avoid some of the "boiler plate" cut and paste coding associated
   with evaluating a parameter:
   
   .. literalinclude:: /../src/main/java/org/geotools/tutorial/function/ExampleFunctionFactory2.java
      :language: java

3. You can see how that would help in quickly banging out a set of functions.

Plugin Checklist
^^^^^^^^^^^^^^^^

**To allow clients to contribute a plugin**


1. Define an interface
   
   Example: Foo
   
2. Define factory interface
   
   Example: FooFactory

3. Define FactoryFinder
   
   Example: FooFactoryFinder

4. Define an Abstract class for implementors
   
   Example: AbstractFoo

**To allow client code access to plug-ins**

1. Make your FactoryFinder public
   
   Example:  FooFinder

**When implementing a Plugin**

1. Create your implementation

   Example:  MyFoo

2. Create you extension factory
   
   Example: MyFooFactory

3. Register with META-INF/services
   
   Example: META-INF/services/Foo