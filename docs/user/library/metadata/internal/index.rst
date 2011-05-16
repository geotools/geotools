Metadata Internals
------------------

The GeoTools library is big nasty (and plays with big nasty amounts of data) and as such is always slight ahead of its time. We run into the limits of Java - often years before good solutions show up as part of the Java language.

.. toctree::
   :maxdepth: 1
   
   collections
   cache
   utilities

In a perfect world none of these utility classes would need to exist - and we could just use software components from off the shelf projects. In many cases we have found that the volume of GeoSpatial information breaks assumptions made by projects, such as commons collections, leaving us no choice but to roll our own.

You are welcome to use these classes in your own application, in the event any of these solutions are deprecated instructions will be provided on how to move on.
