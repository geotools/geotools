Wizard
------

Wizards are dialogs that prompt for user inputs, possibly with a sequence of dialog pages (think Next and Back buttons). They also provide validation of the inputs.

Swing doesn't have a wizard class so GeoTools has filled this void with JWizard.

JDataStoreWizard
^^^^^^^^^^^^^^^^

Quickly collect connection parameter information for a DataStore:

The DataStore api makes use of a list of **Parameter**s to describe valid connection parameters. You can make use of **JDataStoreWizard** to prompt the user for appropriate values.

* You can allow your user to select the kind of DataStore they want to use::
  
        JDataStoreWizard wizard = new JDataStoreWizard();
        int result = wizard.showModalDialog();
        if (result == JWizard.FINISH) {
            Map<String, Object> connectionParameters = wizard.getConnectionParameters();
            dataStore = DataStoreFinder.getDataStore(connectionParameters);
            if (dataStore == null) {
                JOptionPane.showMessageDialog(null, "Could not connect - check parameters");
            }
        }
  
  Allowing your users for example to work with PostGIS:
  
  .. image:: /tutorial/filter/images/postgisWizard1.png
     :scale: 60

* You can limit your user to a specific kind of file extension::
        
        JDataStoreWizard wizard = new JDataStoreWizard("shp");
        int result = wizard.showModalDialog();
        if (result == JWizard.FINISH) {
            Map<String, Object> connectionParameters = wizard.getConnectionParameters();
            dataStore = DataStoreFinder.getDataStore(connectionParameters);
            if (dataStore == null) {
                JOptionPane.showMessageDialog(null, "Could not connect - check parameters");
            }
        }
  
  Presented as as a two page wizard with file selection:
  
  .. image:: /tutorial/filter/images/shapeWizard1.png
     :scale: 60
  
  And advanced settings:
  
  .. image:: /tutorial/filter/images/shapeWizard2.png
     :scale: 60

* Or you can prompt for a specific datastore; such as ShapefileDataStoreFactory::

        DataStoreFactorySpi format = new ShapefileDataStoreFactory();
        JDataStoreWizard wizard = new JDataStoreWizard(format);
        int result = wizard.showModalDialog();
        if (result == JWizard.FINISH) {
            Map<String, Object> connectionParameters = wizard.getConnectionParameters();
            dataStore = DataStoreFinder.getDataStore(connectionParameters);
            if (dataStore == null) {
                JOptionPane.showMessageDialog(null, "Could not connect - check parameters");
            }
        }

JParameterListWizard
^^^^^^^^^^^^^^^^^^^^

The process api also makes use of a list of **Parameter**s to describe inputs. You can make use of **JParameterListWizard** to prompt the user for appropriate values.

.. image:: /images/paramlistwizard.png

Defining your own parameters represents the quickest way to set up a wizard for use.

Example::
        
        List> list = new ArrayList>();
        list.add(new Parameter("image", File.class, "Image",
                "GeoTiff or World+Image to display as basemap",
                new KVP( Parameter.EXT, "tif", Parameter.EXT, "jpg")));
        list.add(new Parameter("shape", File.class, "Shapefile",
                "Shapefile contents to display", new KVP(Parameter.EXT, "shp")));

        JParameterListWizard wizard = new JParameterListWizard("Image Lab",
                "Fill in the following layers", list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
        File shapeFile = (File) wizard.getConnectionParameters().get("shape");

JWizard
^^^^^^^

The base class for these wizards is **JWizard**, you can use this base class in the construction of your own custom wizards.

Example
'''''''

The following example shows how to create a wizard from scratch using the base classes mentioned above.

* Page 1
  .. image:: /images/jwizard1.gif

* Page 2
  .. image:: /images/jwizard2.gif

You can download the complete example here: :download:`JExampleWizard.java </../src/main/java/org/geotools/swing/JExampleWizard.java>`

1. Create the wizard
   
   .. literalinclude:: /../src/main/java/org/geotools/swing/JExampleWizard.java
     :language: java
     :start-after: // example wizard start
     :end-before: // example wizard end
  
  Note that the wizard pages are:
  
  * constructed with simple string id
  * hooked up to each other by describing their next and previous id
  * you can change these values at runtime to have a dynamic wizard
    that changes the next page based on user input

2. Create the first wizard page
   
   To construct this wizard we begin by making the page in much the
   same way that we would when creating a JDialog or JFrame, except
   that here we derive our page class from JPage:
   
   .. literalinclude:: /../src/main/java/org/geotools/swing/JExampleWizard.java
     :language: java
     :start-after: // page1 start
     :end-before: // page1 end

3. Note that we override the JPage.isValid method to check that the user has entered a valid number.
   
   * getJWizard().getController() is added as a listener to the
     field. The controller implements most swing listeners allowing
     you to use it with fields, lists and buttons as needed
   
   * The controller is responsible for listening to any and all
     user input resulting in its syncButtonsToPage() method being
     callled.
   
   * The syncButtonsToPage() method will use page.isValid() 
     to determine if the **Next** or **Finish** buttons should
     be enabled.

4. We can create the second page in a similar fashion:

   .. literalinclude:: /../src/main/java/org/geotools/swing/JExampleWizard.java
     :language: java
     :start-after: // page2 start
     :end-before: // page2 end

3. Using the wizard:
   
   .. literalinclude:: /../src/main/java/org/geotools/swing/JExampleWizard.java
     :language: java
     :start-after: // use wizard start
     :end-before: // use wizard end
