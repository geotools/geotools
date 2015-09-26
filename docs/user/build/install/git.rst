Git Install
-----------

Git is an advanced version management tool.

Git offers the GeoTools project:

* the ability to version directories and renames
* the use of GitHub hosting facilities

Reference:

* `The Git Book <http://git-scm.com/book/>`_
* `Understanding Git Conceptually <http://www.sbf5.com/~cduan/technical/git/>`_
* `Setup Git - GitHub Help <https://help.github.com/articles/set-up-git>`_
* `Git Cheatsheet <http://ndpsoftware.com/git-cheatsheet.html>`_
* `Git Pretty <http://justinhileman.info/article/git-pretty/>`_

Developers guide:

* `Working with Git <http://docs.geotools.org/latest/developer/procedures/git.html>`_
* `Pull Requests <http://docs.geotools.org/latest/developer/procedures/pull_requests.html>`_

Although desktop applications and IDE integration are available, no GUI will substitute for an understanding of the underlying git repository model. Even if you have previous version control experience 

Downloads:

* http://git-scm.com/downloads
* https://code.google.com/p/tortoisegit/ windows shell integration
* https://windows.github.com - user interface with *sync* button, will set you up with latest command line tools
* https://mac.github.com  - the same *sync* button for mac
* http://www.git-tower.com - commercial client for mac

Git Preflight Configuration
'''''''''''''''''''''''''''

#. Configure git with your user name::

     git config user.name "Jane Doe"
     
   And email address::
   
     git config --global user.email janedoe@example.com

#. Ensure your git configuration is a friendly one to cross platform projects::

     git config --global core.autocrlf input
   
   This option may also be set on a repository by repository basis if for some reason 
   you require a different global default. You can verify your git configuration with::
   
     git config --list
     