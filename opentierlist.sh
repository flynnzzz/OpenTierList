MODULE_PATH="./lib"
ADD_MODULES="javafx.controls"
JAR_FILE="./target/opentierlist.jar"
LOG_FILE="./otl.log"

if [ ! -d "$MODULE_PATH" ]; then
  echo "[ERROR] --- /lib folder does not exist ---"
fi

if [ ! -f "$JAR_FILE" ]; then
  echo "[ERROR] --- Jar file not found at: $JAR_FILE ---"
fi

java --module-path $MODULE_PATH \
  --add-modules $ADD_MODULES \
  -jar $JAR_FILE \
  2>>$LOG_FILE
