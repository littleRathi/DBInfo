package de.bs.dbinfo;

import static de.bs.dbinfo.CallUtil.callMethod;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Set;

import de.bs.cli.jpar.Option;
import de.bs.cli.jpar.Values;
import de.bs.cli.jpar.CliProgram;
import de.bs.cli.jpar.Arguments;
import de.bs.cli.jpar.JPar;
import de.bs.dbinfo.exporter.DataBlock;
import de.bs.dbinfo.exporter.Exporter;
import de.bs.dbinfo.exporter.console.ConsoleExporter;

@CliProgram(name = "DB Info Extractor", description = "This Programm extract (meta)data from a connection to a databse using JDBC. "
		+ " To etablish a connection, the JDBC driver is needed, also with the required in classpath and the "
		+ " options ${*.required} (see option description for more information on these options). "
		+ " Here is a generated list: ",
		authors={"littleRathi"})
public class InfoExtractor {
	private static final String OPTION_TYPE_SIMPLE = "simple";

	@Option(name = "url", description = "The connection url for the database.", required = true)
	private String url;
	@Option(name = "user", description = "The user for the database.", required = true)
	private String user;
	@Option(name = "pw", description = "The password for the given user for the database connection.", required = true)
	private String pw;

	@Option(name = "metaData", description = "Enable or Disable the info category for mataData. Enable this, to see categories.")
	private boolean showMetaDataInfo = true;
	@Option(name = "typeInfo", description = "Enable or Disable the info category for typeInfo. Enable this, to see type infos.")
	private boolean showTypeInfo = true;
	@Option(name = "maxValues", description = "Enable or Disable the info category for maxValues. Enable this, to see all max values.")
	private boolean showMaxValues = true;
	@Option(name = "supportValues", description = "Enable or Disable the info category for supportValues. Enable this, to see support values.")
	private boolean showSupportValues = true;
	@Option(name = "connectionInfo", description = "Enable or Disable the info category for connectionInfo. Enable this, to see connection informations.")
	private boolean showConnectionInfo = true;
	@Option(name = "catalogs", description = "Enable or Disable the info category for catalogs. Enable this, to see catalogs.")
	private boolean showCatalogs = true;
	@Option(name = "schemas", description = "Enable or Disable the info category for schemas. Enable this, to see schemas.")
	private boolean showSchemas = true;
	@Option(name = "tables", description = "Enable or Disable the info category for tables. Enable this, to see all tables.")
	private boolean showTables = true;

	@Option(name = "exporter", description = "Class of the exporter that have to be used, default is de.bs.dbinfo.exporter.console.ConsoleExporter.",
			sourceType = Class.class)
	private Exporter useExporter = new ConsoleExporter();

	private Set<Integer> allowTypes = null;

