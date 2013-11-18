package tests.gui.icewalker;

import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;
import java.sql.*;

public class DatabaseAdapter {

	private static Connection connection;
	private static Statement statement;
	private static DatabaseMetaData metaData = null;

	public static String EXCEL_DRIVER = "{Microsoft Excel Driver (*.xls)}";
	public static String ACCESS_DRIVER = "{Microsoft Access Driver (*.mdb)}";
	private static String DRIVER = ACCESS_DRIVER;
	private static String catalog = "";

	public static boolean isODBC = false;

	private static Vector<String> resvWords = new Vector<String>();

	private static String lastSQLQuery = "", lastSQLUpdate = "";


	/**
	 * Sets the current Database Driver to the supplied argument
	 */
	public static void setODBCDriver(String driver) {
		DRIVER = driver;
	}

	/**
	 * Returns the current database Driver in use
	 */
	public static String getODBCDriver() {
		return DRIVER;
	}

	public static void setDriver(String driver) {
		DRIVER = driver;
	}

	public static String getDriver() {
		return DRIVER;
	}


	/**
	 * Returns an array of all the Database Drivers known to the class
	 */
	public static String[] getDriverList() {
		return new String[] {ACCESS_DRIVER, EXCEL_DRIVER};
	}

	public static void setUseODBCRules(boolean b) {
		isODBC = b;
	}

	public static boolean useODBCRules() {
		return isODBC;
	}

	public static boolean connectToDatabase(String driver, File file) {
		if(driver == ACCESS_DRIVER || driver == EXCEL_DRIVER || driver.contains("odbc") ||
			driver.contains("Microsoft") ) {

			setUseODBCRules(true);
		}

		setDriver(driver);
		return connectToDatabase(file);
	}

	/**
	 * Establishes a connection the database file supplied. This is intended to work
	 * well with MS Access and MS Excel files
	 */
	public static boolean connectToDatabase(File file) {
		setUseODBCRules(true);
		return connectToDatabase( file.getAbsolutePath() );
	}

	public static boolean connectToDatabase(String driver, String url, String usern, String pass) {
		try {
			if(driver == ACCESS_DRIVER || driver == EXCEL_DRIVER || driver.contains("odbc") ||
				driver.contains("Microsoft") ) {

				setUseODBCRules(true);
			}

			Class.forName(driver);
			connection = DriverManager.getConnection(url, usern, pass);
			metaData = connection.getMetaData();
			statement = connection.createStatement();

			createReservedWordList();

		} catch(Exception ex) {
            JOptionPane.showMessageDialog(null,"Failed to open connection to DataBase:\n " + ex.getMessage(),
            	"Connection Error", JOptionPane.ERROR_MESSAGE);

            try {
            	connection.close();
            } catch(SQLException sqe) {
            	System.out.println(sqe);
            }

            return false;
        }

        return true;
	}

	public static boolean connectToDatabase(String dbfile) {
		try	{

			Class c = Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
 			String urlDetails = "jdbc:odbc:Driver=" + getODBCDriver() + ";DBQ=" + dbfile;

			//DriverManager.setLogStream(System.err);

			connection = DriverManager.getConnection(urlDetails,"","");
			metaData = connection.getMetaData();
			statement = connection.createStatement();

			System.out.println("Connected To: " + dbfile);

			SQLWarning warn = connection.getWarnings();

			while(warn != null) {
				System.out.println("SQLState: " + warn.getSQLState());
				System.out.println("Message: " + warn.getMessage());
				System.out.println("Vendor: " + warn.getErrorCode());
				System.out.println("");
				warn = warn.getNextWarning();
			}

			createReservedWordList();

		} catch(Exception ex) {
            JOptionPane.showMessageDialog(null,"Failed to open connection to DataBase:\n " + ex.getMessage(),
            	"Connection Error", JOptionPane.ERROR_MESSAGE);

            try {
            	connection.close();
            } catch(SQLException sqe) {
            	System.out.println(sqe);
            }

            return false;
        }

        return true;
	}

