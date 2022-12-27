/*
 * A class that represents a single chess tile
 * Numbers that chess tile from 0-63
 * 
 * two subclasses -> emptyTile & occupiedTile
 * 
 * effective java immutability - thread safety, no thread cannot observe any effect of another thread
 * or an immutable object --> immutable objects can be shared freely --> reuse existing instances where ever possible.
 * public static final values for frequently used values.
 * public static final Complex ZERO = new Complex(0,0); 
 * public static final Complex ONE = new Complex(1,0); 
 * public static final Complex I = new Complex(0,1); 
 * 
 * ZERO, ONE and I can be used freely, u would never need to create a new Complex(x,x);
 * 
 * do not provide any way to mutate state on your class object. 
 *
 * */
package com.chess.engine.board; //has a board.
import java.util.HashMap;


import java.util.Map;
import com.chess.engine.pieces.Piece;//has a Piece
import com.google.common.collect.ImmutableMap;

public abstract class Tile {
	/**
	 * can only be used by subclasses.
	 * once this members filed is set, we are not allowed set it again.
	 * this makes it immutable. 
	 */
	protected final int tileCoordinate;
	private static final Map<Integer, EmptyTile> EMPTY_TILES_CACHE = createAllPossibleEmptyTiles();
	/**
	 * Only way to create new tile.
	 */
	public static Tile createTile(final int tileCoordinate, final Piece piece) {
		return piece!=null ? new OccupiedTile(tileCoordinate, piece) : EMPTY_TILES_CACHE.get(tileCoordinate);
	}
	/**
	 * Creates an individual tile
	 * @param tileCoordinate
	 */
	private Tile(int tileCoordinate){
		this.tileCoordinate = tileCoordinate;
	}
	/**
	 * 
	 * @return
	 */
	/**
	 * checks if tile is occupied
	 * @return boolean
	 */
	public abstract boolean isTileOccupied();
	/**
	 * returns Piece on a tile
	 * @return Piece or Null
	 */
	public abstract Piece getPiece();
	
	public int getTileCoordinate() {
		return this.tileCoordinate;
	}
	
	private static Map<Integer, EmptyTile> createAllPossibleEmptyTiles() {
		final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();
		for(int i=0; i<BoardUtils.NUM_TILES; i++) {
			emptyTileMap.put(i, new EmptyTile(i));
		}
		/*
		 * Cannot be mutated.
		 */
		return ImmutableMap.copyOf(emptyTileMap);
	}
	

	
	public static final class EmptyTile extends Tile {
		EmptyTile(final int coordinate){
				super(coordinate);
		}
		
		@Override
		public String toString() {
			return "-";
		}
			
		@Override
		public boolean isTileOccupied() {
			return false;
		}
	
		@Override
		public Piece getPiece() {
			return null;
		}
			
	}
	public static final class OccupiedTile extends Tile{
		/**
		 * class private and immutable
		 */
		private final Piece pieceOnTile;
		
		private OccupiedTile(int tileCoordinate, final Piece pieceOnTile){
			super(tileCoordinate);
			this.pieceOnTile = pieceOnTile;
		}
		
		@Override
		public String toString() {
			return getPiece().getPieceAlliance().isBlack() ? getPiece().toString().toLowerCase() :
				getPiece().toString(); 	
		}

		@Override
		public boolean isTileOccupied() {
			return true;
		}

		@Override
		public Piece getPiece() {
			return this.pieceOnTile;
		}
	}

}
