# DBInfo
Little tool that extract some information from a db connection


## InfoExtractor

Is the program that extract data from the db connection. Depending on the database that is used, the 
correct db driver is also needed and in classpath. An example would be:
java de.bs.dbinfo.InfoExtractor -user:MyUser -pw:Co0lPw -url:jdbc:postgresql://localhost:5432/abc

To get Information about all possible options use -help option:
java de.bs.dbinfo.InfoExtractor -help

