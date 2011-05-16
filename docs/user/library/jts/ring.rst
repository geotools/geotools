Closing a LineString
--------------------

There are several occasions where you will need to take a LineString and close it (so the start and end points are exactly the same. This step is needed to create a LinearRing used as the outer boundary of a user supplied Polygon.::
  
  CoordinateList list = new CoordinateList( lineString.getCoordinates() );
  list.closeRing();
  LinearRing ring = factory.createLinearRing( list.toCoordinateArray() );

**Copy Coordinates**
  
You will see a lot of code that ops to just copy coordinate sequence and duplicate the initial point.::

  LinearRing ring = null;
  if( lineString.isClosed() )
     ring = factory.createLinearRing( splitter.getCoordinateSequence() );
  else {
     CoordinateSequence sequence = lineString.getCoordinateSequence();
     Coordinate array[] = new Coordinate[ sequence.size() + 1 ];
     for( int i=0; i<sequence.size();i++){
     array[i] = sequence.getCoordinate(i);
     array[array.length-1] = sequence.getCoordinate(0);                        
     ring = factory.createLinearRing( array );
  }
  Polygon polygon = factory.createPolygon( ring, null );

