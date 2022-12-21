package com.chess.engine.pieces;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import com.chess.engine.Alliance;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.board.Move;
import com.chess.engine.board.Move.MajorMove;
import com.google.common.collect.ImmutableList;
/**
 * Capture directionality? -> -8 || 8
 */
public class Pawn extends Piece {
	private final static int[] CANDIDATE_MOVE_COORDINATE = { 7, 8, 9, 16 };
	public Pawn(final Alliance pieceAlliance, final int piecePosition) {
		super(PieceType.PAWN, piecePosition, pieceAlliance);
	}
	@Override
	public Collection<Move> calculateLegalMoves(final Board board) {
		final List<Move> legalMoves = new ArrayList<>();
		for(final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATE) {
			//apply the offset to the current position
			//for white -> -8 : for black -> 8
			final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceAlliance().getDirection() * currentCandidateOffset);
			//is not valid coordinate
			if(!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)) {
				continue;
			}
			//offset == 8 and the tile is not occupied, REGULAR MOVE
			if(currentCandidateOffset==8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
				//TODO: implement a pawnMove, deal with promotions.
				legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));
				//first move? jump two steps forward if tiles are not occupied.
			}else if(currentCandidateOffset== 16 && this.isFirstMove() && 
					(BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isBlack()) || 
					((BoardUtils.SECOND_ROW[this.piecePosition] && this.getPieceAlliance().isWhite()))) {
				final int behindCandidateDestinationCoordiante = this.piecePosition + (this.pieceAlliance.getDirection() * 8);
				//is the square behind the destinationsTile occupied?
				if(!board.getTile(behindCandidateDestinationCoordiante).isTileOccupied() && 
				   !board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					//
					legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));	
				}
			//attacking move, Stepping 7 for white or black does not work in the first or eighth Column.
			}else if(currentCandidateOffset == 7 &&
					!((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite() || 
				    (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack())))) {
				//if tile is occupied
				if(board.getTile(candidateDestinationCoordinate).isTileOccupied()) {
					//get that piece
					final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
					//if they are enemies
					if(this.pieceAlliance!=pieceOnCandidate.getPieceAlliance()) {
						//TODO: More to do. Attacking 
						legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));	
					}
				}
			//attacking move, stepping 9 for black or white in these columns does not work.
			}else if(currentCandidateOffset == 9 && 
					!((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.getPieceAlliance().isWhite() || 
			         (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.getPieceAlliance().isBlack())))) {
					 //TODO: More to do. Attacking 
					 legalMoves.add(new MajorMove(board, this, candidateDestinationCoordinate));	
			}
		}
		return ImmutableList.copyOf(legalMoves);
	}
	@Override
	public String toString() {
		return pieceType.PAWN.toString();
	}
}