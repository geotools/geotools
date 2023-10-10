Text
----

Many projects use Java ``String`` to represent text. This page goes over some of the alternatives used to handle text in a consistent manner.

``Enum``
^^^^^^^^^

The first alternative is a normal Java 5 ``Enum``. We use enumerations to represent a frozen set of machine readable text.

The key idea here is:

* this is machine readable; you don't need to show this to your end users
* it is frozen; literally it is boiled into the jar you download and you cannot modify the list of defined codes in any way
* If you are reading any of the ISO specifications you will find this concept is called an ``Enum`` there as well, our use agrees with typical Java practice.

Here is an example for reference::

  public enum OverlapBehavior {
      LATEST_ON_TOP, 
      EARLIEST_ON_TOP, 
      AVERAGE,
      RANDOM
  }

As this set of text form directives to a rendering engine on handling overlapping images they are represented as an ``Enum``.

* The "vocabulary" of allowed text values is fixed at compile time (and is well documented for those implementing a rendering engine).
* As this text is intended to direct a rendering engine the entries are considered machine readable.

An easy test to see if something is considered machine readable is to consider if internationalizing the values would "break" the codebase.

CodeList
^^^^^^^^

A ``CodeList`` is used to represent an extensible set of machine readable text.

In pragmatic terms this is behaves just like a Java 5 ``Enum``, except that you can add to it at runtime.

The ISO specifications call this concept is a ``CodeList`` as well,

Here is an example of a ``CodeList``::
   
   public final class SortOrder extends CodeList<SortOrder> {
      private static final long serialVersionUID = 7840334200112859571L;
      private static final List all = new ArrayList();
      public static final SortOrder ASCENDING  = new SortOrder("ASC");
      public static final SortOrder DESCENDING = new SortOrder("DESC");
      
      private SortOrder( String name ){
         super(name, all );
      }
      
      public CodeList[] family() {
         return (CodeList[]) all.toArray( new CodeList[ all.size()]);
      }
   }

The above implementation is actually very similar to the definition of an ``Enum`` and can give you some insight into have ``Enum``\ s are represented as ``Object``\ s by Java.

The difference here is that your application can add additional ``SortOrder``\ s at runtime::
   
   SortOrder order = SortOrder.valueOf( SortOrder.class, "NATURAL" );

Once again a ``CodeList`` is considered:

* Machine Readable - the text values are intended for programs not people
* Open ended, application code can add additional values to the "vocabulary"

String
^^^^^^^

String also has a place, in this case for free form machine readable text. Good examples are common query language "query" strings, or version numbers which change with each release.

.. note:: We are not using ``Strings`` to communicate to the end user, we have a special class (``InternationalString``) to address internationalization issues.

The ISO specifications call this concept a ``CharacterString``. However you have to be careful and read how they are using it on a case by case basis. Our use does not agree with typical Java practice; we are reserving the use of String for machine readable text.

Here is an example::
   
    public static Version getVersion(){
      return VERSION;
    }

With this in mind String is considered:

* Machine readable (the values are not translated!)
* Free form (i.e. does not belong to a set vocabulary of defined values)

InternationalString
^^^^^^^^^^^^^^^^^^^

An ``InternationalString`` is used to represent free form human readable text. The ``InternationalString`` interface makes every translation of the text available through one class.

The ISO specifications call this concept a ``CharacterString``, but once again you need to check how they are using it.

To use an ``InternationalString`` with the current locale::
   
   return title.toString();

To use the ``InternationalString`` with a different locale::
   
   return title.toString( locale );

This method of handing internationalization may be unfamiliar to those coming from a desktop computing background. You will find this technique very handy when implementing a web application that is communicating with users from a number of different locales.

With this in mind International String is considered:

* Human Readable
* Free form

We have a number of implementations available, backed on properties files and such like.