	private static void createReservedWordList() {
		resvWords.removeAllElements();

		try {
			//System.out.println("reserved words\n-----------------");

			String[] words = metaData.getSQLKeywords().split(",");

			for(String word: words) {
				resvWords.addElement(word.toLowerCase());
				System.out.print(word + " ");
			}

			String[] otherResWords = {"Date", "Year","FullText", "Text"};

			for(String word: otherResWords) {
				if(!resvWords.contains(word)) {
					resvWords.addElement(word.toLowerCase());
					System.out.print(word + " ");
				}
			}
		} catch(SQLException sql) {
			sql.printStackTrace();
		}

	}

	/**
	 * Returns a list of all the Tables in the database.
	 **/
	/*public static Vector<String> getDatabaseTables() {
		Vector<String> tables = new Vector<String>();

		try {

			ResultSet catalogs = metaData.getCatalogs();
			ResultSetMetaData rsdata = catalogs.getMetaData();
			int colCount = rsdata.getColumnCount();

			System.out.println("Tables");
			System.out.println("-------------");
			while(catalogs.next()) {
				catalog = catalogs.getObject(1).toString();

				ResultSet rs = metaData.getTables(catalog, null, null, new String[] { "TABLE" });
				colCount = rs.getMetaData().getColumnCount();

				while(rs.next()) {
					String tableName = rs.getObject(3).toString();
					System.out.println( tableName );

					tables.addElement(tableName);
				}

				rs.close();
			}

			catalogs.close();
			catalogs = null;

		} catch(SQLException sq) {
			JOptionPane.showMessageDialog(null, "Could Not Retrieve The Database Tables",
			"Connection Error", JOptionPane.ERROR_MESSAGE);
			return new Vector<String>();
		}

		return tables;
	}*/

	/**
	 * Returns a list of all the Tables in the database.
	 **/
	public static Vector<String> getDatabaseTables() {
		Vector<String> tables = new Vector<String>();

		try {
			ResultSet rs = metaData.getTables(null, null, null, new String[] { "TABLE" });
			int colCount = rs.getMetaData().getColumnCount();

			System.out.println("Database Tables\n------------------------");

			while(rs.next()) {
				String tableName = rs.getObject(3).toString();
				System.out.println( tableName );

				tables.addElement(tableName);
			}

			rs.close();

		} catch(SQLException sq) {
			JOptionPane.showMessageDialog(null, "Could Not Retrieve The Database Tables",
			"Connection Error", JOptionPane.ERROR_MESSAGE);
			return new Vector<String>();
		}

		return tables;
	}

