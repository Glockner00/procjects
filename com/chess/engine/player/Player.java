package com.chess.engine.player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.Move;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
public abstract class Player {
	protected final Board board;
	protected final King playerKing;
	protected final Collection<Move> legalMoves;
	private boolean isInCheck;

	Player(final Board board, final Collection<Move> legalMoves, final Collection<Move> opponentMoves){
		this.board = board;
		this.playerKing = establishKing();
		this.legalMoves = legalMoves;
		//does the opponents moves the players KingPosition? --> get all those different attacks
		//is that not empty?? --> current player is in check.
		this.isInCheck = !Player.calculateAttacksOnTile(this.playerKing.getPiecePosition(),
				opponentMoves).isEmpty();
	}
	
	public King getPlayerKing() {
		return this.playerKing;
	}
	
	public Collection<Move> getLegalMoves(){
		return this.legalMoves;
	}

	//does the enemies move overlapp with the playerKings cooridinate??
	//if it does it is attacking the king.
	//returns all attacking moves.
	private static Collection<Move> calculateAttacksOnTile(final int piecePosition, 
			final Collection<Move> moves) {
		final List<Move> attackMoves = new ArrayList<>();
		for(final Move move : moves) {
			//kings piecePosition
			if(piecePosition == move.getDestinationCoordinate()) {
				attackMoves.add(move);
			}
		}
		return ImmutableList.copyOf(attackMoves);
	}

	private King establishKing() {
		for(final Piece piece: getActivePieces()) {
			if(piece.getPieceType().isKing()) {
				return(King)piece;
			}
		}
		throw new RuntimeException("Not a valid board!");
	}
	
	public boolean isMoveLegal(final Move move) {
		return this.legalMoves.contains(move);
	}

	
	public boolean isInCheck() {
		return this.isInCheck;
	}
	
	//no way of escaping ceck.
	public boolean isInCheckMate() {
		return this.isInCheck && !hasEscapeMoves();
	}
	
	//go through each of the players moves and make those moves on a new board(not existing)
	//after we make that move we look att the board --> succsesfull move --> isDone --> true
	//else --> false.
	protected boolean hasEscapeMoves() {
		for(final Move move : this.legalMoves) {
			final MoveTransition transition = makeMove(move);
			if(transition.getMoveStatus().isDone()) {
				return true;
			}
		}
		return false;
	}
	
	
	//current player is not in check and doesnt have any escapeMoves
	public boolean isInStaleMate() {
		return !this.isInCheck() && !hasEscapeMoves();
	}
	
	//TODO implement these methods.3
	public boolean isCastled() {
		return false;
	}
	public MoveTransition makeMove(final Move move) {
		
		//if the move is illeagal
		if(!isMoveLegal(move)) {
			//returns the same board
			return new MoveTransition(this.board, move, MoveStatus.ILLEGAL_MOVE);
		}
		
		//polymorphicly executes the move and returns a new board that we transition too.
		//if the current player is White the transitionBoards player is going to be Black.
		final Board transitionBoard = move.execute();
		
		//where where whites king? and get Blacks legal moves, to se if they attack that king.
		//calculate the attacks on the tile on the current players opponents players kings position on the new board. xD
		final Collection<Move> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(),
				transitionBoard.currentPlayer().getLegalMoves());
		//if there are king attacks?? Return the same board and a check, YOU CANNOT EXPOSE YOUR KING FOR ATTACKS!!
		if(!kingAttacks.isEmpty()) {
			return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
			
		}
		
		
		return new MoveTransition(transitionBoard, move, MoveStatus.DONE);
	}
	
	//resolved polymorphicly 
	public abstract Collection<Piece> getActivePieces();
	public abstract Alliance getAlliance();
	public abstract Player getOpponent();
	
}