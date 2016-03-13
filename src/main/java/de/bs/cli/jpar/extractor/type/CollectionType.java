package de.bs.cli.jpar.extractor.type;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedArguments;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class CollectionType extends Type {
	public CollectionType(final Class<?> targetType, final ExtractedOption option, final ExtractedArguments arguments) {
		super(targetType, option, arguments);
		Class<?> genericType = option.getSourceType();
//		Needed, because, the generic type of Set gets erased (so this information is missing)
		if (genericType == null || genericType == Void.class || genericType == void.class) {
			throw new JParException(EXC_TYPE_MISSING_SOURCE_TYPE, option.getOptionName(), genericType);
		}
		
		if (arguments == null) {
			throw new JParException(EXC_TYPE_MISSING_VALUES, getOption().getOptionName());
		}
		if (arguments.getDelimiter() == null || arguments.getDelimiter().isEmpty()) {
			throw new JParException(EXC_TYPE_MISSING_DELEMITER, option.getOptionName(), arguments.getDelimiter());
		}
		if (arguments.getValues() != null && arguments.getValues().length > 0) {
			genericType = String.class;
		}
		
		getCollectionObject(targetType);
		checkGenericType(genericType);
	}

	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		ExtractedOption option = getOption();
		Class<?> listType = option.getSourceType();
		descriptionBuilder.append(getOption().getOptionName()).append("<").append(listType.getSimpleName()).append(">[").append(getArguments().getDelimiter()).append("<").append(listType.getSimpleName()).append(">]");
		
		createWithSpecific(option, descriptionBuilder, true);
	}

	@Override
	public Object processArgs(String option, String argument, Parameters args) {
		String[] argValues = argument.split(getArguments().getDelimiter());
		
		if (getArguments() != null && !getArguments().validValues(argValues)) {
			throw new JParException(EXC_TYPE_INVALID_VALUE, argument, getOption().getOptionName());
		}
		
		Class<?> sourceType = getOption().getSourceType();
		Collection<Object> collection = getCollectionObject(getTargetType());
		for (String value: argValues) {
			collection.add(castTo(sourceType, value));
		}
		
		return collection;
	}
	
	private void checkGenericType(final Class<?> type) {
		Method valueOf = null;
		
		if (Collection.class.isAssignableFrom(type)) {
			throw new JParException(EXC_TYPE_GENERIC_TYPE_COLLECTION, getOption().getOptionName(), type);
		}
		
		try {
			valueOf = type.getMethod("valueOf", String.class);
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		}
		
		if (valueOf == null) {
			Constructor<?> con = null;
			try {
				con = type.getConstructor(String.class);
			} catch (NoSuchMethodException e) {
			} catch (SecurityException e) {
			}
			
			if (con == null) {
				throw new JParException(EXC_TYPE_COLLECTION_UNSUPPORTED_GEN_TYPE, type, getOption().getOptionName());
			}
		}
	}
	
	// TODO move to Type?
	private static Object castTo(final Class<?> newType, final String value) {
		if (String.class.equals(newType)) {
			return value;
		}

		Object result = castWithValueOf(newType, value);
		
		if (result != null) {
			result = castWithConstructor(newType, value);
		}
		
		if (result == null) {
			throw new JParException(EXC_TYPE_NEEDED_CONSTRUCTOR, newType);
		}
		
		return result;
	}
	
	private static Object castWithConstructor(final Class<?> newType, final String value) {
		try {
			Constructor<?> con = newType.getConstructor(String.class);
			return con.newInstance(value);
		} catch (NoSuchMethodException e) {
		} catch (Exception e) {
		}
		return null;
	}
	
	private static Object castWithValueOf(final Class<?> newType, final String value) {
		try {
			Method valueOf = newType.getMethod("valueOf", String.class);
			
			if (value != null && Modifier.isStatic(valueOf.getModifiers())) {
				return valueOf.invoke(null, value);
			}
		} catch (NoSuchMethodException e) {
		} catch (SecurityException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (InvocationTargetException e) {
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Object> getCollectionObject(final Class<?> collectionType) {
		if (Set.class.equals(collectionType)) {
			return new HashSet<Object>();
		}
		
		if (List.class.equals(collectionType)) {
			return new LinkedList<Object>();
		}
		
		if (Collection.class.equals(collectionType)) {
			return new HashSet<Object>();
		}
		
		if (Collection.class.isAssignableFrom(collectionType)) {
			try {
				return (Collection<Object>)collectionType.newInstance();
			} catch (InstantiationException e) {
				throw new JParException(EXC_TYPE_COLLECTION_NOT_INSTANCIABLE, collectionType, getOption().getOptionName());
			} catch (IllegalAccessException e) {
				throw new JParException(EXC_TYPE_COLLECTION_NOT_INSTANCIABLE, collectionType, getOption().getOptionName());
			}
		}
		throw new JParException(EXC_TYPE_UNKNOWN_COLLECTION_TYPE, collectionType);
	}

	private void createWithSpecific(final ExtractedOption option, final StringBuilder result, final boolean multiple) {
		String[][] values = getArguments().getValues();
		if (values != null) {
			result.append(". Following values can be used: ");
			for (int i = 0; i < values.length; i++) {
				String[] subValues = values[i];
				
				if (subValues.length > 1) {
					if (multiple) {
						result.append("one or more of ");
					} else {
						result.append("one of ");
					}
					result.append(subValues[0]);
					for (int j = 0; j < subValues.length; j++) {
						result.append(", ").append(subValues[j]);
					}
					result.append(" ");
				} else {
					result.append("single option " + subValues[0]);
				}
			}
		}
	}
}
