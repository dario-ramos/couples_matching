import java.io.*;
import java.lang.Character;

public class ArchivoEntrada {
	private FileReader lector;
	private BufferedReader entrada;
	private String nomArch;
	//Los siguientes atributos representan el caracter devuelto cuando hay un fin
	//de archivo o algun error.
	public final static String EOFstr = "" + Character.MAX_VALUE;
	public final static char EOFchr = Character.MAX_VALUE;
	/*
	 * Averiguar cuales son la cadena vac�a que se devuelve al leer una l�nea en blanco
	 * o el caracter de fin de l�nea (o el 1� y el �ltimo de una l�nea en blanco).
	 * OBS: creo que son:
	 *  TEXTO\n
	 *  \r\n
	 *  \rTEXTO\n
	 *  \rTEXTO...
	 */


	public ArchivoEntrada(String nomArch) throws IOException{
		this.nomArch = nomArch;
		lector = new FileReader (nomArch);
		entrada = new BufferedReader(lector);
	}

	/*
	 * Lee una l�nea del archivo de entrada.
	 * Si ocurre un error, devuelve una cadena vac�a.
	 */
	public String readln(){
		try{
			return entrada.readLine();
		}catch (IOException e){
			return EOFstr;
		}
	}
	/*
	 * Lee un caracter del archivo de entrada.
	 * Si ocurre un error, devuelve el caracter nulo.
	 */
	public int read(){
		try{
			return entrada.read();
		}catch(IOException e){
			return EOFchr;
		}
	}

	/*
	 * Se ubica en el primer caracter del archivo.
	 */
	public void reset(){
		try{
			//Cierro el archivo y lo vuelvo a abrir.
			entrada.close();
			lector = new FileReader (nomArch);
			entrada = new BufferedReader(lector);
		}catch(IOException e){
			e.printStackTrace(System.err);
		}
	}

}