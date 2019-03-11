package edu.uclm.esi.games.sudoku;

import edu.uclm.esi.games.Board;
import edu.uclm.esi.games.Match;
import edu.uclm.esi.games.Player;

public class SudokuBoard extends Board {

	private int[] sudokuResuelto;
	private int[] tablero;
	private int[] sudoku0;
	private int[] sudoku1;


	public SudokuBoard(SudokuMatch sudokuMatch) {
		super(sudokuMatch);
		this.sudokuResuelto = new int [] {5,3,4,6,7,8,9,1,2,6,7,2,1,9,5,3,4,8,1,9,8,3,4,2,5,6,7,8,5,9,7,6,1,4,2,3,4,2,6,8,5,3,7,9,1,7,1,3,9,2,4,8,5,6,9,6,1,5,3,7,2,8,4,2,8,7,4,1,9,6,3,5,3,4,5,2,8,6,1,7,9};
		this.sudoku0 = new int[81];
		this.sudoku1 = new int[81];
		this.tablero = getTableroAleatorio();
	}

	public int[] getTablero() {
		return tablero;
	}

	// Iría en realidad a buscar un sudoku a la BD
	private int[] getTableroAleatorio() {
		int[][] tableros = {
				new int [] {5,3,0,0,7,0,0,0,2,6,0,0,1,9,5,0,0,0,0,9,8,0,0,0,0,6,0,8,0,0,0,6,0,0,0,3,4,0,0,8,0,3,0,0,1,7,0,0,0,2,0,0,0,6,0,6,0,0,0,0,2,8,0,0,0,0,4,1,9,0,0,5,0,0,0,0,8,0,0,7,9}
		};
		return tableros[0];
	}

	@Override
	public void move(Player player, int[] coordinates) throws Exception {

		if (this.match.getPlayers().get(0) == player) {
			rellenar(sudoku0, coordinates);
		} else {
			rellenar(sudoku1, coordinates);
		}
	}

	private void rellenar(int[] tiradas, int []vector) {
		for (int i=0; i<tiradas.length; i++) {
			tiradas[i] = vector[i];
		}
	}

	@Override
	public Player getWinner() {
		if(esSolucion(sudoku0)) {
			return this.match.getPlayers().get(0);
		}else if (esSolucion(sudoku1)){
			return this.match.getPlayers().get(1);

		}else {
			return null;
		}
	}

	/*	private Player gana(int[] a, int[] b) {
		int victoriasA = 0, victoriasB=0;
		for (int i=0; i<a.length; i++) {
			if (gana(a[i],b[i]))
				victoriasA++;
			else
				victoriasB++;
		}
		return victoriasA > victoriasB ? this.match.getPlayers().get(0) : this.match.getPlayers().get(1);
	}*/

	private boolean esSolucion(int []a) {	
		for(int i= 0;i<sudokuResuelto.length;i++) {
			if(a[i]!=sudokuResuelto[i]) {
				return false;
			}		
		}
		return true;

	}

	public int[] getTiradas1() {
		return sudoku0;
	}

	public int[] getTiradas2() {
		return sudoku1;
	}

	@Override
	public boolean end() {
		if(this.getWinner()!= null) {
			return true;
		}else {

			return false;
		}
	}



}
