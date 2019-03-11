package edu.uclm.esi.games;

import java.io.IOException;

import org.bson.BsonDocument;
import org.bson.BsonString;
import org.json.JSONObject;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Player {
	@Bsonable
	private String userName;
	@Bsonable
	private String email;
	@Bsonable
	@JsonIgnore
	private String pwd;
	@JsonIgnore
	private Match currentMatch;
	@Bsonable
	private String idGoogle;
	@Bsonable
	private String tipo;
	@Bsonable
	private byte[]foto;


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	private void setPwd(String pwd) {
		this.pwd=pwd;
	}

	public static Player modificarPWD(String userName, String pwd) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("userName", new BsonString(userName));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		MongoBroker.get().delete("Player", criterion);
		player.setPwd(pwd);
		MongoBroker.get().insert(player);
		return player;
	}

	public String getPwd() {
		return pwd;
	}

	public static Player identify(String userName, String pwd) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("userName", new BsonString(userName)).put("pwd", new BsonString(pwd));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public static Player identifyGoogle(String id, String nombre, String email) throws Exception {
		BsonDocument criterion=new BsonDocument();
		criterion.append("idGoogle", new BsonString(id)).put("userName", new BsonString(nombre));
		criterion.append("email",new BsonString(email));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		return player;
	}

	public static Player register(String email, String userName, String pwd) throws Exception {
		Player player=new Player();
		player.setEmail(email);
		player.setUserName(userName);
		player.setPwd(pwd);
		MongoBroker.get().insert(player);
		return player;
	}



	public static Player registerGoogle(String id, String nombre, String email) throws Exception {
		Player player=new Player();
		player.setEmail(email);
		player.setUserName(nombre);
		player.setIdGoogle(id);
		MongoBroker.get().insert(player);
		return player;
	}

	public void setIdGoogle(String id) {
		this.idGoogle=id;
		this.tipo=tipo;
	}
	public void setCurrentMatch(Match match) {
		this.currentMatch=match;
	}
	

	public Match getCurrentMatch() {
		return currentMatch;
	}

	public Match move(int[] coordinates) throws Exception {
		return this.currentMatch.move(this, coordinates);
	}

	public static Player solicitarToken(String userName) {
		Player player=null;
		try {
			BsonDocument criterion=new BsonDocument();
			criterion.append("userName", new BsonString(userName));
			player=(Player)MongoBroker.get().loadOne(Player.class, criterion);
			player.createToken();

		}catch(Exception e) {

		}

		return player;
	}

	private void createToken() throws Exception {
		Token token =new Token(this.userName);
		MongoBroker.get().insert(token);
		EMailSenderService email=new EMailSenderService();
		email.enviarPorGmail(this.email, token.getValor());
	}

	public void setFoto(byte []bytes) {
		this.foto=bytes;
	}

	public byte[] loadFoto(){
		try {
			BsonDocument criterion=new BsonDocument();
			MongoBroker.get().loadBinary("Fotos",criterion);
			BsonDocument result=MongoBroker.get().loadBinary("Fotos",criterion);
			return result.getBinary("bytes").getData();
		}catch(Exception e) {

		}
		return null;

	}
	public static Player modificarPWDEmail(String token, String pwd) throws Exception {
		BsonDocument criterion=new BsonDocument();
		String user=getPlayerByToken(token);
		criterion.append("userName", new BsonString(user));
		Player player=(Player) MongoBroker.get().loadOne(Player.class, criterion);
		MongoBroker.get().delete("Player", criterion);
		player.setPwd(pwd);
		MongoBroker.get().insert(player);
		return player;
	}
	
	private static String getPlayerByToken(String token) throws Exception {
	    BsonDocument criterion=new BsonDocument();
	    criterion.append("valor", new BsonString(token));
	    Token tok=(Token) MongoBroker.get().loadOne(Token.class, criterion);
	    return tok.getUserName();
	  }

}
