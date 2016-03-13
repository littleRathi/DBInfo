# DBInfo
Little tool that extract some information from a db connection


## InfoExtractor

Is the program that extract data from the db connection. Depending on the database that is used, the 
correct db driver is also needed and in classpath. An example would be:
java de.bs.dbinfo.InfoExtractor -user:MyUser -pw:Co0lPw -url:jdbc:postgresql://localhost:5432/abc

To get Information about all possible options use -help option:
java de.bs.dbinfo.InfoExtractor -help


# jPar
Library to manager to handle parameters which are given on program start.

## API

Relevant are only the package de.bs.cli.jpar:

- @CliProgram annotation: to mark a class, that contains all the options
- @Option: mark a method or field as a option, method should only have one parameter
- @Arguments: that to mark possible values for an option, can be on a field/method that also marked with @Option or on a method, that has no parameters and returns String\[][]
- JPar: method that take care of processing parameters or creating manual information
- Values: contains static method for creating String\[][] arrays, useful for methods annotated with @Arguments

## Todo
- ObjectType; create simple instances from targetType, with String as a parameter: example would be new File(arg)
- extends de.bs.cli.jpar.process.Parameters with methods:
 * isNextOption(): boolean
- include in the manualDescription part the valid values + add tests for that

## Structures

### Annotation extracted structures

ExtractedOption (dependencies)
	-> Type
	-> ExtractedArguments
Type (dependencies to)
	-> ExtractedOption
	-> ExtractedArguments
ExtractedArguments (dependencies to)
	-> nothing


