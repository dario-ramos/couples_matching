public class TablaHash {
	private Individuo[] individuos;
	private int tamanio;
	private int cant; //Representa la cantidad de elementos de la tabla.

	public TablaHash(){
		individuos = new Individuo[1000];
		tamanio = 1000;
		cant = 0;
	}

	public TablaHash(int tamanio){
		individuos = new Individuo[tamanio];
		this.tamanio = tamanio;
		cant = 0;
	}

	/*
	 * Devuelve el individuo cuyo nombre es el dado.
	 * En caso de que no pertenezca a la tabla, devuelve null.
	 * Para hallarlo se aplica el siguiente m�todo:
	 * 1_ Calculo: "a = hashCode (nombre del individuo)".
	 * 2_ Si la ubicaci�n est� vac�a, devuelvo null.
	 * 3_ Si la ubicaci�n est� ocupada por el individuo pedido, lo devuelvo.
	 * 4_ En otro caso, sumo uno a "a" y vuelvo al paso 2.
	 */
	public Individuo get(String nombre){
		int cod = hashCode (nombre);
		do{
			if (individuos[cod] == null) return null;
			if (individuos[cod].getNombre().equals(nombre)) return individuos[cod];
			cod++;
		}while (true);
	}

	/*
	 * Agrega el individuo a la tabla.
	 * Para elegir la ubicaci�n en la que se coloca se siguen los siguientes pa-
	 * sos:
	 * 1_ Calculo: "a = hashCode (nombre del individuo)".
	 * 2_ Si la ubicaci�n est� vac�a, lo agrego.
	 * 3_ En otro caso, sumo uno a "a" y vuelvo al paso 2.
	 * Si llevo la tabla a un nivel de ocupaci�n de m�s del 70%, duplico su tama-
	 * �o y reubico todos los elementos.
	 */
	public void add(Individuo i){
		int cod = hashCode (i.getNombre());
		while (individuos[cod] != null){
			cod++;
			cod%= tamanio;
		}
		individuos[cod] = i;
		cant++;
		if (cant >= Math.round(tamanio * 0.7f)) reHashear();
	}

/*
 *EN EL PROGRAMA QUE IMPLEMENTAMOS NO ES NECESARIO REMOVER ELEMENTOS.
	public void remove(Individuo i){
		return;
	}
*/

	/*
	 * Devuelve el c�digo de hash de un individuo dado su nombre.
	 * codigo = c0 * B^n + c1 * B^(n-1) + ... + cn
	 * Donde ci es el i-�simo caracter del nombre (convertido a min�sculas y qui-
	 * t�ndole los espacios que haya antes de la primera inicial o despu�s de la
	 * �ltima letra).
	 * B = 26 (de este modo, si no hubiera restricciones como el tama�o de la ta-
	 * bla de hash o de un m�ximo representable por "int", la asignaci�n del c�-
	 * digo ser�a biun�voca).
	 */ 
	private int hashCode(String nombre){
		//El nombre en min�sculas y sin espacios al comienzo o al final.
		String cad = nombre.trim().toLowerCase();
		int codigo = (int)(nombre.charAt(0));
		for (int i=1;i<cad.length();i++){
			codigo*= 5;
			codigo+= (nombre.charAt(0) - 'a') + 1;
			codigo%= tamanio;
		}
		return codigo;
	}
	/*
	 * Duplica el tamanio de la tabla de hash.
	 * Para logarlo, crea una nueva tabla del doble de tamanio que la actual y
	 * copia todo el contenido de la vieja en ella.
	 */
	private void reHashear(){
		int cantCopiada = 0;
		TablaHash aux = new TablaHash(tamanio * 2);
		for (int i = 0;(i < tamanio)||(cantCopiada < cant); i++)
			if (individuos[i] != null){
				aux.add(individuos[i]);
				cantCopiada++;
			}
		this.individuos = aux.individuos;
	}
}