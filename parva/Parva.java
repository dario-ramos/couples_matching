package parva;

/**
 * Parva/mont�culo/heap de m�nimos. Requiere conocer, para la
 * creaci�n, la cantidad m�nima de elementos. De todos modos, la
 * estructura puede crecer din�micamente.  
 */
public class Parva {
	private Elemento[] vector;
	private int tamAct; //Cantidad actual de elementos.
	
	public Parva(int n, Comparable infinito){ 
		 vector = new Elemento[n+1];
		 tamAct = 0;
		 vector[0] = new Elemento(null, infinito); 
		 /* vector[0] es un padre falso de la ra�z, una cota superior de
		  * todas las prioridades. Su fin es la simplificaci�n de ciertas
		  * operaciones.
		  */
		 for(int i=1;i<=n;i++)
			vector[i] = null;			
	}
	public boolean esVacia(){
		return (tamAct==0);
	}
	public void borrar(){
		for (int i=1;i<tamAct;i++)
			vector[i] = null;
		tamAct = 0;
	}
	/**
	 * PRE: Parva creada. 
	 * @return Los datos asociados al elemento de mayor prioridad.
	 * Si la parva est� vac�a, devuelve "null".
	 */
	public Object primero(){
		if (vector[1] == null) return null; 
		return vector[1].getDato();
	}
	
	public void insertar(Object o, Comparable prior){
		if (tamAct == vector.length-1)
			duplicarVector(); //La estructura puede crecer en tiempo de ejecuci�n.
		int hueco = tamAct+1;
		/* El elemento sube en el "�rbol" en tanto su prioridad sea menor que la
		 * de su "padre actual". */
		while(((vector[hueco/2].getPrioridad()).compareTo(prior))>0){			
			vector[hueco] = vector[hueco/2];
			hueco/=2;
		}
		vector[hueco] = new Elemento(o, prior);
		tamAct++;
	}
	/**
	 * PRE: Parva creada y vac�a. 
	 * Aumenta al doble el espacio para insertar elementos dentro
	 * de la parva.  
	 */
	private void duplicarVector(){
		Elemento[] nuevoVector;
		nuevoVector = new Elemento[vector.length*2];
		for(int i=0;i<vector.length;i++){
			nuevoVector[i] = vector[i];
		}
		vector = nuevoVector;
	}
	/**
	 * PRE: Parva creada.	 
	 */
	public Object getMaximo(){
		if (vector[1] == null) return null;
		Elemento e = (Elemento)vector[1];
		vector[1] = vector[tamAct];
		// El �ltimo elemento es ahora la ra�z.
		vector[tamAct] = null;
		tamAct--;
		/* Ahora hay que restablecer la propiedad heap, de ser
		 * ello necesario. */
		hundir(1);
		return e.getDato();
	}
	/**
	 * Es el swapdown. "Hunde" al elemento en la parva, manteniendo
	 * la propiedad heap.
	 */
	private void hundir(int hueco){
		int hijo = hueco*2;
		if (hijo > tamAct) return;//El elemento a hundir no tiene hijos.		
		if (hijo+1<=tamAct)//Si tiene m�s de un hijo, elijo al de mayor prioridad	
			if (vector[hijo].getPrioridad().compareTo(vector[hijo+1].getPrioridad())>0)
				hijo++;
		if (vector[hijo].getPrioridad().compareTo(vector[hueco].getPrioridad())<0){
			//Intercambio al nodo con el hijo que eleg� en el paso anterior 
			Elemento aux = vector[hijo];
			vector[hijo] = vector[hueco];
			vector[hueco] = aux;			
			hundir(hijo);
		}
	}
	/**
	 * La inserci�n es ordenada, por lo que este m�todo no ser�a necesario.
	 * S�lo est� por cuestiones de extensibilidad. 
	 */
	private void arreglarHeap(){
		for(int i=tamAct/2;i>0;i--){
			hundir(i);
		}
	}	
}