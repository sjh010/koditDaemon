INZI_HOME=/pgms/InziSoft

export LIBPATH=$LIBPATH:${INZI_HOME}/module

# dev
export INZISOFT_LICENSE_FILE=${INZI_HOME}/license/inzi.license.kodit.DCRAP.20211214_OP

# prd1
#export INZISOFT_LICENSE_FILE=${INZI_HOME}/license/inzi.license.kodit.PCRAP1.20211214_OP

# prd2
#export INZISOFT_LICENSE_FILE=${INZI_HOME}/license/inzi.license.kodit.PCRAP2.20211214_OP

# dr
#export INZISOFT_LICENSE_FILE=${INZI_HOME}/license/DR/inzi.license.kodit.PCRAP1.20211214_OP


CLASSPATH=""
# jar
CLASSPATH=$CLASSPATH:${INZI_HOME}/koditDaemon/lib/*
# class
CLASSPATH=$CLASSPATH:${INZI_HOME}/koditDaemon/bin

#PID Check
pid=$(ps -ef |grep -v grep | grep "Dproject=koditDaemon" |awk {'print $2'})

if [[ $pid = "" ]]; then
	echo "start koditDaemon"
	nohup /usr/java8/bin/java -Dproject=koditDaemon com.kodit.daemon.KoditDaemon  1> /dev/null 2> error.info & 
    waitingTime=0
	
	while [ $waitingTime -ne 10 ]
		do
			if [ $(ps -ef |grep -v grep | grep "Dproject=koditDaemon" | wc -l) -eq 1 ]
			then
				echo "koditDaemon is start!! Process ID=$(ps -ef |grep -v grep | grep "Dproject=koditDaemon" |awk {'print $2'})"
				exit 0;
			else
				sleep 1
				echo "waiting $waitingTime seconds..."
				waitingTime=`expr $waitingTime + 1`
			fi
		done
	exit -1;
else
	echo "already koditDaemon running"
	exit 0;
fi


