# DeviceDataProcessor

input is the directory path where inout data files are stored.

# build and run
 javac .\src\com\paresh\*.java 
 jar cfm DeviceDataProcessor.jar Manifest.txt .\src\com\paresh\*
 java -cp DeviceDataProcessor.jar com.paresh.Main "C:\Documents\Paresh\data-dump" 
 
 # TODO:
 use mvn for build management
 For squireel event type try to use distributed environment like kafka to produce events in a batch size.