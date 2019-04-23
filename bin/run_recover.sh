#!/bin/bash

##version 2019.4.11

echo "`date`: run_recover begin."

TARGET_CMD=$1
PARAMETERS=${@:2}
need_continue=1

exit_code=1

while [[ ${exit_code} -ne 0 ]]
do 
  current_time=`date`
  echo "$current_time:run recover start: cmd[$TARGET_CMD] with parameters[$PARAMETERS]"
  eval "\"${TARGET_CMD}\"" ${PARAMETERS}
  exit_code=$?
  sleep 3
done

current_time=`date`
echo "$current_time:run recover finish: cmd[$TARGET_CMD] with parameters[$PARAMETERS]"






