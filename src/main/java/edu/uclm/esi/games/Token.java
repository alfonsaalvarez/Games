package edu.uclm.esi.games;

import java.util.UUID;

import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Token {

	@Bsonable
	private String userName;
	@Bsonable
	private Object caducidad;
	@Bsonable
	private String valor;



	public Token(String userName){
		this.userName=userName;
		this.caducidad=System.currentTimeMillis()+5*60*1000;
		this.valor=UUID.randomUUID().toString();
	}
	public Token() {
	    
	  }
	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
	public String getUserName() {
		return userName;
	}
}
