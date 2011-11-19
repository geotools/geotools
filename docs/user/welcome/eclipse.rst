Welcome Eclipse Developers
==========================

Eclipse is a popular integrated development environment for Java development. Since GeoTools is a Java library we would really like it to work out of the box for you.

Our Eclipse Quickstart provides an excellent introduction to setting up eclipse for use with GeoTools covering the M2Eclipse project, command line maven or downloading GeoTools and using the jars in a project.

Making Associations with the SRC download
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The first time you go to look at any of the GeoTools classes you will need to tell Eclipse where the source code for that jar is.

1. From the "Source not found" editor press the Attach Source... Button.
2. Press the Workspace button
3. Select the gt2-2.4-RC0-src.zip
4. And press OK

.. image:: /images/eclipseTip1.png

5. If you are a interested in the details, the above steps made a change to
   your .classpath file::
     
     <classpathentry exported="true" kind="lib"
                     path="gt2-2.4-RC0/gt2-api-2.4-RC0.jar"
                     sourcepath="gt2-2.4-RC0-src.zip"/>
6. You can quickly cut and paste sourcepath="gt2-2.4-RC0-src.zip" for the rest of the
   gt2 jars.
