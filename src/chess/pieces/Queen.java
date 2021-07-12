package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

public class Queen extends ChessPiece{

	public Queen(Board board, Color color) {
		super(board, color);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "Q";
	}

	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		//above - acima
		p.setValues(position.getRow() - 1, position.getColumn());
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setRow(p.getRow() -1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//left - esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setColumn(p.getColumn() -1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		//right - direita
		p.setValues(position.getRow(), position.getColumn());
		if (position.getColumn() < 7) {
			p.setValues(position.getRow(), position.getColumn() + 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				if (p.getColumn() >= 7) {
					break;
				} else {
					p.setColumn(p.getColumn() + 1);					
				}
			}
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		//below - abaixo
		p.setValues(position.getRow(), position.getColumn());
		if (position.getRow() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn());
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				if (p.getRow() >= 7) {
					break;
				} else {
					p.setRow(p.getRow() +1);
				}
			}
		}	
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		//NW - noroeste
		p.setValues(position.getRow() - 1, position.getColumn() -1);
		while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
			p.setValues(p.getRow() -1, p.getColumn() -1);
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		//NE - nordeste
		p.setValues(position.getRow(), position.getColumn());
		if (position.getColumn() < 7) {
			p.setValues(position.getRow() -1, position.getColumn() + 1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				if (p.getColumn() >= 7) {
					break;
				}
				p.setValues(p.getRow() -1, p.getColumn() +1);
			}
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		//SE - sudeste
		p.setValues(position.getRow(), position.getColumn());
		if (position.getRow() < 7 && position.getColumn() < 7) {
			p.setValues(position.getRow() +1, position.getColumn() +1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				if (p.getColumn() >= 7) {
					break;
				}
				p.setValues(p.getRow() +1, p.getColumn() +1);
			}
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}

		//SO - sudoeste
		p.setValues(position.getRow(), position.getColumn());
		if (position.getRow() < 7 ) {
			p.setValues(position.getRow() +1, position.getColumn() -1);
			while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
				mat[p.getRow()][p.getColumn()] = true;
				if (p.getRow() >= 7) {
					break;
				}
				p.setValues(p.getRow() +1, p.getColumn() -1);
			}
		}
		if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}		
		
		return mat;
	}

	
}
