package org.usco.software.conexionBD;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class ListarDatos {
	
	public void listar(BasicDBObject data) {
		
		ConexionBD conexion = new ConexionBD();
		
		DBCollection coll = conexion.getConexion();
		
		DBObject documento = (DBObject) coll.find();
		
		
		
	}
	

}
