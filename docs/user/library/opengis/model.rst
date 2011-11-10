Model
-----

GeoTools provides a clear separation between:

* data model - **feature** responsible for holding values
* query model - **filter** and **expression** used to select content and drill in and retrieve values
* metadata model - **feature type** describing contents in sufficient details for validation and query construction

References:

* :doc:`type`
* :doc:`feature`
* :doc:`gt-main feature <../main/feature>` code examples
* :doc:`gt-main filter <../main/filter>` code examples

Comparison to Java:

* The data structure Feature is used to hold information. Each Feature "belongs to" a FeatureType which is used to describe valid contents. This is a dynamic type system because FeatureType is a data structure we can define at runtime.
* The data structure Object is used to hold information. Each Object "belongs to" a Class which is used to describe valid contents. This is a static type system because Class is a data structure we need to compile before the application is started.

========================= ============================================= =======================================
Java Class System         GeoTools Feature Model                        Java Beans System
========================= ============================================= =======================================
static type system        dynamic type system                           dynamic type system
Object                    SimpleFeature                                 Object
(reflection)              Attribute                                     (reflection)
(reflection)              GeometryAttribute                             (reflection)
Class                     SimpleFeatureType                             BeanInfo
Field	                     AttributeDescriptor                           PropertyDescriptor
Field                     GeometryAttributeDescriptor                   PropertyDescriptor
Method                    OperationType                                 MethodDescriptor
Field.getType()           AttributeDescriptor.getType().getBinding()    PropertyDescriptor.getPropertyType()
Field.getName()           AttributeDescriptor.getName().getLocalName()  PropertyDescriptor.getName()
Field.get( obj )          expression.evaulate( feature, Class )         descriptor.getReadMethod().invoke(obj)
========================= ============================================= =======================================

Java example:

  .. literalinclude:: /../src/main/java/org/geotools/opengis/FeatureExamples.java
     :language: java
     :start-after: // javaFlag start
     :end-before: // javaFlag end

Feature example:

  .. literalinclude:: /../src/main/java/org/geotools/opengis/FeatureExamples.java
     :language: java
     :start-after: // featureFlag start
     :end-before: // featureFlag end
