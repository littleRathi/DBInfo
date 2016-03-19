package de.bs.cli.jpar;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.bs.cli.jpar.config.Consts;

// TODO Rename property values to validValues()?
// rename annotation to ValidArguments is wrong, because values is not required
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Arguments {
	String name() default Consts.NAME;
	String delimiter() default Consts.EMPTY; //Consts.LIST_DELIMITER; // change to EMPTY and later if empty change to default
	// must be set, when the Annotation is added with the 
	// @Argument annotation (so the field, contains the
	// target for the selected value! In this case, name
	// must not be set. When @Argument is not present
	// the the return 
	// 2016.03.03 -> perhaps not needed to be set, if not set
	// it is a flexible list of sourceType divided by the given delimiter sign
	String[] values() default {};
}
