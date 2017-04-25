package org.usco.software.conexionBD;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class ConexionBD {
	
	
	public DBCollection getConexion() {
		

		MongoClient mongoclient = new MongoClient();
		DB db = mongoclient.getDB("datos");
		
		DBCollection dbcoll = db.getCollection("medidas");
		
		
		return dbcoll;
		
	}
	
	

	

}


