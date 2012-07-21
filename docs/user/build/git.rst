Using Git
=========

The following helpful git tips, as so many others, are attributed to IanS and have been stolen from his email.

Git Repository
^^^^^^^^^^^^^^

The GeoTools git repository holds the source code for GeoTools:

* https://github.com/geotools/geotools/

This repository is setup with the following branches of GeoTools:

.. list-table::
   :header-rows: 1

   * - Branch
     - Version
   * - `master <https://github.com/geotools/geotools/tree/master>`_
     - this is what we are currently working on
   * - `8.x <https://github.com/geotools/geotools/tree/8.x>`_
     - stable development branch
   * - `2.7.x <https://github.com/geotools/geotools/tree/2.7.x>`_
     - maintenance branch

Typical Development Environment
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Typically, a developer will create a local 'geotools' directory, move into that directory, and do a checkout of the GeoTools repository. Within the 
repository switching between branches is trivial::

   [geotools]% git branch
      2.7.x
      8.x
    * master
   [geotools]% git checkout master
   [geotools]% git checkout 8.x
   [geotools]% git checkout 2.7.x

Developers generally do most work on a branch, even small scale bug fixes. This is typical with Git since branches are so cheap::

   [geotools]% git checkout -b bugfix master
   [geotools]% git branch
      2.7.x
      8.x
    * bugfix
      master
   
A developers repository will usually contain several of these "feature branches" but they generally never get pushed into the main/canonical git
repository.

Git Workflow
^^^^^^^^^^^^

Git is unlike version control systems such as Subversion and CVS in that commits are always local. It is not until one pushes local commits up 
to a repository that they become "public". The basic workflow of a single change in git is:

#. Make a change
#. Stage the change
#. Commit the change
#. Push the change

While extra steps may seem cumbersome at first they are actually very handy in many cases. For example consider making a 
documentation change. ``git status`` gives the current state of the local repository::

  [geotools]% git status
  # On branch master
  # Changes not staged for commit:
  #   (use "git add <file>..." to update what will be committed)
  #   (use "git checkout -- <file>..." to discard changes in working directory)
  #
  #	modified:   advanced/build/source.rst
  #
  no changes added to commit (use "git add" and/or "git commit -a")
  
Which reports that we have modified a single file but have yet to stage the file for commit. We use ``git add`` to 
stage the file for commit::

  [geotools]% git add advanced/build/source.rst
  [geotools]% git status
  # On branch master
  # Changes to be committed:
  #   (use "git reset HEAD <file>..." to unstage)
  #
  #	modified:   advanced/build/source.rst
  #
  
Status now reports the file is ready for commit. The syntax of ``git commit`` is identical to that of Subversion::

  [geotools]% git commit -m "updating documentation for change to git"
  
Doing another status reports no more local changes::

  [geotools]% git status
  # On branch master
  # Your branch is ahead of 'geotools/master' by 1 commit.
  #
  nothing to commit (working directory clean)

But also reports that our local branch is ahead of the remote branch by 1 commit. This is because we have yet to push
the commit. Before pushing it is always a good idea to first pull in case we have any commits that conflict with 
other commits that have already been pushed up to the repostory::

  [geotools]% git pull geotools master
  [geotools]% git push geotools master

At this point the change has been pushed up to the remote git repository.

Ignoring Files
^^^^^^^^^^^^^^

Git uses a special file named ``.gitignore`` that contains a list of files and patterns to ignore::

    .project
    .classpath
    .settings
    target
    *.patch
    *.class

These settings ignore files not suitable to be committed into the repository such as eclipse project
files, compiled class files, and patch files.

Typically a ``.gitignore`` file is located in the root of the repository but such a file can be 
contained in any project directory.

Reverting Changes
^^^^^^^^^^^^^^^^^

How to rollback a change depends on which stage of the workflow the change is at. For changes that have yet to be staged it is 
simply a matter of using ``git checkout``::

  git checkout /path/to/file/to/rollback

If the change has been staged but not yet committed::

  git reset HEAD /path/to/file/to/rollback
  git checkout /path/to/file/to/rollback

If the change has been committed **but not pushed** it gets interesting. If the change to rollback is at the tip of the branch 
(ie is the most recent commit) you can use git reset::

  git reset <previous_commit>
  
Where ``previous_commit`` is the commit that is directly before the commit to rollback.

If the change has been committed **but not pushed** and the change is not at the tip of the branch then an interactive
rebase can be used::

  git rebase -i <previous_commit>
  
Where ``previous_commit`` is the commit that is directly before the commit to rollback. An interactive rebase provides 
an editor that allows us to delete commits from history. With it we can simply delete the commit(s) we wish to revert 
and it will be as if it never happened. Again it is **important** to note that this can only be done on local commits that
have yet to pushed up to a remote repository.

If the change has been committed and pushed up to a remote repository then the only option is to manually roll it back by 
applying a revert commit. Thankfully git provides the ``git revert`` command for just this purposes::

  git revert <commit>

Where ``commit`` is the commit we wish to roll back.

Log
^^^

Tells you info about commits/revision history::
   
   git log

Blame
^^^^^

My favourite. Annotates a document with who changed what and when::
   
   git blame Sample.java