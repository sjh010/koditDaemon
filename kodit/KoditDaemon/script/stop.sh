#PID Check
pid=$(ps -ef |grep -v grep | grep "Dproject=koditDaemon" |awk {'print $2'})

if [[ $pid = "" ]]
then
	echo "there is no process [edocDaemon]"
	exit;
else
	kill -9 $pid
	echo "koditDaemon : kill -9 $pid"
	while [ $(ps -ef |grep -v grep | grep Dproject=koditDaemon | wc -l) -ne 0 ]
		do
			sleep 1
		done
	echo "stopped koditDaemon"
	exit 0;
fi



