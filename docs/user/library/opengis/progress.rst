Progress
--------

The callback object ProgressListener is used to report on long running operations and provide a chance for the end user to cancel them.

There are several implementations available in GeoTools:

* ProgressWindow - provides a swing dialog showing operation progress
* ProgressPrinter - reports operation progress to standard out
* ProgressMailer - will email when the operation is completed

As you can see these really are intended for operations that may take minuets to complete.

There are a couple implementations of direct interest to implementors:

* NullProgressListener - null object to use when you are not interested
* SubProgressListener - used by implementors when delegating a portion of work to a sub operation
* DelegateProgressListener

Example use::
  
  public void doSomething( ProgressListener progress ){
      if( progress == null ) progress = new NullProgressListener();
  
      progress.started();
      progress.setDecsription("Connect");
      
      ..connect to data store and obtain feature collection...
      
      progress.progress( 20 ); // connecting represents 20% of the work
      
      progress.setDescription("Process features");
      featureCollection.accepts( visitor, new SubProgress( progress, 80 ) );
      
      progress.completed();
  }

This code could be called using:

* doSomething( new ProgressWindow( null ) ); // from a Swing Application
* doSomething( new ProgressMailer( mailserver, emailAddress ) ); // from a web application
* doSomething( new ProgressPrinter() ); // for a command line application
* doSomething( new NullProgressListener() ); // if you don't care

Additional adapters for SWT project are available in the uDig project.

Using ProgressListener to Cancel
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ProgressListener is a general user interface callback object; as such it allows the a user to cancel a long running operations.::
  
  final ProgressListener progress= new ProgressWindow( null );
  Thread worker = new Thread( new Runnable(){
      public void run(){
          doSomething( progress );
      }
  });
  cancel.addActionListener( new ActionListener(){
      public void actionPerformed( ActionEvent e ) {
         progress.setCanceled( true );
      }            
  });
