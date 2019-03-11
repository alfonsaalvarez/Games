package edu.uclm.esi.web.ws;



import java.util.Map.Entry;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;


import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.internal.Base64;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.games.sudoku.SudokuBoard;
import edu.uclm.esi.mongolabels.dao.MongoBroker;
import edu.uclm.esi.web.Manager;

@Component
public class WSServer extends TextWebSocketHandler {
	private static ConcurrentHashMap<String, WebSocketSession> sessionsById=new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, WebSocketSession> sessionsByPlayer=new ConcurrentHashMap<>();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessionsById.put(session.getId(), session);
		Player player = (Player) session.getAttributes().get("player");
		String userName=player.getUserName();
		if (sessionsByPlayer.get(userName)!=null) {
			sessionsByPlayer.remove(userName);

		}
		byte[]foto=player.loadFoto();
		if(foto!=null)
			sendBinary(session, foto);
		sessionsByPlayer.put(userName, session);
	}



	private void sendBinary(WebSocketSession session, byte[] foto) {
		// TODO Auto-generated method stub
		String imagen = Base64.encode(foto);
		JSONObject jso = new JSONObject();
		try {
			jso.put("type", "FOTO");
			jso.put("foto", imagen);
			WebSocketMessage<?> message = new TextMessage(jso.toString());
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}



	@Override
	protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
		Player player = (Player) session.getAttributes().get("player");
		byte[] bytes = message.getPayload().array();
		player.setFoto(bytes);
		try {
			MongoBroker.get().insertBinary("Fotos",player.getUserName(),bytes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println(message.getPayload());
		JSONObject jso=new JSONObject(message.getPayload());
		if (jso.getString("TYPE").equals("MOVEMENT")) {
			Player player = (Player) session.getAttributes().get("player");
			JSONArray coordinates = jso.getJSONArray("coordinates");
			Match match=Manager.get().move(player,coordinates);
			send(match.getPlayers(), match);
		}else if (jso.getString("TYPE").equals("MENSAJE")) {
			sendChat(session, jso);
		}
	}
	
	public static void sendChat(WebSocketSession session, JSONObject jso) throws Exception {
		JSONObject obj = new JSONObject();
		obj.put("TYPE", "MENSAJE");
		obj.put("remitente", jso.getString("remitente"));
		obj.put("contenido", jso.getString("contenido"));

		WebSocketMessage<?> message = new TextMessage(obj.toString());

		for (Entry<String, WebSocketSession> entry : sessionsByPlayer.entrySet()) {
			try {
				entry.getValue().sendMessage(message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void send(Vector<Player> players, Match match) {
		ObjectMapper mapper = new ObjectMapper(); 
		JSONObject jso;
		try {
			jso = new JSONObject(mapper.writeValueAsString(match));
			jso.put("TYPE", "MATCH");
			for (Player player : players) {
				WebSocketSession session=sessionsByPlayer.get(player.getUserName());
				WebSocketMessage<?> message = new TextMessage(jso.toString());
				session.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void startMatch(Vector<Player> players, Match match) {
		ObjectMapper mapper = new ObjectMapper(); 
		JSONObject jso;
		try {
			jso = new JSONObject(mapper.writeValueAsString(match));
			jso.put("TYPE", "START_MATCH");
			for (Player player : players) {
				WebSocketSession session=sessionsByPlayer.get(player.getUserName());
				WebSocketMessage<?> message = new TextMessage(jso.toString());
				session.sendMessage(message);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
