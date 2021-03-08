Working on a stable branch
==========================

Working on a stable branch like 2.7.x or 8.x is a bit different than working on trunk.
For one we are not adding new features, and we need to back out changes that fail.

Let us start with what is restricted:

* Changing **any** API. Additive-only API changes may be backported from main after one month, where technically feasible.
* Breaking any client code that previously worked.

With that in mind here is what we can do on a stable branch:

* applying a fix
* creating a new plugin
* create an additional plug-in


Applying a fix may also involve upgrading a dependency. When doing so, make sure the API of the dependency has not changed - you should treat an API change in a dependency the same as an API change in GeoTools, as code that depends on your module may also depend upon the funcionality of its dependencies. In some cases, upgrading a dependency to fix a bug may also bring with it API changes in the dependency. Consider whether a backport is really necessary in such cases. Security fixes may need to force an api change.
  
Since plugins are optional, and the functionality is available through the interfaces of the core library new plugins can be added to the library as there will be required change in client code.

Applying a Fix to the Stable Branch
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Do you have a Jira issue? Chances are any change worth doing on the branch has a Jira issue. If not, create one.

Has the fix been applied to main? Before applying a fix to the stable branch, it must have been commited to main, unless doing so is impossible or unnecessary for some reason. If not, see "Applying your change to main", below.

#. Pull from the canonical repository (use the stable branch instead of 8.x)

      git checkout 8.x
      git pull geotools 8.x

#. Do the full maven cycle of maven build, maven createRelease
#. Checkout a working branch
#. Commit and push your change, and create a pull request against the stable branch.
   
   You did clear it with the module maintainer first?

#. Mark down the commit revision(s) (git log will easily tell you this)
#. Remember: Mark the issue as RESOLVED in Jira and be sure to include:
   
   * The Fix Version (8.0-RC1, 8.1, etc...)
   * Commit: 9e6b6fca (Usually the first 7-8 characters of a git revision are sufficient)
   
   This will help when applying your change to main....

Applying your Change to main
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

#. Grab the commit ids of the fix
#. From your main check out apply the patch::
     
     git cherry-pick 9e6b6fca
     
   If cherry picking multiple commits be sure to pick them in the same order as they were
   applied on the stable branch.

#. Do the complete maven cycle of: clean, build, createRelease
#. If the change works all is well commit and push your change, and create a pull request against the main branch.
     
#. If not back out the change ... and open a jira bug on the matter.::
     
     git reset --hard HEAD

   Note: this will essentially remove all local commits.

Applying a fix from main
^^^^^^^^^^^^^^^^^^^^^^^^

To merge in an existing fix from main you will need:

* The commit(s) for the changes to merge

#. Update the 8.x branch::

      git checkout 8.x
      git pull geotools 8.x
      
#. Cherry-pick the commits from main::
      
      git cherry-pick <commit>
      ...
      
   If cherry picking multiple commits be sure to pick them in the same order as they were
   applied on the main branch.

#. Do the complete maven cycle of: clean, build, createRelease
#. If the change works all is well push changes and open a pull request against the stable branch.

#. If not back out the change ... and (re)open a Jira bug about it.::
      
      git reset --hard HEAD

   Note: This will back out *all* of your local changes.
   
#. Remember: please update any Jira bug to indicate that the issue is fixed on 8.x as well.

Backing out a Change
^^^^^^^^^^^^^^^^^^^^

If you find out later that a change is bad sometime after your commit, all is not lost.

#. Revert the commit::
     
     git revert <commit>

   Or revert a range of commits::
   
     git revert <commit1>..<commitN>

#. Push the changes and open a pull request against the applicable branch.
     
#. Remember to (re)open any associated Jira issue.
