package dbsql.borala;

import java.sql.*;

public class Connections {

	private String host;
	private String user;
	private String pass;

	public Connections(){
		host="jdbc:postgresql://localhost:5432/postgis";
		user="postgres";
		pass="root";
	}

	public Connection getConnection(){
		try {
			Class.forName("org.postgresql.Driver");
			Connection con = DriverManager.getConnection(host, user, pass);
			System.out.println("conexão executada com sucesso");
			return con;
		}catch(ClassNotFoundException ex){
			return null;
		}catch(SQLException ex){
			return null;
		}
	}

	//conexao = new Connections();
	//conexao.getConnection();

}