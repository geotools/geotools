Metadata FAQ
------------

Q: Utility Classes?
^^^^^^^^^^^^^^^^^^^

GeoTools also includes utility classes used to support the implementation of GeoTools.

For the most part these classes are considered internal, as an example to iron out differences between Java versions.

Q: Where are the Interfaces for Metadata?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The interfaces are in the :doc:`gt-opengis </library/opengis/index>` module.

If you don't have access to the ISO documentation (who does?)
just reading the javadocs is a great place to start.

Q: Why did you implement this class it is in Java 6?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you are running Java 6 feel free to use Java 6 facilities.

Some of the utility classes are just here to get the job done in Java 1.4 - they
have nothing to do with spatial anything. You will find examples of checked
collections (yes I know that is available in Java 5); an implementation of an
object cache (there is a JSR for that); and so on ...

Q: Why are these utility classes in the Metadata Module?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Because metadata is the "lowest" implementation jar in our software stack; needed by
everyone else. These classes really are not interesting enough to separate out into
their own module.

Q: Why does ``gt-metadata`` contain all this factory stuff?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The ``gt-metadata`` module introduces some of the "glue code" needed
used to hook up GeoTools to services provided by your project. You will
find pages here covering logging and JNDI integration.

For a more detailed discussion of how to integrate GeoTools into your
application please review the advanced section on integration.
