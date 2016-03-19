package de.bs.cli.jpar;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import de.bs.cli.jpar.config.Consts;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface Option {
	String name();
	/* The description of the option, and what it does. */
	String description();
	/* To mark the option as required. */
	boolean required() default Consts.REQUIRED;
	/* Can be used, when there is no real targetField,
	 * but there must be implizit or expizit a field, 
	 * or method -> in other words, there have to be a 
	 * target. Especially, when implementing the method 
	 * to set the values. Could be usefull by list, damn
	 * generic types ^^ */
	Class<?> sourceType() default Void.class;
}
