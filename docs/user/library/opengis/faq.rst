OpenGIS FAQ
-----------

Q: Relationship with "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

* GeoAPI was started in 2002 James McGill (who also set up GeoTools). The aim at that time was to provide common API for independent projects like GeoTools, deegree and OpenJump, allowing the easy exchange of code.
* Later on, the Open Geospatial Consortium started the "GO-1" project with a similar goal. The "Geospatial Object" project was led by Polexis which was at the time based in the United States.
* Given the similarity between GO-1 and GeoAPI goals, we got in touch each other and managed to merge the two projects.
* Polexis produced the following official OGC specification based on GeoAPI 2.0: http://www.opengeospatial.org/standards/go (now retired).
* Later Polexis was bought by Sys Technology, with the new owner and priorities their investment in GeoAPI / GO-1 stopped.
* The GO-1 / GeoAPI working group at OGC was dissolved due to lack of activity.
* GeoTools contributors gradually took over the GeoAPI project and in GeoTools 2.7 folded these interfaces back into the GeoTools OpenGIS module.

Q: Why am I seeing Java exceptions related to the org.opengis namespace?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

You may have both GeoAPI and GeoTools in the same application, and the classloader is finding the GeoAPI implementation first.  
Check your classpath, including any shaded jars that might incorporate GeoAPI classes.

