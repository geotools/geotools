Module Maintainers
====================

Module maintainers are the lifeblood of GeoTools. They own a specific module and make the majority of project decisions related to their module. In GeoTools, module maintainers have the most direct responsibility and say over the code in the project, via their module.

Module Maintainer Advantages
----------------------------

* Contact point for your module
* Review proposed changes pull requests / patches
* Commit to your module without having to go through review.
  
  If a committer shows a good understanding of the module and coding conventions you can extend them this opportunity as well.
   
* Supported modules
  
  * Include module in GeoTools Release
  * Module considered with each API change
  * Use of build box for Testing
  
* Unsupported modules
  
  * Include module in GeoTools codebase
  * Include module in a build profile

Module Maintainer Responsibilities
----------------------------------

Formal "Supported" Modules
^^^^^^^^^^^^^^^^^^^^^^^^^^

Module maintainers have the three most critical responsibilities in GeoTools:

1. Thine module shalt not break the build.

2. You must keep your module up to date
   
   * Your source code must meet the developers guide to the letter
   * Your test code coverage is measured by the build box (you can provide a profile - if normal JUnit tests take too long)
   * Your module code up should be up to date with other released modules (no deprecations)

3. You must keep your external documentation up to date
   
   * The Jira issue tracker should be up to date
   * Your module should have a couple of pages of User Guide
   * Your module's wiki page should be accurate (see Module Matrix for the complete list)

If these requirements are not met for a release, or if the module maintainer cannot be found, the module will revert to unsupported status.

Maintainers are removed by their own choice or a 75% majority of the PMC. If a volunteer is found for an abandoned module the PMC will need a 75% majority to appoint them.

Experimental "Unsupported" Modules
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We have a "relaxed" set of requirements for "unsupported" modules - providing a great opportunity for experiments and new contributors to get involved:

1. Thine module shalt not break the build.
2. You must keep your module up to date
   
   Recommended:
   
   * User documentation will help reduce the amount of email you recieve
   * Set up a module wiki page (see `Module Matrix <http://docs.codehaus.org/display/GEOTOOLS/Module+Matrix>`_ for the complete list)
    
3. We have no process for "volunteering" to work on an unsupported module at this time; email the
   developer list and we will figure it out.

Reference:

 * :doc:`/procedures/create`
 * :doc:`/procedures/supported`
 