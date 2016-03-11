package de.bs.cli.jpar.extractor.type;

import java.awt.List;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

import de.bs.cli.jpar.JParException;
import de.bs.cli.jpar.extractor.ExtractedOption;
import de.bs.cli.jpar.process.Parameters;

public class CollectionType extends Type {
	public CollectionType(Class<?> targetType, ExtractedOption option) {
		super(targetType, option);
		Class<?> genericType = option.getSourceType();
		if (option.getDelimiter() == null || option.getDelimiter().isEmpty()) {
			throw new JParException(EXC_TYPE_MISSING_DELEMITER, option.getOptionName(), option.getDelimiter());
		}
//		Needed, because, the generic type of Set gets erased (so this information is missing)
		if (genericType == null || genericType == Void.class || genericType == void.class) {
			throw new JParException(EXC_TYPE_MISSING_SOURCE_TYPE, option.getOptionName(), genericType);
		}
		if (option.getValues() != null && option.getValues().length > 0) {
			genericType = String.class;
		}
		
		getCollectionObject(targetType);
		checkGenericType(genericType);
	}

	@Override
	public void getManualDescription(final StringBuilder descriptionBuilder) {
		ExtractedOption option = getOption();
		Class<?> listType = option.getSourceType();
		descriptionBuilder.append(getOption().getOptionName()).append("<").append(listType.getSimpleName()).append(">[").append(option.getDelimiter()).append("<").append(listType.getSimpleName()).append(">]");
		
		createWithSpecific(option, descriptionBuilder, true);
	}

	@Override
	public boolean isAssignable(Object value) {
		if (value == null) {
			return false;
		} else {
			return getTargetType().isAssignableFrom(value.getClass());
		}
	}

	@Override
	public Object processArgs(String argumentName, String argumentValue, Parameters args) {
		if (getValues() == null) {
			throw new JParException(EXC_TYPE_MISSING_VALUES, getOption().getOptionName()); // TODO make only delimiter required
		}
		String[] argValues = argumentValue.split(getValues().getDelimiter());
		
		if (getValues() != null && !getValues().validValues(argValues)) {
			throw new JParException(EXC_TYPE_INVALID_VALUE, argumentValue, getOption().getOptionName());
		}
		
		Class<?> sourceType = getOption().getSourceType();
		Collection<Object> collection = getCollectionObject(getTargetType());
		for (String value: argValues) {
			collection.add(castTo(sourceType, value));
		}
		
		return collection;
	}
	
//	private String listToString(final String[] values) {
//		if (values != null && values.length > 0) {
//			StringBuilder sb = new StringBuilder(values[0]);
//			for (int i = 1; i < values.length; i++) {
//				sb.append(", ").append(values[i]);
//			}
//		}
//		return "";
//	}
	
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
//TODO			throw new JParException(e,EXC_TYPE_INTERNAL, value, newType.toString());
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
//		if (HashSet.class.equals(collectionType)) {
//			return new HashSet<Object>();
//		}
		if (Set.class.equals(collectionType)) {
			return new HashSet<Object>();
		}
		
//		if (ArrayList.class.equals(collectionType)) {
//			return new ArrayList<Object>();
//		} 
//		if (LinkedList.class.equals(collectionType)) {
//			return new LinkedList<Object>();
//		}
		if (List.class.equals(collectionType)) {
			return new LinkedList<Object>();
		}
		
//		if (PriorityQueue.class.equals(collectionType)) {
//			return new PriorityQueue<Object>();
//		}
		
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
//		if (Queue.class.equals(collectionType)) {
//			return null
//		}
		throw new JParException(EXC_TYPE_UNKNOWN_COLLECTION_TYPE, collectionType);
	}

	private static void createWithSpecific(final ExtractedOption ap, final StringBuilder result, final boolean multiple) {
		String[][] values = ap.getValues();
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