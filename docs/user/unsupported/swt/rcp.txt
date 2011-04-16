Rich Client Platform
^^^^^^^^^^^^^^^^^^^^

Well , the reason for having an SWT module is to be able to exploit a lightweight map viewer inside
an RCP application, whenever the "weight" of the RCP GIS application uDig is too much.

The Hello World RCP application
''''''''''''''''''''''''''''''''

To integrate the gt-swt map panel inside an rcp application, we will start with an existing simple
rcp hello world application that contains a view. To explain how to achieve that is beyond the aim
of this tutorial, if you need help, there are a ton of tutorials out there that will guide you in
that (one example is the really good rcp tutorial by Vogella).

References:

* http://www.vogella.de/articles/EclipseRCP/article.html

Put the map panel inside the rcp application
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The source of the rcp application has been bundeled together with all the needed geotools libs and
uploaded here. Simply import the project into eclipse and you are ready to go.

* http://jgrasstools.googlecode.com/files/rcp-gt-swt.tar.gz


Putting the gt-swt map panel inside the view of your application:

1. It is as easy as implementing the createPartControl method of the view like the following::
     
     public void createPartControl( Composite parent ) {
        // handle icons, will be explained later
        handleImages();

        // create the default mapcontext
        MapContext context = new DefaultMapContext();
        context.layers();

        // create the main composite, with or without layer panel
        Composite mainComposite = null;
        if (showLayerTable) {
            SashForm sashForm = new SashForm(parent, SWT.HORIZONTAL | SWT.NULL);
            mainComposite = sashForm;
            MapLayerComposite mapLayerTable = new MapLayerComposite(mainComposite, SWT.BORDER);
            mapPane = new SwtMapPane(mainComposite, SWT.BORDER | SWT.NO_BACKGROUND);
            mapPane.setMapContext(context);
            mapLayerTable.setMapPane(mapPane);
            sashForm.setWeights(new int[]{1, 3});
        } else {
            mainComposite = parent;
            mapPane = new SwtMapPane(mainComposite, SWT.BORDER | SWT.NO_BACKGROUND);
            mapPane.setMapContext(context);
        }

        mapPane.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
        // set the renderer
        StreamingRenderer renderer = new StreamingRenderer();
        mapPane.setRenderer(renderer);
     }

2. One method that has to be explained is the handleImages. Since the handling of file paths
   inside a java project and an rcp plugin project are quite different, the ImageCache isn't
   able to retrieve the necessary icons fro the gui from the gt-swt module. Therefore it is
   necessary to supply the needed images to the cache before starting. This can be done as
   follows based on the rcp file path handling::
   
     private void handleImages() {
        // get the image cache
        ImageCache imageCache = ImageCache.getInstance();
        // get all the relative paths needed by the cache
        List<String> relativePaths = imageCache.getRelativePaths();
        /*
           * Create all the needed images. The images have to reside in the plugin root 
           * in the same folder structure as in the gt-swt resources folder. 
           * 
           * The icons of the gt-swt module are currently located in /icons/
           */
        for( String path : relativePaths ) {
            Image image =
               AbstractUIPlugin.imageDescriptorFromPlugin(GtSwtPlugin.PLUGIN_ID, path).createImage();
            // feed the image into the cache for further use
            imageCache.addImage(path, image);
        }
     }
    
3. For the exact same reason it is also necessary to have the language file of the gt-swt module
   insid the plugin root as::
     
      /resources/Text.properties
   
   in order to be properly picked by the plugin.
   
Adding the map tools as view actions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Right now we have only the map view. The best place to put the tools like pan and zoom is probably
the toolbar of the map view. To do so, we can add a viewActions extention point to our view, then
add a viewContribution to the viewActions and finally all the actions we need there.

1. To add the Info Tool action, you add::

       <action
             class="org.geotools.swt.actions.InfoAction"
             icon="icons/info_mode.gif"
             id="rcp-gt-swt.info"
             label="Info Action"
             style="push"
             toolbarPath="gtswt">
       </action>
           
2. Then we have to implement the action itself.
   
   That is fairly easy, since we can delegate the activation of the tools to the gt-swt map panel::
     
     public class InfoAction implements IViewActionDelegate {
        private IViewPart view;
        public void init( IViewPart view ) {
            this.view = view;
        }
        public void run( IAction action ) {
            SwtMapPane mapPane = ((MapView) view).getMapPane();
            mapPane.setCursorTool(new InfoTool());
        }
        public void selectionChanged( IAction action, ISelection selection ) {
        }
     }

3. The same applies to all other actions.

Adding the layer addition as menu commands
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

One last thing we will add is a command to load shapefiles from the filesystem.  This just to show
how to retrieve the map panel in case the view is not directly accessible.

1. First you create a command through the proper extension point. It should look like this::

     <extension
           point="org.eclipse.ui.commands">
      <command
            defaultHandler="org.geotools.swt.actions.OpenShapefileCommand"
            id="rcp-gt-swt.openshp"
            name="Open Shapefile">
      </command>
     </extension>
     
2. Then that command can be added to the already existing File menu::

     <extension
           point="org.eclipse.ui.menus">
      <menuContribution
            locationURI="menu:org.eclipse.ui.main.menu">
           <menu
                 label="File">
            <command
                  commandId="org.eclipse.ui.file.exit"
                  label="Exit">
            </command>
            <!-- add the commadn to the menu -->  
            <command
                  commandId="rcp-gt-swt.openshp"
                  icon="icons/open.gif"
                  label="Open Shapefile"
                  style="push"
                  tooltip="Opens a shapefile from the filesystem">
            </command>
           </menu>
      </menuContribution>
     </extension>

3. Last thing is to implement the org.geotools.swt.actions.OpenShapefileCommand, that we supplied in
   the above command as the one that would handle the command.
  
   There is not that much behind that. We open a file browser, gather the selected file and add it to
   the mapcontext of the map pane, that we as usual retrieve from the map view::

     public class OpenShapefileCommand extends AbstractHandler {
        
        @Override
        public Object execute( ExecutionEvent event ) throws ExecutionException {
          IWorkbenchPage activePage = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
          MapView mapView = (MapView) activePage.findView(MapView.ID);
  
          SwtMapPane mapPane = mapView.getMapPane();
  
          Display display = Display.getCurrent();
          Shell shell = new Shell(display);
          File openFile = JFileDataStoreChooser.showOpenFile(new String[]{"*.shp"}, shell); 
  
          try {
              if (openFile != null && openFile.exists()) {
                  MapContext mapContext = mapPane.getMapContext();
                  FileDataStore store = FileDataStoreFinder.getDataStore(openFile);
                  SimpleFeatureSource featureSource = store.getFeatureSource();
                  Style style = Utils.createStyle(openFile, featureSource);
                  mapContext.addLayer(featureSource, style);
                  mapPane.redraw();
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
          return null;
       }
     }

Resulting RCP
^^^^^^^^^^^^^

If everything went smooth, you should be able to run the application and see something like:

.. image:: /images/gtswt_rcp_01.png

And with some layers loaded:


.. image:: /images/gtswt_rcp_02.png