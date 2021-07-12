package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Knight extends ChessPiece {

	public Knight(Board board, Color color) {
		super(board, color);
	}

	@Override
	public String toString() {
		return "N";
	}

	public boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		// verificar se peça a ser movida p é nula e se não pertence ao adversario
		return p == null || p.getColor() != getColor();
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		p.setValues(position.getRow() - 1, position.getColumn() -2);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		p.setValues(position.getRow() -2, position.getColumn() -1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		if (position.getColumn() < 7) {
			p.setValues(position.getRow() -2, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}

		if (position.getColumn() < 6) {
			p.setValues(position.getRow() -1, position.getColumn() + 2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		if (position.getColumn() < 6 && position.getRow() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn() +2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		if (position.getColumn() < 7 && position.getRow() < 6) {
			p.setValues(position.getRow() +2, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		if (position.getRow() < 6) {
			p.setValues(position.getRow() + 2, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		if (position.getRow() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn() -2);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		return mat;
	}
	
}
