MODULE_PATH="./lib/"
ADD_MODULES="javafx.controls"
JAR_FILE="./target/opentierlist.jar"
LOG_FILE="./otl.log"

java --module-path $MODULE_PATH \
  --add-modules $ADD_MODULES \
  -jar $JAR_FILE \
  2>>$LOG_FILE
