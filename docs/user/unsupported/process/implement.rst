Implement
---------

This API was designed with the goal of making the implementation of new processes quick. We have a number of base classes and annotations to make it quick and easy to implement a new process.

Implementing a new Process
^^^^^^^^^^^^^^^^^^^^^^^^^^

By using the interfaces provided in this API, implementing a new process is as easy as following the steps below.

For our example, let's create a geometry buffer process (the code for this is already included in the process API module).

0. Create a New Project
   
   Create a new project for your process using your favourite IDE or maven.

1. Create Process Factory
   
   Create a factory for your process in your new project.::
   
        public class BufferFactory extends AbstractProcessFactory {
            // making parameters available as static constants to help java programmers
            /** Geometry for operation */
            static final Parameter<Geometry> GEOM1 =
            new Parameter<Geometry>("geom1", Geometry.class, Text.text("Geometry"), Text.text("Geometry to buffer") );
        
            /** Buffer amount */
            static final Parameter<Double> BUFFER = 
            new Parameter<Double>("buffer", Double.class, Text.text("Buffer Amount"), Text.text("Amount to buffer the geometry by") );
        
            /**
            * Map used for getParameterInfo; used to describe operation requirements for user
            * interface creation.
            */
            static final Map<String,Parameter<?>> prameterInfo = new TreeMap<String,Parameter<?>>();
            static {
            prameterInfo.put( GEOM1.key, GEOM1 );
            prameterInfo.put( BUFFER.key, BUFFER );
            }    
        
            static final Parameter<Geometry> RESULT = 
            new Parameter<Geometry>("result", Geometry.class, Text.text("Result"), Text.text("Result of Geometry.getBuffer( Buffer )") );
        
            /**
            * Map used to describe operation results.
            */
            static final Map<String,Parameter<?>> resultInfo = new TreeMap<String,Parameter<?>>();
            static {
            resultInfo.put( RESULT.key, RESULT );
            }
            
            public Process create(Map<String, Object> parameters)
                    throws IllegalArgumentException {
                return new BufferProcess( this );
            }
        
            public InternationalString getDescription() {
                return Text.text("Buffer a geometry");
            }
        
            public Map<String, Parameter<?>> getParameterInfo() {
                return Collections.unmodifiableMap( prameterInfo );
            }
        
            public Map<String, Parameter<?>> getResultInfo(
                    Map<String, Object> parameters) throws IllegalArgumentException {
                return Collections.unmodifiableMap( resultInfo );
            }
        
            public InternationalString getTitle() {
                // please note that this is a title for display purposes only
                // finding an specific implementation by name is not possible
                return Text.text("Buffer");
            }
        
            public Process create() throws IllegalArgumentException {
                return new BufferProcess( this );
            }
        
            public String getName() {
                return "buffer";
            }
        
            public boolean supportsProgress() {
                return true;
            }       
        
            public String getVersion() {
                return "1.0.0";
            }     
        
        }
2. Next we need to implement the Process class itself.
   
   Create Process Class
   
   Create a new process class and implement the execute method.::
        
        class BufferProcess extends AbstractProcess {
            private boolean started = false;
        
            public BufferProcess( BufferFactory bufferFactory ) {
            super( bufferFactory );
            }
        
            public ProcessFactory getFactory() {
            return factory;
            }
        
            public Map<String,Object> execute(Map<String,Object> input, ProgressListener monitor){
                if (started) throw new IllegalStateException("Process can only be run once");
                started = true;
                
                if( monitor == null ) monitor = new NullProgressListener();
                
                try {
                    monitor.started();
                    monitor.setTask( Text.text("Grabbing arguments") );
                    monitor.progress( 10.0f );
                    Geometry geom1 = (Geometry) input.get( BufferFactory.GEOM1.key );          
                    Double buffer = (Double) input.get( BufferFactory.BUFFER.key );
        
                    monitor.setTask( Text.text("Processing Buffer") );
                    monitor.progress( 25.0f );
        
                    if( monitor.isCanceled() ){
                    return null; // user has canceled this operation
                    }
        
                    Geometry resultGeom = geom1.buffer(buffer);
        
                    monitor.setTask( Text.text("Encoding result" ));
                    monitor.progress( 90.0f );
        
                    Map<String,Object> result = new HashMap<String, Object>();
                    result.put( BufferFactory.RESULT.key, resultGeom );
                    monitor.complete(); // same as 100.0f
        
                    return result;
                }
                catch (Exception eek){
                    monitor.exceptionOccurred(eek);
                    return null;
                }
                finally {
                    monitor.dispose();
                }        
                }
        }
        
3. Finally, we need to make sure our new factory will get found and listed by the
   Processors FactoryFinder.
   
   Create a "services" file to list your factory with the plugin system:
   
   * /src/main/resources/META-INF/services/org.geotools.process.ProcessFactory
   
4. List your factories (one per line) in the above file::
      
      org.geotools.process.BufferFactory
      
5. When this is combined into a jar; the ProcessFactoryFinder will use the files
   in META-INF to "discover" your process factory.

4. Now we can use the new process. Let's write a JUnit test to ensure it works.
   
   Create a JUnit test case in the test folder, let's call it MyProcessTest.java::

        public class MyProcessTest extends TestCase {
         
            public void testBufferProcess() throws Exception {
                // create a Well-Known-Text reader so we can create a quick polygon geometry for our test
                WKTReader reader = new WKTReader( new GeometryFactory() );
                
                Geometry geom1 = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");
                Double buffer = new Double(213.78);
                
                Map<String, Object> map = new HashMap<String, Object>();
                map.put( BufferFactory.GEOM1.key, geom1 );
                map.put( BufferFactory.BUFFER.key, buffer );
                
                BufferProcess process = new BufferProcess( null );        
                Map<String, Object> resultMap = process.execute( map, null );
                
                assertNotNull( resultMap );
                Object result = resultMap.get(BufferFactory.RESULT.key);
                assertNotNull( result );
                assertTrue( "expected geometry", result instanceof Geometry );
                Geometry bufferedGeom = geom1.buffer(buffer);
                assertTrue( bufferedGeom.equals( (Geometry) result ) );
                
                // do some more assets to validate the result of your process
            }
        }

5. Now run your test as a JUnit test. When your test passes, your implementation is complete!
