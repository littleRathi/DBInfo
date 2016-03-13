package de.bs.cli.jpar.extractor.type;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class ClassObjectType extends Type {
	public ClassObjectType(final Class<?> targetType, final ExtractedOption option, final ExtractedArguments arguments) {
		super(targetType, option, arguments);
		
		if (!Class.class.equals(option.getSourceType())) {
			throw new JParException(EXC_TYPE_SOURCE_MUST_BE_CLASS, option.getOptionName(), option.getSourceType());
		}
	}
	
	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		String extendVariant = getTargetType().isInterface() ? "implements" : "extends";
		String type = getTargetType().isInterface() ? "Interface" : "Class";
		
		descriptionBuilder.append(getOption().getOptionName()).append("<class> where given class has to ").append(extendVariant).append(" ")
			.append(getTargetType().getName()).append(" ").append(type)
			.append(" and need a constructor without parameters (Default constructor).");
	}
	
	@Override
	public Object processArgs(String option, String argument, Parameters args) {
		String className = argument;
		
		if (getArguments() != null && !getArguments().validValue(className)) {
			throw new JParException(EXC_TYPE_VALUE_NOT_VALID, className, getOption().getOptionName());
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
				throw new JParException(EXC_TYPE_OBJECT_NOT_INSTANTIABLE, className, getOption().getOptionName());
			}
		}
		return null;
	}
}
