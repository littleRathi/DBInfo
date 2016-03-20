package de.bs.cli.jpar.config;

import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.Option;

public interface ExceptionMessages {
	String EXCEPTION_MISSING_CALLABLE_PROGRAM = "No Class found in the stacktrace that has the @" + CliProgram.class + " annotation.";
	
	String EXC_TYPE_MISSING_DELEMITER = "The property delimiter() from the annotation '@" + Arguments.class + "' have to be set for argument '%s', actual it is '%s'.";
	String EXC_TYPE_MISSING_SOURCE_TYPE = "The property sourceType() from the annotation '@" + Option.class + "' have to be set for argument '%s', actual it is '%s'.";
	String EXC_TYPE_INVALID_VALUE = "The values '%s' is not valid for '%s'.";
	String EXC_TYPE_NOT_VALIDATEABLE = "Argument '%s' cannot have a value list (possible values are already '+' or '-').";
	String EXC_TYPE_WRONG_VALUE = "For boolean argument '%s', only '+' and '-' are valid values (given was '%s').";
	String EXC_TYPE_VALUE_NOT_VALID = "The value '%s' is not valid for argument '%s'.";
	String EXC_TYPE_OBJECT_NOT_INSTANCE = "The class '%s' is not a subclass of '%s'.";
	String EXC_TYPE_OBJECT_NOT_INSTANTIABLE = "The class '%s' could not be instanced for argument '%s'.";
	String EXC_TYPE_NOT_SUPPORTED = "The type '%s' is not supported."; // will not
	String EXC_TYPE_UNSUPPORTED = "The type '%s' is not supported yet."; // perhaps some day. No duplicate to EXC_TYPE_NOT_SUPPORTED
	String EXC_TYPE_MISSING_VALUES = "The argument '%s' needs also a element with the '@" + Arguments.class + "' annotation, to specify some behaviours (required is property delimiter()).";
	String EXC_TYPE_UNKNOWN_COLLECTION_TYPE = "The type '%s' is unkown or not implemented yet.";
	String EXC_TYPE_NEEDED_CONSTRUCTOR = "The type '%s' has no constructor with <init>(String).";
	String EXC_TYPE_COLLECTION_UNSUPPORTED_GEN_TYPE = "The set type '%s' for property sourceType() in '" + Option.class + "' with option name '%s' is not support (need a static valueOf(String) method or constructor with 1 parameter from type String.";
	String EXC_TYPE_COLLECTION_NOT_INSTANCIABLE = "From the collection type '%s' for parameter '%s' could not be instanziated.";
	String EXC_TYPE_GENERIC_TYPE_COLLECTION = "The property sourceType() from '" + Option.class + "' for the parameter '%s' is a collection type (class: is '%s')";
	String EXC_TYPE_SOURCE_MUST_BE_CLASS = "The property sourceType() from annotation '" + Option.class + "' type must be set to '" + Class.class + "' for parameter '%s' but is '%s'.";
	String EXC_TYPE_SOURCE_MUST_NOT_BE_SET = "The property sourceType() from annotation '" + Option.class + "' must not be set for parameter '%s', sourceType() is '%s' for %s.";
	String EXC_TYPE_ARGUMENTS_NOT_ALLOWED = "For Option '%s' is no '" + Arguments.class + "' annotation allowed.";
	String EXC_TYPE_TARGET_TYPE_IS_NULL = "The target type is null for option '%s'.";
	String EXC_TYPE_MISSING_OPTION_ANNOTATION = "Missing '" + Option.class + "' annotation, with given target type '%s'.";
	
