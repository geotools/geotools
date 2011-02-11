Converting URLs to Files
-------------------------

Converting a File to a URL is difficult across different versions of Java; and across different platforms is tough.

=================================== ==================================================
File                                URL
=================================== ==================================================
file.txt                            file:file.txt
/Users                              file:/Users
C:\                                 file:///C:/
C:\Users                            file:///C:/Users
C:\direct with a space\file.txt     file://C:/directory%20with%20a%20space/file.txt
\\host.org\share\directory\file.txt file://///host.org/share/directory/file.txt
/Users/Jody/java                    file:///Users/Jody/java/
=================================== ==================================================

**DataUtiltiies** provides a couple of methods to centralise the handling of platform eccentricities, and ensure the correct encoding and decoding of characters such as spaces that are permitted in file paths but not in URLs. Use them wherever possible.

DataUtilities.fileToURL
^^^^^^^^^^^^^^^^^^^^^^^
Here is how to create a URL for a File in GeoTools::
   
   URL url = DataUtilities.fileToURL(file);

The official Java 6 way to do this is to use::
   
   URL url = file.toURI().toURL(); // can produce problems on Mac OSX

The traditional Java 1.4 way is now deprecated::
   
   URL url = file.toURL(); // will produce invalid URL if spaces are in the path

DataUtilities.urlToFile
^^^^^^^^^^^^^^^^^^^^^^^^

When presented with a URL it is hard to know what to do - since we cannot know how a user has constructed the URL. So we have to assume the worst and try every possible way to untangle
the provided URL.

We have spent a lot of time trying to package up a nice clean way of doing this for our code::
   
   File f = DataUtilities.urlToFile(url);

An early workaround for Java 1.4 code was to use the file authority information (to account for drive letters like "C:" and "D:"). If you find any of this code please update it to use DataUtilities.urlToFile(url).::
   
   File f = url.getAuthority() == null ?
               new File( url.getPath() ) :
               new File("//" + url.getAuthority() + url.getPath());
