-------------------------------------------------------------------------------
 README file for Geotools developers - Do NOT copy this file to the web server
-------------------------------------------------------------------------------

The HTML pages in this directory provide the content for the following site:

    http://maven.geotools.org/  (TODO: not yet enabled)
    http://maven.geotools.fr/

Changes in those files will be automatically reflected to those web sites after
some server-dependent delay. It can also be reflected to any mirrors configured
as below:

  - A local SVN repository updated on a regular basis.

  - Soft links created as below (the base directory names provided below
    are just examples and will change for different server installation):

    cd /var/www/geotools.org/maven/
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/index.html index.html

    cd /var/www/geotools.org/maven/repository
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/repository/HEADER.html HEADER.html

    cd /var/www/geotools.org/maven/resources
    ln -s /home/localsvn/geotools/trunk/gt/maven/www/resources/banner.gif banner.gif
