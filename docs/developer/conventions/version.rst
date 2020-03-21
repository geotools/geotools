Versioning
===========

**Release**

GeoTools jars are versioned as a group with version numbers are based on 3 digits::
  
  <major>.<minor>.<patch>
  
* Major (first digit), is incremented to indicate that a module has lost full compatibility to earlier versions.
  
  So you can safely upgrade to later versions of a module so long as the major version has not changed.

* Minor (second digit) is incremented whenever new features are added.
  
  Modules are forward compatible across minor versions, but usually not backward compatible.

* Patch (last digit) is for bug fixes.
  
  It is used to indicate fixes in bugs only. No new features were made and full compatibility is preserved.

**Trunk**

GeoTools jars released from trunk are versioned with version number based on 2 digits::
  
    <major>.<minor>-M<milestone>
    <major>.<minor>-RC<candidate>

* Milestone is used to mark alpha or beta releases as appropriate (example 8.0-M1)
* Release Candidates are marked 8.0-RC1 and indicate that the branch has been forked
  off trunk in preparation for final release.

In practice
^^^^^^^^^^^

The above is theory. In practice, we released some patch versions that contained new features. We also released minor versions that were not forward compatible with older releases (usually through the removal of deprecated methods). This policy on numbering may be revisited later in order to better match the current practice.

Version Number and Maven
^^^^^^^^^^^^^^^^^^^^^^^^^

Maven is intimently aware of version numbers (in fact they have been seeing each other for some time).

Maven tracks the following information:

* `gt/pom.xml` - id is used to name the project (gt2eclipse will use this)
* `gt/pom.xml` - groupId is used to name the maven repository folder
* `module/*/pom.xml` - name is used for the display name (website and output)
* `module/*/pom.xml` - id is used to generate the name of the jar (and track dependency)
* `module/*/pom.xml` - groupId is used to name the target maven repository folder dependency

The module version number is set in the <version> element in::
   
   <module>/pom.xml

Maven uses this value during the build process to figure out what to call generated jars.

* Name: GeoTools
* ID: gt-geotools
* groupId: org.geotools
* CurrentVersion = 2.4-SNAPSHOT

* Name: Main Module
* ID: gt-main
* GroupId: org.geotools
* CurrentVersion: 2.4-SNAPSHOT

Tagging Releases
^^^^^^^^^^^^^^^^^

The release process has changed a bit since moving to subversion for details please see **How to cut a release** under project procedures.

@since javadoc tag
^^^^^^^^^^^^^^^^^^^

Every public and protected class, interface, method or field should have a @since javadoc tag. If the GeoTools 2.2 release is under development, then every new API should be identified with a @since 2.2 tag. For the end user, it means that:

* All classes and methods with a @since 2.0 or @since 2.1 javadoc tag are safe. Because they were there is previous releases, they will not change except for bug fixes (a few of them may be deprecated however).

* Any classes and methods with a @since 2.2 javadoc tag may be modified, moved or deleted without warning until the 2.2 final release.