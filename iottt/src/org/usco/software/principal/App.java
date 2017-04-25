package org.usco.software.principal;

import java.io.OutputStream;

import org.usco.software.principal.SerialCom;

public class App {
	
	private static OutputStream output;

	public static void main(String[] args) {
		String puerto = "COM4";
		SerialCom main = new SerialCom();
		main.initialize(puerto);
		output = main.getOutput();
		System.out.println("=== INICIO DEL PROGRAMA ===");
		
	}	

}
