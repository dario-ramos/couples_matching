package couplesmatching.parva;

public class Elemento {
	private Object dato;
	private Comparable prioridad;	
	
	public Elemento(Object d, Comparable p){
		this.dato = d;
		this.prioridad = p;
	}
	public Object getDato(){
		return dato;
	}
	public Comparable getPrioridad(){
		return prioridad;
	}	
	public void setDato(Object d){
		dato = d;
	}
	public void setPrioridad(Comparable p){
		prioridad = p;
	}	
}
