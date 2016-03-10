package de.bs.cli.jpar;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class Values {
	public static String[] createSimpleValueList(final String... values) {
		return values;
	}
	
	public static String[] createSimpleValueList(final Class<?> valueListClass, final String... additionalValues) {
		List<String> values = new LinkedList<String>();
		
		Field[] fields = valueListClass.getFields();
		for (Field field: fields) {
			int modifiers = field.getModifiers();
			if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers)) {
				values.add(field.getName());
			}
		}
		
		for (String additional: additionalValues) {
			values.add(additional);
		}
		return values.toArray(new String[values.size()]);
	}
	
	public static String[][] createSingleGroup(final Class<?> valueListClass, final String... additionalValues) {
		return new String[][]{createSimpleValueList(valueListClass, additionalValues)};
	}
	
	public static String[][] createGroups(final String[]... groups) {
		return groups;
	}
}
