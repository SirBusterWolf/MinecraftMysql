package cf.sirbusterwolf.dragonhardcore.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Mysql {
    final String user;
    final String database;
    final String password;
    final String port;
    final String hostname;
    Connection connection;
    
    public Mysql(String mysqlHostname,String mysqlDatabase,String mysqlUser,String mysqlPassword,String mysqlPort){
        this.hostname = mysqlHostname;
        this.port = mysqlPort;
        this.database = mysqlDatabase;
        this.user = mysqlUser;
        this.password = mysqlPassword;
        this.connection = null;
    }
    public boolean firstConnection(){
    	try{
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + this.hostname + ":" + this.port + "/?user=" + this.user + "&password=" + this.password;
            Connection conn = DriverManager.getConnection(url);
            conn.close();
            return true;
    	}catch(Exception e){
    		return false;
    	}
    }
    public Connection openConnection() {
        try {
            if (checkConnection()) {
                return connection;
            }
            Class.forName("com.mysql.jdbc.Driver");
            String s = "jdbc:mysql://" + this.hostname + ":" + this.port + "/" + this.database;
            connection = DriverManager.getConnection(s, this.user, this.password);
            return connection;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    public boolean checkConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection != null;
    }

    public boolean closeConnection() {
        if (connection == null) {
            return false;
        }
        try {
            connection.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        return true;
    }
    
    public Connection getConnection() {
        if (!checkConnection()) {
            openConnection();
        }
        return connection;
    }
    
    public ResultSet executeQuery(String query)
    {
      Connection c = null;
      if (checkConnection()) {
        c = getConnection();
      } else {
        c = openConnection();
      }
      Statement s = null;
      try
      {
        s = c.createStatement();
      }
      catch (SQLException e1)
      {
        e1.printStackTrace();
      }
      ResultSet ret = null;
      try
      {
        ret = s.executeQuery(query);
      }
      catch (SQLException e)
      {
        e.printStackTrace();
      }
      closeConnection();
      return ret;
    }
    public void executeUpdate(String update)
    {
      Connection c = null;
      if (checkConnection()) {
        c = getConnection();
      } else {
        c = openConnection();
      }
      Statement s = null;
      try
      {
        s = c.createStatement();
        s.executeUpdate(update);
      }
      catch (SQLException e1)
      {
        e1.printStackTrace();
      }
      closeConnection();
    }

}
