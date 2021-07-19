package chess;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Queen;
import chess.pieces.Rook;

public class ChessMatch {

	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	private ChessPiece enPassantVulnerable;
	private ChessPiece promoted;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8,8);
		turn = 1;
		currentPlayer = Color.WHITE;
		check = false;
		initialSetup();
	}
	
	public int getTurn() {
		return this.turn;
	}
	
	public Color getCurrentPlayer() {
		return this.currentPlayer;
	}
	
	public boolean getCheck() {
		return this.check;
	}
	
	public boolean getCheckMate() {
		return this.checkMate;
	}
	
	public ChessPiece getEnPassantVulnerable() {
		return this.enPassantVulnerable;
	}
	
	public ChessPiece getPromoted() {
		return this.promoted;
	}
	
	public ChessPiece[][] getPieces() {
		// criando matriz ChessPiece com a quantidade de linhas e colunad definida no ChessMatch
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		
		// percorrer a matriz para cada item visando Downcast
		// fazendo Downcast de Board(tabuleiro) para ChessPiece que é a partida em si 
		// (tabuleiro e peças do jogo)
		for (int i=0;i<board.getRows();i++) {
			for (int j=0;j<board.getColumns();j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		
		return mat;
	}
	
	public boolean[][] possibleMoves(ChessPosition sourcePosition) {
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();	
	}
	
	public ChessPiece performChessMove (ChessPosition sourcePosition, ChessPosition targetPosition) {
		// converte ChessPosition para Position 
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		validateSourcePosition(source);
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		// verifica se o movimento feito pelo jogador atual o colocou ainda em check
		// se sim, desfaz o movimento e devolve mensagem avisando do ocorrido
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException("You can't put yourself in check");
		}
		
		ChessPiece movedPiece = (ChessPiece)board.piece(target);
		
		// #specialmove promotion - troca do peão quando chega na linha final por uma peça
		promoted = null;
		if (movedPiece instanceof Pawn) {
			if ((movedPiece.getColor() == Color.WHITE && target.getRow() == 0) || (movedPiece.getColor() == Color.BLACK && target.getRow() == 7)) {
				promoted = (ChessPiece)board.piece(target);
				promoted = replacePromotedPiece("Q");  // macete setando a Rainha para depois perguntar ao jogador
			}
		}
		
		
		// testa se o movimento colocou o oponente em check e seta a partida com o estando em cheque
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		// testando se oponente está em chequemate após movimento da peça
		if (testCheckMate(opponent(currentPlayer))) {
			checkMate = true;
		} else {
			nextTurn();
		}
		
		// specialmove En passant - testando se peão se moveu duas casas (primeiro movimento)
		if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() -2 || target.getRow() == source.getRow() + 2 )) {
			enPassantVulnerable = movedPiece;
		} else {
			enPassantVulnerable = null;
		}
		
		return (ChessPiece) capturedPiece;  // downcast de ChessPiece para capturedPiece
	}
	
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position)) {
			throw new ChessException("There is no piece on source position");
		}
		
		// verificando se a peça escolhida é do outro jogador, se for está errado
		// aqui é feito downcast do getColor é propriedade do ChessPiece enquanto
		// o board.piece é da classe mais generica (piece).
		if (currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			throw new ChessException("The chose piece is not yours");
		}
		
		if (!board.piece(position).isThereAnyPossibleMove()) {
			throw new ChessException("There is no possible moves for the chose piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if (!board.piece(source).possibleMove(target)) {
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	public ChessPiece replacePromotedPiece(String type) {
		if (promoted == null) {
			throw new IllegalStateException("There is no piece to be promoted");
		}
		if (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
			throw new InvalidParameterException("Invalid type for promotion");
		}
		Position pos = promoted.getChessPosition().toPosition();
		Piece p = board.RemovePiece(pos);
		piecesOnTheBoard.remove(p);
		
		ChessPiece newPiece = newPiece(type, promoted.getColor());
		board.PlacePiece(newPiece, pos);
		piecesOnTheBoard.add(newPiece);
		
		return newPiece;
	}
	
	private ChessPiece newPiece(String type, Color color) {
		if (type.equals("B")) return new Bishop(board,color);
		if (type.equals("N")) return new Knight(board,color);
		if (type.equals("Q")) return new Queen(board,color);
		return new Rook(board,color);
	}
	
	private Piece makeMove(Position source, Position target) {
		ChessPiece p = (ChessPiece)board.RemovePiece(source);
		p.increaseMoveCount();
		Piece capturedPiece = board.RemovePiece(target);
		board.PlacePiece(p, target);
		
		if (capturedPiece !=null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		
		// specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			// pegar posição atual da torre (nunca se mexeu e esta 3 casas à direita do rei) 
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			// movendo a torre no castling
			ChessPiece rook = (ChessPiece)board.RemovePiece(sourceT);
			board.PlacePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		// specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			// pegar posição atual da torre (nunca se mexeu e esta 3 casas à direita do rei) 
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			// movendo a torre no castling
			ChessPiece rook = (ChessPiece)board.RemovePiece(sourceT);
			board.PlacePiece(rook, targetT);
			rook.increaseMoveCount();
		}
		
		// especialmove en passant - peao anda na diagonal onde está vazia e captura a peça que estava ao lado na posição de origem e abaixo da nova posicao
		if (p instanceof Pawn) {
			if (source.getColumn() != target.getColumn() && capturedPiece == null) {
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(target.getRow() + 1, target.getColumn());
				} else {   // black color
					pawnPosition = new Position(target.getRow() - 1, target.getColumn());
				}
				capturedPiece = board.RemovePiece(pawnPosition);
				capturedPieces.add(capturedPiece);
				piecesOnTheBoard.remove(capturedPiece);
			}
		}
		
		return capturedPiece;
	}
	
	// metodo para desfazer o movimento makeMove
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		ChessPiece p = (ChessPiece)board.RemovePiece(target);
		p.decreaseMoveCount();
		board.PlacePiece(p, source);
		
		if (capturedPiece != null) {
			board.PlacePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
		
		// specialmove castling kingside rook
		if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
			// pegar posição atual da torre (nunca se mexeu e esta 3 casas à direita do rei) 
			Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
			Position targetT = new Position(source.getRow(), source.getColumn() + 1);
			// movendo a torre no castling
			ChessPiece rook = (ChessPiece)board.RemovePiece(targetT);
			board.PlacePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// specialmove castling queenside rook
		if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
			// pegar posição atual da torre (nunca se mexeu e esta 3 casas à direita do rei) 
			Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
			Position targetT = new Position(source.getRow(), source.getColumn() - 1);
			// movendo a torre no castling
			ChessPiece rook = (ChessPiece)board.RemovePiece(targetT);
			board.PlacePiece(rook, sourceT);
			rook.decreaseMoveCount();
		}
		
		// especialmove en passant - peao anda na diagonal onde está vazia e captura a peça que estava ao lado na posição de origem e abaixo da nova posicao
		if (p instanceof Pawn) {
			if (source.getColumn() == target.getColumn() && capturedPiece == enPassantVulnerable) {
				ChessPiece pawn = (ChessPiece)board.RemovePiece(target);
				Position pawnPosition;
				if (p.getColor() == Color.WHITE) {
					pawnPosition = new Position(3, target.getColumn());
				} else {   // black color
					pawnPosition = new Position(4, target.getColumn());
				}
				board.PlacePiece(pawn, pawnPosition);
			}
		}

	}
	
	private void nextTurn() {
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE);
	}
	
	private Color opponent(Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	
	// testando se o Rei (king) tem alguma peça do adversario (outra cor)
	// cujo movimento possível poder remover o rei
	private boolean testCheck(Color color) {    // recebe a cor do rei sendo ameaçado
		Position kingPosition = king(color).getChessPosition().toPosition();
		// lista de peças no tabuleiro do oponente (cor contraria ao do rei)
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent(color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean[][] mat = p.possibleMoves();  // traz as posicoes possiveis de movimentacao da peça p do oponente do REI (king)
			// verifica na matriz se algum dos movimentos da peça do oponente coincide com a posição do REI
			// lembrando que possbileMoves é uma matriz booleana com true na posição possível da peça p
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {   // se é true
				return true;
			}
		}
		return false;
	}
	
	private boolean testCheckMate(Color color) {
	
		if (!testCheck(color)) {
			return false;
		}
		
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for (Piece p : list) {
			boolean[][] mat = p.possibleMoves();
			for (int i=0;i<board.getRows();i++) {    // percorrendo linhas da matriz
				for (int j=0;j<board.getColumns();j++) {    // percorrendo colunas da matriz
					if (mat[i][j]) {
						// downcast para ChessPiece e converter posição xadres (a1..h8) para posição de array (0-7)
						Position source = ((ChessPiece)p).getChessPosition().toPosition();
						// para checar o xequemate será movido o REI para esta posição mat[i][j] e fazer novo teste
						Position target = new Position(i, j);
						// simulando o movimento do REI...
						Piece capturedPiece = makeMove(source, target);
						// testando se estará em cheque com o movimento simulado
						boolean testCheck = testCheck(color);
						undoMove(source, target, capturedPiece);
						if (!testCheck) {
							return false;
						}
					}
				}
			}
		}
		return true;   // se passar aqui não ha posicao para o REI sem sair do checkmate
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.PlacePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
	}
	
	private void initialSetup() {
		
		/*   para testes
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
        placeNewPiece('c', 2, new Rook(board, Color.WHITE));
        placeNewPiece('d', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 2, new Rook(board, Color.WHITE));
        placeNewPiece('e', 1, new Rook(board, Color.WHITE));
        placeNewPiece('d', 1, new King(board, Color.WHITE));
		placeNewPiece('h', 1, new Rook(board, Color.WHITE));

        placeNewPiece('c', 7, new Rook(board, Color.BLACK));
        placeNewPiece('c', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 7, new Rook(board, Color.BLACK));
        placeNewPiece('e', 8, new Rook(board, Color.BLACK));
        placeNewPiece('d', 8, new King(board, Color.BLACK));
        */
		// peças para xequemate
	/*	placeNewPiece('h', 7, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new King(board, Color.WHITE));
		
		placeNewPiece('b', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 8, new King(board, Color.BLACK));
	*/
		placeNewPiece('a', 1, new Rook(board, Color.WHITE));
		placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this));
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
		placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));
		placeNewPiece('a', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('b', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('c', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('d', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('e', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('f', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('g', 2, new Pawn(board, Color.WHITE, this));
		placeNewPiece('h', 2, new Pawn(board, Color.WHITE, this));
		
		placeNewPiece('a', 8, new Rook(board, Color.BLACK));
		placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this));
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
		placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));
		placeNewPiece('a', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('b', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('c', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('d', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('e', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('f', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('g', 7, new Pawn(board, Color.BLACK, this));
		placeNewPiece('h', 7, new Pawn(board, Color.BLACK, this));

	}
}
