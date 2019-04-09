#!/bin/bash


echo "hello"

echo $?

cmd=$*
need_continue=1

current_time=`date`
echo $current_time": run cmd["$cmd"]"
$cmd
exit_code=$?
while [ $exit_code -ne 0 ]
do 
  current_time=`date`
  echo "$current_time: recover cmd=[$cmd]"
  $cmd
  exit_code=$?
  sleep 3
done

current_time=`date`

echo "$current_time: FINISH"






