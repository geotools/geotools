Coding Style
------------

Coding conventions describe the coding styles developers should use when writing code.

For example, whether you use 2, 4, or 8 space indents. Standardising on a coding style across a project improves legibility of the code, and automatic code formatters make conforming to these standards easy.

Sun Coding Conventions and a little bit more
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We follow:

* Sun coding convention: http://java.sun.com/docs/codeconv/
* but allow for 100 characters in width
* developers should use spaces for indentations, not tabulations.
  The tab width (4 or 8 spaces) is not the same on all editors.

But what about the header?
^^^^^^^^^^^^^^^^^^^^^^^^^^

The header for all core GeoTools code is::
   
   /*
    *    GeoTools - The Open Source Java GIS Toolkit
    *    http://geotools.org
    *
    *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
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

The header for all demo code is::
   
   /*
    *    GeoTools - The Open Source Java GIS Toolkit
    *    http://geotools.org
    *
    *    (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
    *
    *    This file is hereby placed into the Public Domain. This means anyone is
    *    free to do whatever they wish with this file. Use it well and enjoy!
    */

Use of Formatting Tools
^^^^^^^^^^^^^^^^^^^^^^^^

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

Here are some eclipse settings that you can import when working on geotools work:

* http://svn.osgeo.org/geotools/trunk/build/eclipse/codetemplates.xml
* http://svn.osgeo.org/geotools/trunk/build/eclipse/formatter.xml

To use:

1. Open up Window > Preferences and navigate to the **Java > Code Style > Formatter** page

2. Press the import button and select the file from your GeoTools check out (located in build/eclipse.formatter.xml)

3. Change to the Java > Code Style > Code Templates page
4. Import the template definition from your geotools check out (located in build/eclipse/codetemplates.xml)
5. These files will have the header already to go for you; the right format options and so on.

NetBeans
''''''''
 
NetBeans also has the sun settings built in; please modify these defaults to match our project.

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
* We have a custom \@source annotation to help our javadocs look pretty (and reference the
  appropraite module).
  
  The taglet code discards \$URL: \$ if you would like to use svn to expand the correct path
  as needed.
  
  You can use the InsertSourceTag tool to add the \@source to files. 
  
  The following will insert the \@source tag in any source file in module **gt-wps** that does
  not already contain it::
    
    cd trunk/build/maven/javadoc
    mvn exec:java -Dexec.mainClass=org.geotools.maven.tools.InsertSourceTag -Dexec.args="../../../modules/unsupported/wps/src"

  Adding ``--replace`` after the source path argument above will cause any existing \@source tags to be replaced by newly
  generated ones.

  Adding ``--svn`` will result in the \@source tag content being enclosed in svn keyword delimiters (\$URL, \$) which enables
  svn keyword expansion to update the path if the source file is later moved to another package or module. Note, keyword
  expansion only works with svn, and not for git-svn. When using the ``--svn option`` it is also a good idea to ensure that
  the svn properties of all source files are set. On a 'nix system you can do this::

    find modules/unsupported/wps/src -type f -name '*.java' -exec svn ps svn:keywords "Id URL" {} \;

* If you would like to include any diagrams or pictures please add them to a *doc-files* folder.
  (This is a reminder of normal javadoc convention).
