import parva.Parva;
/*
 * Esta clase modela a un individuo conteniendo toda la información requerida
 * para resolver el problema de las asignaciones estables.
 */
public class Individuo {
	private String nombre;
	private boolean hombre; //Indica si es o no un hombre.
	private int ID;
	private Pareja pareja; //Pareja a la cual pertenece éste individuo.
	private int[] posiciones; //Posiciones en el ranking.
	private int cantRanqueados;
	private Parva pretendidos; //ID de los individuos pretendidos ordenados por puesto.
	private Parva pretendientes;  //Individuos que se le declararon a éste.

	public Individuo (String nombre, boolean hombre){
		this.nombre = nombre;
		this.hombre = hombre;
		pareja = null;
		//Cantidad estimativa de la cantidad de individuos: 1000.
		cantRanqueados = 1000;
		posiciones = new int[cantRanqueados]; 
		pretendientes = new Parva(1000, new Integer(Integer.MIN_VALUE));
		pretendidos = new Parva(1000, new Integer(Integer.MIN_VALUE));
	}
	public Individuo (String nombre, boolean hombre,int totIndividuos){
		this.nombre = nombre;
		this.hombre = hombre;
		pareja = null;
		//Cantidad estimativa de la cantidad de individuos: 1000.
		cantRanqueados = totIndividuos;
		posiciones = new int[cantRanqueados]; 
		pretendientes = new Parva(totIndividuos, new Integer(Integer.MAX_VALUE));
		pretendidos = new Parva(totIndividuos, new Integer(Integer.MAX_VALUE));
	}
	
	public String getNombre(){
		return nombre;
	}
	public int getNumero(){
		return ID;
	}
	public Pareja getPareja(){
		return pareja;
	}
	public boolean isHombre(){
		return hombre;
	}
	public boolean isSoltero(){
		return (pareja == null);
	}
	/*
	 * Devuelve el puesto dado por el individuo a i.
	 * Y, en caso de haber un error, por no haber asignado un puesto a
	 * i, se devuelve -1.
	 */
	public int getPuesto(Individuo i){
		if (i.getNumero() < 0) return -1;
		if (i.getNumero() > cantRanqueados) return -1; 
		return posiciones[i.getNumero()];
	}
	/*
	 * Devuelve el individuo preferido por éste. En la próxima llamada, devolverá el 
	 * que lo sigue.
	 */
	public Individuo mejorPretendido(){
		return (Individuo) pretendidos.getMaximo();
	}

	/*
	 * Devuelve el pretendiente con mejor puesto para este individuo.
	 */
	public Individuo pretendienteFavorito(){
		return (Individuo)pretendientes.primero();
	}
	public void olvidarPretendientes(){
		pretendientes.borrar();
	}
	
	public void setNombre(String nombre){
		this.nombre = nombre;
	}
	public void setNumero(int ID){
		this.ID = ID;
	}
	public void formarPareja(Pareja pareja){
		this.pareja = pareja;
	}
	public void separar(){
		pareja = null;
	}

	public void setHombre(boolean hombre){
		this.hombre = hombre;
	}
	/*
	 * Si dos individuos tienen el mismo número, no serán distinguidos por
	 * este método.
	 * Si se pasa por parámetro un puesto negativo, será asignado como
	 * cero.
	 */
	public void setPuesto(Individuo i, int puesto){
		if (i.getNumero() > cantRanqueados){
			//Aumento el tamaño del vector para que tenga más cantidad de posiciones
			//de tal modo que exista una posición posiciones[i.getNumero()]
			//pero, de paso, aprovecho y lo hago más grande aún para prevenir
			//futuros aumentos de tamaño ya que son costosos.
			int nuevoTam= Math.round(i.getNumero() * 1.5f);
			int[] nuevo = new int[nuevoTam];
			for (int j=0; j<cantRanqueados; j++) nuevo[j] = posiciones[j];
			cantRanqueados = nuevoTam;
			posiciones = nuevo;
		}
		posiciones[i.getNumero()] = Math.max (puesto,0);
		pretendidos.insertar(i,new Integer(puesto));
	}
	public void addPretendiente(Individuo i){
		pretendientes.insertar(i,new Integer(this.getPuesto(i)));
	}
}