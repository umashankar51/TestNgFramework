package utils;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.*;

@Slf4j
public class DBUtils {

	private static Map<String, Connection> connectionMap = new HashMap<String, Connection>();

	private static Connection getDBConnection(String schema) throws Exception {
		if (!connectionMap.containsKey(schema) || connectionMap.get(schema).isClosed()) {
			try {
				String driver = ConfigUtils.getEnvVariable("db.driver.classname");
				String uri = ConfigUtils.getEnvVariable("db." + schema + ".connecturi");
				String username = ConfigUtils.getEnvVariable("db." + schema + ".name");
				String password = ConfigUtils.getEnvVariable("db." + schema + ".password");
				Class.forName(driver);
				Connection conn = DriverManager.getConnection(uri, username, password);
				log.info("###################### Connection Opened ######################");
				connectionMap.put(schema, conn);
			} catch (SQLException | ClassNotFoundException e) {
				log.info("{}", e);
			}
		} else {
			log.info("Connection is already established ... ");
		}
		return connectionMap.get(schema);
	}

	private static void closeDbConnection(String schema) {
		try {
			if (connectionMap.get(schema) != null && !connectionMap.get(schema).isClosed()) {
				connectionMap.get(schema).close();
				log.info("###################### Connection Closed ######################");
			}
		} catch (SQLException se) {
			log.error("{}", se);
		}
	}

	public static void closeAllConnections() {
		log.info("Closing DB Connections ... ");
		connectionMap.forEach((schema, conn) -> {
			try {
				log.info("Schema : {} Connection : {}", schema, conn);
				conn.close();
			} catch (Exception e) {
				log.info(e.getMessage());
			}
		});
	}

	public static List<String> getDbResultSetAsList(String query, String schema) throws SQLException, Exception {
		List<String> dbValues = new ArrayList<String>();
		try {
			Connection connection = getDBConnection(schema);
			log.info("Query: {}", query);
			PreparedStatement sqlStatement = connection.prepareStatement(query);
			ResultSet resultSet = sqlStatement.executeQuery();
			while (resultSet.next()) {
				String dbValue = resultSet.getString(1);
				dbValues.add(dbValue);
			}
			resultSet.close();
		} catch (SQLException exception) {
			log.info("Exception in Result set : {}", exception.getMessage());
		}
		return dbValues;
	}
}
