package edu.uclm.esi.games;

import java.util.UUID;
import java.util.Vector;

import org.json.JSONObject;


public abstract class Match {
	protected UUID id;
	protected Vector<Player> players;
	protected int currentPlayer;
	protected Player winner;
	protected Board board;
	
	public Match() {
		this.id=UUID.randomUUID();
		this.players=new Vector<>();
		this.currentPlayer=-1;
	}
	
	public UUID getId() {
		return id;
	}

	public void addPlayer(Player player) {
		this.players.add(player);
		player.setCurrentMatch(this);
	}
	
	public Vector<Player> getPlayers() {
		return players;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	public Match move(Player player, int[] coordinates) throws Exception {
		if (!tieneElTurno(player))
			throw new Exception("You are not the current player");
		if (this.board.end())
			throw new Exception("The match is finished");
		this.board.move(player, coordinates);
		this.currentPlayer=(this.currentPlayer+1)%this.players.size();
		this.winner=this.board.getWinner();
		if(this.board.end()) 
			save();
		return this;
	}
	

	protected abstract void save() throws Exception;

	protected abstract boolean tieneElTurno(Player player);

	public abstract void calculateFirstPlayer();


}
