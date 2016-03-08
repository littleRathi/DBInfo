package de.bs.program.argument.extractor.type;

import java.awt.List;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;

import de.bs.program.argument.ArgumentException;
import de.bs.program.argument.extractor.ExtractedArgument;
import de.bs.program.argument.process.Arguments;

public class CollectionType extends Type {
	public CollectionType(Class<?> targetType, ExtractedArgument extractedArgument) {
		super(targetType, extractedArgument);
		getCollectionObject(targetType);
	}

	@Override
	public void getUsageDescription(final StringBuilder descriptionBuilder) {
		ExtractedArgument ap = getExtractedArgument();
		Class<?> listType = ap.getSourceType();
		if (ap.getDelimiter() == null || ap.getDelimiter().isEmpty()) {
			throw new ArgumentException(EXC_TYPE_MISSING_DELEMITER, ap.getArgumentName(), ap.getDelimiter());
		}
//		Needed, because, the generic type of Set gets erased (so this information is missing)
		if (listType == null || listType == Void.class || listType == void.class) {
			throw new ArgumentException(EXC_TYPE_MISSING_SOURCE_TYPE, ap.getArgumentName(), ap.getDelimiter());
		}
		if (ap.getValues() != null && ap.getValues().length > 0) {
			listType = String.class;
		}
		
		descriptionBuilder.append(getExtractedArgument().getArgumentName()).append("<").append(listType.getSimpleName()).append(">[").append(ap.getDelimiter()).append("<").append(listType.getSimpleName()).append(">]");
		
		createWithSpecific(ap, descriptionBuilder, true);
	}

	@Override
	public boolean isAssignable(Object value) {
		return false;
	}

	@Override
	public Object processArgs(String option, String argPart, Arguments args) {
		String data = argPart.substring(option.length());
		if (getExtractedValues() == null) {
			throw new ArgumentException(EXC_TYPE_MISSING_VALUES, option);
		}
		String[] argValues = data.split(getExtractedValues().getDelimiter());
		
		if (getExtractedValues() != null && !getExtractedValues().validValues(argValues)) {
			throw new ArgumentException(EXC_TYPE_INVALID_VALUE, listToString(argValues), getExtractedArgument().getArgumentName());
		}
		
		Class<?> sourceType = getExtractedArgument().getSourceType();
		Collection<Object> collection = getCollectionObject(getTargetType());
		for (String value: argValues) {
			collection.add(castTo(sourceType, value));
		}
		
		return collection;
	}
	
	private String listToString(final String[] values) {
		if (values != null && values.length > 0) {
			StringBuilder sb = new StringBuilder(values[0]);
			for (int i = 1; i < values.length; i++) {
				sb.append(", ").append(values[i]);
			}
		}
		return "";
	}
	
	private static Object castTo(final Class<?> newType, final String value) {
		if (String.class.equals(newType)) {
			return value;
		}
		
		try {
			Constructor<?> con = newType.getConstructor(String.class);
			return con.newInstance(value);
		} catch (NoSuchMethodException e) {
			throw new ArgumentException(e, EXC_TYPE_NEEDED_CONSTRUCTOR, newType);
		} catch (Exception e) {
			throw new ArgumentException(e,EXC_TYPE_INTERNAL, value, newType.toString());
		}
	}
	
	private static Collection<Object> getCollectionObject(final Class<?> collectionType) {
		if (HashSet.class.equals(collectionType)) {
			return new HashSet<Object>();
		}
		if (ArrayList.class.equals(collectionType)) {
			return new ArrayList<Object>();
		} 
		if (LinkedList.class.equals(collectionType)) {
			return new LinkedList<Object>();
		}
		if (PriorityQueue.class.equals(collectionType)) {
			return new PriorityQueue<Object>();
		}
		if (Set.class.equals(collectionType)) {
			return new HashSet<Object>();
		}
		if (List.class.equals(collectionType)) {
			return new LinkedList<Object>();
		}
//		if (Queue.class.equals(collectionType)) {
//			return null
//		}
		throw new ArgumentException(EXC_TYPE_UNKNOWN_COLLECTION_TYPE, collectionType);
	}

	private static void createWithSpecific(final ExtractedArgument ap, final StringBuilder result, final boolean multiple) {
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
