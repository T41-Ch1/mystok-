#!/bin/sh
sed -i _backup -e s@ conf/server.xml
rm -f conf/server.xml_backup

