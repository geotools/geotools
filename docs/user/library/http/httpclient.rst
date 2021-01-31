HTTPClient
----------


Properties
^^^^^^^^^^

========================= ==================================================
Property                    Description
========================= ==================================================
``user``                    Username to use in requests
``password``                Password for the user
``connectTimeout``          Timeout connecting to the host (seconds)
``readTimeout``             Timeout while fetching the response (seconds)
``tryGZip``                 Specify if we should get compressed response
========================= ==================================================


Methods
^^^^^^^
All methods returns a HTTPResponse, and takes a url for the address we should call.

========================================= ==================================================
Signature                                   Description
========================================= ==================================================
post(url, postContent, postContentType)     Make a POST call with postContent as body
get(url)                                    Make a GET call
get(url, headers)                           Make a GET call with the additional headers
========================================= ==================================================