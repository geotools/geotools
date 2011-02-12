Refactoring
============

Refactoring is the process of restructuring/renaming your code to ensure your design remains clean as your requirements and functionality change and grow with time.

Refactoring is best explored using the excelent book writen my Martin Fowler.

Subversion Warning
^^^^^^^^^^^^^^^^^^

SVN wants to know what you are doing, please use the svn move name1 name2 command
when refactoring to let svn keep track of history information.

This is especially important when using either of the tools at the end of this page - although we can hope that more complete svn intergration is available for Eclipse shortly.

Changing public API
^^^^^^^^^^^^^^^^^^^

Before to refactor a method, make sure that it did not have public access in the previous Geotools release.

* If it was public, then we need to go through a "deprecate, then remove" cycle.
* If not was not public, go ahead with with the refactoring. 

For example consider the following method::
   
   /**
    * Do some stuff.
    */
   public void devolution(double x, double y) {
       // Do something
   }

Suppose that Geotools 2.3 is already released and we are working on Geotools 2.4. Suppose that we want to add a String argument to the above method. Do not refactor this method directly like this:

WRONG::
   
   /**
    * Do some stuff.
    */
   public void devolution(String method, double x, double y) {
       // Do something
   }

Instead, duplicate the method for at least one Geotools release:

Better::
   
   /**
    * Do some stuff.
    *
    * @deprecated Replaced by {@link #foo(String,double,double)}.
    */
   public void devolution(double x, double y) {
       foo(someDefaultTitle, x, y);
   }
   
   /**
    * Do some stuff.
    *
    * @since 2.4
    */
   public void devolution(String method, double x, double y) {
       // Do something
   }

Note the @since 2.4 javadoc tag. It is an important hint for both users and the module maintainer, so do not forget it. The deprecated method can be removed in Geotools 2.5.

Keep methods private
^^^^^^^^^^^^^^^^^^^^^

Does it sound like tedious to apply the above procedure everytime we want to refactor a method? Then keep your methods private!! Hide the internal mechanic as much as possible and declare public only the methods that are really expected to be used. If you need to share the internal mechanic with other classes that perform a related work, consider the package private access (chances are that related classes are declared in the same package). Think very hard before to give public access to a method. In case of doubt, keep it private until we get more experience about that particular method. It is much easier to turn a private method into a public one later than trying to change an already public method.
Giving public access to a private method

Wanting to turn a private method into a public one? Before to do so, make sure that the method is declared at the right place for public access. In many cases, private methods are convenience methods performing some work that are only indirectly related to the main purpose of the enclosing class. For example an Apple class may have a compareOranges private method for whatever internal reason. If you need public access to this method, replacing the private keyword by public is not enough! Consider moving this method to the Orange class. Keep also in mind that the method will now be used in a wider context than it originaly did, so check if it made any assumptions that need to be revisited.

Refactoring tools
^^^^^^^^^^^^^^^^^

RefactorIt
   To ease refactoring you can use RefactorIt which provides tools to:
   
   * research, probe and understand existing source code,
   * move, organise and transform existing code,
   * and provide code metrics.
   * More details can be found from the online help.
   
   RefactorIt is commercial, but provides free licences for Open Source projects like Geotools.
   See the RefactorIt web pages for details. It can be plugged into a variety of IDEs, including
   Netbeans.
   
   Installation in to GEOTOOLS:Netbeans fairly strait forward through
   **Tools -> RefactorIt -> Project Options**, although setting up soucepath and classpath is a
   difficult if some of the geotools files don't compile. You will need to either remove the
   offending files, get them to compile or remove them from RefactorIt's sourcepath.

Eclipse Developers Guide Refactoring
   Eclipse has many refactorings available, mostly through context menus.

Netbeans Refactoring
   Netbeans has many refactorings available, mostly through context menus.
