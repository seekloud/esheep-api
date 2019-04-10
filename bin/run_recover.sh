#!/bin/bash


echo "`date`: run_recover begin."

TARGET_CMD=$1
PARAMETERS=${@:2}
need_continue=1

current_time=`date`
echo ${current_time}": run cmd[$TARGET_CMD] with parameters[$PARAMETERS]"
eval exec "\"${TARGET_CMD}\"" \"\$PARAMETERS\"
exit_code=$?
while [ ${exit_code} -ne 0 ]
do 
  current_time=`date`
  echo "$current_time: recover cmd[$TARGET_CMD] with parameters[$PARAMETERS]"
  eval exec "\"${TARGET_CMD}\"" \"\$PARAMETERS\"
  exit_code=$?
  sleep 3
done

current_time=`date`

echo "$current_time: FINISH"






