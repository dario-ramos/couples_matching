
import java.util.ArrayList;
import java.util.Iterator;
/*
 * Esta clase representa a una agencia que contiene la información de todos
 * los hombres y mujeres (incluido su ranking).
 * Además, la agencia es la que se encarga de asignarle un número único a los
 * hombres o a las mujeres. 
 */
public class Agencia{
	private ArrayList hombres;
	private ArrayList mujeres;
	private ArrayList parejas; //Solución transitoria
	private ArrayList solucion; //Solución definitiva.
	private int cantParejas; //Cuenta la cantidad de parejas no rotas de la colección "parejas"
							 //(usado en el algoritmo de Gale & Shapley).
	
	public Agencia (){
		hombres = new ArrayList();
		mujeres = new ArrayList();
		parejas = new ArrayList();
		cantParejas = 0;
	}

	public Individuo getHombre(int ID){
		return (Individuo) hombres.get(ID);
	}
	public Individuo getMujer(int ID){
		return (Individuo) mujeres.get(ID);
	}	
	public int cantHombres(){
		return hombres.size();
	}
	public int cantMujeres(){
		return mujeres.size();
	}
	public void addHombre(Individuo hombre){
		hombre.setNumero(hombres.size());
		hombres.add(hombre);
	}
	public void addMujer(Individuo mujer){
		mujer.setNumero(mujeres.size());
		mujeres.add(mujer);
	}
	/*
	 *----------------------------------------
	 *    RESOLVIENDO POR GALE & SHAPLEY
	 *----------------------------------------
	 */

		/*
		 * PRE: todos los hombres deben tener un ranking de mu-
		 * jeres y viceversa.
		 */
		public ArrayList asignarConGS (){
			parejas.clear();
			cantParejas = 0;
			//PRIMERA ETAPA
			//Cada hombre elige a su mujer favorita.
			for(int i=0;i<this.cantHombres();i++){
				Individuo hombre = this.getHombre(i);
				hombre.mejorPretendido().addPretendiente(hombre);
				//El hombre es ahora pretendiente de su mujer favorita.
			}
			//Cada mujer se empareja con el pretendiente que más le agrade.
			for(int i=0; i<this.cantMujeres();i++){
				Individuo mujer = this.getMujer(i);
				Individuo hombre = mujer.pretendienteFavorito();
				if (hombre != null) { //Si alguien se le había declarado a la mujer.
					Pareja pareja = new Pareja(hombre,mujer);
					parejas.add(pareja);
					cantParejas++;
				}
				mujer.olvidarPretendientes();
			}

			//RESTO DE LAS ETAPAS
			while(!listoGS()){//Mientras no esté lista la asignación...
				//Cada hombre elige a la mujer favorita que no lo ha rechazado aún.
				for(int i=0;i<this.cantHombres();i++){
					Individuo hombre = this.getHombre(i);
					if(hombre.isSoltero()){
						Individuo mujer = hombre.mejorPretendido();
						/*El hombre es ahora pretendiente de la mujer preferida que no lo
						  rechazó aún.*/
						mujer.addPretendiente(hombre);
						if (!mujer.isSoltero()) mujer.addPretendiente(mujer.getPareja().getHombre());
						/*La última línea es para que cuando mujer devuelva su preten-
						  diente favorito, también tenga en cuenta a su marido.*/
					}
				}
				//Cada mujer elige entre los pretendientes y su marido (si tiene).
				for(int i=0; i<this.cantMujeres();i++){
					Individuo mujer = this.getMujer(i);
					Individuo hombre = mujer.pretendienteFavorito();
					if (hombre != null) {
					//Si se le declaró alguien en esta ronda a la mujer.
						//Si la mujer tiene pareja, que rompa con ella.
						if (!mujer.isSoltero()){
							mujer.getPareja().romper();
							cantParejas--; 
						}
						Pareja pareja = new Pareja(hombre,mujer);
						parejas.add(pareja);
						cantParejas++;
						mujer.olvidarPretendientes();
					}
				}
			}
			quitarParejasRotas (parejas);
			/*El último paso se debe a que "parejas" está llena de parejas que han ro-
			  to y no fueron removidas de allí. Esto se debe a que es más eficiente
			  hacer sólo un recorrido a la colección removiendo todas las parejas rotas
			  que ir haciéndolos durante la ejecución del algoritmo.*/
			solucion = parejas;
			return solucion;
		}

