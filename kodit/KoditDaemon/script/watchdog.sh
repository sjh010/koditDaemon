WATCH_HOME=/apps/inzisoft/koditDaemon/script/watchdog

while [ 1 ]
do
  daemon_pid=`ps -ef | grep -v grep | grep "Dproject=koditDaemon" | grep -v vi | awk '{print $2}'`
  DATE=$(date "+%Y-%m-%d %H:%M:%S")

  if [ "${daemon_pid}" == "" ]
  then
    echo "[$DATE] koditDaemon start" >> $WATCH_HOME/restart.log
    watchdog_pid=`ps -ef | grep -v grep | grep "/bin/sh /pgms/InziSoft/koditDaemon/script/start.sh" | grep -v vi | awk '{print $2}'`
    /bin/sh /pgms/InziSoft/koditDaemon/script/start.sh &
    echo "[$DATE] koditDaemon start complete" >> $WATCH_HOME/restart.log
    
    kill -9 ${watchdog_pid}

    #sleep 5
    #continue
  fi
  
  sleep 10 
done

