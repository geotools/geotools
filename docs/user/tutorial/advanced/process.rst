:Author: Jody Garnett
:Version: |release|
:License: Creative Commons with attribution

Process Tutorial
----------------

The GeoTools process system is a great way to package up useful functionality for use in your
application (or publish out via a "web processing service" such as GeoServer or 52N).

When used with a Process Engine these individual processes can be chained together to make for
interesting and useful scripts.

This process data structure is a lot more capable than the Functions we added to the geotools filter
system and is able to work on large quantities of data as needed. The general idea is similar in that you get to write some simple java code and package it up
for the GeoTools library to use. 

We have a number of options:

* Annotations
* DataStructure

Reference:

* :doc:`/unsupported/process/index`

Process Annotations
^^^^^^^^^^^^^^^^^^^

This is the fastest way to create a process; and is great (as long as your process produces a
single result).

1. To start with we need to create a class that extends StaticMethodsProcessFactory:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/process/ProcessTutorial.java
      :language: java
      :end-before: // constructor start
      
2. We have a little bit of work to fill in the constructor

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/process/ProcessTutorial.java
      :language: java
      :start-after: // constructor start
      :end-before: // constructor end

3. We can now implement our function::

    static public Geometry octagonalEnvelope( Geometry geom) {
        return new OctagonalEnvelope(geom).toGeometry(geom.getFactory());
    }
    
4. And then we can fill in the annotations to desribe our process, result and parameters.
   
   * @DescribeProcess
   * @DescribeParameter
   * @DescribeResult
   
   .. literalinclude:: /../src/main/java/org/geotools/tutorial/process/ProcessTutorial.java
      :language: java
      :start-after: // octagonalEnvelope start
      :end-before: // octagonalEnvelope end
   
5. And then hook it up to Factory SPI (as was done for the Function tutorial).
   
   Create the file:
   
   * META_INF/services/org.geotools.process.ProcessFactory

6. Fill in the following contents (one implementation class per line)::
   
      org.geotools.tutorial.process.ProcessTutorial

7. That is it octagnalEnvelope is now published.

Things to Try
^^^^^^^^^^^^^

* Try calling your process using the *Processors* utility class

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/process/ProcessExample.java
      :language: java
      :start-after: // octo start
      :end-before: // octo end
  
  Here is what that looks like:
  
  .. image:: images/octagonal_envelope.png

* The Processors class can also list a Map<String,Parameter> allowing you to show a wizard
  for data entry (just like when connecting to a DataStore).
  
   .. literalinclude:: /../src/main/java/org/geotools/tutorial/process/ProcessExample.java
      :language: java
      :start-after: // param start
      :end-before: // param end
