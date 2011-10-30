Main
====

The gt-main module provides default implementations for the remaining *gt-api* and *gt-opengis*
interfaces (Filter, Style, Feature etc...) and enough glue code to make creating an application
possible (various builders and utility classes).

.. image:: /images/gt-main.png

The gt-main module is responsible for:

* Default implementation :doc:`gt-opengis <../opengis/index>` interfaces for Feature, FeatureType and Filter and Style
* Default set of :doc:`gt-opengis <../api/convert>` Converters supporting basic Java types
* Default set of :doc:`gt-opengis <../opengis/filter>` Functions for working with spatial data
* Helper classes for your own application development such as *DataUtilities* and *SimpleFeatureTypeBuilder*
* Abstract classes to help implementors of :doc:`gt-api <../api/index>` DataStore

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-main</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal

.. toctree::
   :maxdepth: 1
   
   data
   filter
   feature
   collection
   shape
   geometry
   repository
   
**SOSNOKILLLICENSE**

The file **DateUtils** requires that this license be included with your GeoTools documentation.::
   
   Copyright (c) 2002-2004, Dennis M. Sosnoski.
   All rights reserved.
   Redistribution and use in source and binary forms, with or without modification,
   are permitted provided that the following conditions are met:

   * Redistributions of source code must retain the above copyright notice, this
     list of conditions and the following disclaimer.
   * Redistributions in binary form must reproduce the above copyright notice,
     this list of conditions and the following disclaimer in the documentation
     and/or other materials provided with the distribution.
   * Neither the name of JiBX nor the names of its contributors may be used
     to endorse or promote products derived from this software without specific
     prior written permission.
   
   THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
   ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
   WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
   DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
   ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
   (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
   LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
   ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
   (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
   SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
