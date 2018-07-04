URLs
----

**org.geotools.util.URLs** provides methods for safely handling URLs.

* URLs.fileToUrl(File)
* URLs.urlToFile(URL)

One of the changes between Java 5 and Java 6 is the deprecation of the File.toURL() method. Earlier version of Java implemented this method incorrectly, and Java 6 offers File.toURI().toURL() as a replacement.

Problem is that does not help anyone - since when we are provided a URL we cannot be sure if it was constructed correctly.

These two methods have been extensively tested and can handle odd corner cases such as files located on windows shared folders.

We use this method internally to make sure we can understand each URL that is handed to the library. It is available for use in your application as well.

Here is a scary example of a windows network share::
  
    File file = new File("\\\\host\\share\\file");
    URL url = URLs.fileToUrl(file);

See also:

* changeUrlExt(URL, String)
* extendUrl(URL, String)
* getParentUrl(URL)

References:

* `URLs <http://docs.geotools.org/latest/javadocs/org/geotools/util/URLs.html>`_ (javadocs)
