package de.bs.program.argument.extractor;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import de.bs.program.argument.Argument;
import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.process.Arguments;

public class ExtractedArgumentMethod extends ExtractedArgument {

	private Method method;
	
	public ExtractedArgumentMethod(final Method method, final Argument argument) {
		super(argument);
		this.method = method;
		
		if (Modifier.isStatic(method.getModifiers())) {
			throw new ArgumentException(EXC_EXTRACTOR_FIELD_NOT_STATIC, method.toString());
		}
		
		Class<?>[] paramClass = method.getParameterTypes();
		if (paramClass.length == 1) {
			setTargetType(paramClass[0]);
		} else if (paramClass.length == 0) {
			throw new ArgumentException(EXC_EXTRACTOR_NO_ARGUMENTS, method);
		} else {
			throw new ArgumentException(EXC_EXTRACTOR_TO_MANY_ARGUMENTS, method);
		}
	}
	@Override
	public String getTargetName() {
		return method.getName().toUpperCase();
	}

	@Override
	public void prozessArg(final Object program, final String argOption, final String argPart, final Arguments args) {
		Object value = null;
		try {
			value = getType().processArgs(argOption, argPart, args);
			method.invoke(program, value);
		} catch (Exception e) {
			throw new ArgumentException(e, EXC_EXTRACTOR_COULD_NOT_SET, value, argOption);
		}
	}
}
