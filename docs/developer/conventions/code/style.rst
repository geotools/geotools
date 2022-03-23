Coding Style
------------

Coding conventions describe the coding styles developers should use when writing code.

We follow the `Google formatting conventions <https://google.github.io/styleguide/javaguide.html>`__, 
with the AOSP variant (4 spaces indent instead of 2).


But what about the header?
^^^^^^^^^^^^^^^^^^^^^^^^^^

Most of the time all you need to do is update the header boilerplate to include the correct
dates on the copyright statement, the boilerplate itself covers project name and LGPL license.

* If the code is being created by you strictly for the GeoTools project it should be a simple copy   and paste, followed filling in the current year:
  
  .. code-block:: java
   
     /*
      *    GeoTools - The Open Source Java GIS Toolkit
      *    http://geotools.org
      *
      *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
   
  The above code reflects a file that was created in 2022 and explicitly contributed to the GeoTools project (the Open Source Geospatial Foundation is our legal entity holding the copywrite). The file is being published by OSGeo using an LGPL License (which we include with our source code and in our project documentation).

* The header for all demo, tutorial and sample code is:

  .. code-block:: java
   
      /*
       *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
       *    https://docs.geotools.org
       *
       *    To the extent possible under law, the author(s) have dedicated all copyright
       *    and related and neighboring rights to this software to the public domain worldwide.
       *    This software is distributed without any warranty.
       * 
       *    You should have received a copy of the CC0 Public Domain Dedication along with this
       *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
       */
  
  In this case we are placing the sample code into the public domain so users can cut and paste
  the examples into their own code as they are learning.

* When reusing code from another LGPL project reflect the prior organizations that were custodians of the code:
  
  .. code-block:: java
  
      /*
       *    GeoTools - The Open Source Java GIS Toolkit
       *    http://geotools.org
       *    
       *    (C) 2006, Open Source Geospatial Foundation (OSGeo)
       *    (C) 2002, MyCompany Ltd.
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

  The the above example assumes a fictious class that has been developed by "MyCompany"
  and then published LGPL license in 2002.  The work was contributed to GeoTools 2006.

* When reusing code published under another compatible license do not modify the header at all (if you make a chance you can add the GeoTools header on top).

  * :download:`DateUtil.java </../../modules/library/main/src/main/java/org/geotools/feature/type/DateUtil.java>`    
  
  Based on the license restrictions you may need to add some credits to the GeoTools user guide; and list the license in your jar.
  
  * `user guide license <http://docs.geotools.org/latest/userguide/welcome/license.html>`_ lists all licenses
  * `gt-main license page <http://docs.geotools.org/latest/userguide/library/main/index.html>`_ lists license for DateUtil.java

* When given explicit permission to relicense code (for an example an email from the GeoServer Project Steering Committee) be very careful to update the header appropriately:

  .. code-block:: java
  
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

The Google formatter plugin is embedded in the build and will reformat the code at each build, matching the coding conventions. Please always build before committing!.

The `google-java-format <https://github.com/google/google-java-format>`__ project also offers plugins for various IDEs, if your IDE is not supported, please just build once on the command line before committing.

Also you could use a `modified version <https://github.com/fernandor777/google-java-format/releases/download/1.7-gsgt/google-java-format-eclipse-plugin-1.6.0.jar>`__ 
with 4 spaces indent by default, putting the jar file on Eclipse dropins folder.