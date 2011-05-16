Subversion Install
------------------

Subversion is an advanced version management tool with the same command line syntax as CVS.

SVN offers the GeoTools project:

* the ability to version directories and renames
* the chance to get off of the SourceForge CVS repository

Reference:

* Subversion Book (http://svnbook.red-bean.com/svnbook/index.html)

Although links to various IDE interfaces will be made available, no GUI will substitute for an understanding of the underlying subversion versioning model, and how the system is actually doing work.

Subversion Config
^^^^^^^^^^^^^^^^^^

Please note that whatever client you use; we need to ask you to configure subversion correctly to work with our repository.

1. Copy :download:`config</artifacts/config>`
2. Into the following location
   
   ============= ===========================================================================
   Plartform     Config File Location
   ============= ===========================================================================
   Windows XP:   ``C:\Documents and Settings\ %USERID% \Application Data\Subversion\config``
   Vista:        ``C:\Users\ %USERID% \App Data\Roaming\Subversion\config``
   Windows7:     ``C:\Users\ %USERID% \App Data\Roaming\Subversion\config``
   Linux:        ``~/.subversion/config``
   Mac:          ``~/.subversion/config``
   ============= ===========================================================================
   
3. Why use this config file:
   
   * If you don't do this every java file you edit will appear to be 100% modified!
   * We do not wish to accidentally commit ".class" files
   * We mark the various source code files so that line feeds are handled consistently (CRLF) 
   * We mark the various image formats so that they are committed with the correct mime type allowing you to visually browse the repository.

Windows Setup  for SVN Command Line
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Here are the installation instructions for SVN Windows:

Command line access is similar to cvs. Once again, your best reference is the subversion book.

The svn tool can be downloaded from here:

* http://subversion.apache.org
* http://www.sliksvn.com/en/download

The use of the windows shell extension Tortoise (http://tortoisesvn.tigris.org/) is recommended in conjunction with the command line. IDE integration is available for both NetBeans and Eclipse.

1. Download a win32 installer from here: http://www.sliksvn.com/en/download
2. Run the installer you downloaded. Our if you downloaded the zip file, please unzip the archive and the bin folder to %PATH%
3. Copy the config file to where subversion keeps its configuration:
   
   * Windows XP: C:\Documents and Settings\ %USERID% \Application Data\Subversion\config
   * Vista: C:\Users\ %USERID% \App Data\Roaming\Subversion\config
   * Windows7: C:\Users\ %USERID% \App Data\Roaming\Subversion\config

4. You’ll need to have view system folders turned on to see "Application Data" in explorer.
   Subversion should have an example config file there already.
5. (Optional) change any of your client settings in the config file
6. (Optional) change your servers file to account for any firewalls

Eclipse
^^^^^^^

Eclipse has plugin support for subversion:

* Subclipse: http://subclipse.tigris.org/
* Subversive: http://www.eclipse.org/subversive/

The eclipse IDE assumes on directory per project; while the GeoTools library is split up into a number of smaller projects. As such we will still perform a checkout on the command line. Installing subversion support into eclipse will allow you to commit from the repository.

SVN Linux
^^^^^^^^^

Linux setup for svn command line access:
1. If svn is not already installed, get it here:
   
   http://subversion.apache.org
   
   Most distributions already have it installed.
   
2. Install/compile the package
3. Copy the config file to: ~/.subversion/config
4. (Optional) change any of your client settings in the config file

SVN Netbeans
^^^^^^^^^^^^

1. A netbeans profile is available here: http://vcsgeneric.netbeans.org/profiles/
2. To ignore the ubiquitous .svn folders click the filesystem node and set the “Ignored Files” property to include: .svn

SVN Tortoise for Windows
^^^^^^^^^^^^^^^^^^^^^^^^

A windows shell extension is available: http://tortoisesvn.tigris.org/ 

This is highly recommended for developers uncomfortable with the command line.

ToroiseSVN Checkout of Geotools:

1. Create a folder for geotools to live in: C:\java\geotools
2. Navigate to the folder, and right-click
3. Select "Checkout ..."
   
   * URL of Repository: http://svn.geotools.org/geotools/trunk
   * Checkout directory: C:\java\geotools
   * Revision: Head Revision
   
4. Press OK