		/*
		 * La asignación está lista solo si se han emparejado todos los hombres (y,
		 * por ende, todas las mujeres).
		 */
		private boolean listoGS(){
			return(cantParejas == this.cantHombres());
		}

		/*
		 * Quito de "parejas" todas las parejas que se hayan separado (las identifico
		 * porque la mujer o el hombre son "null".
		 */
		private void quitarParejasRotas(ArrayList parejas){
			Iterator it = parejas.iterator();
			while (it.hasNext()){
				Pareja p = (Pareja) it.next();
				if ((p.getHombre()==null)||(p.getMujer()==null)) it.remove();
			}
		}

	/*
	 *----------------------------------------
	 *      RESOLVIENDO POR BACKTRACKING
	 *----------------------------------------
	 */
		/*
		 * PRE: todos los hombres deben tener un ranking de mu-
		 * jeres y viceversa.
		 */
		public ArrayList asignarConBT (){
			parejas.clear();
			cantParejas = 0;
			solucion = null;
			extender(0);
			return solucion;
		}

		/*
		 * Parte principal de la solución por backtracking.
		 * OBS: Durante la ejecución de este algoritmo, cantParejas equivale
		 * a parejas.size()
		 */
		private void extender (int hombreActual){
			if (!this.listoBT()){
			//Si aún no está lista la solución => se intenta expanderla.
				for (int mujer = 0;mujer < this.cantMujeres();mujer++){
				//Se prueba con todas las mujeres posibles.
					Pareja parejaNueva = new Pareja (this.getHombre(hombreActual),this.getMujer(mujer));
					registrar(parejaNueva);
					if (esEstable())
						extender(hombreActual+1);
					if (solucion != null)
						return;
					olvidar();
				}
			}else solucion = parejas;			
		}

		private void registrar(Pareja p){
			parejas.add(p);
			cantParejas++;
		}

		private void olvidar(){
			parejas.remove(parejas.size() - 1);
			cantParejas--;
		}

		/*
		 * La asignación está lista si es factible y además todos los hombres (y, por
		 * ende, las mujeres) están en pareja.
		 */
		private boolean listoBT(){
			return(esEstable() && (parejas.size() == this.cantHombres()));
		}

		/*
		 * Devuelve true si la asignación es estable (no tiene pares bloqueantes).
		 */
		private boolean esEstable(){
			//Una asignación vacía o con sólo una pareja es estable.
			if ((parejas.size() == 0)||(parejas.size() == 1)) return true; 
			//Dado que las n-1 parejas ya han sido chequeadas entre sí, sólo hay que
			//verificar la estabilidad entre la nueva pareja y todas las demás.
			Pareja p = (Pareja) parejas.get(parejas.size()-1); //La última pareja agregada
			for(int i=0; (i+1)<parejas.size() ;i++){
				if (this.esBloqueante((Pareja) parejas.get(i),p)) return false;
			}
			return true;
		}

		/*
		 * Devuelve "true" si y solo si un hombre y una mujer de las dos parejas
		 * distintas preferiría estar casado entre sí en lugar de con su compañero
		 * actual.
		 */
		private boolean esBloqueante(Pareja p1, Pareja p2){
			Individuo h1 = p1.getHombre();
			Individuo m1 = p1.getMujer();
			Individuo h2 = p2.getHombre();
			Individuo m2 = p2.getMujer();

			if ((h1 == h2) || (m1 == m2)) return true; 
			//Si al hombre 1 le gusta más la mujer 2 que su esposa, la mujer 1, y
			//a la mujer 2 le ocurre lo mismo con él, hay inestabilidad.
			if ((h1.getPuesto(m2) < h1.getPuesto(m1)) &&
				 (m2.getPuesto(h1) < m2.getPuesto(h2)))
				return true;
			//Si a la mujer 1 le gusta más el hombre 2 que su marido, el hombre 1, y
			//al hombre 2 le ocurre lo mismo con ella, hay inestabilidad.
			if ((m1.getPuesto(h2) < m1.getPuesto(h1)) &&
				 (h2.getPuesto(m1) < h2.getPuesto(m2)))
				return true;
			//En caso contrario, la asignacion consistente en sólo estas dos parejas
			//es estable.
			return false;
		}
	}