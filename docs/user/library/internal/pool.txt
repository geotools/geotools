Commons Pool
------------

The Commons Pool project is built around a single idea - the creation of an ObjectPool. This page captures our "best practices" discovered over the course of building the GeoTools project.

References:

* http://commons.apache.org/pool/

ObjectPool as a Cache
^^^^^^^^^^^^^^^^^^^^^

You can configure an SoftReferenceObjectPool to use weak references, the result functions the same as a cache.

ObjectPool for Interning
^^^^^^^^^^^^^^^^^^^^^^^^

You can configure an ObjectPool to use weak references in order to "Intern" objects and prevent duplicates from being
used in your application.

ObjectPool as a Pool
^^^^^^^^^^^^^^^^^^^^

First comes the set up - so the object pool can create the objects as needed.::
  
  public interface KeyedPoolableObjectFactory {
    Object makeObject(Object key);
    void activateObject(Object key, Object obj);
    void passivateObject(Object key, Object obj);
    boolean validateObject(Object key, Object obj);
    void destroyObject(Object key, Object obj);
  }

ObjectPool Configuration
''''''''''''''''''''''''

And then comes the configuration (of GenericKeyedObjectPool ):


* Number of Objects
  
  maxActive
    capacity of created objects
  
  maxIdle
    maximum number of lurkers

* What to do when at capacity:
  
  whenExhausedAction
     fail, grow or block when capacity reached
  
* When to check objects in pool
  
  testOnBorrow
    Check with validateObject when borrowed
  
  testOnreturn
    Check with validateObject when returned
  
  testWhenIdle
    Check with validateObject from the eviction thread

* Eviction Thread
  
  timeBetweenEvictionRunsMillis
    Time to sleep
  
  minEvictableIdleTimeMills
    time to wait before an object could be cleaned up

The way it works is this, for a maxActive of 20 at most 20 objects will be created. If they are all returned the pool will throw the instances away until it has maxIdle (this is kind of its comfort zone). After this point each object will have to time out (based on minEvictableIdleTimeMills) before it is reclaimed.::
      
      maxActive
   /--This is the absolute capacity of our ObjectCache
   |
   |  Objects are immediately evicted when they are not in use
   |
   \-> maxIdle
   /-- This is the "comfort zone" that your ObjectCache will try for under heavy use
   |
   |  Objects are evicted when they have not been used, every minEvictableIdleTimeMills 
   |
   \-> Empty

Eviction
''''''''

Here is how eviction works:

1. it will grab numTestsPerEvictionRun objects from the pool
2. for any object that has exceeded minEvictableIdleTimeMillis it will call validateObject
3. it will throw away any object that fails the validateObject test
4. If we have less than minIdle left in the pool new objects will be spawned

You can perform eviction in a thread controlled by setting testWhenIdle and providing a value for timeBetweenEvictionRunsMillis.

GenericObjectPool Configuration
'''''''''''''''''''''''''''''''

GenericObjectPool introduces the concept of a "soft min" operating as a preferred number object objects to hold in reserved (even when we are completly idle).

This concept is a little freaky in that it looks like the GenericObjectPool will create these objects in a background thread even if nothing is going on.

minIdle
   minimum number of lurkers

softMinEvictableIdleTimeMills
   time to wait before an object is cleaned up

The way it works is this, for a maxActive of 20 at most 20 objects will be created. If they are all returned the pool will throw the instances away until it has maxIdle (this is kind of its comfort zone). As each object times out we will be left with minIdel number of objects in the pool.::
    
      maxActive
   /--This is the absolute capacity of our ObjectCache
   |
   |  Objects are immediately evicted when they are not in use
   |
   \-> maxIdle
   /-- This is the "comfort zone" that your ObjectCache will try for under heavy use
   |
   |  Objects are evicted when they have not been used, every minEvictableIdleTimeMills 
   |
   \-> minIdle
   /-> This is the number of Objects reserved when we are completely idle
   |
   | Objects are spawned as needed, every softMinEvictableIdleTimeMills
   |
   \-- Empty
