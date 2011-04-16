Internal
--------

Now that we have looked at how much fun the Process Module is for users here
is some background information on how to hook your own work into the process module.

Overview
^^^^^^^^

A Process defines an operation that can be executed. Each process has a list of input parameters specific to its operation, as well as a list of results that it will return.

An example would be a buffer process which accepts a geometry and a buffer value as input parameters, and returns a new geometry that has been buffered as the result.

The purpose of the Process API is to allow developers to easily wrap up an algorithm for reuse. Processes can then be used within various contexts, such as Web Process Services (WPS Plugin).

This API allows developers to:

* find and create all process implementations
* execute processes
* track the progress of an executing process

This API allows implementors to:

* define what the input parameters are (with a map of parameters)
* define what the output parameters are (with a map of results)
* implement processes without regard for all the scheduling stuff

A main design goal of this module is to make a process quick and easy to implement. The following sections will show the design of this module, how to implement a new process, and how to use the processes within a GUI application.

Process
^^^^^^^

The Process interface is used to define a process. Making a new process is simple and only requires implementing the execute method (which is where the work is done to actually run a process). This method accepts the inputs and a ProgressListener. It is up to the implementor to update this listener with the progress of the process execution. There is also a SimpleProcess abstract class that simple processes can extend when they are so quick to execute that updating the progress is not necessary.

BufferProcess example::

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

ProcessFactory
^^^^^^^^^^^^^^


To advertise (and describe) a process implementation (like buffer above) we will need to create a ProcessFactory.

The ProcessFactory interface is used to describe a process and its parameters, and for creating new instances of those processes. Typically, a ProcessFactory will be found and created using the Processors FactoryFinder. Below is an example of an implemented ProcessFactory.

The process factory includes other implementation (like name and version) to allow user interfaces and WPS implementations to make this service available in a controlled manner.

BufferFactory Example::

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
    
Parameter
^^^^^^^^^

Every Process contains a map of input parameters and a map of results which are also defined as parameters.

The Parameter interface provides a way to define these objects. This interface contains required information and optional metadata about the parameter, such as its name, description, class type, whether it is required or not, how many are required, and so on. There are a few constructors for creating a Parameter, depending on how much optional metadata you want to set on the object.

The Parameter interface is part of the 04 API module and is used to define connection parameters for data access.

Here is an example of asking the user to supply a "geomIn" parameter::

    Parameter<Geometry> geomInput = new Parameter<Geometry>("geomIn", Geometry.class, Text.text("Geometry"), 
        Text.text("Geometry input parameter"),  true, 2, -1, null, null);

In a similar manner you can indicate the result you produce::

    Parameter<Geometry> geomResult = new Parameter<Geometry>("geomOut", Geometry.class, Text.text("Geometry"), Text.text("Geometry result"));

Process Executor
^^^^^^^^^^^^^^^^

The ProcessExecutor provides methods to manage process executions and for tracking asynchronous tasks. By implementing the submit method that returns a Progress object, the process execution can be tracked or terminated. Below is an example of a very simple implementation of the ProcessExecutor interface.

You may wish to implement the Process Executor class if you are backing onto an existing scheduling facility such as that provided by a workflow engine.

ThreadPoolProcessExecutor Example::

    public class ThreadPoolProcessExecutor extends ThreadPoolExecutor 
        implements ProcessExecutor {
    
        
        public ThreadPoolProcessExecutor( int nThreads, ThreadFactory threadFactory ) {
            super( nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(), threadFactory );        
        }
    
        public Progress submit( Process task, Map<String,Object> input ) {
            if (task == null) throw new NullPointerException();
            ProgressTask ftask = new ProgressTask(task, input );
            execute(ftask);
            return ftask;
        }
    
    }
