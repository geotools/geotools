SWTMapFrame
-----------

The following is a tutorial that explains how to use the ``gt-swt`` module in standalone mode. 
The tutorial assumes the user is already confident with GeoTools development.

Example
^^^^^^^

Using the standalone ``gt-swt`` module is fairly easy and is best explained with a code snippet::
  
    public class Main {
       public static void main( String[] args ) throws Exception {
         // create a default mapcontext
         MapContent context = new MapContent();
         // set the title
         context.setTitle("The SWT Map is in the game");
         // and show the map viewer
         SwtMapFrame.showMap(context, null);
       }
     }
   
Which results in:
   
    .. image:: /images/gtswt_standalone_01.png


It is possible to supply to the mapframe an actionhandler that allows to add actions to the file and 
navigation menus. For example the following code snippet shows how to add an add-OSM-layer action to
the file menu (in addition to the default entries)::
   
    public static void main( String[] args ) throws Exception {
        // create a default mapcontext
        MapContent context = new MapContent();
        // set the title
        context.setTitle("The SWT Map is in the game");
      
        // create an action handler with the selected MapActions
        SwtActionsHandler actionsHandler = new SwtActionsHandler(){
            private OpenShapefileAction openShapefileAction = new OpenShapefileAction();
            private OpenGeotiffAction openGeotiffAction = new OpenGeotiffAction();
            private AddOsmAction addOsmAction = new AddOsmAction();

            @Override
            public MapAction[] getFileMenuActions() {
                return new MapAction[]{openShapefileAction, openGeotiffAction, addOsmAction};
            }

            @Override
            public MapAction[] getFileNavigationMenuActions() {
                return new MapAction[0];
            }

        };

        // and show the map viewer
        SwtMapFrame.showMap(context, actionsHandler);
    }


where the MapAction itself would be a class like::


    public class AddOsmAction extends MapAction implements ISelectionChangedListener {

        public AddOsmAction() {
            super("Add OSM", "Add an Openstreetmap layer to the viewer.", null);
        }

        public void run() {
            MapContent mapContent = mapPane.getMapContent();
            
            String baseURL = "https://tile.openstreetmap.org/";
            TileService service = new OSMService("OpenStreetMap", baseURL);
            TileLayer layer = new TileLayer(service);
            layer.setTitle("OpenStreetMap");
            mapContent.addLayer(layer);
            mapPane.redraw();
        }

        public void selectionChanged( SelectionChangedEvent arg0 ) {
        }
    }

   
The viewer will now have a new entry in the File menu:
   
   .. image:: /images/gtswt_standalone_02.png


By default, the viewer supports loading of shapfiles and geotiffs. If you do not know where 
to get a shapefile, you can download the dataset of the world's countries from the Naturalearth Project `at this link 
<https://www.naturalearthdata.com/http//www.naturalearthdata.com/download/110m/cultural/ne_110m_admin_0_countries.zip>`_.

Once downloaded and unzipped, just use the **Open Shapefile** action from the file menu and load the shapefile:

   .. image:: /images/gtswt_standalone_03.png

The viewer features a simple style editor for vector layers and a couple of utilities to
change the order of the layers, remove a layer and bulk select and unselect the layers visibility.