Process
-------

The Process module gathers together interesting geospatial algorithms along with an API
that allows applications to discover what implementations are available at runtime.

Typical Use
^^^^^^^^^^^
While processes can be used directly (you can see how to do that below) the real benefit
is the have a thread pool ready to execute processes in the background.

1. Here is a code example showing creation of a process, and scheduling it to run in a
   thread pool::

    // 3 is one more then the current number of cpu cores
    ProcessExecutor backgroundExecutor = Processors.newProcessExecutor( 3, null );
    
    Process buffer = Processors.createProcess( new NameImpl("gt","buffer") );
    Map<String,Object> input = new KVP("features", inputFeatureCollection, "buffer", 0.01 );
    
    Progress progress = backgroundExecutor.schedule( buffer, input );

2. You can check on the progress of a running process::
    
    if( progress.getProgress() == Progress.WORKING ){
      System.out.println( "Progress: Working");
    else {
      System.out.println( "Progress:"+progress.getProgress() + "percent complete" );
    }

3. To read the value when done::

    Map<String,Object> result = progress.get();
    SimpleFeatureCollection resultFeatureCollection = (SimpleFeatureCollection) result.get("features");

4. If you ask for the value before the progress is complete your current thread will block while the
   value is produced.
   
   This is the Java Future API and represents a thread safe way of calculating something in a
   background thread.

Direct Use
''''''''''

By convention Process implementations have a static helper method that you can call directly.::

    features = RasterToVectorProcess.process( gridCoverage, 2, Collections.EMPTY, null );

For instructions on how to use this method you will need to read the javadocs of the
method in question.

By convention the last parameter is a progress monitor you can use to report back
what is going on to your user - and let them cancel.

Since processes can take minuets to hours this is a good plan.::

    features = RasterToVectorProcess.process( gridCoverage, 2, Collections.EMPTY, monitor );

This is all well and good - what else could happen to make things complicated?

Processors Utility Class
^^^^^^^^^^^^^^^^^^^^^^^^

You can also use the Processors utility class to do things like list all the processes that
are available to be called::

    // find all the process factories and print out their names
    Set<ProcessFactory> processFactories = Processors.getProcessFactories();
    Iterator<ProcessFactory> iterator = processFactories.iterator();
    while (iterator.hasNext()) {
        System.out.println("Process Factory: " + iterator.next().getTitle().toString() );
    }

Setting up A Process
''''''''''''''''''''

You can now create a Process object; and sort out what input it needs::

    Name name = new NameImpl(ProcessFactory.GT_NAMESPACE, "RasterToVectorProcess");
    
    Process r2v = Processors.createProcess(name);
    
    Map<String, Object> params = new HashMap<String, Object>();
    
    params.put(RasterToVectorFactory.RASTER.key, cov);
    params.put(RasterToVectorFactory.BAND.key, Integer.valueOf(0));
    params.put(RasterToVectorFactory.BOUNDS.key, env);
    params.put(RasterToVectorFactory.OUTSIDE.key, Collections.singleton(0.0d));

You can execute your process by hand in the current thread (we are using a swing progress bar
to report progress below).::

    ProgressListener progress = new ProgressWindow(null);
    Map<String, Object> results = r2v.execute(params, progress);

Using a ProcessorExecutor
'''''''''''''''''''''''''

The real fun with using the Processes utility class is that you can set up work and then schedule
the work in two separate steps. This is a great way to use todays multi-core processors in
your application.

1. You can then use a ProcessorExecutor is going to be in charge of running your processes - you
   can indicate how many threads you want it to keep going at once.
   
   A good idea is the number of cores you have plus 1.::
        
        ProcessExecutor schedule = Processors.newProcessExecutor( 3, null );
        
2. You can then schedule your process - which will produce a Progress - this is a placeholder
   object that you can use to retrive the answer when it is ready.
   
   You can think of it like a "work ticket".::
    
        Process workTicket = schedule.submit( buffer, input );
    
3. When you want the answer::
       
        Map<String,Object> answer = workTicket.get();

4 The advantage of this technique is that you can get an object that represents the
  activity of doing the work; it will not actually do anything until you need the work done.
