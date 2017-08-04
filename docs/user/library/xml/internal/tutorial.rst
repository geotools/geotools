Purchase Order Tutorial
^^^^^^^^^^^^^^^^^^^^^^^

This tutorial covers how to implement XML support for a particular schema.

Prerequisites at the time of writing:

* Maven 3
* Build GeoTools trunk locally; the gt2-xmlcodegen maven plugin is not published anywhere - you need to build it

Create a New Project
''''''''''''''''''''
Somewhere on your system:

1. Create a new directory called xml-po
2. Add the the following **pom.xml** to the xml-po directory

   .. literalinclude:: /artifacts/xml/pom1.xml

3. Create the directories src/main/java and src/main/xsd::

     mkdir -p src/main/java
     mkdir -p src/main/resources
     mkdir -p src/test/java

4. Copy :download:`po.xsd <../../../artifacts/xml/po.xsd>` into src/main/resources

   .. literalinclude:: ../../../artifacts/xml/po.xsd

Generate the Model
''''''''''''''''''

The first step is to come up with an object model to work with. There are a number of technologies that will take an XML schema and generate Java code from it. To name a few:

* Eclipse Modelling Framework
* Java Architecture for XML Binding (JAXB)
* XML Beans

For this tutorial we will use JAXB.

First we must configure the pom.xml file of our project to use the `maven-jaxb2-plugin <http://java.net/projects/maven-jaxb2-plugin/>`_ plugin, and also depend on jaxb as it will add some annotations.

.. note::

   We collected our :download:`po.xml </artifacts/xml/po.xml>` schema from the sample projected available here:

   * http://confluence.highsource.org/display/MJIIP/Sample+projects

   You may wish to download a newer copy of the instructions if they seem out of date.

1. Add a dependency on jaxb api:

.. literalinclude:: /artifacts/xml/pom2.xml
   :start-after: </name>
   :end-before: <build>

2. Configure the jaxb maven plugin:

   .. literalinclude:: /artifacts/xml/pom2.xml
      :start-after: </dependencies>
      :end-before: </project>

   The above configuration targets our project source directory;
   as we are only planning on running this generation code once.

3. At this point, your pom should look like this :download:`pom.xml </artifacts/xml/pom2.xml>` .

4. Now we are ready to generate the model.

   Run the jaxb2 code generation::

     [xml-po]$ mvn jaxb2:xjc
     [INFO] Scanning for projects...
     [INFO]
     [INFO] ------------------------------------------------------------------------
     [INFO] Building Purchase Order XML Support 0.0.1-SNAPSHOT
     [INFO] ------------------------------------------------------------------------
     [INFO]
     [INFO] --- jaxb2-maven-plugin:2.3:xjc (default-cli) @ xml-po ---
     [INFO] Adding 'extension' flag to XJC arguments, since the 'generateEpisode' argument is given. (XJCs 'episode' argument requires that the 'extension' argument is provided).
     [INFO] Ignored given or default xjbSources [/Users/vickdw/Documents/workspace/xml-po/src/main/xjb], since it is not an existent file or directory.
     [INFO] ------------------------------------------------------------------------
     [INFO] BUILD SUCCESS
     [INFO] ------------------------------------------------------------------------
     [INFO] Total time: 0.998 s
     [INFO] Finished at: 2017-01-10T08:09:37-06:00
     [INFO] Final Memory: 12M/303M
     [INFO] ------------------------------------------------------------------------

5. Build the project::

     [xml-po]% mvn install

6. At this point, the model should have been generated under src/main/java.

   * Items.java
   * ObjectFactory.java
   * PurchaseOrderType.java
   * USAddress.java

