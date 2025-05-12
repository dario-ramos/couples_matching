package couplesmatching;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class tdatp2{

	/*
	 * Escritura del archivo con las soluciones.
	 * La salida es del modo:
	 * ------------------------------
	 * Cantidad de parejas: XXXX
	 * Tiempo requerido: XXXXms
	 * -----------SOLUCIÓN-----------
	 * #1: Hombre1 - Mujer1
	 * #2: Hombre2 - Mujer2
	 * ...
	 * #n: Hombren - Mujern
	 */
	public static void salida(String nomArch, ArrayList parejas, long tiempo){
		ArchivoSalida salida = new ArchivoSalida (nomArch);
		String linea = "------------------------------";
		salida.println(linea);
		System.out.println(linea);
		linea = "Cantidad de parejas: " + parejas.size();
		salida.println(linea);
		System.out.println(linea);
		linea = "Tiempo requerido: " + tiempo + "ms";
		salida.println(linea);
		System.out.println(linea);
		linea = "-----------SOLUCIÓN-----------";
		salida.println(linea);
		System.out.println(linea);
		for (int i=0; i<parejas.size(); i++){
			Pareja p = (Pareja)parejas.get(i);
			linea = "#" + (i+1) + ": ";
			linea+= p.getHombre().getNombre();
			linea+= " - ";
			linea+= p.getMujer().getNombre();
			salida.println(linea);
			System.out.println(linea);
		}
		salida.guardarCambios();
	}

	public static void main (String[] args){
		if (args.length == 0) {
			System.err.println("error: es necesario especificar el archivo de entrada");
			System.exit(1);
		}

		Agencia agencia;
		try {
			FileReader inputFileReader = new FileReader(args[0]);
			BufferedReader inputReader = new BufferedReader(inputFileReader);
			AgencyBuilder builder = new AgencyBuilder(inputReader);
			agencia = builder.build();
		} catch (Exception ex) {
			System.err.println("Fatal error reading input file: " + ex.getMessage());
			System.exit(2);
			return;
		}

		long Ti,Tf;
		//Se llama al recolector de basura antes de comenzar, por las dudas.
		System.gc();
		//Comienza la medición del tiempo (se desprecia el parseo).
		Ti = System.currentTimeMillis();
		ArrayList parejas = agencia.asignarConBT();
		Tf = System.currentTimeMillis();  // Finaliza la medición del tiempo.
		salida("sc_BT.txt",parejas,Tf-Ti);

		//Rompo todas las parejas para que los individuos estén solteros al comenzar el
		//algoritmo de Gale & Shapley.
		for (int i = 0;i<parejas.size();i++){
			Pareja p = (Pareja) parejas.get(i);
			p.romper();
		}
		System.gc();
		Ti = System.currentTimeMillis();
		parejas = agencia.asignarConGS();
		Tf = System.currentTimeMillis();
		salida("sc_GS.txt",parejas,Tf-Ti);
	}
}
