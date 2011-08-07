Comitters
=========

Contributors who give frequent and valuable contributions to GeoTools (docs and bug fixes) can have
their status promoted to that of a formal "Committer" to GeoTools.

Advantages
----------

* Work on your own unsupported module
* Apply your own patches - after they have been reviewed

Responsibilities
----------------
A Committer has write access to the whole source code repository which entails certain responsibilities.

* Committers must coordinate their efforts with the maintainers of any modules they wish to modify or extend, i.e. committers need to ask permission. Contact details for each module's maintainer can be found in the source code as part of the pom.xml file or in the Module Matrix. If committers are working in their own module, then they are themselves maintainers of that module and they can do as they wish in that module only.

* Committers are expected to follow GeoTools conventions. They are required to read this developer's guide. Committers are responsible for any code they submit which means they are required to know and correctly document the origin of any code they submit and required to ensure they are legally allowed to submit and share that code.

* Committers are required to submit clean code. Code formatting should follow the project guidelines.

* Committers must not submit data files without prior approval. We want to include only the minimal amount of data necessary, and have that all contained in the sample-data module. If new data is needed in that module, we can add the data there.

Otherwise, Committers have one primordial responsibility:

* Thou shalt not break the build.

This means that all code should be run through a full maven install and test cycle::
   
   mvn clean install -Dall
   
prior to commit. Yes, this takes time; yes, it is necessary.

Process
-------

1. In order for a Contributor to become a Committer we need an email:
   
   * Another Committer can nominate that Contributor; or
   * Contributor can ask for Committer status
   
   Please keep in mind that this process is for contributors so we need to see a history
   of patches and documentation that follow the developers guide.
   
2. Once a Contributor is nominated, all existing committers will vote.

   * If there are at least three positive votes; and
   * no negative votes
   
   The Contributor will be considered a Committer and can be set up with write access to
   the source code repository.

3. Welcome letter
   
   This is an example offer letter that should be sent to the volunteer after 3 positive votes
   have been received::

       Dear Contributor,
       
       The GeoTools project would like to offer you commit privileges. If you are interested in
       having commit privileges, please create an OSGeo user id on this page:
          
          http://www.osgeo.org/osgeo_userid/
          
       And send your user id to myself or any other member of GeoTools.
       
       GeoTools is a member of the Open Source Geospatial Foundation, we have attached a copy of the
       GeoToolsContributorsAgreement to this email. The attachment contains instructions for use,
       and the mailing address for the OSGeo foundation is on the first page.
       
       We all hope that you accept this invitation, welcome to the project!
       
       GeoTools Project Management Committee

   Attachment:

   * :download:`GeotoolsAssignmentToOSGeo.pdf</artifacts/GeotoolsAssignmentToOSGeo.pdf>`

4. Getting write access to the repository
   
   Once we have confirmation that you have sent in the document any GeoTools committer can grant
   you access to the project:

   * Create an osgeo login id using this page:
  
     http://www.osgeo.org/osgeo_userid/
   
   * Send an email with your id to the mailing list (or any GeoTools committer you know) and they
     can add you using the following link:
     
     https://www.osgeo.org/cgi-bin/auth/ldap_group.py?group=geotools

   For more information on the OSGeo SVN Repository facilities:
   
   * http://wiki.osgeo.org/wiki/Subversion

5. Lapses due to Inactivity
   
   At times, Committers may go inactive for a variety of reasons. A Committer who has been inactive
   for 12 months or more may lose their status as a Committer. Getting access back is as simple as
   re-requesting it on the project's Developer mailing list.