XMLUtils
--------

GeoTools provides an ``XMLUtils`` utility class to perform common XML factory and instantiation activities.
By providing these methods the GeoTools library can respect the factory configuration `Hints` including  `GeoTools.getDefaultEntityResolver(Hints)` configuration provided by the surrounding application.

The library is configured to use `DefaultEntityResolver` which allows access to OGC and INSPIRE schemas. It also defaults to locked down settings within the limits of the OGC Standards implemented by the library.

Reference:

* `XML External Entity Prevention Cheat Sheet <https://cheatsheetseries.owasp.org/cheatsheets/XML_External_Entity_Prevention_Cheat_Sheet.html>`__ (OWASP)

DocumentBuilderFactory and DocumentBuilder
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Use XMLUtils for DocumentBuilder construction:

.. code-block:: java

   DocumentBuilder builder = XMLUtils.newDocumentBuilder();
   Document document = builder.parse(stream);

For greater control configuration is available using DocumentBuilderFactory and Hints:

.. code-block:: java

   DocumentBuilderFactory documentBuilderFactory = XMLUtils.newDocumentBuilderFactory(hints);
   documentBuilderFactory.setNamespaceAware(namespaceAware);
   
   DocumentBuilder builder = XMLUtils.newDocumentBuilder(documentBuilderFactory);
   Document document = builder.parse(stream);

GeoTools uses the locked down ``XMLConstants.FEATURE_SECURE_PROCESSING`` when setting up document parsing.
You may carefully relax settings as needed if working with local files:

.. code-block:: java
   
   // Allow http access, with DefaultEntityResolver limiting access to OGC Schemas 
   Hints hints = GeoTools.getDefaultHints();
   hints.put(Hints.ENTITY_RESOLVER, DefaultEntityResolver.INSTANCE);
   
   DocumentBuilderFactory documentBuilderFactory = XMLUtils.newDocumentBuilderFactory(hints);
   documentBuilderFactory.setNamespaceAware(namespaceAware);
   documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "http");
   
   DocumentBuilder builder = XMLUtils.newDocumentBuilder(documentBuilderFactory);
   Document document = builder.parse(stream);

TransformerFactory and Transformer
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The same approach is taken with TransformerFactory and Transformer.

XMLTransformer:

.. code-block:: java

   InputSource gmlSource = new InputSource(uri.toString());

   DocumentBuilderFactory documentBuilderFactory = XMLUtils.newDocumentBuilderFactory(hints);
   documentBuilderFactory.setNamespaceAware(true);
   DocumentBuilder documentBuilder = XMLUtils.newDocumentBuilder(documentBuilderFactory, hints);
   Document document = documentBuilder.parse(gmlSource);
   assertNotNull(document);

   TransformerFactory txFactory = XMLUtils.newTransformerFactory(hints);
   txFactory.setAttribute("indent-number", 3);

   // Transformer
   Transformer tx = XMLUtils.newTransformer(txFactory, hints);
   tx.setOutputProperty(OutputKeys.INDENT, "yes");

   StringWriter writer = new StringWriter();
   tx.transform(XMLUtils.source(document), new StreamResult(writer));


SAXParserFactory and SAXParser
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Use XMLUtils to create SAXParserFactory and SAXParser:

.. code-block:: java

   SAXParserFactory parserFactory = XMLUtils.newSAXParserFactory(hints);
   parserFactory.setNamespaceAware(true);
   
   SAXParser parser = XMLUtils.newSAXParser(parserFactory);
   parser.parse(file, contentHandler);

Use Hints to provide factory configuration:

.. code-block:: java

   Hints hints = GeoTools.getDefaultHints();
   hints.put(Hints.ENTITY_RESOLVER, NullEntityResolver.INSTANCE);
   
   SAXParserFactory parserFactory = XMLUtils.newSAXParserFactory(hints);
   parserFactory.setNamespaceAware(true);
   parserFactory.setValidation(false);
   
   SAXParser parser = XMLUtils.newSAXParser(parserFactory);
   parser.parse(file, contentHandler);
