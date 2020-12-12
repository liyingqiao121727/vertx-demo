package com.bgi.configuration;

import com.bgi.vtx.Config;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.db.SqlOperation;
import com.bgi.vtx.Config;
import com.bgi.vtx.DbOperation;
import com.bgi.vtx.db.SqlOperation;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.asyncsql.MySQLClient;
import io.vertx.ext.sql.SQLConnection;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.sql.SQLClient;

public class DbConfig extends Config {

	public static final JsonObject configuration = Config.config;

	public static SQLClient misRxSqlClientR;

	public static synchronized SQLClient misRxSqlClientR() {
		if (null == misRxSqlClientR) {
			misRxSqlClientR = io.vertx.reactivex.ext.asyncsql.MySQLClient.createShared(
					Vertx.newInstance(vertx), config.getJsonObject("misMysql"),"MYSQL-POOL-MIS-1");
		}
		return misRxSqlClientR;
	}

	private static io.vertx.ext.sql.SQLClient misExtsqlClient;

	public static synchronized io.vertx.ext.sql.SQLClient misExtsqlClient() {
		if (null == misExtsqlClient) {
			misExtsqlClient = MySQLClient.createShared(vertx,
					config.getJsonObject("misMysql"), "MYSQL-POOL-MIS-2");
		}
		return misExtsqlClient;
	}

	private static io.vertx.ext.sql.SQLClient misSqlClient;

	public static synchronized io.vertx.ext.sql.SQLClient misSqlClient() {
		if (null == misSqlClient) {
			misSqlClient = MySQLClient.createShared(vertx,
					config.getJsonObject("misMysql"), "MYSQL-POOL-MIS-3");
		}
		return misSqlClient;
	}

	public static SQLClient misRxSqlClientW;

	public static synchronized SQLClient misRxSqlClientW() {
		if (null == misRxSqlClientW) {
			misRxSqlClientW = io.vertx.reactivex.ext.asyncsql.MySQLClient.createShared(
					Vertx.newInstance(vertx), config.getJsonObject("misMysql"),"MYSQL-POOL-MIS-4");
		}
		return misRxSqlClientW;
	}

	private static DbOperation<SQLConnection> misMysqlOperation;

	public static synchronized DbOperation<SQLConnection> misMysqlOperation() {
		if (null == misMysqlOperation) {
			try {
				misMysqlOperation = new SqlOperation(misExtsqlClient());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return misMysqlOperation;
	}

	public static SQLClient rxSqlClientR;

	public static synchronized SQLClient rxSqlClientR() {
		if (null == rxSqlClientR) {
			rxSqlClientR = io.vertx.reactivex.ext.asyncsql.MySQLClient.createShared(
					Vertx.newInstance(vertx), config.getJsonObject("mysqlOperation"),"MYSQL-POOL1");
		}
		return rxSqlClientR;
	}

	private static io.vertx.ext.sql.SQLClient extsqlClient;

	public static synchronized io.vertx.ext.sql.SQLClient extsqlClient() {
		if (null == extsqlClient) {
			extsqlClient = MySQLClient.createShared(vertx,
					config.getJsonObject("mysqlOperation"), "MYSQL-POOL2");
		}
		return extsqlClient;
	}

	private static io.vertx.ext.sql.SQLClient sqlClient;

	public static synchronized io.vertx.ext.sql.SQLClient sqlClient() {
		if (null == sqlClient) {
			sqlClient = MySQLClient.createShared(vertx,
					config.getJsonObject("mysqlOperation"), "MYSQL-POOL3");
		}
		return sqlClient;
	}

	public static SQLClient rxSqlClientW;

	public static synchronized SQLClient rxSqlClientW() {
		if (null == rxSqlClientW) {
			rxSqlClientW = io.vertx.reactivex.ext.asyncsql.MySQLClient.createShared(
					Vertx.newInstance(vertx), config.getJsonObject("mysqlOperation"),"MYSQL-POOL4");
		}
		return rxSqlClientW;
	}

	private static DbOperation<SQLConnection> mysqlOperation;

	public static synchronized DbOperation<SQLConnection> mysqlOperation() {
		if (null == mysqlOperation) {
			try {
				mysqlOperation = new SqlOperation(extsqlClient());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mysqlOperation;
	}

}
