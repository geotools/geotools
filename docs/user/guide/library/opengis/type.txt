FeatureType
-----------

**FeatureType** provides metadata model describing the represented information.  This is considered "metadata" as it is a description of the information stored in the features.

FeatureType is used when:

* Accessing information as a description of the available attribute names when making a Expression
* Creating a new feature you can check to ensure your values are valid

References:

* `org.opengis.feature.type <http://docs.geotools.org/stable/javadocs/org/opengis/feature/type/package-summary.html>`_
* ISO19107
* OGC General Feature Model
* `OGC Reference Model <http://portal.opengeospatial.org/files/?artifact_id=890>`_
* `OGC Features <http://portal.opengeospatial.org/files/?artifact_id=890>`_
* Geographic Markup Language
* :doc:`../main/feature` gt-main feature code examples

PropertyType
^^^^^^^^^^^^

The type is represented by PropertyType, AttributeType, GeometryType, ComplexType, FeatureType.


.. image:: /images/feature_type_model.PNG

This forms a "dynamic type system" indicating we can describe new types of information at runtime. To make this a complete type system we have support for references (with AssociationType) and methods (with OperationType) although use of these faciities is considered experimental at present.

PropertyDescriptor
^^^^^^^^^^^^^^^^^^

As shown above a ComplexType contains a list of **properties** each represented as a PropertyDescriptor with a distinct name and property type.



.. image:: /images/feature_type_model_descriptors.PNG