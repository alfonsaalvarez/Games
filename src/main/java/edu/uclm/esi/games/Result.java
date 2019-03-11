package edu.uclm.esi.games;

import edu.uclm.esi.mongolabels.labels.Bsonable;

public class Result {
	@Bsonable
	private String userName;
	@Bsonable
	private String Username2;
	@Bsonable
	private String winner;

	public Result(String userName, String userName2, String winner) {
		this.userName=userName;
		this.Username2=userName2;
		this.winner=winner;
	}

}
