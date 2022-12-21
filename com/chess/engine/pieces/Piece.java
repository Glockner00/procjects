package com.chess.engine.pieces;
import java.util.Collection;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;

/*
 * Most important method: calculate legal moves. 
 */
public abstract class Piece {
	protected final int piecePosition;
	protected final Alliance pieceAlliance;
	protected final boolean isFirstMove;
	
	Piece(final int piecePosition, final Alliance pieceAlliance){
		this.piecePosition = piecePosition;
		this.pieceAlliance = pieceAlliance;
		//TODO: More code :(
		this.isFirstMove = false;
	}
	public int getPiecePosition() {
		return this.piecePosition;
	}
	public Alliance getPieceAlliance() {
		return this.pieceAlliance;
	}
	public boolean isFirstMove() {
		return this.isFirstMove;
	}
	/**
	 * takes a given board and for the given piece calculates that pieces legal moves
	 * for each concrete piece we will override this method.	
	 * @param board
	 * @return
	 */
	public abstract Collection<Move> calculateLegalMoves(final Board board);
	
	public enum pieceType{
		PAWN("P"),
		KNIGHT("N"),
		BISHOP("B"),
		ROOK("R"),
		QUEEN("Q"),
		KING("K");
		
		private String pieceName;
		pieceType(final String pieceName){
			this.pieceName = pieceName;
		}
		@Override
		public String toString() {
			return this.pieceName;
		}
	}
	
	
}