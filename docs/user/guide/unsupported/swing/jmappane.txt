JMapPane
--------

MapPane serves as an example of a simple Swing widget that can display a map, and provide limited interaction.

JMapFrame
^^^^^^^^^

JMapFrame packages up a JMapPane for a quick visual display. In GeoTools we often use this class to in our example code to show results.

* Here is how to show a map::
    
    MapContext context = new MapContext();
    
    // add map layers here
    
    // Title will be used as the title for JMapFrame
    context.setTitle("The Map is Back");
    
    // Show the Map to the user
    JMapFrame.showMap( context );
  
  The resulting map is displayed in a simple frame.
  
  
  .. image:: /images/jmapframe1.jpg
     :scale: 60

* Please note that when the user closes the JMapFrame the application will exit.
  
  To prevent this (say if you are using JMapFrame for debugging) please use the
  following::
    
    JMapFrame show = new JMapFrame( context );
    show.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
    show.setVisible(true);

* Turning on additional tools
  
  By default JMapFrame only shows the JMapPane and is not very exciting.
  
  To turn additional features on::
    
    JMapFrame show = new JMapFrame( context );
    
    // list layers and set them as visible + selected
    show.enableLayerTable( true );  
    
    // zoom in, zoom out, pan, show all
    show.enableToolBar( true ); 
    
    // location of cursor and bounds of current 
    show.enableStatusBar( true ); 
    
    // display
    show.setVisible( true );
  
  Here is JMapFrame with toolbar, status bar and layer table.
  
  .. image:: /images/jmapframe2.jpg
     :scale: 60

JMapPane
^^^^^^^^

JMapPane is a simple map display panel (derived from Swing's JPanel) that works with a GTRenderer and MapContext to display features. It supports the use of tool classes to implement, for example, mouse-controlled zooming and panning

* JMapPane is used in our tutorials in order to offer a chance to learn the library
  in a visual fashion. For more information on the rendering process please review
  the gt-render module.

* Please keep in mind that this is an unsupported example of how to
  do things. The code is very simple; it maintains the following
  information:
  
  * MapContext - defines contents of the map; the list of layers to
    display in the correct order
  * MapArea - the area of the world to display
  
  With this information in mind the paint method of the JPanel 
  calls the normal GeoTools renderer to draw a map as the
  background of the widget. To control the appearance of the
  Map you will need to look at how styles work etc...

Within GeoTools, JMapPane is used in the JMapFrame widget, but you can also use it directly in your own code or as the starting point for a more specialised GUI components.

This snippet shows the typical way of constructing and initializing a JMapPane::
  
  JMapPane mapPane = new JMapPane();
  mapPane.setBackgroundColor(Color.WHITE);
  
  // set a renderer to use with the map pane
  mapPane.setRenderer( new StreamingRenderer() );
  
  // set the map context that contains the layers to be displayed
  MapContext context = ...
  mapPane.setMapContext( context );

Display Area
''''''''''''

Here is how to set or query the current display bounds (in world coordinates)::
  
  import org.opengis.geometry.Envelope;
  
  ...
  
  // get the current display bounds
  Envelope bounds = mapPane.getEnvelope();
  
  // set a new area to display
  CoordinateReferenceSystem crs = ...
  double minX = ...
  double maxX = ...
  double minY = ...
  double maxY = ...
  Envelope newBounds = new ReferencedEnvelope(minX, maxX, minY, maxY, crs);
  
  // this will set the new bounds and cause the map pane to repaint
  mapPane.setEnvelope(newBounds);

Linking toolbar buttons
'''''''''''''''''''''''

The swing module includes a small selection of Action classes that make it easy to create toobar buttons or other controls for zooming, panning and resetting the map display.

For example, this code creates two toolbar buttons to zoom in and out::
  
  toolBar = new JToolBar();
  toolBar.setOrientation(JToolBar.HORIZONTAL);
  toolBar.setFloatable(false);
  
  ButtonGroup cursorToolGrp = new ButtonGroup();
  
  JButton zoomInBtn = new JButton(new ZoomInAction(mapPane));
  toolBar.add(zoomInBtn);
  cursorToolGrp.add(zoomInBtn);
  
  JButton zoomOutBtn = new JButton(new ZoomOutAction(mapPane));
  toolBar.add(zoomOutBtn);
  cursorToolGrp.add(zoomOutBtn);

The zoom Action classes in the code above are each associated with a map pane CursorTool class (e.g. ZoomInTool) which handles setting the cursor and responding to mouse actions. You can use these tool and Action classes as the starting point for your own specialized controls.

Example
'''''''

1. You will need to define your own MapContext (defining what
   layers to display) prior to using a JMapPane::
    
     private static void showMap(MapContext map) throws IOException {
        final JMapPane mapPane = new JMapPane(new StreamingRenderer(), map);
        mapPane.setMapArea(map.getLayerBounds());
        JFrame frame = new JFrame("ImageLab2");

        frame.setLayout(new BorderLayout());
        frame.add(mapPane, BorderLayout.CENTER);
        JPanel buttons = new JPanel();
        JButton zoomInButton = new JButton("Zoom In");
        zoomInButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapPane.setState(JMapPane.ZoomIn);
            }
        });
        buttons.add(zoomInButton);

        JButton zoomOutButton = new JButton("Zoom Out");
        zoomOutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapPane.setState(JMapPane.ZoomOut);
            }
        });
        buttons.add(zoomOutButton);

        JButton pamButton = new JButton("Move");
        pamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mapPane.setState(JMapPane.Pan);
            }
        });
        buttons.add(pamButton);

        frame.add(buttons, BorderLayout.NORTH);

        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
     }

Actions
^^^^^^^

The ZoomInAction, ZoomOutAction, PanAction actions used in the above example shows changing the map "state" (the state of the map controls what it does in response to mouse clicks).

You will find ready to go actions that also change the map state::
  
  JMapPane mapPane = ...
  JButton zoomInButton = new JButton(new ZoomInAction(mapPane));
  JButton zoomOutButton = new JButton(new ZoomOutAction(mapPane));
  JButton panButton = new JButton(new PanAction(mapPane));

* Mouse Wheel
  
  From email - Perhaps the easiest way of doing it, without sub-classing JMapPane, is
  something like this::
    
    // somewhere in your code...
    double clickToZoom = 0.1;  // 1 wheel click is 10% zoom
    
    // wheel event handler
    public void handleMouseWheelEvent(MouseWheelEvent ev) {
       int clicks = ev.getWheelRotation();
       // -ve means wheel moved up, +ve means down
       int sign = (clicks < 0 ? -1 : 1);

       Envelope env = mapPane.getMapArea();
       double width = env.getWidth();
       double delta = width * clickToZoom * sign;

       env.expandBy(delta);
       mapPane.setMapArea(env);
       mapPane.repaint();
    }
  
  I imagine we can set up a "tool" to respect mousewheel events; perhaps you would like to submit a patch?

