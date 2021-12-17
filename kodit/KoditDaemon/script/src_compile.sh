DAEMON_HOME=/apps/inzisoft/koditDaemon

cd $DAEMON_HOME

CLASSPATH=""
# jar
CLASSPATH=$CLASSPATH:${DAEMON_HOME}/lib/*
# class
CLASSPATH=$CLASSPATH:${DAEMON_HOME}/bin

export CLASSPATH

SRC_PATH=${DAEMON_HOME}/src/$1
BIN_PATH=${DAEMON_HOME}/bin/$1

cd $SRC_PATH

echo "compile $SRC_PATH/$2.java"

# compile
javac $2.java

echo "move $2.class file to $BIN_PATH

mv $2.class $BIN_PATH



