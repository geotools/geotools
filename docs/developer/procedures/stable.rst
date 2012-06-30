Working on a stable branch
==========================

Working on a stable branch like 2.7.x or 8.x is a bit different than working on trunk.
For one we are not adding new features, and we need to back out changes that fail.

Let us start with what is restricted:

* Changing **any** API.
* Breaking any client code that previously worked

With that in mind here is what we can do on a stable branch:

* applying a fix
* creating a new plugin
* create an additional plug-in
  
Since plugins are optional, and the functionality is available through the interfaces of the core library new plugins can be added to the library as there will be required change in client code.

Applying a Fix to the Stable Branch
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Do you have a Jira issue? Chances are any change worth doing on the branch has a jira issue

#. Pull from the canonical repository
#. Do the full maven cycle of maven build, maven createRelease
#. Commit and push your change
   
   You did clear it with the module maintainer first?

#. Mark down the commit revision(s) (git log will easily tell you this)
#. Remember: Mark the issue as RESOLVED in Jira and be sure to include:
   
   * The Fix Version (8.0-RC1, 8.1, etc...)
   * Commit: 9e6b6fca (Usually the first 7-8 characters of a git revision are sufficient)
   
   This will help when applying your change to master....

Applying your Change to master
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

It is nice to do this after every fix, or you may want to save up a couple.
Please try to apply your fixes to trunk before the next trunk Milestone release (we like to release the best code we can - and that includes your fixes).

#. Grab the commit id's
#. From your master check out apply the patch::
     
     git cherry-pick 9e6b6fca
     
   If cherry picking multiple commits be sure to pick them in the same order as they were
   applied on the stable branch.

#. Do the complete maven cycle of: clean, build, createRelease
#. If the change works all is well commit::
     
     git pull --rebase geotools master
     git push geotools master
     
#. If not back out the change ... and open a jira bug on the matter.::
     
     git reset --hard HEAD

   Note: this will essentially remove all local commits.

Applying a fix from master
^^^^^^^^^^^^^^^^^^^^^^^^^^

To merge in a change you will need:

* The commit(s) for the changes to merge

#. Update the 8.x branch::

      git checkout 8.x
      git pull geotools 8.x
      
#. Cherry-pick the commits from master::
      
      git cherry-pick <commit>
      ...
      
   If cherry picking multiple commits be sure to pick them in the same order as they were
   applied on the master branch.

#. Do the complete maven cycle of: clean, build, createRelease
#. If the change works all is well push changes::
     
      git push geotools 8.x

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

#. Push changes::

     git pull --rebase geotools master
     git push  geotools master
     
#. Remember to (re)open any associated Jira issue.
