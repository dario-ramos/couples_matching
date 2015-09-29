import java.util.ArrayList;
import java.io.IOException;

public class tdatp2{
	/*
	 * Lectura del archivo con los datos de entrada.
	 * El formato de la entrada es:
	 * Hombre1: Mujer11,Mujer12,Mujer13,...,Mujer1N
	 * ...
	 * HombreN: MujerN1,MujerN2,MujerN3,...,MujerNN
	 *
	 * Mujer1: Hombre11,Hombre12,Hombre13,...,Hombre1N
	 * ...
	 * MujerN: HombreN1,HombreN2,HombreN3,...,HombreNN
	 *
	 * Donde N es la cantidad de hombres (y mujeres) y, MujerIJ es la mujer cali-
	 * ficada con J por el hombre I (idem para HombreIJ).
	 * Para realizar el parseo, tengo en cuenta el hecho de que con la primer lí-
	 * nea del archivo puedo obtener los nombres de todas las mujeres (y, por lo
	 * tanto, crearlas).
	 * Luego, comienzo la lectura línea a línea agregando a la lista de preferen-
	 * cias de cada hombre la mujer que aparezca (y en cada línea que leo debo
	 * crear un hombre).
	 * Cuando llega el momento de leer las preferencias de cada mujer ya tengo a
	 * todos los hombres creados (los creé mientras leía sus preferencias).
	 * Para tener acceso rápido al objeto "Individuo" que representa a cada hom-
	 * bre o a cada mujer mientras leo el archivo (esto es, identificándolos por
	 * su nombre) utilizo una tabla de hash.
	 */
	public static Agencia parsear(String nomArch){
		Agencia agencia = new Agencia();
		ArchivoEntrada entrada = null;
		try{
			entrada = new ArchivoEntrada(nomArch);
		}catch(IOException e){
			System.out.println("ERROR EN LA ENTRADA.");
			System.exit(1);//Sale del programa si no se puede parsear.
		}
		String primLineaH = entrada.readln(); //Primera linea con calificaciones de hombres
		TablaHash mujeres = new TablaHash();
		TablaHash hombres = new TablaHash();

		//CREO TODAS LAS MUJERES:
		int i = 0;
		//Me salteo el nombre del primer hombre.
		while (primLineaH.charAt(i)!=':') i++;
		i+=2;//Me ubico en la inicial del nombre de la primera mujer.
		//Obtengo el nombre de todas las mujeres.
		int iAnt = i;
		while (i <= primLineaH.length()){
			if((i == primLineaH.length())||(primLineaH.charAt(i)==',')){
				//Acabo de leer el nombre de una mujer => la agrego a la lista.
				String nombreM = primLineaH.substring(iAnt,i);
				Individuo mujer = new Individuo(nombreM,false);
				mujeres.add(mujer);
				agencia.addMujer(mujer);
				iAnt = i+1;
			}
			i++;
		}

		//CARGO LOS HOMBRES CON SUS LISTAS DE PREFERENCIAS
		entrada.reset();
		String linea = entrada.readln();
		while (!linea.equals("")){//Mientras no sea una línea vacía...
			int calif = 0;
			i = 0;
			//Obtengo el nombre del hombre para crearlo.
			while (linea.charAt(i)!=':') i++;
			Individuo hombre = new Individuo (linea.substring(0,i),true);
			i+=2;//Me ubico en la inicial del nombre de la primera mujer.
			//Obtengo el nombre de cada mujer y la agrego a la lista de preferencias.
			iAnt = i;
			while (i<=linea.length()){
				if((i == linea.length())||(linea.charAt(i)==',')){
					//Acabo de leer el nombre de una mujer => la agrego a la lista.
					String nombreM = linea.substring(iAnt,i);
					Individuo mujer = mujeres.get(nombreM);
					hombre.setPuesto(mujer,calif);
					iAnt = i+1;
					calif++;
				}
				i++;
			}
			hombres.add(hombre);
			//La última línea es para que cuando cargue a las mujeres ya tenga a
			//todos los hombres cargados.
			agencia.addHombre(hombre);
			linea = entrada.readln();
		}
		//CARGO LAS MUJERES CON SUS LISTAS DE PREFERENCIAS
		//Valen los mismos comentarios que para la carga de los hombres.
		linea = entrada.readln();
		while (linea != null){//Mientras no llegue al fin de archivo...
			int calif = 0;
			i = 0;
			while (linea.charAt(i)!=':') i++; //Obtengo el nombre de la mujer.
			Individuo mujer = mujeres.get(linea.substring(0,i));
			i+=2;
			iAnt = i;
			while (i<=linea.length()){
				if((i == linea.length())||(linea.charAt(i)==',')){
					String nombreH = linea.substring(iAnt,i);
					Individuo hombre = hombres.get(nombreH);
					mujer.setPuesto(hombre,calif);
					iAnt = i+1;
					calif++;
				}
				i++;
			}
			linea = entrada.readln();
		}
		return agencia;
	}
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
		long Ti,Tf;
		ArrayList parejas = new ArrayList();
		Agencia agencia = parsear(args[0]);
		
		//Se llama al recolector de basura antes de comenzar, por las dudas.
		System.gc();
		//Comienza la medición del tiempo (se desprecia el parseo).
		Ti = System.currentTimeMillis();
		parejas = agencia.asignarConBT();
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