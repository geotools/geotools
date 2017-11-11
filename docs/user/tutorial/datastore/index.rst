:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

ContentDataStore Tutorial
=========================

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

The GeoTools project strives to support as many geographical data formats as possible. This tutorial is provided to help you hook your data directly into the GeoTools API. In order to transform
a data format into the GeoTools feature representation one must write an implementation of
the **DataStore** interface.

Once a DataStore implementation is written, any information written in that format becomes available
not only for GeoTools users, but also for projects built on top of GeoTools such as GeoServer, GeoMajas, GeoScript and uDig.

Writing a new DataStore for GeoTools is one of the best ways to get involved in the project, as
writing it will make clear many of the core concepts of the API. The modular nature of GeoTools allows new DataStores to quickly become part of the next release, so that new formats can quickly be distributed to all GeoTools users.

**Contents**

.. toctree::
   :maxdepth: 1
   
   intro
   source
   read
   store
   write
   optimisation
   qa
   strategy

.. note::
   
   AbstractDataStore is the original GeoTools 2.0 class; since that time we have learned
   a number of tricks and have a much easier starting point for you in the form of
   **ContentDataStore**.
   
   Thus far JDBCDataStore, ShapefileDataStore and WFSDataStore have each migrated to a *ng* (for next generation) implementation based on ContentDataStore. Anyone else still using AbstractDataStore should make migration plans now!

**Workshop**

This is an intermediate workshop -if you are unfamiliar with mapping concepts you may wish to try the tutorials on features, geometries and coordinate reference systems first.

Due to the length of this workshop we will create our data store in stages, trying out the functionality as we implement.
   
