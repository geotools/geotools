:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Quality Assurance
-----------------

Since this tutorial has been written the result has been broken out into a distinct **gt-csv** plugin (with some feedback from the GeoServer community).

To get an idea of what kind of "extra work" is required for an unsupported plugin:

#. Ask on the email list - a Project Steering Committee member can often reply with a +1 and go about getting you commit access on GitHub. The project is fairly relaxed with a safe "unsupported" area for new experiments.
#. Set up a pom.xml and hook the module in to the build
#. Use a profile in unsupported/pom.xml to include your module in the build

To get the module supported (and included in each GeoTools release):

* IP check to ensure module can be distributed by the GeoTools team
* Follow developers guide (for coding style, logging, exception handling, testing)
* Test coverage over 40%
* User documentation (a single code example in the user guide is fine)

For more information see the `developers guide <http://docs.geotools.org/latest/developer/procedures/supported.html>`_.

Directory Support
^^^^^^^^^^^^^^^^^

Earlier copies of this tutorial would read an entire directory of files at a time. This functionality has been factored out into a support class and is used by implementations such as ShapefileDataStore.

The same steps can be taken to CSVDataStore - although it is a real trade off between the code being clear vs less typing.

Info
^^^^

This is more a case of polish, you can fill in a "info" data structure to more accurately describe your information to the uDig or GeoServer catalog.

In our case we could pick up any comments at the top of the file and use them as the initial file description.