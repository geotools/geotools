SWTMapFrame
-----------

The following is a tutorial that explains how to use the ``gt-swt`` module in standalone mode. 
The tutorial assumes the user is already confident with GeoTools development.

Example
^^^^^^^

Using the standalone ``gt-swt`` module is fairly easy.

1. It is best explained with a code snippet::
  
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

2. It is possible to supply to the mapframe an actionhandler that allows to add actions to the file and 
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
   
   Which results in:
   
   .. image:: /images/gtswt_standalone_02.png
