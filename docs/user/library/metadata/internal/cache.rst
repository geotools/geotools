ObjectCache
-----------

There is a utility class available for your caching needs::
  
  class ObjectCaches {
       ObjectCache create( String policy, int size );
       ObjectCache create( Hints hints );
       String toKey( String code );
       Object toKey( String code1, String code2 );
  }

The actual cache is of the form::
  
  interface ObjectCache {
      Object get( Object key );
      void put( Object key, Object value );
      Object peek( Object key );
      writeLock( Object key );
      writeUnLock( Object key );
  }

You can use this interface to safely work with a cache from multiple threads::
  
  try {
      cache.writeLock( key );
      value = cache.peek( key );
      if( value == null ){
          value = generateContent( key );
          cache.put( key, value );
      }
  }
  finally {
      cache.writeUnLock();
  }

As you can see the peek method is used to sample the cache from within an already established writeLock. Please make use of try / finally to ensure any acquired writeLock is released.

The javadocs has additional examples of how to use this class safely.

* Comparison with JSR 107
  
  Java Specification Request 107 has been tabled in order to
  define an object cache . We have made our implementation method
  compatible in the event JSR 107 is accepted.

* Comparison with Commons Pool
  
  You may of noticed that GeoTools makes use of the commons pool
  as a dependency - and that project is well known for offering a
  bang up implementation of ObjectPool. So your first question
  should very well be - why the heck does GeoTools have an
  ObjectCace of its very own.
  
  Well the truth is that we actually need a cache (rather than a
  pool) and we need it to be threadsafe. You can configure an
  KeyedObjectPool to behave like a cache (by asking it to use weak
  references) - but truth is commons pool wants to manage a pool
  of instances so can return one right away for performance
  reasons.
  
  Since we are worried about stressing memory (rather than only
  performance) our ObjectCache is set up have "per entry"
  read/write locks. We are using it to manage immutable objets so
  we do not mind returning the same value again and again.

The following classes are current implementations of the ObjectCache:

* NullObjectCache
  
  This does nothing and stores nothing.  It is similar to a
  NullProgressMonitor.
  
  Policy: none

* DefaultObjectCache
  
  The cache uses a HashMap for storing values.  All values are
  stored in the map and not removed unless explicitly removed by
  the programmer.

  Policy: all  (or no policy specified)
  
  Note: That with the default object cache a call to get(key) will
  add an entry to the map if the key does not exist.

* FixedSizeObjectCache
  
  Values are held in a WeakValueHashSet, the garbage collector may
  reclaim them at any time. After the LIMIT is reached additional
  values are ignored by the cache.  So it is possible to put
  something in the cache then ask for it later and have it be
  null.

  Policy: fixed
  
* WeakObjectCache 
  
  Values are held in a WeakReference, the garbage collector may
  reclaim them at any time.  Similar to the previous one, it is
  possible to put something in the cache then ask for it later and
  have it be null.  It is possible to specify an initial size for
  the hashmap.
  
  Policy: weak