Generate the Bindings
'''''''''''''''''''''

At this point, we have generated objects from the original schema. The next step will be to generate XML bindings from the original schema. To do this we will make use of the geotools XML generation plugin.

1. Add a dependency on the geotools xml module:

   .. literalinclude:: /artifacts/xml/pom3.xml
      :start-after: <dependencies>
      :end-before: </dependencies>

2. Configure the xml generation plugin:

   .. literalinclude:: /artifacts/xml/pom3.xml
      :start-after: <!--  geotools generator -->
      :end-before: </plugins>

3. A couple of things to note about the configuration:

   * The schemaLocation tag defines the target schema, in this case po.xsd.
   * The destinationPackage defines which package we would like the generate
      code to end up in, in this case org.geotools.po.bindings
   * The constructorArguments tag defines the constructor arguments we want our
     xml bindings to take, in this case org.geotools.po.ObjectFactory which is
     a factory that was generated in our model in the previous section

4. At this point, your pom should look like this :download:`pom.xml </artifacts/xml/pom3.xml>` .

5. Now we are ready to generate the bindings.

   Run the XML binding generator::

    [INFO] Scanning for projects...
    [INFO]
    [INFO] ------------------------------------------------------------------------
    [INFO] Building Purchase Order XML Support 0.0.1-SNAPSHOT
    [INFO] ------------------------------------------------------------------------
    [INFO]
    [INFO] --- xmlcodegen:17-SNAPSHOT:generate (default-cli) @ xml-po ---
    [INFO] org.apache.maven.artifact.factory.DefaultArtifactFactory@1ddae9b5
    [INFO] Parsing schema: /Users/vickdw/Documents/workspace/xml-po/src/main/resources/po.xsd
    Downloading: https://repo.boundlessgeo.com/main/org/geotools/geotools/17-SNAPSHOT/maven-metadata.xml
    Downloaded: https://repo.boundlessgeo.com/main/org/geotools/geotools/17-SNAPSHOT/maven-metadata.xml (572 B at 0.5 KB/sec)
    Downloading: https://repo.boundlessgeo.com/main/org/geotools/geotools/17-SNAPSHOT/geotools-17-20170109.184824-56.pom
    Downloaded: https://repo.boundlessgeo.com/main/org/geotools/geotools/17-SNAPSHOT/geotools-17-20170109.184824-56.pom (71 KB at 182.0 KB/sec)
    [INFO] Generating bindings...
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for comment
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for purchaseOrder
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for Items
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for PurchaseOrderType
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for SKU
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for USAddress
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    INFO: Generating binding for Items_item
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.AbstractGenerator copy
    INFO: Copying /Users/vickdw/Documents/workspace/xml-po/src/main/resources/po.xsd to /Users/vickdw/Documents/workspace/xml-po/src/main/resources/org/geotools/po/bindings/po.xsd
    Jan 10, 2017 8:38:03 AM org.geotools.maven.xmlcodegen.BindingGenerator generate
    WARNING: Could not copy file /Users/vickdw/Documents/workspace/xml-po/src/main/resources/po.xsd
    java.io.FileNotFoundException: /Users/vickdw/Documents/workspace/xml-po/src/main/resources/org/geotools/po/bindings/po.xsd (No such file or directory)

    [INFO] ------------------------------------------------------------------------
    [INFO] BUILD SUCCESS
    [INFO] ------------------------------------------------------------------------
    [INFO] Total time: 4.092 s
    [INFO] Finished at: 2017-01-10T08:38:03-06:00
    [INFO] Final Memory: 15M/291M
    [INFO] ------------------------------------------------------------------------

5. At this point, the bindings should have been generated under src/main/java::

     [xml-po]% ls src/main/java/org/geotools/po/bindings

     ItemsBinding.java            POSchemaLocationResolver.java
     Items_itemBinding.java       po.xsd
     POBindingConfiguration.java  PurchaseOrderTypeBinding.java
     POConfiguration.java         SKUBinding.java
     PO.java                      USAddressBinding.java

6. Some explanation of the generated files is in order:

   * Binding.java

     These are the bindings themselves, there should be one for every type in the purchase order schema
   * PO.java

     An interface which contains constants for the qualified names of attributes, elements, and types defined in the schema
   * POConfiguration

     The parser configuration used to prepare the xml parser for parsing an instance of the schema
   *  POSchemaLocationResolver.java

      Helper class used by the configuration to locate our schema file while parsing
   * POBindingConfiguration.java

     Helper class used by the configuration to register the generated bindings with the parser


Implement the Bindings
''''''''''''''''''''''

The next step is to implement the bindings, which are at this point contain stub methods.

1. At this time it will be useful to switch to your development environment. The following steps can be used by Eclipse users:

   Run the command::

     [xml-po]% mvn eclipse:eclipse
     [INFO] Scanning for projects...
     [INFO] Searching repository for plugin with prefix: 'eclipse'.
     [INFO] ------------------------------------------------------------------------
     [INFO] Building Purchase Order XML Support
     [INFO]    task-segment: [eclipse:eclipse]
     [INFO] ------------------------------------------------------------------------
     [INFO] Preparing eclipse:eclipse
     [INFO] No goals needed for project - skipping
     [INFO] [eclipse:eclipse {execution: default-cli}]
     [INFO] Using Eclipse Workspace: null
     [INFO] Adding default classpath container: org.eclipse.jdt.launching.JRE_CONTAINER
     [INFO] Wrote settings to /Users/jody/java/geotools/xml-po/.settings/org.eclipse.jdt.core.prefs
     [INFO] Wrote Eclipse project for "gt2-xml-po" to /Users/jody/java/geotools/xml-po.
     [INFO]

