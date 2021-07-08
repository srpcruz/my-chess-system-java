package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Pawn extends ChessPiece {

	// peão 
	public Pawn(Board board, Color color) {
		super(board, color);
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		// peão branco subtrai na matriz (0 a 7) para "subir" no tabuleiro
		if (getColor() == Color.WHITE) {
			p.setValues(position.getRow() -1 , position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			// peão pode mover duas casas na primeira jogada
			p.setValues(position.getRow() -2 , position.getColumn());
			Position p2 = new Position(position.getRow() -1 , position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			// nas diagonais - uma casa direita e esquerda mesmo que tenha uma oponente - captura
			p.setValues(position.getRow() -1 , position.getColumn() -1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			if (position.getColumn() +1 < 8) {
				p.setValues(position.getRow() -1 , position.getColumn() +1);
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;				
				}
			}
		} else {   // peao preto + adiciona
			p.setValues(position.getRow() +1 , position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			// peão pode mover duas casas na primeira jogada
			p.setValues(position.getRow() +2 , position.getColumn());
			Position p2 = new Position(position.getRow() +1 , position.getColumn());
			if (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && getMoveCount() == 0) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			// nas diagonais - uma casa direita e esquerda mesmo que tenha uma oponente - captura
			p.setValues(position.getRow() +1 , position.getColumn() -1);
			if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;				
			}
			if (position.getColumn() +1 < 8) {
				p.setValues(position.getRow() +1 , position.getColumn() +1);
				if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
					mat[p.getRow()][p.getColumn()] = true;				
				}			
			}
		}
		
		return mat;
	}
	
	@Override
	public String toString() {
		return "P";
	}

}