	@Option(name = "all", description = "Enable or Disable all info categories (following options: ${SHOW*}) for "
			+ "showing information, see other options for all info categories.")
	private void setAll(final boolean allValue) {
		Class<?> ieClass = InfoExtractor.class;

		Field[] showFields = ieClass.getDeclaredFields();
		for (Field field : showFields) {
			if (field.getName().startsWith("show")) {
				try {
					field.setAccessible(true);
					field.setBoolean(this, allValue);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Option(name = "types", description = "List of type that should be shown if the argument ${TYPES} "
			+ " is active.", sourceType=String.class)
	private void setTypes(final Set<String> types) {
		allowTypes = new HashSet<Integer>();

		if (types.size() == 1) {
			String type = types.toArray(new String[1])[0];
			if (OPTION_TYPE_SIMPLE.equals(type)) {
				allowTypes = Consts.typesAllSimple();
			} else {
				allowTypes.add(Consts.intForType(type));
			}
		} else if (types.size() > 1) {
			for (String type : types) {
				allowTypes.add(Consts.intForType(type));
			}
		}
	}
	
	@Arguments(name = "types")
	private static String[][] getTypeValues() {
		return Values.createGroups(
				Values.createSimpleValueList(Types.class), 
				Values.createSimpleValueList("simple")
		);
	}

	private Connection connection;

	public static void main(String[] args) {
		InfoExtractor ie = new InfoExtractor(args);
		ie.run(ie.useExporter);
	}

	public InfoExtractor(final String[] args) {
		JPar.process(this, args);
	}

	public void run(final Exporter exporter) {
		try {
			connection();

			extractInfos(connection, exporter);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	private void close() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
			}
		}
	}

	private void connection() throws SQLException {
		connection = DriverManager.getConnection(url, user, pw);
	}

	private void extractInfos(final Connection dbConnection, final Exporter exporter) {
		DatabaseMetaData metadata;
		try {
			metadata = dbConnection.getMetaData();
			exporter.setIdentifier(metadata.getDatabaseProductName() + "_" + metadata.getDatabaseProductVersion());

			if (showMetaDataInfo) {
				showMetaDataInfo(metadata, exporter);
			}
			if (showTypeInfo) {
				showTypeInfos(metadata, exporter);
			}
			if (showMaxValues) {
				showMaxValues(metadata, exporter);
			}
			if (showSupportValues) {
				showSupportValues(metadata, exporter);
			}
			if (showConnectionInfo) {
				showConnectionInfo(dbConnection, exporter);
			}
			if (showCatalogs) {
				showCatalogs(metadata, exporter);
			}
			if (showSchemas) {
				showSchemas(metadata, exporter);
			}
			if (showTables) {
				showTables(metadata, exporter);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void showSupportValues(final DatabaseMetaData metadata, final Exporter exporter) {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Support values (no args)");
		Class<? extends DatabaseMetaData> metadataClass = metadata.getClass();

		String methodName;
		String[] keyValue = new String[2];
		Method[] methods = metadataClass.getMethods();
		for (Method method : methods) {
			methodName = method.getName();

			if (methodName.startsWith("support") && method.getParameterTypes().length == 0) {
				keyValue[0] = methodName;
				keyValue[1] = callMethod(method, metadata);
				db.addRow(null, keyValue);
			}
		}
	}

	private void showMaxValues(final DatabaseMetaData metadata, final Exporter exporter) {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Max values");
		Class<? extends DatabaseMetaData> metadataClass = metadata.getClass();

		String methodName;
		String[] keyValue = new String[2];
		Method[] methods = metadataClass.getMethods();
		for (Method method : methods) {
			methodName = method.getName();

			if (methodName.startsWith("getMax")) {
				keyValue[0] = methodName;
				keyValue[1] = callMethod(method, metadata);
				db.addRow(null, keyValue);
			}
		}
	}

	private void showCatalogs(DatabaseMetaData metadata, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Catalogs (" + metadata.getCatalogTerm() + ")");
		ResultSet rs = null;
		try {
			rs = metadata.getCatalogs();

			showResultSetInfo(rs, db);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void showTypeInfos(DatabaseMetaData metadata, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();

		db.setHeader("Types");

		ResultSet rs = null;
		try {
			rs = metadata.getTypeInfo();

			ResultSetMetaData rsmd = rs.getMetaData();
			showHeaderLabels(rsmd, db);
			showHeaderNames(rsmd, db);
			showHeaderTypes(rsmd, db);
			showHeaderTypeNames(rsmd, db);
			showHeaderDisplaySize(rsmd, db);

			int nrOfCols = rs.getMetaData().getColumnCount();

			if (rs.next()) {
				String[] data = new String[nrOfCols];
				for (int i = 1; i <= nrOfCols; i++) {
					data[i - 1] = getClassName(rs.getObject(i));
				}
				db.addHeaderRow("class", data);

				do {
					if (showType((Integer) rs.getObject(2), allowTypes)) {
						for (int i = 1; i <= nrOfCols; i++) {
							if (i == 2) {
								data[i - 1] = typeToText(rs.getObject(i));
							} else {
								data[i - 1] = String.valueOf(rs.getObject(i));
							}
						}
						db.addRow("row", data);
					}
				} while (rs.next());
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private boolean showType(Integer type, Set<Integer> allowTypes) {
		return allowTypes != null ? allowTypes.contains(type) : true;
	}

	private String typeToText(Object typeValue) {
		if (typeValue != null && typeValue instanceof Integer) {
			Integer in = (Integer) typeValue;
			return Consts.getTypeName(in);
		}
		return "<no Int or null>";
	}

	private void showTables(DatabaseMetaData metadata, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Tables");
		
		ResultSet rs = null;
		try {
			rs = metadata.getTables(null, null, "", null);
			
			showResultSetInfo(rs, db);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void showSchemas(DatabaseMetaData metadata, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Schemas");

		ResultSet rs = null;
		try {
			rs = metadata.getSchemas();

			showResultSetInfo(rs, db);
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	private void showResultSetInfo(final ResultSet rs, final DataBlock db) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		showHeaderLabels(rsmd, db);
		showHeaderNames(rsmd, db);
		showHeaderTypes(rsmd, db);
		showHeaderTypeNames(rsmd, db);
		showHeaderDisplaySize(rsmd, db);

		showResultSetData(rs, db);
	}

	private void addKeyValue(final String key, final Object value, final DataBlock db) {
		String[] keyValue = new String[1];
		keyValue[0] = String.valueOf(value);
		db.addRow(key, keyValue);
	}

	private void showMetaDataInfo(DatabaseMetaData metadata, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("MetaData");

		addKeyValue("Database Product Name", callMethod("getDatabaseProductName", metadata), db);
		addKeyValue("Database Product Version", callMethod("getDatabaseProductVersion", metadata), db);
		addKeyValue("Database Major Version", callMethod("getDatabaseMajorVersion", metadata), db);
		addKeyValue("Database Minor Version", callMethod("getDriverMinorVersion", metadata), db);
		addKeyValue("Catalog Seprator", callMethod("getCatalogSeparator", metadata), db);
		addKeyValue("Catalog Term", callMethod("getCatalogTerm", metadata), db);
		addKeyValue("Schema Term", callMethod("getSchemaTerm", metadata), db);
	}

	private void showConnectionInfo(Connection dbConnection, final Exporter exporter) throws SQLException {
		DataBlock db = exporter.createDataBlock();
		db.setHeader("Connection");

		addKeyValue("Auto Commit", callMethod("getAutoCommit", dbConnection), db);
		addKeyValue("Catalog", callMethod("getCatalog", dbConnection), db);
		addKeyValue("Schema", callMethod("getSchema", dbConnection), db);
		addKeyValue("Network Timeout [ms]", callMethod("getNetworkTimeout", dbConnection), db);
		addKeyValue("Read Only", callMethod("isReadOnly", dbConnection), db);
		addKeyValue("Transaction Isolation",
				Consts.getIsolationLevel(callMethod("getTransactionIsolation", dbConnection)), db);
	}

	private static String getClassName(Object object) {
		return object != null ? object.getClass().getSimpleName() : "<null>";
	}

	private static void showResultSetData(ResultSet rs, final DataBlock db) throws SQLException {
		int nrOfCols = rs.getMetaData().getColumnCount();

		if (rs.next()) {
			String[] data = new String[nrOfCols];
			for (int i = 1; i <= nrOfCols; i++) {
				data[i - 1] = getClassName(rs.getObject(i));
			}
			db.addHeaderRow("class", data);

			do {
				for (int i = 1; i <= nrOfCols; i++) {
					data[i - 1] = String.valueOf(rs.getObject(i));
				}
				db.addRow("row", data);
			} while (rs.next());
		}
	}

	private static void showHeaderLabels(ResultSetMetaData rsmd, final DataBlock db) throws SQLException {
		int nrOfCols = rsmd.getColumnCount();
		String[] data = new String[nrOfCols];

		for (int i = 1; i <= nrOfCols; i++) {
			data[i - 1] = rsmd.getColumnLabel(i);
		}
		db.addHeaderRow("Labels", data);
	}

	private static void showHeaderNames(ResultSetMetaData rsmd, final DataBlock db) throws SQLException {
		int nrOfCols = rsmd.getColumnCount();
		String[] data = new String[nrOfCols];

		for (int i = 1; i <= nrOfCols; i++) {
			data[i - 1] = rsmd.getColumnName(i);
		}

		db.addHeaderRow("Names", data);
	}

	private static void showHeaderTypes(ResultSetMetaData rsmd, final DataBlock db) throws SQLException {
		int nrOfCols = rsmd.getColumnCount();
		String[] data = new String[nrOfCols];

		for (int i = 1; i <= nrOfCols; i++) {
			data[i - 1] = String.valueOf(rsmd.getColumnType(i));
		}
		db.addHeaderRow("Type", data);
	}

	private static void showHeaderTypeNames(ResultSetMetaData rsmd, final DataBlock db) throws SQLException {
		int nrOfCols = rsmd.getColumnCount();
		String[] data = new String[nrOfCols];

		for (int i = 1; i <= nrOfCols; i++) {
			data[i - 1] = rsmd.getColumnTypeName(i);
		}
		db.addHeaderRow("Typ-N", data);
	}

	private static void showHeaderDisplaySize(ResultSetMetaData rsmd, final DataBlock db) throws SQLException {
		int nrOfCols = rsmd.getColumnCount();
		String[] data = new String[nrOfCols];

		for (int i = 1; i <= nrOfCols; i++) {
			data[i - 1] = String.valueOf(rsmd.getColumnDisplaySize(i));
		}
		db.addHeaderRow("Size", data);
	}
}
