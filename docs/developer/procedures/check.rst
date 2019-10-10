Gold Star Quality Assurance Check
==================================

GeoTools Modules makes use of a gold star system, 3 stars or more is great, an X is used to indicate non working modules.

Check each module for a README.md for developer notes.

This test is something quick, accessible and visible to end users.

Module QA Test
^^^^^^^^^^^^^^^^

Here is how a library (aka part of the GeoTools library API) may earn a star:

* Passes IP check documented in review.txt file, basically has correct headers
* Releasable - has no non blocking bugs in jira
* Quality Assurance - Test Case coverage
* Stability - based on stable / reviewed interfaces (gt-api or gt-opengis) representing collaboration or ISO/OGC specification
* Supported - user docs, module maintainer watches user list, answers email etc.. (example referencing)

Plugin QA Test
^^^^^^^^^^^^^^^^

Here is how a plugin (aka hooks into the geotools library) may earn a star:

* Passes IP check, basically has correct headers
* Releasable - has no non blocking bugs in jira
* Used in anger - Used by GeoServer or uDig or Geomajas (i.e. some kind of large real world datasets)
* Optimised - has been tuned to meet hard performance requirements (example shapefile)
* Supported - user docs, module maintainer watches user list, answers email etc.. (example referencing)

Extension QA Test
^^^^^^^^^^^^^^^^^^^^

Here is how a extension (aka build on top of the GeoTools library) may earn a star:

* Passes IP check, basically has correct headers
* Releasable - has no non blocking bugs in jira
* Quality Assurance - Test Case coverage
* Stability - based on appropriate interfaces or base classes
* Supported - user docs, module maintainer watches user list, answers email etc..
