create database geotools using codeset UTF-8 territory US;
update db cfg for geotools using APPLHEAPSZ 2048;
update db cfg for geotools using APP_CTL_HEAP_SZ 2048;
update db cfg for geotools using LOGPRIMARY 10;
update db cfg for geotools using LOGFILSIZ 1000;
update db cfg for geotools using LOCKLIST 100;
!db2se enable_db geotools;
