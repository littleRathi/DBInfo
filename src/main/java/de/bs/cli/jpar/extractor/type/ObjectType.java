package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class ObjectType extends Type {
	public ObjectType(final Class<?> targetType, final ExtractedOption extractedArgument) {
		super(targetType, extractedArgument);
	}
	
	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		String extendVariant = getTargetType().isInterface() ? "implements" : "extends";
		String type = getTargetType().isInterface() ? "Interface" : "Class";
		
		descriptionBuilder.append(getExtractedArgument().getOptionName()).append("<class> where given class has to ").append(extendVariant).append(" ")
			.append(getTargetType().getName()).append(" ").append(type)
			.append(" and need a constructor without parameters (Default constructor).");
	}
	
	@Override
	public boolean isAssignable(final Object value) {
		return true;
	}
	
	@Override
	public Object processArgs(String argumentName, String argumentValue, Parameters args) {
		String className = argumentValue;
		
		if (getExtractedValues() != null && !getExtractedValues().validValue(className)) {
			throw new JParException(EXC_TYPE_VALUE_NOT_VALID, className, getExtractedArgument().getOptionName());
		}
		
		if (className != null && !className.isEmpty()) {
			try {
				Class<?> objectClass = Class.forName(className);
				
				if (getTargetType().isAssignableFrom(objectClass)) {
					return objectClass.newInstance();
				} else {
					throw new JParException(EXC_TYPE_OBJECT_NOT_INSTANCE, className, getTargetType());
				}
			} catch (Exception e) {
				throw new JParException(EXC_TYPE_OBJECT_NOT_INSTANTIABLE, className, getExtractedArgument().getOptionName());
			}
		}
		return null;
	}

}
