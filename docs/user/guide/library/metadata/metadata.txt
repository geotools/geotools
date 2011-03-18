Metadata
--------

The MetaData interfaces can be used as straight up Java Beans, with properties and so on.

Simple Properties::
  
  public void referenceDocument( Citation citation ) {
      System.out.println( citation.getTitle() );
      System.out.println( citation.getTitle().toString( Locale.FRENCH) );
  }

Collections::
  
  System.out.println( citation.getIdentifiers() );
  System.out.println( citation.getAlternateTitles() );

Use in this manner is straightforward.

Predefined Metadata
^^^^^^^^^^^^^^^^^^^

The GeoTools library provides predefined constants for the usual suspects, using Citation as an example the usual suspects are among others:

* EPSG
* OGC
* Oracle

All of these organisations have published documentation, or specifications, that you may wish to offer as a citation when describing your own information.

The **Citations** class rounds up these constants, and a few more, for you to reuse::
  
  referenceDocument( Citations.EPSG );
  referenceDocument( Citations.OGC );
  referenceDocument( Citations.ORACLE );

These constants however are "frozen" and may not be changed.

Custom Metadata
^^^^^^^^^^^^^^^

The metadata package does not offer a factory at this time. As such we must ask you to work directly with the implementation classes.

Creating metadata is easy::
  
  CitationImpl citation = new CitationImpl();

No surprises here, a no argument constructor exists, just a regular Java Bean. Some other constructors accept an argument but they are just convenience.

You can set up using the bean properties using set methods::
  
  citation.setEditionDate( new Date() ); // today

And sometimes a bit of a chore (reading javadocs to see what is needed)::
  
  Collection parties = Collections.singleton( ResponsiblePartyImpl.GEOTOOLS );
  citation.setCitedResponsibleParties( parties );

The above method set the whole collection, discarding any previous collection for the "cited responsible parties" property. The code below is an alternative that add a responsible party without discarding the previous ones::
  
  citation.getCitedResponsibleParties().add( ResponsiblePartyImpl.GEOTOOLS );

Freeze
^^^^^^

Next we have our first trick; although you can set up metadata classes using setter methods, the result is not generally considered threadsafe.

There are three techniques commonly employed for threadsafety:

* Do nothing: and in the javadocs ask developers to only edit from one thread
* Synchronise: really safe, but you pay a performance hit when reading values from many threads
* Immutable: super safe as you can only configure the object during creation, allowing it to be quickly read from many threads at once. It is however inconvenient to setup. 

With the limitations of those two ideas in mind, the metadata has an interesting compromise:

* Freeze: set up the object in one thread, and freeze it into a read-only mode

The Freeze step "throws a switch" and keeps the object from having any further changes applied.::
  
  Citation f = (Citation) c.unmodifiable();

Note that the original code c citation stay modifiable; only the returned one is unmodifiable.

The OpenGIS interfaces are not really set up to be mutable, that is once created a lot of code expects them to never change. Hence the freeze step, it works better then the honour system.

Metadata WKT
^^^^^^^^^^^^

WKT is actually "Well Known Text" and don't worry nobody knows this the first time.

Not all meatadata beans have a WKT representation, the ones used in defining a CoordianteReferenceSystem are generally well behaved.::
  
  CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
  System.out.println( crs.toWKT() );

Scary I know, CorodinateReferenceSystems are actually metadata - in this case the define the meaning of all those coordinates we shunt around a GIS system.

Metadata ISO19115
^^^^^^^^^^^^^^^^^

Metadata is often stored as XML files based on ISO 19115. GeoTools
does not have a parser for these documents at this time.

If you are interested in volunteering or funding this work please
talk to us on the development list.

Metadata Database
^^^^^^^^^^^^^^^^^

The metadata module provides support for working with metadata entries stored in a database.

Any JDBC database will do, and the detailed configurations are in the javadocs.

Once again please consult the javadocs for details::
  
  Connection     connection = ...
  MetadataSource source = new MetadataSource(connection);
  Telephone telephone  = (Telephone) source.getEntry(Telephone.class, id);

