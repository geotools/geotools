API Internal
------------

This page is devoted to the internals of the API package; in this particular case (since gt-api
mosty consists of interfaces) there is not a lot of interesting design elements to discuss.

Style
^^^^^

The gt-api interfaces for Style are a straight extension of the gt-opengis interfaes. This does
come with a drawback; we need to ask you to be careful of thread safety.

Filter
^^^^^^

The Filter classes in gt-api are deprecated; and have been so since GeoTools 2.3. We are having
trouble removing all the existing test cases that depend on these old Filter definitions.
