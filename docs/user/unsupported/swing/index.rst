Swing
=====

The **gt-swing** module contains GUI and utility classes which are based on the Java Swing library. Its main use is to
provide the visual components for the GeoTools `tutorial applications <../../tutorial/index.html>`_. You can also use it
for basic display purposes in your own projects, or use the classes as a starting point for your own custom components.
If you are unfamiliar with the Swing library, the Oracle `Swing tutorial
<http://download.oracle.com/javase/tutorial/uiswing/>`_ is a good place to start.

This module is not intended to be a fully-featured GUI widget set for geo-spatial applications. The focus of the
GeoTools project is on handling geospatial data, not developing user interface components. If you want to build a GIS
application with all the GUI bells and whistles that's great and GeoTools is here to help you, but we can't do it for
you. 

.. tip::
        For a ready-made, fully-featured, extendible desktop GIS application based on
        GeoTools, see `uDig <http://udig.refractions.net/>`_ which is based on `SWT
        <http://www.eclipse.org/swt/>`_ rather than Swing.

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-swing</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq

.. toctree::
   :maxdepth: 1

   jmappane
   jmapframe
   dialog
   wizard


**Overview**

The module is centred around **JMapPane**, a spatially-aware canvas class which works with the GeoTools rendering
system. If you need basic display services in your application this is the class you should start with. On the other
hand, if you all need is to quickly display one or more layers, **JMapFrame** is your friend. This is a top-level frame
class containing a JMapPane plus, optionally, a toolbar, status bar and layer list table. It is used extensively in the
GeoTools tutorial applications. For simplest uses it has a static *showMap* method, as used in the GeoTools `Quickstart
example <../../tutorial/quickstart/index.html>`_.

The module also provides a small selection of dialog classes including **JFileDataStoreChooser**, a format-aware version
of Swing's JFileChooser; **JDataStoreWizard** to prompt for connection parameters for a data store;
**JSimpleStyleDialog** to set basic rendering style elements for vector layers; **JCRSChooser** to select a map
projection.

**History**

Of all the GeoTools modules, gt-swing has the longest history of participation from the GeoTools user list, has seen no
fewer than five module maintainers come and go (a bit like drummers in Spinal Tap), and has been branched off as a
separate project at various points in its history. However, if this doesn't put you off and you have a brilliant idea, a
bug-fix or, best of all, a patch, we would love to hear from you.
