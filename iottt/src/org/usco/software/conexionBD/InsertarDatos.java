package org.usco.software.conexionBD;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;

public class InsertarDatos {
	
	public void insertar(BasicDBObject data) {
		
		ConexionBD conexion = new ConexionBD();
		
		DBCollection coll = conexion.getConexion();
		
		coll.insert(data);
		
	}
	

}
