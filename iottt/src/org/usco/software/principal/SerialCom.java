package org.usco.software.principal;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.util.Enumeration;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
//import org.usco.dao.DatosDAO;
//import org.usco.implement.DatosImpl;
import org.usco.software.conexionBD.InsertarDatos;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.BasicDBObject;

public class SerialCom implements SerialPortEventListener {
	SerialPort serialPort;
	
	private BufferedReader input;
	/** The output stream to the port */
	private static OutputStream output;
	/** Milliseconds to block while waiting for port open */
	private static final int TIME_OUT = 2000;
	/** Default bits per second for COM port. */ 
	private static final int DATA_RATE = 9600;

	public void initialize(String puerto) { 
		String PORT_NAMES[] = { puerto }; 
		// the next line is for Raspberry Pi and 
		// gets us into the while loop and was suggested here was suggested 
		// http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186 
		System.setProperty("gnu.io.rxtx.SerialPorts", puerto); 
		CommPortIdentifier portId = null; 
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers(); 
		// First, Find an instance of serial port as set in PORT_NAMES. 
		while (portEnum.hasMoreElements()) { 
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement(); 
			for (String portName : PORT_NAMES) { 
				if (currPortId.getName().equals(portName)) { 
					portId = currPortId; 
					break; 
				} 
			} 
		} 
		if (portId == null) { 
			System.out.println("Could not find COM port."); 
			return; 
		} 
		try { 
			// open serial port, and use class name for the appName. 
			serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT); 
			// set port parameters 
			serialPort.setSerialPortParams(DATA_RATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE); 
			// open the streams 
			input = new BufferedReader( new InputStreamReader(serialPort.getInputStream())); 
			output = serialPort.getOutputStream(); 
			// add event listeners 
			serialPort.addEventListener(this); 
			serialPort.notifyOnDataAvailable(true); 
			} 
		catch (Exception e) { 
			System.err.println(e.toString()); 
		} 
	} 
	/** * This should be called when you stop using the port. This will prevent * port locking on platforms like Linux. */ 
	public synchronized void close() { 
		if (serialPort != null) { 
			serialPort.removeEventListener(); 
			serialPort.close(); 
		} 
	} 
	/** * Handle an event on the serial port. Read the data and print it. */ 
	public synchronized void serialEvent(SerialPortEvent oEvent) { 
		if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) { 
			try { 
				String inputLine = input.readLine(); 
				if (inputLine.length() > 0) { 
					
					JsonParser parser = new JsonParser();

					JsonObject json = parser.parse(inputLine).getAsJsonObject();

					double sl = json.get("sl").getAsDouble();
					double sw = json.get("sw").getAsDouble();
					double pl = json.get("pl").getAsDouble();
					double pw = json.get("pw").getAsDouble();
					
					String categorias = categoria(sl, sw, pl, pw);
					
					BasicDBObject documentos = new BasicDBObject("sepal_lenght",sl)
							.append("sepal_wight", sw).append("petal_lenght", pl)
							.append("petal_wight", pw).append("categoria", categorias);
					
					InsertarDatos insertarD = new InsertarDatos();
					
					insertarD.insertar(documentos);
				} 
			} 
			catch (Exception e) { 
				 System.err.println("Error... " + e.toString()); 
			} 
		}  
	} 
	
	public OutputStream getOutput() { 
		return output;
	} 
	
	private static String categoria(double sl, double sw, double pl, double pw) {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String categorias = "";
		String responsBody = "";

		try {
			String url = "http://127.0.0.1:5000/" + sl + "/" + sw + "/" + pl + "/" + pw;

			HttpGet httpGet = new HttpGet(url);
			response = httpclient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			responsBody = EntityUtils.toString(entity);
			
			EntityUtils.consume(entity);

		} catch (Exception e) {
			
			System.out.println("Error: " + e.toString());
			
		} finally {

			try {
				response.close();
				httpclient.close();
			} catch (Exception ei) {
				System.out.println("Error: " + ei.toString());
			}
		}

		return responsBody/*.replace("[", "").replace("]", "").replace("'", "");*/;
	}
}
