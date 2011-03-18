Name and Record
---------------

The ISO 19103 concept of Names and Records is used internally (mostly in the gt-referencing module).

The central idea is not a bad one, it amounts to a Map<Key,Object> with three additional ideas:

* The set of keys is well defined (by a RecordType)
* The keys themselves have a Namespace so they cannot be confused

The only wrinkle is that this standard comes from what must be a C++ community. As they have made a couple of strange design choices as by hard coding the idea of a linked list into their concept of a LocalName.

.. image:: /images/name.PNG

The actual Record and RecordType classes are straight forward.


.. image:: /images/record.PNG
 