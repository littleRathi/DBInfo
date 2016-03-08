package de.bs.program.argument.extractor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import de.bs.program.argument.Argument;
import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.process.Arguments;

public class ExtractedArgumentField extends ExtractedArgument {
	
	private Field field;
	
	public ExtractedArgumentField(final Field field, final Argument argument) {
		super(argument);
		this.field = field;
		
		if (Modifier.isStatic(field.getModifiers())) {
			throw new ArgumentException(EXC_EXTRACTOR_FIELD_NOT_STATIC, field.toString());
		}
		setTargetType(field.getType());
	}
	@Override
	public String getTargetName() {
		return field.getName().toUpperCase();
	}

	@Override
	public void prozessArg(final Object program, final String argOption, final String argPart, final Arguments args) {
		Object value = null;
		try {
			value = getType().processArgs(argOption, argPart, args);
			field.set(program, value);
		} catch (Exception e) {
			throw new ArgumentException(e, EXC_EXTRACTOR_COULD_NOT_SET, value, argOption);
		}
	}
}
