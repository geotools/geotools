Javadoc utilities
-----------------

The \@source taglet
^^^^^^^^^^^^^^^^^^^

The javadoc module (build/maven/javadoc) includes a custom \@source taglet which adds the identity of
the module and the source code repository path to the javadoc page for each class. You include the
taglet in the class header docs as in this example::

  /**
   * This class does something wonderful.
   *
   * @author Fred Nurk
   * @since 8.0
   * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/foo/src/main/java/org/geotools/foo/Foo.java $
   */
  public class Foo {

The precise position of the taglet in the javadoc block is not important. When a javadoc page is
generated for the above example it will include the following in the class header section:

  **Module:**
     **modules/unsupported/foo (gt-foo.jar)**
  **Source repository**
     ``http://svn.osgeo.org/geotools/trunk/modules/unsupported/foo/src/main/java/org/geotools/foo/Foo.java``
   
The delimiters, ``$URL`` and ``$`` are not compulsory. Their purpose is to allow for Subversion
keyword expansion when the source file is committed, ie. the path between the delimiters will be
automatically refreshed by Subversion if the file is moved to another location (e.g. a different
module or package). However, if you use other version control software such as Git this won't work.
In any case, the @source taglet discards the delimiters when processing the content.


InsertSourceTag utility
^^^^^^^^^^^^^^^^^^^^^^^

You can use the InsertSourceTag utility to add or update the taglet in multiple source files. You
run it from the command line, as in the following example which inserts the tag in any source file
in module **gt-wps** that does not already contain it::
    
    cd trunk/build/maven/javadoc
    mvn exec:java -Dexec.args="../../../modules/unsupported/wps/src"

You can also include any of the following options before or after the input path (options go inside
the quotes):

``--replace``
    Forces any existing \@source tags to be replaced by newly generated ones. The default is to keep
    existing tags.

``--anyclass``
    Instructs the utility to process all classes (including enums and interfaces) rather than just
    public ones (the default).

``--fix``
    The utility will attempt to fix any existing tags that have been incorrectly split across two
    lines by auto-formatting. The default is to report files with such tags.
    
``--svn``
    The tag content will be enclosed in svn keyword delimiters ``$URL`` and ``$`` to be used for
    automatic path updating by Subversion (does not work with Git). The default is to omit the
    delimiters.

    If you use this option you also need to set the Subversion **svn:keywords** property on all of
    the files. On a 'nix system you use **find** to set the property on all soruce files in a directory
    tree like this::

      find modules/unsupported/wps/src -type f -name '*.java' -exec svn ps svn:keywords "Id URL" {} \;

