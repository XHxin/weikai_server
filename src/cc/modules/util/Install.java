package cc.modules.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import cc.modules.constants.Constants;

/**
 * @author handsomeWU
 * 
 */
public class Install {
	public static void dbXml(String fileName, String dbHost, String dbPort, String dbName, String dbUser, String dbPassword)
		throws Exception {
		String s = FileUtils.readFileToString(new File(fileName));
		s = s.replaceFirst("DB_HOST", dbHost);
		s = s.replaceFirst("DB_PORT", dbPort);
		s = s.replaceFirst("DB_NAME", dbName);
		s = s.replaceFirst("DB_USER", dbUser);
		s = s.replaceFirst("DB_PASSWORD", dbPassword);
		FileUtils.writeStringToFile(new File(fileName), s);
	}

	public static Connection getConn(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword)
		throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String connStr = "jdbc:mysql://" + dbHost + ":" + dbPort + "/" + dbName + "?user=" + dbUser + "&password=" + dbPassword;
		Connection conn = DriverManager.getConnection(connStr);
		return conn;
	}

	public static void webXml(String fromFile, String toFile) throws Exception {
		FileUtils.copyFile(new File(fromFile), new File(toFile));
	}

	/**
	 * 
	 * @param dbHost
	 * @param dbName
	 * @param dbPort
	 * @param dbUser
	 * @param dbPassword
	 * @throws Exception
	 */
	public static void createDb(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		String connStr = "jdbc:mysql://" + dbHost + ":" + dbPort + "?user=" + dbUser + "&password=" + dbPassword;
		Connection conn = DriverManager.getConnection(connStr);
		Statement stat = conn.createStatement();
		String sql = "drop database if exists " + dbName;
		stat.execute(sql);
		sql = "create database " + dbName + " ";
		stat.execute(sql);
		stat.close();
		conn.close();
	}

	public static void changeDbCharset(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword)
		throws Exception {
		Connection conn = getConn(dbHost, dbPort, dbName, dbUser, dbPassword);
		Statement stat = conn.createStatement();
		String sql = "alter database " + dbName + " character SET UTF8";
		stat.execute(sql);
		stat.close();
		conn.close();
	}

	/**
	 * ������
	 * 
	 * @param dbHost
	 * @param dbName
	 * @param dbPort
	 * @param dbUser
	 * @param dbPassword
	 * @param sqlList
	 * @throws Exception
	 */
	public static void createTable(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword,
		List<String> sqlList) throws Exception {
		Connection conn = getConn(dbHost, dbPort, dbName, dbUser, dbPassword);
		Statement stat = conn.createStatement();
		for (String dllsql : sqlList) {
			stat.addBatch(dllsql);
		}

		stat.executeBatch();
		stat.close();
		conn.close();
	}

	/**
	 * ��������
	 * 
	 * @param dbHost
	 * @param dbName
	 * @param dbPort
	 * @param dbUser
	 * @param dbPassword
	 * @param domain
	 * @param cxtPath
	 * @param port
	 * @throws Exception
	 */
	public static void updateConfig(String dbHost, String dbPort, String dbName, String dbUser, String dbPassword, String domain,
		String cxtPath, String port) throws Exception {
		Connection conn = getConn(dbHost, dbPort, dbName, dbUser, dbPassword);
		Statement stat = conn.createStatement();

		String sql = "";
		// 如果不为默认端口,填对主页添加端口
		if (!"80".equals(port))
			sql = "update WEB_SITE set DOMAIN='" + domain + ":" + port + "'";
		else
			sql = "update WEB_SITE set DOMAIN='" + domain + "'";
		stat.executeUpdate(sql);

		stat.close();
		conn.close();
	}

	/**
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public static List<String> readSql(String fileName) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), Constants.ENCODING));
		List<String> sqlList = new ArrayList<String>();
		StringBuilder sqlSb = new StringBuilder();
		String s = null;
		while ((s = br.readLine()) != null) {
			if (s.startsWith("/*")) {
				continue;
			}
			if (s.endsWith(";")) {
				sqlSb.append(s);
				sqlSb.setLength(sqlSb.length());
				sqlList.add(sqlSb.toString());
				sqlSb.setLength(0);
			} else {
				sqlSb.append(s);
			}
		}
		br.close();
		return sqlList;
	}
}
