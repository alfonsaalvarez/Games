package edu.uclm.esi.games.sudoku;

import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;
import edu.uclm.esi.games.Result;
import edu.uclm.esi.mongolabels.dao.MongoBroker;

public class SudokuMatch extends Match {
	
	public SudokuMatch () {
		super();
		this.board = new SudokuBoard(this);
	}
	
	protected boolean tieneElTurno(Player player) {
		return true;
	}

	@Override
	public void calculateFirstPlayer() {
	

	}

	protected void save() throws Exception {
		Result result=new Result(this.getPlayers().get(0).getUserName(), this.getPlayers().get(1).getUserName(), this.winner.getUserName());
		MongoBroker.get().insert(result);
	}
}
