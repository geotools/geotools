#!/usr/bin/env bash
set -eux

export LD_LIBRARY_PATH=
# this sets up some informix specific env
. /opt/ibm/scripts/informix_inf.env
export CLIENT_LOCALE=en_US.819
export DB_LOCALE=en_US.819
export PATH=/opt/ibm/informix/bin:.:/home/informix/.local/bin:/home/informix/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

dbaccess sysmaster /home/informix/setup-informix-database
