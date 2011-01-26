This module is used to allow geotools to communicate with the version of the
EPSG included for use with the java HSQL database.

At the difference of 'epsg-access' and 'epsg-postgresql', the 'epsg-hsql' plugin
contains a copy of the EPSG database. This plugin can work "out of the box" without
any specifial action on the user side.

Some instructions included in the javadocs for this module
(see org/geotools/referencing/factory/espg/doc-files/HSQL.html)
are for module maintainers only.
