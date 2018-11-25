## Getting an app
The simplest way to get our application is to download JAR file app.jar, which includes all dependencies, and run it using command:

`java -jar app.jar`

## Requirements
The app should run on every system where Oracle’s JRE/JDK is installed. It does not, however, run on systems with OpenJDK, as that implementation does not contain `javafx` library.

## Compiling from source
If you want to build the app on your own, the following libraries should be included:


- `jfoenix`
- `javax.json`
- `javafx-autocomplete-field`


All of the above can be downloaded from the github page of the project in form of the JAR files. Setting up the app can be done using any  IDE of preference or by sequence of commands:

Linux
```Shell
mkdir out
javac -classpath javafx-autocomplete-field-1.0.jar:javax.json-1.0.4.jar:jfoenix.jar -d out src/idhifi/row_items/*.java src/idhifi/*.java
cp -r src/idhifi/assets src/idhifi/App.css out/idhifi
java -classpath javafx-autocomplete-field-1.0.jar:javax.json-1.0.4.jar:jfoenix.jar:out idhifi.Main
```

Windows
```Batchfile
mkdir out
javac -classpath javafx-autocomplete-field-1.0.jar;javax.json-1.0.4.jar;jfoenix.jar -d out src/idhifi/row_items/*.java src/idhifi/*.java
xcopy  src\idhifi\assets out\idhifi
copy src\idhifi\App.css out\idhifi\assets\
java -classpath javafx-autocomplete-field-1.0.jar;javax.json-1.0.4.jar;jfoenix.jar;out idhifi.Main
```

## Credits
In our work we have used following APIs:
- [Dark Sky API](https://darksky.net/dev/) - for weather data
- [Google Maps Geocoding API](https://developers.google.com/maps/documentation/geocoding/start) - for converting addresses into geographic coordinates

We have also used following libraries:
- [jfoenix](http://www.jfoenix.com/)
- [javax.json](https://docs.oracle.com/javaee/7/api/javax/json/package-summary.html)
- [javafx-autocomplete-field](https://github.com/privatejava/javafx-autocomplete-field)
