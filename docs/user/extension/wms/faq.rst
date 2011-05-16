WMS FAQ
-------

Q: Scrambled PNG Images?
^^^^^^^^^^^^^^^^^^^^^^^^

A couple versions of Java ship with broken native PNG support (gasp!).
If you find your WMS image scrambled you can try the following::
    
    ImageUtilities.allowNativeCodec("png", ImageReaderSpi.class, false);   
   
This code fragments disables the native PNG reader (the pure java one
is fine; and in some platforms faster).
