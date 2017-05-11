package dbsql.borala;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Crud {

	public ResultSet read(String condicao){
		String sql = "SELECT * FROM tb_corporation ";
		sql+=condicao;
		PreparedStatement pst = Connections.getPreparedStatement(sql);

		ResultSet resultado = null;
		try{
			ResultSet res = pst.executeQuery();
			resultado = res;
		}catch(SQLException ex){
			System.out.println("Erro no SQL: ");
			ex.getMessage();
		}

		return resultado;	
	}
}
