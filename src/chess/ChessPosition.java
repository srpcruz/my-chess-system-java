package chess;

import boardgame.Position;

public class ChessPosition {
	private char column;
	private int row;

	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'g' || row < 1 || row > 8) {
			throw new ChessException("Error instantiating chess position. Valid values are from a1 to h8");
		}
		this.column = column;
		this.row = row;
	}

	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}
	
	protected Position toPosition() {
		// no calculo de char temos:
		// 'a' - 'a' = 0
		// 'b' - 'a' = 1
		// 'c' - 'a' = 2 ....
		return new Position(8 - row, column - 'a');
	}
	
	protected static ChessPosition fromPosition(Position position) {
		
		// no xadres primeiro a coluna depois a linha, ou seja, esta sendo invertido aqui...
		return new ChessPosition((char)('a' - position.getColumn()), (8 - position.getRow()));
	}

	@Override
	public String toString() {
		return "" + column + row ;  // colocado espaco para entender concatenação de strings
	}
	
	
}
