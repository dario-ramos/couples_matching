package couplesmatching;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ArchivoSalida {
	/*
	 *------------------------------------------------------------
	 *Constructores:
	 *------------------------------------------------------------
	 */
	public ArchivoSalida(String nomArch){
		this.nomArch = new String (nomArch);
		archivo = abrir (nomArch,false);
	}
	public ArchivoSalida(String nomArch, boolean append){
		this.nomArch = new String (nomArch);
		archivo = abrir (nomArch,append);
	}
	/*
	 *Guarda los cambios realizados hasta el momento en
	 *el archivo.
	 */
	public void guardarCambios(){
		archivo.close();
		archivo = abrir (nomArch,true);
	}
	/*
	 *Imprime una línea en el archivo y genera un
	 *caracter "fin de línea".
	 */
	public void println(boolean b){archivo.println(b);}
	public void println(char c)	{archivo.println(c);}
	public void println(char[] s)	{archivo.println(s);}
	public void println(double d)	{archivo.println(d);}
	public void println(float f)	{archivo.println(f);}
	public void println(int i)		{archivo.println(i);}
	public void println(long l)	{archivo.println(l);}
	public void println(Object o)	{archivo.println(o.toString());}
	/*
	 *Imprime una cadena de caracteres en el archivo y no
	 *genera el "fin de línea".
	 */
	public void print(boolean b)	{archivo.print(b);}
	public void print(char c)		{archivo.print(c);}
	public void print(char[] s)	{archivo.print(s);}
	public void print(double d)	{archivo.print(d);}
	public void print(float f)		{archivo.print(f);}
	public void print(int i)		{archivo.print(i);}
	public void print(long l)		{archivo.print(l);}
	public void print(Object o)	{archivo.print(o.toString());}
	/*
	 *Abre el archivo nomArch preparándolo para la escritura.
	 *Si "append == false", borra todo el contenido previo del
	 *archivo que se abre.
	 */
	private static PrintWriter abrir (String nomArch, boolean append){
		try{
			return new PrintWriter(new BufferedWriter(new FileWriter(nomArch,append)));
		}
		catch(FileNotFoundException archNoEnc){
			System.out.println("Archivo no encontrado: " + archNoEnc);
		}
		catch(IOException ioExc){
			ioExc.printStackTrace(System.err);
		}
		return null;
	}
	/*
	 *------------------------------------------------------------
	 *Atributos:
	 *------------------------------------------------------------
	 */
	private PrintWriter archivo;
	private String nomArch;
}