2. The command will produce a few warnings were it cannot find source or
   javadoc files but it will generate a .project and .classpath allowing you to
   import the project into eclipse.

   .. image:: /images/xml/eclipseImport.png

.. note::
    You may get a compile error from Eclipse noting `The method parse(InstanceComponent, Object) in the type AbstractSimpleBinding is not applicable for the arguments (ElementInstance, Node, Object) PurchaseOrderBinding.java /xml-po/src/main/java/org/geotools/po/bindings  line 60 Java Problem`

    This is easily solved by changing PurchaseOrderBinding to exend AbstractComplexBinding instead of the generated AbstractSimpleBinding.

3. We will take a bottom approach to implementation of the bindings, that is starting with bindings for types that are referenced by other types.

SKU (Simple Type)
'''''''''''''''''

1. We will start by implementing the binding for the SKU type in our schema::

     <xsd:simpleType name="SKU">
       <xsd:restriction base="xsd:string">
         <xsd:pattern value="\d{3}-[A-Z]{2}"/>
       </xsd:restriction>
     </xsd:simpleType>

2. From the schema definition, we see that the SKU type is a Simple Type,
   which extends xsd:string. In the realm of xml schema, the following diagram
   depicts the inheritance hierachy of the SKU type:

   .. image:: /images/xml/skuHierachy.png

3. The parser takes this hierachy into account when executing bindings. The
   bindings are executed in order from the top down. The output of each binding
   is passed on to the next binding in the chain. The following diagram depicts
   the binding execution chain:

   .. image:: /images/xml/skuExecutionChain.png

4. Implement the parse method

   So with that explanation, we can implement the SKUBinding.parse method::

      Object parse( InstanceComponent instance, Object value ) {
         return null;

      }

   The value parameter is the parsed value that has been passed down from the
   parent ( xsd:string ), which is an instance of java.lang.String, so we can
   cast accordingly.::

      Object parse( InstanceComponent instance, Object value ) {
         String sku = (String) value;

         return sku;
      }

   At this point we could call it the end of the day, but the type definition
   for SKU constrains the value space with a pattern facet. So could also add a
   check for the pattern match::

      Object parse( InstanceComponent instance, Object value ) {
         String sku = (String) value;

         if ( !sku.matches( "\\d{3}-[A-Z]{2}" ) ) {
              throw new IllegalArgumentException( "Illegal sku format: " + sku );
         }
         return sku;
      }

5. Implement the getType method

   The last thing to do in implementing the SKU binding is to declare the type
   of the object that it returns. From the implementation parse it is clear that
   this is java.lang.String.::

     Class getType() [
       return String.class;
     }

6. You can download completed :download:`SKUBinding.java </../src/main/java/org/geotools/po/bindings/SKUBinding.java>` .

Comment (Simple Type)
'''''''''''''''''''''

1. We will start by implementing the binding for the Comment type in our schema::

    <xsd:element name="comment" type="xsd:string"/>

2. Implement the parse method::

    Object parse(InstanceComponent instance, Object value) throws Exception {
      String comment = (String) value;
      return comment;
    }

3. Implement the getType method::

    Class getType() {
      return String.class;
    }

4. You can download completed :download:`CommentBinding.java </../src/main/java/org/geotools/po/bindings/CommentBinding.java>` .

USAddress (Complex Type)
''''''''''''''''''''''''

1. Next we will move on to implement a binding for the ComplexType USAddress::

     <xsd:complexType name="USAddress">
       <xsd:sequence>
         <xsd:element name="name"   type="xsd:string"/>
         <xsd:element name="street" type="xsd:string"/>
         <xsd:element name="city"   type="xsd:string"/>
         <xsd:element name="state"  type="xsd:string"/>
         <xsd:element name="zip"    type="xsd:decimal"/>
       </xsd:sequence>
       <xsd:attribute name="country" type="xsd:NMTOKEN" fixed="US"/>
     </xsd:complexType>

