Configuration
^^^^^^^^^^^^^

A configuration is an object that is used to configure the parser or the encoder. A configuration is used to:

* Load bindings into the parser / encoder
* Create the binding context
* Locate schema while parsing an instance document
* Declare depenendencies on other configurations / schemas

More specifically a configuration is an instance of **org.geotools.xml.Configuration**.

Schema Location
'''''''''''''''

A configuration has a one-to-one correspondence to a particular schema. The configuration references two pieces of information about the schema:

1. Namespace uri of the schema
2. Location of the \*.xsd file which defines the schema

Here is what that looks like::
  
  interface Configuration {
       ...
       
      /**
       * @return The namespace of the configuration schema.
       */
      String getNamespaceURI();
       
      /**
       * @return the uri to the file definiing hte schema.
       */
      String getSchemaLocation();
      
      ...
  }

* Namespace URI::
    
    String getNamespaceURI();
  
  The getNamespaceURI method takes no parameters and returns a string which is the namespace uri of the schema.
  
  An example implementation for the purchase order schema::
    
    String getNamespaceURI() {
        return "http://www.geotools.org/po";
   }

* Schema Location::
     
     String getSchemaLocation();
  
  The getSchemaLocation method takes no parameters and returns a URI to a \*.xsd file for the schema. It can be any valid uri, local or remote, however it must be an absolute URI.
  
  * A common practice is to store a copy of the schema in the same package relative to the configuration. An example implementation for the purchase order schema::
      
      String getSchemaLocation() {
        //relative to this class
        return getClass().getResource( "po.xsd" ).toString();
      }
  
  * An implementation referring to a remote location::
      
      String getSchemaLocation() {
         //remote uri
         return "http://schemas.geotools.org/po/po.xsd";
      }
  
  * Or a location on disk::
      
      String getSchemaLocation() {
         //local uri
         return "file:///home/bob/po.xsd"
      }
  
  This location is used by the parser to actually process the schema while parsing an instance document. For schemas which are made up of multiple \*.xsd files, this value should be to the top level file, ie. the file which includes all other files.

Binding Configuration
'''''''''''''''''''''

A binding configuration is a set of name, binding mappings used to load bindings for particular elements, attributes, types. More specifically, a binding configuration is an instance of **org.geotools.xml.BindingConfiguration**::
  
  interface BindingConfiguration {
    /**
     * Configures the container which houses the bindings.
     *
     */
    void configure(MutablePicoContainer container);
  }

The single method configure is used to populate a container with a set of bindings. Each binding is registered under a key. The key is the qualified name (javax.xml.namespace.QName) of the element, attribute, or type the binding is bound to. Example::
  
  class PurchaseOrderBindingConfiguration implements BindingConfiguration {
  
    void configure( MutablePicoContainer container ) {
      
      container.registerComponentImplementation( new QName( "http://www.geotools.org/po", "PurchaseOrderType" ), PurchaseOrderTypeBinding.class );
      container.regsiterComponentImplementation( new QName( "http://www.geotools.org/po", "USAddress" ), USAddressBinding.class ); 
      ...      
    }
  }

SchemaLocationResolver and SchemaLocator
''''''''''''''''''''''''''''''''''''''''

A configuration maintains a reference to two "helper" classes::
     
     /**
       * Returns a schema location resolver instance used to override schema location
       * uri's encountered in an instance document.
       */
      XSDSchemaLocationResolver getSchemaLocationResolver();
      
      /**
       * Returns a schema locator, used to create imported and included schemas
       * when parsing an instance document.
       */
      XSDSchemaLocator getSchemaLocator();

These interfaces are used internally by the eclipse XSD library to parse schemas that are referenced by an instance document.

* XSDSchemaLocationResolver
  
  The job of this interface is to "resolve" a schema location from a namespace uri, and possible partial schema location. Often in an instance document schema references of the following form are encountered.::
    
    <purchaseOrder xsi:schemaLocation="http://www.geotools.org/po po.xsd">
    ...
  
  The above specifies that the purchase order schmea is defined in a file called "po.xsd". That is great, but where can the parser find the "po.xsd" file. If the reference to the file was an absolute reference it would be a different story, but sadly this is often not so. Luckily this is where the XSDSchemaLocationResolver comes in.
  
  Given a namespaceURI, and partial schema location, an instance of this interface must return an "absolute" reference to the file defining the schema. By default, the Configuration class looks in the same package as itself for a resource which matches the schema file name.
  
  This behaviour may be changed by overriding the getSchemaLocationResolver() method of the Conifguration class.

* XSDSchemaLocator
  
  The job of this interface is slightly different from that of hte XSDSchemaLocationResolver interface. Instead of returning the absolute resolved location for a schema, it returns the actual schema itself.
  
  By default, the Configuration class will return an instance of XSDSchemaLocator which uses the getSchemaLocation() method to return a schema. Subclasses may wish to override to change this behaviour.

Configuration Dependency
''''''''''''''''''''''''

Just like a schema has dependencies on other schemas, a configuration has dependencies on other configurations. Not surprisingly the configuration dependencies mirror the schema dependencies.

Configuration dependencies are added with the addDependency method.::
  
  protected void addDependency( Configuration dependency );

The method should be called from the constructor of a subclass. As an example, the filter schema has a dependency on the gml schema::
  
  class OGCConfiguration extends Configuration {
    
    public OGCConfiguration() {
      //dependency on gml
      addDependency( new GMLConfiguration() );
    }
  }
