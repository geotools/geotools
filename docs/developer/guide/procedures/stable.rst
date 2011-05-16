Working on a stable branch
==========================

Working on a stable branch like 2.7.x or 2.6.x is a bit different working on trunk.
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

1. Update - and mark down the resulting revision as BEFORE
2. Do the full maven cycle of maven build, maven createRelease
3. Commit your change
   
   You did clear it with the module maintainer first?

4. Mark down the revision AFTER (your commit told you this)
5. Remember: Mark the issue as CLOSED in Jira and be sure to include:
   
   * Resolved in 2.1.RC1 (you can be more specific)
   * BEFORE: 13926
   * AFTER: 13927
   
   This will help when applying your change to trunk....

Applying your Change to trunk
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

It is nice to do this after every fix, or you may want to save up a couple.
Please try to apply your fixes to trunk before the next trunk Milestone release (we like to release the best code we can - and that includes your fixes).

1. Grabs the url: like http://svn.geotools.org/geotools/branches/2.1.x and revision numbers
2. From your trunk check out apply the patch::
     
     svn merge -r 13926:13927 http://svn.geotools.org/geotools/branches/2.1.x

3. Do the complete maven cycle of: clean, build, createRelease
4. If the change works all is well commit::
     
     svn commit -m &quot;Applied fix for GEOT-538 from 2.1.x&quot;

5. If not back out the change ... and open a jira bug on the matter.::
     
     svn revert -r .

Applying a fix from trunk
^^^^^^^^^^^^^^^^^^^^^^^^^
To merge in a change you will need:

* url: url where the fix is located (although knowing that it is in plugins/wms will help)
* before: revision when the fix was started
* after: revision when the fix was complete

1. Grabs the url: like http://svn.geotools.org/geotools/trunk/gt/plugins/wms
2. From your 2.1.x checkout::
      
      svn merge -r 13926:13927 http://svn.geotools.org/geotools/trunk/gt/plugins/wms

3. Do the complete maven cycle of: clean, build, createRelease
4. If the change works all is well commit::
     
     svn commit -m "Applied fix for GEOT-538 from trunk"

5. If not back out the change ... and (re)open a Jira bug about it.::
      
      svn revert -r .

6. Remember: please update any Jira bug to indicate that the issue is fixed on 2.1.x as well.

Backing out a Change
^^^^^^^^^^^^^^^^^^^^

If you find out later that a change is bad sometime after your commit, all is not lost.

1. Merge the change in reverse::
     
     svn merge -r 13927:13926 http://svn.geotools.org/geotools/trunk 

2. And commit::
     
     svn commit -m "Reopened GEOT-538 Javadoc @url was broken"

3. Remember to (re)open any associated Jira issue.
