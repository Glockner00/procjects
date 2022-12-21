package com.chess.engine.player;
/*
 * Represents the transition from one board to another.
 * Carries with all information that is needed for the next board.
 * 
 * When we make a move a move transition is returned.
 * 
 */

import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

public class MoveTransition {
	private final Board transitionBoard;
	private final Move move;
	private final MoveStatus moveStatus; //where we able to do the move or not?
	
	public MoveTransition(final Board transitionBoard, final Move move, final MoveStatus moveStatus) {
		this.transitionBoard = transitionBoard;
		this.move = move;
		this.moveStatus = moveStatus;
	}
	public MoveStatus getMoveStatus() {
		return this.moveStatus;
	}
}