2. Complex bindings follow the same execution chain as simple types in which a
   parsed value is passed down the chain into the parse method. The parse
   method for a complex binding is a little different then the parse method for
   a simple binding::

     Object parse(ElementInstance instance, Node node, Object value) throws Exception {
       //TODO: implement
       return null;
     }

3. The difference is the addition of the node parameter. A Node instance
   contains the parsed values of all elements and attributes contained within
   the complex type. The node API is map like in which element and attribute
   values can be looked up by name::

     interface Node {
          Object getChildValue( String name );
          Object getAttributeValue( String name ):
     }

  So in implementing the binding for USAddress, we will make use of the node to
  obtain the properties of the address.

4. Implement the parse method

   The first step is to create a new instance of USAddress. To do so we make
   use of the org.geotools.po.ObjectFactory. If you remember earlier when we
   configured the binding generator, we specified that each binding should take
   one of these factories in its constructor. Well know we will put it to use.::

      Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        USAddress address = factory.createUSAddress();
        return address;
      }

5. Next, we must get the values for each of the elements and attributes defined
   in the XML schema type for USAddress from the node parameter::

      Object parse(ElementInstance instance, Node node, Object value) throws Exception {
         USAddress address = factory.createUSAddress();

         //elements
         address.setName( (String) node.getChildValue( "name" ) );
         address.setStreet( (String) node.getChildValue( "street" ) );
         address.setCity( (String) node.getChildValue( "city" ) );
         address.setState( (String) node.getChildValue( "state" ) );
         address.setZip( (BigDecimal) node.getChildValue( "zip" ) );

         //attribute
         address.setCountry( (String) node.getAttributeValue( "country" ) );

         return address;
      }

6. Implement the getType method

   Same as with simple types above, complex bindings must declare the type of
   object they create. An so::

      Class getType() {
         return USAddress.class;
      }

7. You can download a complete :download:`USAddressBinding.java </../src/main/java/org/geotools/po/bindings/USAddressBinding.java>`

Item ( Anonymous Complex Type )
'''''''''''''''''''''''''''''''
Moving along we encounter the Items type. The schema for the type is relatively straight forward, it is an collection of item elements::

  <xsd:complexType name="Items">
    <xsd:sequence>
      <xsd:element name="item" minOccurs="0" maxOccurs="unbounded">
        <xsd:complexType>
          <xsd:sequence>
            <xsd:element name="productName" type="xsd:string"/>
            <xsd:element name="quantity">
              <xsd:simpleType>
                <xsd:restriction base="xsd:positiveInteger">
                  <xsd:maxExclusive value="100"/>
                </xsd:restriction>
              </xsd:simpleType>
            </xsd:element>
            <xsd:element name="USPrice"  type="xsd:decimal"/>
            <xsd:element ref="comment"   minOccurs="0"/>
            <xsd:element name="shipDate" type="xsd:date" minOccurs="0"/>
          </xsd:sequence>
          <xsd:attribute name="partNum" type="SKU" use="required"/>
        </xsd:complexType>
      </xsd:element>
    </xsd:sequence>
  </xsd:complexType>

However, the element "item" itself provides a small wrinkle: it has an Anonymous Inline Type Definition.

1. The convention used for the binding append the element name to the name of the parent.

   In this case "Items_item".::

      Class getType() {
              return Items.Item.class;
      }

      Object parse(ElementInstance instance, Node node, Object value) throws Exception {

            Items.Item item = factory.createItemsItem();

            //elements
            item.setProductName( (String) node.getChildValue( "productName") );
            item.setQuantity( (BigInteger) node.getChildValue( "quantity" ) );
            item.setUSPrice( (BigDecimal) node.getChildValue( "USPrice" ) );item.setComment( (String) node.getChildValue( "comment" ) );

            //attributes
            item.setPartNum( (String) node.getAttributeValue( "partNum" ) );

            return item;
      }

2. You can download a complete :download:`Items_itemBinding.java </../src/main/java/org/geotools/po/bindings/Items_itemBinding.java>`

Items ( Another Complex Type )
''''''''''''''''''''''''''''''

Items is just a container for multiple Item elements.

1. In implementing we make use of the getChildValues() method of Node, which returns a collection of child elements corresponding to a particular name.::

      Class getType() {
             return Items.class;
      }

      Object parse(ElementInstance instance, Node node, Object value) throws Exception {

            //create the items collection
            Items items = factory.createItems();

            //add each item
            items.getItem().addAll( node.getChildValues( "item" ) );

            return items;
      }

