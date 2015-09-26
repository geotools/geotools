############################
VPF DataStore Implementation
############################

This implementation is currently unsupported, and indeed incomplete. If you are interested
in the VPF format please contact the geotools-devel email list - we would be glad to hear
from you.

Headers indicate this work was originally performed by Ionic in 2003.

TODO:

1. Refactor VPFFile and VPFColumn to allow use of default feature model implementation, and
   support GeometryDescriptor, providing appropriate accessors as required.

   * VPFFile `<>---` SimpleFeatureType
   * VPFColumn `<>---` AttributeDescriptor `<>----` AttributeType
   * VPFColumn `<>---` GeometryDescriptor `<>----` GeometryType
   
   This fix will allow tests to run and confirm functionality of the codebase.
   
2. Implement VPFFileFactory as an stand-alone DataStoreFactorySPI
   
   This fix will allow the existing implementation to be used in an application such as GeoServer.

3. Resolve duplication between VPFDataStoreFactory and VPFFileFactory implementations.

   If VPFFileFactory is to remain in use it will need to correctly follow the GeoTools API contract.

4. It would be wise to migrate to the superclass ContentDataStore (provides a clean baseclass
   with less code then the currently used AbstractDataStore).

Code Review
===========

VPFFile
-------

The VPFFile and VPFColumn directly extended the GeoTools 2.0 feature model:

* VPFFile `<|---` FeatureType
* VPFColumn `<|---` AttributeType

An incomplete migration to the GeoTools 2.4.x feature model was performed during a FOSS4G code sprint:

* VPFFile `<|---` FeatureType
* VPFFeatureType `<|---` SimpleFeatureType
* VPFColumn `<|---` AttributeDescriptor `<>----` AttributeType

This incomplete migration has resulted in the following issues:

* The VPF codebase assumes the above are interchangeable, resulting in class cast exceptions on occasion.
* The GeoTools codebase has only been tested with the default implementation of FeatureType and AttributeDescriptor. T
  This results in a brittle experience when integrating the VPF Format with library components such as reprojection
  and rendering.
* The feature model uses both AttributeDescriptor and GeometryDescriptor to describe published attributes. The implementation
  of VPFColumn cannot publish a GeometryDescriptor and is prevented from advertising spatial data to clients.

DataStore implementations
-------------------------

META-INF/services advertises VPFDataStoreFactory for use in DataStoreFactory Finder.

* VPFDataStoreFactory `----->` VPFLibrary
  
  VPFLibrary correctly lists the available feature types in initial testing.
  
  Code predates CoordinateReferenceSystem being mandatory
  (VPFLibrary.getCoordinateReferenceSystem() assume WGS84 and VPFFile does not implement).
  
* VPFFileFactory `----->` VPFFileStore

  VPFFileFactory is a singleton, acting as a container for a single copy of VPFFile. This design predates GeoTools 2, which
  expects applications to manage individual DataStore (in a catalog or registry). For this approach to work factories
  are expected to stateless and responsible for creating an independent DataStore instance on request.
  
  With this assumption in mind DataStoreFactories are instantiated once, and managed by DataStoreFinder.
  
  The factory singleton approach prevents more than one VPF file being used when accessing
  the format through DataStoreFactoryFinder.
  
  *VPFFileStore* does not correctly implement the DataStore contact (listing content for a user to open).
  The list of published feature types is populated in a lazy fashion via requests to getSchema( typeName ).
  
  This limitation requires that users know the internal structure of the data, through prior knowledge, before
  accessing. This assumption does not match up with GeoServer which needs to know the avaialble data in order
  to allow users to select and publish specific layers of content.

Research
--------

Q: Is the implementation streaming?

This is important to allow the use of large files off disk, without loading into memory.

Implementation of VPFFileFeatureReader delegates to VPFFile which appears to support streaming.

Q: Does the implementation support a spatial index?

This is a desirable optimization, allowing rendering without traversing the entire file.

The internals of VPFFile make use of a readFeature method and contain no reference to a bounding box filter
or otherwise. The only sign of hope is the internal use of a RandomAccessFile leading me to believe we
could read individual features in isolation, if a spatial index is available to shortlist likely candidates.

The website http://www.digitalpreservation.gov/formats/fdd/fdd000302.shtml indicates the following spatial index
files may be available:

* csi, Connected Node Spatial Index
* esi, Edge Spatial Index
* fsi, Face Spatial Index
* nsi, Entity Node Spatial Index
* tsi, Text Spatial Index

The codebase has SpatialIndexHeader and SpatialIndexInputStream indicating that some support is in fact available.

