Collections
-----------

GeoTools makes heavy use of Java collections. Given the age of the project you will find some of the older interfaces still make use of arrays. For the most part we have made the same information available as an array (for older code) and a Java collection for ease of use.

We do have a number of custom collections, or collections provided by third party libraries that are worth special mention.

* FeatureCollection
  
  You are also warned that **FeatureCollection** is no long a
  Java collection (as we could not support the "for each" loop
  construct due to the requirement to call iterator.close() ).

* CanocialSet
  
  Can be used to create an internal pool of objects so a factory
  does not hand out duplicates. This works in a manner similar to
  the String.intern() method and is useful for optimising memory
  use.

  Here is an example::
    
    /**
     * Please note that Foo must be *immutable* and have *equals/hashcode* defined.
     */
    class FooFactory {
        public Foo create(String definition) {
              Foo created = new Foo(definition);
              return (Foo) canionicalSet.unique(created);
        }
    }
  
  At a technical level this class is an AbstractSet in which the entries are weak references. This means that CanonicalSet will not
  hold onto created objects when they are no longer in active use by your application.

  Please note this is not a "cache" as the intended use is different. We are optimising for memory use here rather than speed.

* DisjointSet
  
  Two disjoint sets will keep each other in sync (as content is
  added to one set it will be removed from the other and vice
  versa)

* DerivedSet
  
  Abstract class for when you want to make a sub set
  
* DerivedMap
  
  Abstract class for when you want to make a sub map

* Singleton - Deprecated
  
  Remember GeoTools being slightly ahead of its time? This is an
  example of a collection in which a ready alternative is now
  available.
  
  Please use::
    
    Collections.singleton( obj )

* Checked Collections
  
  We had expected Java 5 to provided checked collections, but instead 
  the collections were limited to some creative compile time checking. The only downside to this is if you pass a 
  List<?> around there is a chance it could end up in
  the wrong spot and cause ClassCastExceptions.

  The following collections ask you to indicate the element
  type during creation - and will check that new elements
  are of the required type as you add them to the colleciton.
  
  * CheckedArrayList
  * CheckedCollection
  * CheckedHashMap
  * CheckedHashSet
  
  Check the version of Java you are using to see if a replacement
  for these classes are available. When GeoTools upgrades we will
  cleanly extend a built-in Java implementation to preserve
  compatibility.

* WeakReference Collections
  
  The following collection implementations make use of weak
  reference allowing the contents to "evaporate" when not in use:
  
  * WeakHashSet
  * WeakValueHashMap
  
  The following implementation makes use of a mix of strong and
  weak references in order to operate as an effective cache:
  
  * SoftValueHashMap
  
  Note: You may also wish to in the commons-pool library which
  offer similar functionality when using an **ObjectPool** with
  weak references.
  
  A key difference between many of the uses of WeakReference you
  will find on the web and these classes implemented in GeoTools
  if how aggressive we have had to be in order to manage memory.
  
  While many applications are content to let the garbage collector
  get around to cleaning up memory. GeoTools has had to introduce
  a background thread in order to keep memory size in check when
  working with large datasets.

* KeySortedList
  
  As advertised a list where you can specify the order using a
  look aside index providing the order.

* IntegerList
  
  Integer list that adjust how much memory is used to store the
  values depending on what your expected maximum value is.
  
  The contents are packed into the minimum number of bits needed.

* KVP
  
  Used for quick "key value pair" maps, where they keys are
  Strings and the values can be any object. 
  
  Used for terse programming in test cases and constructors.::
    
    Map<String,Object> lookup = new KVP("first, 1, "second", 2.0 );
  
  The implementation supports any number of parameters.

WeakCollectionCleaner
^^^^^^^^^^^^^^^^^^^^^

The use of this background thread is not at first obvious - it is used internally by several collection classes as a high priority thread (to clean out unused content).

The question is of course why a high priority thread?

First some background - our collection classes do not directly use WeakReference - instead they work with a subclass in which the clean method has been overridden to:

* throw away the value (ie normal behaviour)
* remove itself from the collection

WeakCollectionCleaner is a fast thread that spends most of its time asleep (so your application can do its work), when it does wake up we want it to be a high priority so it can get in there and recover memory .. so your application is not starved for resources.

This is an example of real world use not being very obvious. The real work of your application is often best handled in a low priority thread, with system management work like this happening quickly in a high priority thread.
