Process Feature Plugin
----------------------

The gt-process-feature plugin gathers up a number of high quality processes for working with
vector information.

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-process-feature</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Transform
^^^^^^^^^

Transform a feature collection using a series of expressions.

The definition of the output feature type can be provided as a Definition data structure or using a simple string format::
  
  the_geom=the_geom
  name=name
  area=area( the_geom )
 
This is a very flexible process which can be used to:

* Change the order of attributes - resulting in a new feature type::
     
     INPUT Schema          DEFINITION                     OUTPUT Schema
     the_geom: Polygon     the_geom                       the_geom: Polygon
     name: String          id                             id: Long
     id: Long              name                           name: String
     description: String   description                    description: String
 
* Rename or remove attributes - resulting in a new feature type::

     INPUT Schema          DEFINITION                     OUTPUT Schema
     the_geom: Polygon     the_geom                       the_geom: Polygon
     name: String          id_code=id                     id_code: Long
     id: Long              summary=description            summary: String
     description: String
 
* Process geometry - using functions like "the_geom=simplify( the_geom, 2.0 )" or "the_geom=centriod( the_geom )"::

     INPUT Schema          DEFINITION                     OUTPUT Schema
     the_geom: Polygon     the_geom=centriod(the_geom)    the_geom: Point
     name: String          name                           name: String
     id: Long              id                             id: Long
     description: String   description                    description: String
 
* Generate additional attributes using the form: area=area( the_geom )::

     INPUT Schema          DEFINITION                     OUTPUT Schema
     the_geom: Polygon     the_geom=centriod(the_geom)    the_geom: Point
     name: String          name                           name: String
     id: Long              id                             id: Long
     description: String   description                    description: String
                           area=area( the_geom)           area: Double
                           text=concatenate(name,'-',id)  text: String
 