package couplesmatching;

/*
 * Clase que representa una pareja entre un hombre y una mujer.
 */
public class Pareja{
	private Individuo hombre;
	private Individuo mujer;

	public Pareja (Individuo hombre,Individuo mujer){
		this.hombre = hombre;
		this.mujer = mujer;
		hombre.formarPareja(this);
		mujer.formarPareja(this);
	}

	public Individuo getHombre(){
		return hombre;
	}
	public Individuo getMujer(){
		return mujer;
	}
	public void romper(){
		hombre.separar();
		mujer.separar();
		hombre = null;
		mujer = null;
	}
}
