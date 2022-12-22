package com.chess.engine.board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Piece;

/**
 * Materialze a new board into existens that represents the board that would exist if you made a move on the incomming board.
 * @author axelg
 *
 */

public abstract class Move {
	final Board board;
	final Piece movedPiece;
	final int destinationCoordinate;
	
	public static final Move NULL_MOVE = new NullMove();
	
	private Move(final Board board, 
				 final Piece movedPiece,
				 final int destinationCoordinate ) {
		this.movedPiece = movedPiece;
		this.board = board;
		this.destinationCoordinate = destinationCoordinate;
	}
	
	public int getCurrentCoordinate() {
		return this.getMovedPiece().getPiecePosition(); 
	}
 	public int getDestinationCoordinate() {
		return this.destinationCoordinate;
	}
	public Piece getMovedPiece() {
		return this.movedPiece;
	}
		
	public Board execute() {
		//new board
		final Builder builder = new Builder();
		//itterate over the incoming boards current players active pieces
		for(final Piece piece : this.board.currentPlayer().getActivePieces()) {
			//for all the pieces that arent the moved piece
			if(!this.movedPiece.equals(piece)) {
				//place on there original place. No change.
				builder.setPiece(piece);
			}
		}
		//same thing for the enemies pieces, but the opponents pieces doesnt move.
		for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) {
			builder.setPiece(piece);
			
		}
		//move the moved piece.
		builder.setPiece(this.movedPiece.movePiece(this));
		//set the movemaker to the opponent.
		builder.setMoveMaker(this.board.currentPlayer().getOpponent().getAlliance());
		
		
		return builder.build();
	}

	public static final class MajorMove extends Move {
		public MajorMove(final Board board,
						 final Piece movedPiece,
						 final int destinationCoordinate) {
			super(board, movedPiece, destinationCoordinate);
		}
	}
	
	public static class AttackMove extends Move{
		final Piece attackedPiece;
		
		public AttackMove(final Board board,
						  final Piece movedPiece,
						  final int destinationCoordinate,
						  final Piece attackedPiece ) {
			super(board, movedPiece, destinationCoordinate);
			this.attackedPiece = attackedPiece;
		}
		@Override
		public Board execute() {
			return null;
		}
	}
		
		public static final class PawnMove extends Move {
			public PawnMove(final Board board,
							final Piece movedPiece,
							final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
			}
		}
		
		public static class PawnAttackMove extends AttackMove {
			public PawnAttackMove(final Board board,
						     	  final Piece movedPiece,
						     	  final int destinationCoordinate,
						     	  final Piece attackedPiece) {
				super(board, movedPiece, destinationCoordinate, attackedPiece);
			}
		}
		
		public static final class PawnEnPassantAttackMove extends PawnAttackMove {
			public PawnEnPassantAttackMove(final Board board,
						     	  		   final Piece movedPiece,
						     	  		   final int destinationCoordinate,
						     	  		   final Piece attackedPiece) {
				super(board, movedPiece, destinationCoordinate, attackedPiece);
			}
		}
		
		public static final class PawnJump extends Move {
			public PawnJump(final Board board,
							final Piece movedPiece,
							final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
			}
		}
		
		static abstract class CastleMove extends Move {
			public CastleMove(final Board board,
							 final Piece movedPiece,
							 final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
			}
		}
		
		public static final class KingSideCastleMove extends CastleMove{
			public KingSideCastleMove(final Board board,
									  final Piece movedPiece,
									  final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
			}
		}
		
		public static final class QueenSideCastleMove extends CastleMove{
			public QueenSideCastleMove(final Board board,
									   final Piece movedPiece,
									   final int destinationCoordinate) {
				super(board, movedPiece, destinationCoordinate);
			} 
		}
		
		public static final class NullMove extends Move{
			public NullMove() {
				super(null, null, -1);
			}
			@Override
			public Board execute() {
				throw new RuntimeException("Cannot execute the null move");
			}
			
		}
		
		public static class MoveFactory{
			private MoveFactory() {
				throw new RuntimeException("Not Instantiable");
			}
			
			//given a board and a from-to cooridnate
			//return the move that is avalible at that board.
			public static Move createMove(final Board board,
										  final int currentCoordinate,
										  final int destinationCoordinate){
				for(final Move move : board.getAllLegalMoves()) {
					if(move.getCurrentCoordinate() == currentCoordinate && 
					   move.getDestinationCoordinate() == destinationCoordinate) {
						return move;
					}
				}
				return NULL_MOVE;
			}
			
		}
	
} 