package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessMatch;
import chess.ChessPiece;
import chess.Color;

public class King extends ChessPiece {

	private ChessMatch chessMatch;   // devido a jogada Rock (troca torre pelo rei)
									 // tem que associar o Rei a partida de xadres
	
	public King(Board board, Color color, ChessMatch chessMatch) {
		super(board, color);
		this.chessMatch = chessMatch; 
	}

	@Override
	public String toString() {
		return "K";
	}

	public boolean canMove(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		// verificar se peça a ser movida p é nula e se não pertence ao adversario
		return p == null || p.getColor() != getColor();
	}
	
	// testar se a Torre está apta para o Rock Castling
	public boolean testRookCastling (Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		// retorna true se em p tem uma peça, se é torre, se cor igual a do rei e se nunca se moveu...
		return p != null && p instanceof Rook && p.getColor() == getColor() && p.getMoveCount() == 0;
	}
	
	@Override
	public boolean[][] possibleMoves() {
		boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
		
		Position p = new Position(0, 0);
		
		// above - acima
		p.setValues(position.getRow() - 1, position.getColumn());
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// below - abaixo
		if (position.getRow() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn());
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// left - esquerda
		p.setValues(position.getRow(), position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// right - direita
		if (position.getColumn() < 7) {
			p.setValues(position.getRow(), position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// nw - noroeste
		p.setValues(position.getRow() - 1, position.getColumn() - 1);
		if (getBoard().positionExists(p) && canMove(p)) {
			mat[p.getRow()][p.getColumn()] = true;
		}
		
		// ne - nordeste
		if (position.getColumn() < 7) {
			p.setValues(position.getRow() - 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// sw - sudoeste
		if (position.getRow() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn() - 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// se - sudeste
		if (position.getRow() < 7 && position.getColumn() < 7) {
			p.setValues(position.getRow() + 1, position.getColumn() + 1);
			if (getBoard().positionExists(p) && canMove(p)) {
				mat[p.getRow()][p.getColumn()] = true;
			}
		}
		
		// special move castling
		// se rei nunca se moveu e não esta em check
		if (getMoveCount() == 0 && !chessMatch.getCheck()) {
			// testando rock pequeno - torre mais proxima do rei - specialmove castling kingside rook
			Position posT1 = new Position(position.getRow(), position.getColumn() + 3);
			if (testRookCastling(posT1)) {
				// verificar as duas posições entre o rei e torre estão vazias....
				Position p1 = new Position(position.getRow(), position.getColumn() + 1);
				Position p2 = new Position(position.getRow(), position.getColumn() + 2);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null) {
					mat[position.getRow()][position.getColumn() + 2] = true; // torna posição possível para o rei
				}
			}

			// testando rock grande - torre mais proxima da rainha - specialmove castling queenside rook
			Position posT2 = new Position(position.getRow(), position.getColumn() - 4);
			if (testRookCastling(posT2)) {
				// verificar as duas posições entre o rei e torre a esquerda estão vazias....
				Position p1 = new Position(position.getRow(), position.getColumn() - 1);
				Position p2 = new Position(position.getRow(), position.getColumn() - 2);
				Position p3 = new Position(position.getRow(), position.getColumn() - 3);
				if (getBoard().piece(p1) == null && getBoard().piece(p2) == null && getBoard().piece(p3) == null) {
					mat[position.getRow()][position.getColumn() - 2] = true;
				}
			}

		
		}
		
		return mat;
	}
	
}