	String EXC_EXTRACTOR_DOUBLE_OPTION = "There is already a argument with the name '-%s:'.";
	String EXC_EXTRACTOR_SEVERAL_ARGUMENT_PROGRAM = "The class '%s' contains serveral '@" + CliProgram.class + "' Annotations, but only one is allowed.";
	String EXC_EXTRACTOR_NO_ARGUMENT_PROGRAM = "The class '%s' should contain exactly one '@" + CliProgram.class + "' Annotation.";
	String EXC_EXTRACTOR_ARGUMENTS_NAME_MISSING = "The %s '%s' with '@" + Arguments.class + ".name()' is required when '@" + Option.class + "' is not present on the same element.";
	String EXC_EXTRACTOR_ARGUMENTS_NAME_NOT_ALLOWED = "The %s '%s' with property name() from '@" + Arguments.class + "' is not allowed, when '@" + Option.class + "' is present on the same element.";
//	String EXC_EXTRACTOR_ARGUMENT_VALUES_MISSING_ARGUMENT = "No field or method found that have the '@" + Option.class + "' annotation with the name argument -%s'.";
	String EXC_EXTRACTOR_NAME_WRONG_PATTERN = "The value of property name() from '@" + Option.class + "' is '%s', that does not follow the convetion '%s'.";
	String EXC_EXTRACTOR_COULD_NOT_SET = "Value '%s' could not be set for parameter '%s'.";
	String EXC_EXTRACTOR_TO_MANY_ARGUMENTS = "The method '%s' contains to many arguments, allowed is only one.";
	String EXC_EXTRACTOR_NO_ARGUMENTS = "The method '%s' contains no arguments, but one is needed.";
	String EXC_EXTRACTOR_NEED_VALUES = "The property values() from the '@" + Arguments.class + "' must be a array with minimum 1 value fro argument '%s'.";
	String EXC_EXTRACTOR_VALUES_NO_NAME = "The '@" + Arguments.class + "' annotation for argument '%s' has a value for property name(), which should not be set, because the field/method has also the '@" + Option.class + " annotation.";
	String EXC_EXTRACTOR_NO_VALUES_ALLOWED = "The property values() from '@" + Arguments.class + "' at the element '%s' for argument '%s' should be empty/not be set.";
	String EXC_EXTRACTOR_VALUES_NEED_NAME = "The property name() from '@" + Arguments.class + " have to be set for element '%s'.";
	String EXC_EXTRACTOR_VALUES_ELEMENT_NOT_STATIC = "The element '%s' is not static. A element with the '@" + Arguments.class + "' annotation must be static.";
	String EXC_EXTRACTOR_VALUES_WRONG_TYPE = "The element '%s' should be a array of String as field type or only parameter type of the method, that is annotated with '@" + Arguments.class + "'.";
	String EXC_EXTRACTOR_VALUES_GET_VALUES = "The values from the element '%s' could not be get for property '%s'.";
	String EXC_EXTRACTOR_VALUES_IS_NULL = "The element '%s' contains no values (is null).";
	String EXC_EXTRACTOR_VALUES_MISSING_GROUPS = "In the array from element '%s' should be at least one 'group'.";
	String EXC_EXTRACTOR_VALUES_MISSING_CONTENT = "In the array from element '%s' should be at least contains one value.";
	String EXC_EXTRACTOR_VALUES_METHOD_NO_PARAMETERS = "The method '%s' cannot have any parameters, but have a count of '%s' parameters.";
	String EXC_EXTRACTOR_FIELD_NOT_STATIC = "The element '%s' is static, a element with a '@" + Option.class + "' annotation havn't to be static.";
	String EXC_EXTRACTOR_ARGUMENTS_DOUBLE = "There is already element with a '" + Arguments.class + "' annotation assign to option '%s'.";
	String EXC_EXTRACTOR_MISSING_PROG_CLASS = "Missing a class that taht contains the '" + CliProgram.class + "' annotation.";
	String EXC_EXTRACTOR_MISSING_PROG_ANNOTATION = "Missing '" + CliProgram.class + "' annotation for the given class '%s'.";
	
	String EXC_PROCESS_MISSING_REQUIRED_ARGUMENTS = "Missing following required arguments: '%s'.";
	String EXC_PROCESS_NO_ARGUMENT_ALLOWED = "For option '%s' are no arguments allowed.";
}
