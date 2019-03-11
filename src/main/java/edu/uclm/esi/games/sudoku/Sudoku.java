package edu.uclm.esi.games.sudoku;

import edu.uclm.esi.games.Game;
import edu.uclm.esi.games.Match;

public class Sudoku extends Game {
	
	public Sudoku() {
		super(2);
	}

	@Override
	public String getName() {
		return "Sudoku";
	}

	@Override
	protected Match createMatch() {
	
		return new SudokuMatch();
	}

}
