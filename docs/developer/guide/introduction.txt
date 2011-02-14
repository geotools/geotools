************
Introduction
************

This guide is intended for programmers wishing to improve the GeoTools library itself.  The User Guide is intended for programmers wishing to use GeoTools as a code library.

Think of this document as the “Care and Feeding” instructions we used to create the library for your use.

Document Overview
^^^^^^^^^^^^^^^^^

This document covers processes, conventions, and recommended tools. The guide aims to help developers quickly download or checkout the GeoTools project and start working. You are not expected to read this guide cover-to-cover. You probably should be familiar with the contents and reference the appropriate section as needed.

Where possible this document is concise with references to the web for details like installation instructions. As these instructions change over time  please advise us if any links are broken or can otherwise be updated.

For this guide to be useful it needs to be continually added to and improved as tools are developed, processes improved and projects grow.

Community members should improving or adding sections to reflect current best practice. Where possible we suggest free, cross-platform tools or those that are available to open source developers for use. Where applicable, accepted conventions and open standards are used.

System Overview
^^^^^^^^^^^^^^^^^

GeoTools is an open source, Java GIS toolkit for developing standards compliant solutions. Its modular architecture allows extra functionality to be easily incorporated.

GeoTools aims to support OpenGIS and other relevant standards as they are developed. GeoTools code is built using the latest Java tools and environments and will continue to leverage the capabilities of future Java environments and official extensions as and when the technologies are released and have been through the first maintenance cycle.

It is not the intention of the GeoTools project to develop finished products or applications, but it is the intention to interact and support fully other initiatives and projects which would like to use the GeoTools toolkit to create such resources.

The GeoTools project is divided into separate modules, each of which implements a specific requirement. Only a subset of these modules is usually required to build an application based on GeoTools.

Document License
^^^^^^^^^^^^^^^^^

The original wiki documentation was distributed under the free documentation license::
   
   Copyright (c) 2004-2011 Open Geospatial Foundation
   
   Permission is granted to copy, distribute and/or modify this document under the
   terms of the GNU Free Documentation License, Version 1.1 or any later version
   published by the Free Software Foundation; with the Invariant Sections being with no
   Invariant Sections, with the Front-Cover Texts being no Front-Cover Texts, and with
   the Back-Cover Texts being no Back-Cover Texts.

This generated documentation is distributed using creative commons with attribution. 

For details see http://creativecommons.org/licenses/by/3.0/ 

.. image:: /images/ccAttribution.*

Source License
^^^^^^^^^^^^^^^^^

The source code of the GeoTools library is distributed under the LGPL license::
   
   GeoTools - The Open Source Java GIS Toolkit http://geotools.org
   (C) 2004-2011, Open Source Geospatial Foundation (OSGeo)
   
   This library is free software; you can redistribute it and/or modify it under
   the terms of the GNU Lesser General Public License as published by the Free
   Software Foundation; version 2.1 of the License.
   
   This library is distributed in the hope that it will be useful, but WITHOUT
   ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
   FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.

A few select files have additional license:
* Code contributed by government members is often contributed as public domain
* To facilitate adopting of the GeoTools library the example code used in the user guide is also public domain
