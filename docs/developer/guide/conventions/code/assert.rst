Use of Assertions, IllegalArgumentException and NPE
----------------------------------------------------

The Java language has for a couple of years now made an assert keyword available; this keyword can be used to perform debug only checks. While there are several uses of this facility, a common one is to check method parameters on private (not public) methods. Other uses are post-conditions and invariants.

Reference:

* http://download.oracle.com/javase/6/docs/technotes/guides/language/assert.html

Pre-conditions (like argument checks in private methods) are typically easy targets for assertions. Post-conditions and invariants are sometime less straightforward but more valuable, since non-trivial conditions have more risks to be broken.

* Example 1: After a map projection in the referencing module, an assertion performs the inverse map projection and checks the result with the original point (post-condition).

* Example 2: In DirectPosition.equals(Object) implementations, if the result is true, then the assertion ensures that hashCode() are identical as required by the Object contract.

Use Assert to check Parameters on Private methods
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Here is an example::
   
   private double scale( int scaleDenominator ){
       assert scaleDenominator > 0;
       return 1 / (double) scaleDenominator;
   }

You can enable assertions with the following command line parameter::

   java -ea MyApp

You can turn only GeoTools assertions with the following command line parameter:

   java -ea:org.geotools MyApp

You can disable assertions for a specific package as shown here:

   java -ea:org.geotools -da:org.geotools.referencing MyApp

Use IllegalArgumentExceptions to check Parameters on Public Methods
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The use of asserts on public methods is strictly discouraged; because the mistake being reported has been made in client code - be honest and tell them up front with an IllegalArgumentException when they have screwed up.::
   
   public double toScale( int scaleDenominator ){
       if( scaleDenominator > 0 ){
           throw new IllegalArgumentException( "scaleDenominator must be greater than 0");
       }
       return 1 / (double) scaleDenominator;
   }

Use NullPointerException where needed
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

If possible perform your own null checks; throwing a IllegalArgumentException or NullPointerException with detailed information about what has gone wrong.::
   
   public double toScale( Integer scaleDenominator ){
       if( scaleDenominator == null ){
           throw new NullPointerException( "scaleDenominator must be provided");
       }
       if( scaleDenominator > 0 ){
           throw new IllegalArgumentException( "scaleDenominator must be greater than 0");
       }
       return 1 / (double) scaleDenominator;
   }