2. You can download a complete :download:`ItemsBinding.java </../src/main/java/org/geotools/po/bindings/ItemsBinding.java>`

PurchaseOrder ( Another Complex Type )
''''''''''''''''''''''''''''''''''''''

1. PurchaseOrder is a complex type based upon the complex type PurchaseOrderType::

    <xsd:element name="purchaseOrder" type="PurchaseOrderType"/>

    Class getType() {
      return PurchaseOrderType.class;
    }

    Object parse(ElementInstance instance, Node node, Object value) throws Exception {
      PurchaseOrderType po = new PurchaseOrderType();

      po.setBillTo((USAddress) node.getChildValue("billTo"));
      po.setShipTo((USAddress) node.getChildValue("shipTo"));
      po.setItems((Items) node.getChildValue("items"));
      po.setComment((String) node.getChildValue("comment"));
      po.setOrderDate((XMLGregorianCalendar) node.getAttributeValue("orderDate"));

      return po;
    }

2. You can download a complete :download:`PurchaseOrderBinding.java </../src/main/java/org/geotools/po/bindings/PurchaseOrderBinding.java>`

PurchaseOrderType( The Last One )
'''''''''''''''''''''''''''''''''

Onto the final binding to implement PurchaseOrderType.

1. Compared to the binding for Items it is simple::

      Class getType() {
            return PurchaseOrderType.class;
      }

      Object parse(ElementInstance instance, Node node, Object value)  throws Exception {

            PurchaseOrderType purchaseOrder = factory.createPurchaseOrderType();

            purchaseOrder.setShipTo( (USAddress) node.getChildValue( "shipTo" ) );
            purchaseOrder.setBillTo( (USAddress) node.getChildValue( "billTo" ) );
            purchaseOrder.setComment( (String) node.getChildValue( "comment" ) );
            purchaseOrder.setItems( (Items) node.getChildValue( "items") );

            return purchaseOrder;
      }

2. You can download a complete :download:`PurchaseOrderTypeBinding.java </../src/main/java/org/geotools/po/bindings/PurchaseOrderTypeBinding.java>`

All Done!! ... for now.

And thats that for implementing the bindings. In the continuing sections we will focus on configuring the parser in order to hook up the bindings implemented in this section.

Configure the Binding Context
'''''''''''''''''''''''''''''

You should recall above our discussion about injecting all of our bindings with an instance of org.geotools.po.ObjectFactory. But where does this come from? The answer is the Binding Context. The Binding Context is a container ( PicoContainer ) in which all the bindings are created in. The container uses Dependency Injection to satisfy binding dependencies. Therefore anytime a binding requires a factory or some other class to do work, it just has to declare one in its constructor.

But, these dependencies have to be placed in the container at some point. This is done by the Configuration class. So in our case we would like to place an instance of org.geotools.po.ObjectFactory in the context. We do this by implementing the configureContext method in POConfiguration.::

      class POConfiguration {
           ...

           protected void configureContext(MutablePicoContainer context) {
              context.registerComponentImplementation( ObjectFactory.class );
           }
      }

The complete implementation of POConfiguration can be found here.

Test the Bindings
'''''''''''''''''

Now that the bindings are implemented, it is time to do some testing.

1. Add the junit dependency to the pom::

      <dependency>
          <groupId>junit</groupId>
          <artifactId>junit</artifactId>
          <version>4.11</version>
          <scope>test</scope>
      </dependency>

2. Execute the following command once again: ( If you are using Eclipse )::

     [xml-po]% mvn eclipse:eclipse

3. Switch to your ide and create a new test class: POTest ( remember that test cases live under src/test/java in the project )::

      class POTest extends TestCase {

      }

4. Next, create a test method called test, which will build up a dom tree for an USAddress element.::

      public void test() throws Exception {

        InputStream input = getClass().getResourceAsStream( "po.xml" );
        POConfiguration configuration = new POConfiguration();

        Parser parser = new Parser( configuration );
        PurchaseOrderType po = (PurchaseOrderType) parser.parse( input );

        assertNotNull( po );
      }

5. The complete implementation of POTest can be found here.

6. To run the test we will create a test resource directory::

      [xml-po]% mkdir -p src/test/resources/org/geotools/po

7. Copy po.xml into the new directory
8. Run the test from your IDE