	public static boolean databaseTableExists(String tableName) {
		Vector<String> tableNames = getDatabaseTables();

		for(String t: tableNames) {
			if(t.equalsIgnoreCase( tableName ) ) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns a list of the all the column names in tableName
	 */
	public static Vector<String> getTableFields(String tableName) {
		Vector<String> columnNames = null;

		try {
            //  Get Columns
            ResultSet rs = statement.executeQuery("SELECT * FROM " + tableName);
            ResultSetMetaData md = rs.getMetaData();

            int columns = md.getColumnCount();

            columnNames = new Vector<String>(columns);


            for (int i = 1; i <= columns; i++) {
                columnNames.addElement( md.getColumnName(i) );
            }


            rs.close();
            md = null;
        } catch(SQLException sql) {
        	JOptionPane.showMessageDialog(null, "Could Not Retrieve Column Names For " + tableName,
        	"Error", JOptionPane.ERROR_MESSAGE);
        }

        return columnNames;
	}

	public static TableModel getTableData(String tableName) {
		return new DefaultTableModel( selectRecords(tableName), getTableFields(tableName) );
	}

	public static TableModel getTableDataAsModel(String sql) {
		try {
			ResultSet rs = executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();

			Vector<Object> rows = new Vector<Object>();

			while(rs.next()) {
				Vector<Object> row = new Vector<Object>();
				for(int i = 1; i < md.getColumnCount(); i++) {
					row.addElement( rs.getObject(i) );
				}

				rows.addElement( row );
			}

			Vector<String> cols = new Vector<String>();
			for(int i = 1; i <= md.getColumnCount(); i++) {
				cols.addElement( md.getColumnName(i) );
			}

			return new DefaultTableModel( rows, cols );
		} catch(SQLException sq) {
			sq.printStackTrace();
		}

		return new DefaultTableModel();
	}

	/**
	 * Returns a JDBC Aware table model.
	 */
	public static JDBCAdapter getJDBCTableModel() {
		return new JDBCAdapter(connection);
	}

	/**
	 * Returns a JDBC Aware table model. Runs the SQL Query to popluate the table
	 * on return
	 */
	public static JDBCAdapter getJDBCTableModel(String sql) {
		JDBCAdapter adapter = new JDBCAdapter(connection);
			adapter.executeQuery(sql);

		return adapter;
	}

	/**
	 * Returns a ResultSet for the SQL Query
	 */
	public static ResultSet executeQuery(String query) {
		ResultSet rs = null;

		try {
			rs = statement.executeQuery(query);

			lastSQLQuery = query;
		} catch(SQLException sql) {
			JOptionPane.showMessageDialog(null, "Could not retrieve the record set for query:\n" + query,
				"SQL Query Error", JOptionPane.ERROR_MESSAGE);

			sql.printStackTrace();

			return rs;
		}

		return rs;
	}

	public static boolean executeUpdate(String sqlStmt) {
		System.out.println(sqlStmt);

		try {
			statement.executeUpdate(sqlStmt);

			lastSQLUpdate = sqlStmt;

		} catch(SQLException sql) {
			JOptionPane.showMessageDialog(null, "Could Not Update The Database. SQL Statement Failed To Execute:\n"
				+ "Statement: " + sqlStmt, "Database Update Error", JOptionPane.ERROR_MESSAGE);

			sql.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean createTable(String tableName, Hashtable<String, String> nameTypeMap) {
		return createTable(tableName, nameTypeMap, false);
	}

	public static boolean createTable(String tableName, Hashtable<String, String> nameTypeMap, boolean drop) {
		StringBuffer sql = new StringBuffer();

		if(drop) {
			executeUpdate("DROP TABLE " + toValidDBColName(tableName) );
		}

		sql.append("CREATE TABLE " + tableName + " (");

		if(nameTypeMap == null) {
			throw new NullPointerException("Column Name Or Type Information Is Null");
		}

		Enumeration<String> colNames = nameTypeMap.keys();

		for(int i = 0; colNames.hasMoreElements(); i++) {
			String col = colNames.nextElement();
			String type = nameTypeMap.get(col);
			sql.append( toValidDBColName(col) + " " + type);

			if(i < nameTypeMap.size() - 1) {
				sql.append(",");
			} else {
				sql.append(")");
			}
		}

//		System.out.println( sql.toString() );

		return executeUpdate( sql.toString() );
	}

	public static boolean insertRecord(String tableName, String[] colNames, Vector<Object> values) {
		return insertRecord(tableName, new Vector<String>(Arrays.asList(colNames)), values );
	}

	public static boolean insertRecord(String tableName, Vector<String> colNames, Vector<Object> values) {
		if(tableName == null || tableName.length() == 0 ) {
			throw new NullPointerException("Table Is Null or No Table Name supplied");
		}

		if(values == null) {
			throw new NullPointerException("Column Names or value or undefined");
		}

		if(colNames != null && (colNames.size() != values.size())) {
			throw new IllegalArgumentException("Number of Columns Do Not Match Number of supplied values");
		}

		StringBuffer buf = new StringBuffer("INSERT INTO " + tableName + " ");

		if(colNames != null) {
			buf.append("( ");

			for(int i = 0; i < colNames.size(); i++) {
				String col = colNames.elementAt(i);

				buf.append( toValidDBColName(col) );

				if(i != colNames.size() - 1) {
					buf.append(",");
				}
			}

			buf.append(") ");
		}

		if(values != null) {
			buf.append("VALUES (");
			for(int i = 0; i < values.size(); i++) {
				Object val = values.elementAt(i);

				buf.append( toValidDBValue(val) );


				if(i != values.size() - 1) {
					buf.append(",");
				}
			}

			buf.append(")");
		}

		System.out.println(buf.toString());

		return executeUpdate( buf.toString() );
	}

	public static boolean insertRecord(String tableName, Vector<Object> values) {
		return insertRecord(tableName, (Vector<String>)null, values);
	}
	
	public static boolean updateRecord(String tableName, String[] colNames, Object[] values, 
				String rowMarkerCol, Object rowMarker) {
				
		return updateRecord(tableName, new Vector<String>(Arrays.asList(colNames)), 
			new Vector<Object>(Arrays.asList(values)), rowMarkerCol, rowMarker );	
	}

	public static boolean updateRecord(String tableName, Vector<String> colNames,
					Vector<Object> values, String rowMarkerCol, Object rowMarker) {

		if(tableName == null || tableName.length() == 0) {
			throw new NullPointerException("Cannot execute update. Table undefined");
		}

		if(colNames == null || values == null) {
			throw new NullPointerException("Cannot execute update. Column Names or Values not defined");
		}

		if(rowMarker == null) {
			throw new NullPointerException("Undefined Row Marker for where clause");
		}

		if(colNames.size() != values.size()) {
			throw new IllegalArgumentException("Number of Column Names do not match the number of supplied values");
		}

		StringBuffer buf = new StringBuffer("UPDATE " + tableName + " SET ");

		for(int i = 0; i < colNames.size(); i++) {
			String col = colNames.elementAt(i);
			Object val = values.elementAt(i);

			buf.append( toValidDBColName(col) + " = " + toValidDBValue(val) );

			if(i != colNames.size() - 1) {
				buf.append(", ");
			} else {
				buf.append(" WHERE " + toValidDBColName(rowMarkerCol)
							+ " = " + toValidDBValue(rowMarker) );
			}
		}

		System.out.println(buf.toString());

		return DatabaseAdapter.executeUpdate( buf.toString() );

	}

	public static boolean deleteRecord(String tableName, String primaryKeyCol, Object primaryKey) {
		return deleteRecords( tableName, primaryKeyCol, new Object[] {primaryKey} );
	}

	public static boolean deleteRecords(String tableName, String primaryKeyCol, Object[] primaryKeys) {
		StringBuffer buf = new StringBuffer("DELETE FROM " + tableName + " WHERE ");

		for(int i = 0; i < primaryKeys.length; i++) {
			buf.append( toValidDBColName(primaryKeyCol) + " = " + toValidDBValue(primaryKeys[i]) );

			if(i < primaryKeys.length - 1) {
				buf.append(" OR ");
			}
		}

		//System.out.println( buf.toString() );

		return executeUpdate( buf.toString() );
	}

	public static Vector<Object> selectColumnAsList(String tableName, String colName) {
		Vector<String> cols = new Vector<String>();
			cols.addElement(colName);

		Vector<Vector<Object>> rows = selectRecords(tableName, cols);

		Vector<Object> list = new Vector<Object>();

		for(Vector<Object> row: rows) {
			list.addElement( row.elementAt(0) );
		}

		return list;
	}

	public static Vector<Vector<Object>> selectRecords(String tableName) {
		return selectRecords(tableName, (String[])null);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols) {
		return selectRecords(tableName, cols, null);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, String[] cols) {
		if(cols == null) {
			return selectRecords(tableName, (Vector<String>)null, null);
		}
		
		return selectRecords(tableName, new Vector<String>(Arrays.asList(cols)), null);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker) {

		return selectRecords(tableName, cols, rowMarkerCol, rowMarker, null, null, null);
	}
	
	public static Vector<Vector<Object>> selectRecords(String tableName, String[] cols, String sortColumn, SortOrder sortOrder) {
		return selectRecords(tableName, new Vector<String>(Arrays.asList(cols)), null, null, sortColumn, sortOrder);
	}
	
	public static Vector<Vector<Object>> selectRecords(String tableName, String[] cols, String rowMarkerCol, 
		Object rowMarkerValue, String sortColumn, SortOrder sortOrder) {
			
		return selectRecords(tableName, new Vector<String>(Arrays.asList(cols)), rowMarkerCol, rowMarkerValue, sortColumn, sortOrder);
	}
	
	/*public static Vector<Vector<Object>> selectRecords(String tableName, String[] cols, String[] rowMarkerCols, 
		Object[] rowMarkerValues, String sortColumn, SortOrder sortOrder) {
			
		return selectRecords(tableName, new Vector<String>(Arrays.asList(cols)), new Vector<String>(Arrays.asList(rowMarkerCols)), 
			new Vector<Object>(Arrays.asList(rowMarkerValues)), sortColumn, sortOrder);
	}*/

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols, String sortColumn, SortOrder sortOrder) {
		return selectRecords(tableName, cols, null, null, sortColumn, sortOrder);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols, String[] sortColumns, SortOrder sortOrder) {
		return selectRecords(tableName, cols, (String)null, null, sortColumns, sortOrder);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols, String[] sortColumns, SortOrder[] sortOrders) {
		return selectRecords(tableName, cols, (String)null, null, sortColumns, sortOrders);
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String[] sortColumns, SortOrder[] sortOrders) {

		return selectRecords(tableName, cols, rowMarkerCol, rowMarker, null, sortColumns, sortOrders );
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String[] sortColumns, SortOrder sortOrder) {

		SortOrder[] sortOrders = new SortOrder[sortColumns.length];
		for(int  i = 0; i < sortColumns.length; i++) {
			sortOrders[i] = sortOrder;
		}

		return selectRecords(tableName, cols, rowMarkerCol, rowMarker, null, sortColumns, sortOrders );
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String sortColumn, SortOrder sortOrder) {

		return selectRecords(tableName, cols, rowMarkerCol, rowMarker, null, new String[] {sortColumn}, new SortOrder[] {sortOrder} );
	}

	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols, String whereClause) {
		return selectRecords(tableName, cols, null, null, whereClause, null, null);
	}

	private static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String whereClause, String[] sortColumns, SortOrder[] sortOrders) {

		Vector<Vector<Object>> results = null;

		try {
			StringBuffer sql = new StringBuffer("SELECT ");

			if(cols != null) {
				for(int i = 0; i < cols.size(); i++) {
					sql.append( toValidDBColName(cols.elementAt(i)) );

					if(i != cols.size() - 1) {
						sql.append(", ");
					}
				}
			} else {
				sql.append(" * ");
			}

			sql.append(" FROM " + tableName + " ");

			if(rowMarkerCol != null) {
				sql.append("WHERE " + toValidDBColName(rowMarkerCol) + " = " + toValidDBValue(rowMarker) );
			} else if(whereClause != null) {
				sql.append(whereClause);
			} else {
				//sql.append("WHERE 1");
			}

			if(sortColumns != null && sortOrders != null) {
				sql.append(" ORDER BY ");

				for(int i = 0; i < sortColumns.length; i++) {
					sql.append(toValidDBColName(sortColumns[i]) + " " + (sortOrders[i] == SortOrder.DESCENDING ? "DESC" : "ASC") );

					if(i < sortColumns.length - 1) {
						sql.append(", ");
					}
				}
			}

			System.out.println( sql.toString() );

			if(cols == null) {
				cols = getTableFields(tableName);
			}

			ResultSet rs = executeQuery( sql.toString() );

			if(rs != null) {
				results = new Vector<Vector<Object>>();
			}

			while(rs != null && rs.next()) {
				Vector<Object> row = new Vector<Object>();

				for(int i = 0; i < cols.size(); i++) {
					Object value = rs.getObject( cols.elementAt(i) );

					row.addElement( getDBValueObject(value) );
				}

				results.addElement(row);
			}

			rs.close();

		} catch(SQLException sql) {
			sql.printStackTrace();
		}

		return results;
	}

	public static Vector<Vector<Object>> selectRecordsLike(String tableName, Vector<String> cols,
						 String[] sortColumns, SortOrder sortOrder) {

		return selectRecordsLike(tableName, cols, null, null, sortColumns, sortOrder);
	}

	public static Vector<Vector<Object>> selectRecordsLike(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String whereClause, String sortColumn, SortOrder sortOrder) {


		return selectRecordsLike(tableName, cols, rowMarkerCol,
				rowMarker, whereClause, new String[] { sortColumn }, sortOrder);
	}

	public static Vector<Vector<Object>> selectRecordsLike(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String whereClause, String[] sortColumns, SortOrder sortOrder) {


		return selectRecordsLike(tableName, cols, rowMarkerCol,
				rowMarker, whereClause, sortColumns, sortOrder);
	}

	public static Vector<Vector<Object>> selectRecordsLike(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String[] sortColumns, SortOrder sortOrder) {

		SortOrder[] sortOrders = new SortOrder[sortColumns.length];
		for(int  i = 0; i < sortColumns.length; i++) {
			sortOrders[i] = sortOrder;
		}

		return selectRecordsLike(tableName, cols, rowMarkerCol, rowMarker, null, sortColumns, sortOrders );
	}

	public static Vector<Vector<Object>> selectRecordsLike(String tableName, Vector<String> cols,
					String rowMarkerCol, Object rowMarker, String whereClause, String[] sortColumns,  SortOrder[] sortOrders) {

		Vector<Vector<Object>> results = null;

		try {
			StringBuffer sql = new StringBuffer("SELECT ");

			if(cols != null) {
				for(int i = 0; i < cols.size(); i++) {
					sql.append( toValidDBColName(cols.elementAt(i)) );

					if(i != cols.size() - 1) {
						sql.append(", ");
					}
				}
			} else {
				sql.append(" * ");
			}

			sql.append(" FROM " + tableName + " ");

			if(rowMarkerCol != null) {
				sql.append("WHERE " + toValidDBColName(rowMarkerCol) + " LIKE " + toValidDBValue( "%" + rowMarker + "%") );
			} else if(whereClause != null) {
				sql.append(whereClause);
			} else {
				//sql.append("WHERE 1");
			}

			if(sortColumns != null && sortOrders != null) {
				sql.append(" ORDER BY ");

				for(int i = 0; i < sortColumns.length; i++) {
					sql.append(toValidDBColName(sortColumns[i]) + " " + (sortOrders[i] == SortOrder.DESCENDING ? "DESC" : "ASC") );

					if(i < sortColumns.length - 1) {
						sql.append(", ");
					}
				}
			}

			System.out.println( sql.toString() );

			if(cols == null) {
				cols = getTableFields(tableName);
			}

			ResultSet rs = executeQuery( sql.toString() );

			if(rs != null) {
				results = new Vector<Vector<Object>>();
			}

			while(rs != null && rs.next()) {
				Vector<Object> row = new Vector<Object>();

				for(int i = 0; i < cols.size(); i++) {
					Object value = rs.getObject( cols.elementAt(i) );

					row.addElement( getDBValueObject(value) );
				}

				results.addElement(row);
			}

			rs.close();

		} catch(SQLException sql) {
			sql.printStackTrace();
		}

		return results;
	}
	
	public static Vector<Vector<Object>> selectRecords(String tableName, Vector<String> cols,
					String[] rowMarkerCols, Object[] rowMarkerValues, String[] sortColumns, SortOrder sortOrder) {
		
		SortOrder[] orders = new SortOrder[sortColumns.length];
		for(int i = 0; i < orders.length; i++) {
			orders[i] = sortOrder;
		}
		
		return selectRecords(tableName, cols, new Vector(Arrays.asList(rowMarkerCols)), 
			new Vector(Arrays.asList(rowMarkerValues)), sortColumns, orders );
	}

	public static Vector<Vector<Object>>  selectRecords(String tableName, Vector<String> cols,
					Vector<String> rowMarkerCols, Vector<Object> rowMarkerValues) {
		
		return selectRecords(tableName, cols, rowMarkerCols, rowMarkerValues, null, (SortOrder)null);					
	}
	
	public static Vector<Vector<Object>>  selectRecords(String tableName, Vector<String> cols,
					Vector<String> rowMarkerCols, Vector<Object> rowMarkerValues, String[] sortColumns, SortOrder sortOrder) {
		
		SortOrder[] orders = null;
		if(sortColumns != null) {
			orders = new SortOrder[sortColumns.length];
			for(int i = 0; i < orders.length; i++) {
				orders[i] = sortOrder;
			}	
		}
		
		
		return selectRecords(tableName, cols, rowMarkerCols, rowMarkerValues, sortColumns, orders);
	}
	
	public static Vector<Vector<Object>>  selectRecords(String tableName, Vector<String> cols,
					Vector<String> rowMarkerCols, Vector<Object> rowMarkerValues, String[] sortColumns, SortOrder[] sortOrders) {

		Vector<Vector<Object>> results = null;

		try {
			StringBuffer sql = new StringBuffer("SELECT ");

			if(cols != null) {
				for(int i = 0; i < cols.size(); i++) {
					sql.append( toValidDBColName(cols.elementAt(i)) );

					if(i != cols.size() - 1) {
						sql.append(", ");
					}
				}
			} else {
				sql.append(" * ");
			}

			sql.append(" FROM " + tableName + " ");

			if(rowMarkerCols != null && (rowMarkerCols.size() == rowMarkerValues.size()) ) {
				sql.append("WHERE ");

				for(int i = 0; i < rowMarkerCols.size(); i++) {
					String rowMarkerCol = rowMarkerCols.elementAt(i);
					Object rowMarker = rowMarkerValues.elementAt(i);

					sql.append(toValidDBColName(rowMarkerCol) + " = " + toValidDBValue(rowMarker) );

					if(i < rowMarkerCols.size() - 1) {
						sql.append(" AND ");
					}
				}
			} else {
				throw new IllegalArgumentException("Numbers Columns (" + rowMarkerCols.size() + ") " +
					" does not match the number of supplied values (" + rowMarkerValues.size() + ")" );
			}
			
			if(sortColumns != null && sortOrders != null) {
				sql.append(" ORDER BY ");

				for(int i = 0; i < sortColumns.length; i++) {
					sql.append(toValidDBColName(sortColumns[i]) + " " + (sortOrders[i] == SortOrder.DESCENDING ? "DESC" : "ASC") );

					if(i < sortColumns.length - 1) {
						sql.append(", ");
					}
				}
			}

			System.out.println( sql.toString() );

			if(cols == null) {
				cols = getTableFields(tableName);
			}

			ResultSet rs = executeQuery( sql.toString() );

			if(rs != null && rs.next()) {
				results = new Vector<Vector<Object>>();
				rs.close();

				rs = executeQuery( sql.toString() );
			}

			while(rs != null && rs.next()) {
				Vector<Object> row = new Vector<Object>();

				for(int i = 0; i < cols.size(); i++) {
					row.addElement( rs.getObject( cols.elementAt(i) ) );
				}

				results.addElement(row);
			}

			if(rs != null) {
				rs.close();
			}

		} catch(SQLException sql) {
			sql.printStackTrace();
		}

		return results;
	}

	public static String toValidDBColName(String colName) {
		if(colName.contains(" ") || colName.contains("'") || isReservedWord(colName) ) {
			if(useODBCRules()) {
				return "`" + colName + "`";
			} else {
				return "\"" + colName + "\"";
			}

		}

		return colName;
	}

	public static String toValidDBValue(Object value) {
		if(value == null) return "";

		if(value instanceof Number) {
			return value.toString();
		}

		if(value.toString().contains("'")) {
			value = value.toString().replace("'", "''");
		}

		return "'" + value.toString() + "'";
	}

	public static boolean isReservedWord(String colName) {
		return resvWords.contains(colName.toLowerCase());
	}

	public static boolean isNumber(Object value) {
		if(value instanceof Number) {
			return true;
		}

		try {
			long val = Long.parseLong(value.toString());
		} catch(NumberFormatException nfe) {
			try {
				double d = Double.parseDouble(value.toString());
			} catch(NumberFormatException nfe2) {
				return false;
			}
		} catch(NullPointerException npe) {
			return false;
		}

		return true;
	}

	public static Object getDBValueObject(Object value) {
		if(isNumber(value)) {
			return new Integer(value.toString());
		}

		return value;
	}


	/**
	 * Closes the connection to the database file
	 */
	public static void close() {
        System.out.println("Closing db connection");
      //  resultSet.close();
      	try {
      		statement.close();
        	connection.close();
      	} catch(SQLException sql) {
      		JOptionPane.showMessageDialog(null, "Could Not Safely disconnect from the Database",
      			"Connection Close Error", JOptionPane.ERROR_MESSAGE);
      	}

    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
    
    public static void processSQLScriptFile(File file) {
    	try {
			SQLScriptExecutor executor = new SQLScriptExecutor( file );
				executor.loadScript();
				executor.execute();
			
			executor = null;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
   /**
    * Utility class for reading sql script files and executing the sql commands
    */
    public static class SQLScriptExecutor {

		public final static char QUERY_ENDS = ';';
		private File script;	
		//private Connection con = null;
		//private Statement stat;
		
		/**
		* @param args
		* @throws SQLException
		*/
		
		public SQLScriptExecutor(File file) throws SQLException {
			this( file.getAbsolutePath() );
		}
		
		public SQLScriptExecutor(String scriptFileName) throws SQLException {
			script = new File(scriptFileName);
			
			//try {
				//Class.forName("com.mysql.jdbc.Driver");
				//con = DriverManager.getConnection("jdbc:mysql://192.168.1.63:3306/cmsdev", "root", "root");
			if(connection == null) {
				throw new IllegalStateException("No Connection To Database Established");
			}
			
			statement = connection.createStatement();
			//} catch (ClassNotFoundException e) {
			//	e.printStackTrace();
			//}
		}
		
		public static void main(String[] args) {
			try {
				SQLScriptExecutor SQLScriptExecutor = new SQLScriptExecutor("c:/CMS_Data.sql");
				SQLScriptExecutor.loadScript();
				SQLScriptExecutor.execute();
			
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
		protected void loadScript() throws IOException, SQLException {
			BufferedReader reader = new BufferedReader(new FileReader(script));
			String line;
			StringBuffer query = new StringBuffer();
			boolean queryEnds = false;
			
			while ((line = reader.readLine()) != null) {
				if (isComment(line))
					continue;
					
				queryEnds = checkStatementEnds(line);
				query.append(line);
					
				if (queryEnds) {
					System.out.println("query->" + query);
					statement.addBatch(query.toString().substring(0, query.toString().lastIndexOf(";") ) );
					query.setLength(0);
				}
			}
			
			//reader.flush();
			reader.close();
		}
		
		private boolean isComment(String line) {
			if ((line != null) && (line.length() > 0))
				return (line.charAt(0) == '#');
			
			return false;
		}
		
		public void execute() throws IOException, SQLException {
			statement.executeBatch();
		}
		
		private boolean checkStatementEnds(String s) {
			return (s.indexOf(QUERY_ENDS) != -1);
		}
	
	}
}