package de.bs.dbinfo;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Types;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Consts {
	private static Map<Integer, String> types;
	private static Map<Integer, String> isolationLevels;
	private static Class<Types> typesClass = Types.class;
	
	static {
		instanziateTypes();
		instanziateIsolationLevels();
	}
	
	private Consts() {
	}
	
	private static void instanziateTypes() {
//		Class<Types> typesClass = Types.class;
		Field[] fields = typesClass.getFields();
		types = new HashMap<Integer, String>();
		
		for (Field field: fields) {
			try {
				types.put(field.getInt(null), field.getName());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void instanziateIsolationLevels() {
		Class<Connection> connectionClass = Connection.class;
		Field[] fields = connectionClass.getFields();
		isolationLevels = new HashMap<Integer, String>();
		
		String name = null;
		for (Field field: fields) {
			name = field.getName();
			if (name.startsWith("TRANSACTION_")) {
				try {
					isolationLevels.put(field.getInt(null), getReadableName(name));
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private static String getReadableName(final String name) {
		String[] nameSplit = name.toLowerCase().split("_");
		StringBuilder sb = new StringBuilder(upperFirst(nameSplit[0]));
		
		for (int i = 1; i < nameSplit.length; i++) {
			sb.append(" ").append(upperFirst(nameSplit[i]));
		}
		
		return sb.toString();
	}
	
	private static String upperFirst(final String value) {
		return value.substring(0, 1).toUpperCase() + value.substring(1);
	}
	
	public static String getTypeName(final int value) {
		return types.get(value);
	}
	
	public static String getIsolationLevel(final Object value) { 
		if (value instanceof Integer) {
			return isolationLevels.get((Integer)value);
		}
		return "<no integer>";
	}
	
	public static Integer intForType(final String text) {
		Exception e = null;
		try {
			return typesClass.getField(text.toUpperCase()).getInt(null);
		} catch (IllegalArgumentException iae) {
			e = iae;
		} catch (IllegalAccessException iae) {
			e = iae;
		} catch (NoSuchFieldException nsfe) {
			e = nsfe;
		} catch (SecurityException se) {
			e = se;
		}
		throw new IllegalArgumentException("Probem with the type '" + text + "', see following.", e);
	}
	
	public static Set<Integer> typesAllSimple() {
		Set<Integer> allowTypes = new HashSet<Integer>();
		
		allowTypes.add(Types.BIGINT);
		allowTypes.add(Types.BINARY);
		allowTypes.add(Types.BIT);
		allowTypes.add(Types.BLOB);
		allowTypes.add(Types.BOOLEAN);
		allowTypes.add(Types.CHAR);
		allowTypes.add(Types.CLOB);
		allowTypes.add(Types.DATE);
		allowTypes.add(Types.DECIMAL);
		allowTypes.add(Types.DISTINCT);
		allowTypes.add(Types.DOUBLE);
		allowTypes.add(Types.FLOAT);
		allowTypes.add(Types.INTEGER);
		allowTypes.add(Types.LONGNVARCHAR);
		allowTypes.add(Types.LONGVARBINARY);
		allowTypes.add(Types.LONGVARCHAR);
		allowTypes.add(Types.NCHAR);
		allowTypes.add(Types.NCLOB);
		allowTypes.add(Types.NULL);
		allowTypes.add(Types.NUMERIC);
		allowTypes.add(Types.NVARCHAR);
		allowTypes.add(Types.REAL);
		allowTypes.add(Types.SMALLINT);
		allowTypes.add(Types.SQLXML);
		allowTypes.add(Types.TIME);
		allowTypes.add(Types.TIME_WITH_TIMEZONE);
		allowTypes.add(Types.TIMESTAMP);
		allowTypes.add(Types.TIMESTAMP_WITH_TIMEZONE);
		allowTypes.add(Types.TINYINT);
		allowTypes.add(Types.VARBINARY);
		allowTypes.add(Types.VARCHAR);
		
//		Types.DATALINK;
//		Types.JAVA_OBJECT;
//		Types.OTHER;
//		Types.REF;
//		Types.REF_CURSOR;
//		Types.ROWID;
//		Types.STRUCT;
		
		return allowTypes;
	}
}
