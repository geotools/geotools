Exception handling
--------------------

Exception handling in a GIS system is a little different than in other systems because data is usually much more valuable than the software itself. So:

* On one side, the software should try hard to cope with minor problems in the data and do a best effort operation, instead of throwing an exception right away.
* On the other side, best effort handling may hide data problems that the user should be aware of. For some peoples using GIS for decision purpose, a wrong answer is much worst than no answer at all. Some users will want an exception right away rather than risking a wrong decision based on uncorrectly processed data.

Striking a balance is not easy, but nevertheless, the following guidelines do apply.

Let the User Recover from Exceptions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If the API allows for exceptions, and the user is supposedly able to recover from them and keep on doing work, do throw the exception.::
   
   try {
     // some code that may throw an exception
   } finally {
       // cleanup whatever resource was used and let exceptions go
   }

Don't Break the Chain
^^^^^^^^^^^^^^^^^^^^^

If a different exception class is needed, either chain the old one or at least log it, but don't let the old exception fade away in thin air::
   
   try {
     // some code that may throw an exception
   } catch(IllegalAttributeException e) {
     throw new DatastoreException("Error occurred while building feature type", e);
   } finally {
     // cleanup whatever resource was used and let exceptions go
   }

An alternative (but the chained exception above is preferred)::
   
   try {
     // some code that may throw an exception
   } catch(IllegalAttributeException e) {
     LOGGER.log(Level.FINE, "Attribute building error occurred", e);
     throw new DatastoreException("Error occurred while building feature type");
   } finally {
     // cleanup whatever resource was used and let exceptions go
   }

Converting to Runtime Exception (only in no checked exception is appropriate - in such case, consider adding one before to fallback on RuntimeException)::
   
   try {
     // some code that may throw an exception
   } catch(IllegalAttributeException e) {
     throw (RuntimeException) new RuntimeException("Error occurred while building feature type").initCause( e);
   } finally {
     // cleanup whatever resource was used and let exceptions go
   }

Provide warning when Making Assumptions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If a legitimate return value can be returned when an exception occurs (bbox computations for example), do return the legitimate value, but log the exception as a warning (don't let it disappear). 

The unexpectedException convenience method in org.geotools.util.Logging helps log these situtations::
   
   try {
     // some code that may throw an exception
   } catch(DataSourceException e) {
     LOGGER.log(Level.WARNING, "Could not determine bounding box - assuming valid CRS area", e);
     return new ReferencedEnvelope(-180,180,-90,90); // empty bounds
   } finally {
     // cleanup whatever resource was used and let exceptions go
   }

Otherwise
^^^^^^^^^

Otherwise, please try to keep on going.

It is important (for example, during rendering) to avoid blocking because of a handful of bad Feature objects.

Provide some way to log the exceptions and report them to the user for later inspection. It is recommended to process all unexpected exceptions into a unexpectedException method that can be overridden giving developers a chance to stop the process if the error is critical to their application.

Here is an example::
   
   // Paint the map.
   public void paint() {
     Iterator it = featureCollection.iterator();
     String fid;
     try {
       while (it.hasNext()) {
         try {
           Feature feature = (Feature) it.next();
           fid = feature.getId();
           // some code that may throw an exception  
         } catch (IllegalAttributeException e) {
           // Feature was invalid - continue to the next one..
           unexpectedException("Skipping invliad Feature", e);
         } catch (NullPointerException e2) {
           unexpectedException("Problem working with Feature", e);
         }
       }
     }
     catch (IOException io) {
       if (LOGGER.isLoggable(Level.FINER)) {
         // example of "guarding" an expensive logging statement
         unexpectedException("Feature ("+fid+") failed:"+ io.getMessage(), e);
    }
    throw (IOException) new IOException("Problem processing "+featureCollection);
     }
     finally {
       featureCollection.close(it);
     }
   }
   
   /**
    * Invoked when an unexpected exception occurred. The default    implementation
    * log the exception at the warning level. Subclasses can override this
    * method if they need to handle the exception in an other way (for example
    * throwing an other exception, if failure to render a feature is critical).
    */
   protected void unexpectedException(String message, Throwable e) {
     LOGGER.log(Level.FINE, message, e);
   }

So, to sum up, never, ever throw away exceptions, either throw or log them. Failing to do so makes debugging really hard, especially when all you have is a log file from one of your users!

As for logging, in the case where the exception is not thrown, think hard about the level you'll use to log exceptions, and follow the logging guidelines to avoid wasting CPU cycles in it. Sometimes it's a nice compromise to just log the exception message at WARNING or FINE level, and log the stack trace at a FINEST level.