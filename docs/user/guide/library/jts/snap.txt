Snap a Point to a Line
----------------------

In this example we show how to use SpatialIndex to organise data by bounding box; and we show how get behind Geometry methods and use the internals of JTS.

Related:

* http://2007.foss4g.org/presentations/view.php?abstract_id=115

This example is an optimised real world example - the kind of thing JTS is used for day in and day out.

A couple of points to notice about the approach:

* we are no longer being limited by the nice friendly Geometry classes; we are getting down into the guts and using the same utility classes that are behind method calls like geometry.getDistance( point ).
* we are we are making use of several facilities of JTS (SpatialIndex and LocationIndexLine) in an intelligent manner based on the problem we are trying to solve.

To start with let us read some features into a FeatureColection::

    public static void main(String[] args) throws Exception {
        if( args.length != 1 ){
            System.out.println("Please provide a filename");
            return;
        }
        File file = new File( args[0]);
        System.out.println("Snapping against:"+file);
        Map<String,Serializable> params = new HashMap<String,Serializable>();
        if( file.getName().endsWith(".properties")){
            Properties properties = new Properties();
            FileInputStream inStream = new FileInputStream(file);
            try {
                properties.load( inStream);
            }
            finally {
                inStream.close();
            }
            for( Map.Entry<Object,Object> property : properties.entrySet() ){
                params.put( (String) property.getKey(), (String) property.getValue() );
            }
        }
        else {
            params.put("url", file.toURL() );            
        }        
        DataStore data = DataStoreFinder.getDataStore(params);        
        List<Name> names = data.getNames();
        
        FeatureSource<SimpleFeatureType, SimpleFeature> source = data.getFeatureSource( names.get(0));
        
        FeatureCollection<SimpleFeatureType, SimpleFeature> features = source.getFeatures();

Remember the FeatureCollection is just like a "result set" it does not actually do anything until we visit the features in the collection (the features stay on disk).

We can now process these features into an internal form. We are going to use a use a SpatialIndex to hold information about each feature. A SpatialIndex is like a Map that you can query by Envelope. In this case our "keys" will be the bounds of each feature; and the value will be a ... LocationIndexedLine.

We are using a FeatureVisitor to look at each feature in turn::

        final SpatialIndex index = new STRtree();     

        System.out.println("Slurping in features ...");
        features.accepts( new FeatureVisitor(){
            public void visit(Feature feature) {
                SimpleFeature simpleFeature = (SimpleFeature) feature;                
                Geometry geom = (MultiLineString) simpleFeature.getDefaultGeometry();
                Envelope bounds = geom.getEnvelopeInternal();
                if( bounds.isNull() ) return; // must be empty geometry?                
                index.insert( bounds, new LocationIndexedLine( geom ));
            }
        }, new NullProgressListener() );

We are going to quickly make some sample points to snap to our lines; in a real world application these are often GPS data that we are trying to snap onto a road network.::

        ReferencedEnvelope limit = features.getBounds();
        Coordinate[] points = new Coordinate[10000];
        Random rand = new Random(file.hashCode());
        for( int i=0; i<10000;i++){
            points[i] = new Coordinate(
                    limit.getMinX()+rand.nextDouble()*limit.getWidth(),
                    limit.getMinY()+rand.nextDouble()*limit.getHeight()
            );
        }

Putting this together we are going to snap as many points as we can in a given duration. We are going to make an envelope of a set size around each point; allowing us to select from the spatial index some lines that are close enough to consider.::

        final int DURATION = 6000;

        double distance = limit.getSpan(0) / 100.0;
        long now = System.currentTimeMillis();
        long then = now+DURATION;
        int count = 0;
        System.out.println("we now have our spatial index and are going to snap for "+DURATION);

        while( System.currentTimeMillis()<then){
            Coordinate pt = points[rand.nextInt(10000)];
            Envelope search = new Envelope(pt);
            search.expandBy(distance);
            
            List<LocationIndexedLine> hits = index.query( search );
            double d = Double.MAX_VALUE;
            Coordinate best = null;
            for( LocationIndexedLine line : hits ){
                LinearLocation here = line.project( pt );                
                Coordinate point = line.extractPoint( here );
                double currentD = point.distance( pt );
                if( currentD < d ){
                    best = point;
                }
            }
            if( best == null ){
                // we did not manage to snap to a line? with real data sets this happens all the time...
                System.out.println( pt + "-X");
            }
            else {
                System.out.println( pt + "->" + best );
            }
            count++;
        }
        System.out.println("snapped "+count+" times - and now I am tired");
        System.out.println("snapped "+count/DURATION+" per milli?");
    }
  }

You can experiment with this code:

* try some actual roads - using real data makes a difference
* try using a QuadTree - it is often much slower (but you can add and remove things from a QuadTree at runtime)
* are you getting whacky results? Check if your geometry.isValid() prior to using it
* try simplifying the lines prior to creating the LocationIndexedLine - it should be much faster
* If you are unsure how to do activities listed above consult the "Secrets of JTS" link provided at the top of the page.
