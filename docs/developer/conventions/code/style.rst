Coding Style
------------

Coding conventions describe the coding styles developers should use when writing code.

For example, whether you use 2, 4, or 8 space indents. Standardising on a coding style across a project improves legibility of the code, and automatic code formatters make conforming to these standards easier.

Sun Coding Conventions and a little bit more
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We follow:

* Sun coding convention: http://www.oracle.com/technetwork/java/javase/documentation/codeconvtoc-136057.html
* but allow for 100 characters in width
* developers should use spaces for indentations, not tabulations.
  The tab width (4 or 8 spaces) is not the same on all editors.

But what about the header?
^^^^^^^^^^^^^^^^^^^^^^^^^^

Most of the time all you need to do is update the header boilerplate to include the correct
dates on the copyright statement, the boilerplate itself covers project name and LGPL license.

* If the code is being created by you strictly for the GeoTools project it should be a simple copy   and paste, followed filling in the current year::
   
       /*
        *    GeoTools - The Open Source Java GIS Toolkit
        *    http://geotools.org
        *
        *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
        *
        *    This library is free software; you can redistribute it and/or
        *    modify it under the terms of the GNU Lesser General Public
        *    License as published by the Free Software Foundation;
        *    version 2.1 of the License.
        *
        *    This library is distributed in the hope that it will be useful,
        *    but WITHOUT ANY WARRANTY; without even the implied warranty of
        *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        *    Lesser General Public License for more details.
        */
   
  The above code reflects a file that was created in 2016 and explicitly contributed to the GeoTools project (the Open Source Geospatial Foundation is our legal entity holding the copywrite). The file is being published by OSGeo using an LGPL License (which we include with our source code and in our project documentation).

* The header for all demo code is::
   
       /*
        *    GeoTools - The Open Source Java GIS Toolkit
        *    http://geotools.org
        *
        *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
        *
        *    This file is hereby placed into the Public Domain. This means anyone is
        *    free to do whatever they wish with this file. Use it well and enjoy!
        */
  
  In this case we are placing the demo code into the public domain so people can cut and paste
  the examples into their own code as they are learning.

* When reusing code from another LGPL project reflect the prior organizations that were custodians of the code::
  
       /*
        *    GeoTools - The Open Source Java GIS Toolkit
        *    http://geotools.org
        *    
        *    (C) 2006, Open Source Geospatial Foundation (OSGeo)
        *    (C) 2002, Navel Ltd.
        *
        *    This library is free software; you can redistribute it and/or
        *    modify it under the terms of the GNU Lesser General Public
        *    License as published by the Free Software Foundation;
        *    version 2.1 of the License.
        *
        *    This library is distributed in the hope that it will be useful,
        *    but WITHOUT ANY WARRANTY; without even the implied warranty of
        *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
        *    Lesser General Public License for more details.
        */

  The the above example from the gt-caching module some of the spatial index functionality
  has been produced using of code from Navel Ltd that was published under the LGPL license in 2002.
  The work was contributed to GeoTools 2006.

* When reusing code published under another compatible license do not modify the header at all (if you make a chance you can add the GeoTools header on top).

  * :download:`DateUtil.java </../../modules/library/main/src/main/java/org/geotools/feature/type/DateUtil.java>`    
  
  Based on the license restrictions you may need to add some credits to the GeoTools user guide; and list the license in your jar.
  
  * `user guide license <http://docs.geotools.org/latest/userguide/welcome/license.html>`_ lists all licenses
  * `gt-main license page <http://docs.geotools.org/latest/userguide/library/main/index.html>`_ lists license for DateUtil.java

* When given explicit permission to relicense code (for an example an email from the GeoServer Project Steering Committee) be very careful to update the header appropriately::
  
     /*
      *    GeoTools - The Open Source Java GIS Toolkit
      *    http://geotools.org
      *
      *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
      *    (C) 2008-2011 OpenPlans - www.openplans.org.
      *
      *    This library is free software; you can redistribute it and/or
      *    modify it under the terms of the GNU Lesser General Public
      *    License as published by the Free Software Foundation;
      *    version 2.1 of the License.
      *
      *    This library is distributed in the hope that it will be useful,
      *    but WITHOUT ANY WARRANTY; without even the implied warranty of
      *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
      *    Lesser General Public License for more details.
      */
  
  In this case we are carefully crediting the GeoServer project and distributing the code under the LGPL license because we have obtained permission to do so.
  
Use of Formatting Tools
^^^^^^^^^^^^^^^^^^^^^^^

Get it right the first time - seriously. You cannot expect to go in and reformat your code later in
time - because that would make applying fixes between branches of GeoTools needless difficult.

If you are going to take the time to use one of these tools - do so on the first commit. And before
every commit. After the code is branched you are stuck.

If you must:
   If you must change the formatting in an existing file; please be sure to do so with a distinct
   commit. Do not combine code modification (ie a bug fix) with changing the file around to make it
   look pretty. You need to do this as a separate commit in order to have any confidence you can 
   apply patches fixes as they are developed for other branches in the past.

Eclipse
'''''''

Here are some Eclipse settings that you can import when working on GeoTools (in the source tree under `build/eclipse`):

* `codetemplates.xml  <https://github.com/geotools/geotools/blob/master/build/eclipse/codetemplates.xml>`__
* `formatter.xml <https://github.com/geotools/geotools/blob/master/build/eclipse/formatter.xml>`__

To use:

#. Open up **Window > Preferences** and navigate to the **Java > Code Style > Formatter** page
#. Press the import button and select the file from your GeoTools check out (located in build/eclipse.formatter.xml)
#. Change to the **Java > Code Style > Code Templates** page
#. Import the template definition from your GeoTools check out (located in build/eclipse/codetemplates.xml)
#. These files will have the header already to go for you; the right format options and so on.

NetBeans
''''''''
 
NetBeans also has the sun settings built in; please modify these defaults to match our project.

IntelliJ
''''''''
The `formatter.xml` can also be imported into IntelliJ as of version 13. Under **Settings -> Editor ->
Code Style** and click *Manage*. From there you can choose *Import* and select the Eclipse formatter.xml.

Javadocs
^^^^^^^^

The project does not have any minimum standards for javadocs; so it is not something that will
cause you to fail a code review.

Here is an example javadoc to use as a reference for the following conversation:

.. literalinclude:: /../../modules/library/opengis/src/main/java/org/opengis/feature/simple/SimpleFeature.java
   :language: java
   :start-after: /**
   :end-before: public interface SimpleFeature extends Feature {
   :prepend: /**
   
We do encourage developers to:

* Have "topic sentence" for each class (to help IDE developers understand what the class is).
  (This is a reminder of normal javadoc convention).
* Use **<pre>** for any code examples
  (This is a reminder of normal javadoc convention).
* Use \@author tags to credit individuals involved, you can credit their organisation in brackets
* Use \@since to list the version number in which the interface first appeared
* Update \@version annotation if the interface is ever modified
* If you would like to include any diagrams or pictures please add them to a *doc-files* folder.
  (This is a reminder of normal javadoc convention).
* We have a custom \@source taglet to help our javadocs look pretty (and reference the
  appropriate module). See :doc:`javadocutils`
  

