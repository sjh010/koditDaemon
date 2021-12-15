#PID Check
pid=$(ps -ef |grep -v grep | grep "Dproject=koditDaemon" |awk {'print $2'})

if [[ $pid = "" ]]
then
	echo "there is no process [edocDaemon]"
	exit;
else
	echo "koditDaemon is running.. PID : $pid"
	exit 0;
fi



