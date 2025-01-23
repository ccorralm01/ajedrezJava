package com.mycompany.ajedrez;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Hud;
import com.mycompany.ajedrez.gameComponents.Piece;
import com.mycompany.ajedrez.server.Request;
import com.mycompany.ajedrez.managers.AnimationManager;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.panels.BoardPanel;
import com.mycompany.ajedrez.panels.HudPanel;
import com.mycompany.ajedrez.server.Room;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;

public class GameController {
    private Board board;
    private BoardPanel boardPanel;
    private HudPanel hudPanel;
    private SpriteManager spriteManager;
    private boolean myTurn = false;
    private int selectedX = -1, selectedY = -1;
    private List<Point> validMoves = new ArrayList<>();
    private List<Point> killMoves = new ArrayList<>();
    private AnimationManager animationManager;
    private Cliente cliente;
    private Room room;

    public GameController(Board board, BoardPanel boardPanel, HudPanel hudPanel, SpriteManager spriteManager) {
        this.cliente = new Cliente();
        this.board = board;
        this.boardPanel = boardPanel;
        this.hudPanel = hudPanel;
        this.spriteManager = spriteManager;
        this.animationManager = new AnimationManager(hudPanel); // Inicializar AnimationManager
        initMouseListener();
    }

    private void initMouseListener() {
        boardPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!myTurn) {
                    System.out.println("No es tu turno.");
                    return;
                }

                int tileSize = 64;
                int offset = 2 * tileSize;
                int x = (e.getX() - offset) / tileSize;
                int y = (e.getY() - offset) / tileSize;

                if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                    handleClick(x, y);
                } else {
                    System.out.println("Clic fuera del tablero lógico (8x8).");
                }
            }
        });
    }

    private void handleClick(int x, int y) {
        if (selectedX == -1 && selectedY == -1) {
            selectPiece(y, x);
        } else {
            movePiece(y, x);
        }
    }

    private void selectPiece(int y, int x) {
        Piece piece = board.getPiece(y, x);
        if (piece != null && piece.getColor().equals("negro")) {
            selectedX = x;
            selectedY = y;
            validMoves = calculateValidMoves(y, x);

            List<Point> selectedMoves = new ArrayList<>();
            List<Point> killMoves = new ArrayList<>();

            for (Point move : validMoves) {
                Piece targetPiece = board.getPiece(move.y, move.x);
                if (targetPiece != null && !targetPiece.getColor().equals(piece.getColor())) {
                    killMoves.add(move);
                } else {
                    selectedMoves.add(move);
                }
            }

            // Enviar los movimientos de captura al BoardPanel
            boardPanel.setKillMoves(killMoves);
            boardPanel.setKillHudImage(spriteManager.getHudSprites(Hud.KILL));

            // Enviar los movimientos seleccionados al HudPanel
            hudPanel.setSelectedHud(new Hud("selected", Hud.SELECTED, spriteManager.getHudSprites(Hud.SELECTED)));
            hudPanel.setSelectedPosition(x, y);
            hudPanel.setValidMoves(selectedMoves);

            animationManager.startAnimation();
            hudPanel.repaint();
            boardPanel.repaint();
            System.out.println("Pieza seleccionada: " + piece.getSymbol() + " en (" + y + ", " + x + ")");
        }
    }

    private void movePiece(int y, int x) {
        if (isValidMove(y, x)) {
            Piece selectedPiece = board.getPiece(selectedY, selectedX);
            if (selectedPiece != null) {
                board.movePiece(selectedY, selectedX, y, x);
                clearSelection();
                System.out.println("Movimiento válido: " + selectedPiece.getSymbol() + " a (" + y + ", " + x + ")");

                // Crear el objeto Request
                // Request request = new Request(room.getHost(), room.getRoomName(), room.getRoomPassword(), selectedPiece, selectedY, selectedX, y, x);

                // Enviar el objeto Request al servidor
                // cliente.sendRequestToServer(request);
            }
        } else {
            System.out.println("Movimiento inválido.");
            clearSelection();
        }
    }

    private void clearSelection() {
        selectedX = -1;
        selectedY = -1;
        validMoves.clear();
        killMoves.clear(); // Limpiar movimientos de captura

        // Limpiar el HudPanel
        hudPanel.setSelectedHud(null);
        hudPanel.setValidMoves(validMoves);

        // Limpiar el BoardPanel
        boardPanel.setKillMoves(new ArrayList<>()); // Limpiar los movimientos de captura en el BoardPanel
        boardPanel.setKillHudImage(null); // Limpiar la imagen del Hud.KILL

        animationManager.stopAnimation(); // Detener la animación
        boardPanel.repaint(); // Volver a dibujar el BoardPanel
        hudPanel.repaint(); // Volver a dibujar el HudPanel
    }

    private List<Point> calculateValidMoves(int startX, int startY) {
        List<Point> moves = new ArrayList<>();
        Piece selectedPiece = board.getPiece(startX, startY);
        if (selectedPiece == null) {
            return moves; // Si no hay pieza seleccionada, no hay movimientos válidos
        }

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Verificar si el movimiento es válido
                if (selectedPiece.isValidMove(startX, startY, i, j, board)) {
                    // Verificar si el camino está libre (excepto para el caballo)
                    if (selectedPiece.getType() == Piece.KNIGHT || isPathClear(startX, startY, i, j)) {
                        // Verificar si no hay una pieza del mismo equipo en la casilla de destino
                        Piece targetPiece = board.getPiece(i, j);
                        if (targetPiece == null || !targetPiece.getColor().equals(selectedPiece.getColor())) {
                            moves.add(new Point(j, i)); // Almacenar la posición válida
                        }
                    }
                }
            }
        }
        return moves;
    }

    /**
     * Verifica si una casilla es un movimiento válido.
     */
    private boolean isValidMove(int x, int y) {
        for (Point move : validMoves) {
            if (move.x == y && move.y == x) {
                return true;
            }
        }
        return false;
    }

    private boolean isPathClear(int startX, int startY, int endX, int endY) {
        int dx = Integer.compare(endX, startX); // Dirección en X (-1, 0, 1)
        int dy = Integer.compare(endY, startY); // Dirección en Y (-1, 0, 1)

        int x = startX + dx;
        int y = startY + dy;

        // Verificar cada casilla en el camino
        while (x != endX || y != endY) {
            if (board.getPiece(x, y) != null) {
                return false; // Hay una pieza bloqueando el camino
            }
            x += dx;
            y += dy;
        }

        return true; // El camino está libre
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        if (!myTurn) {
            clearSelection();
        }
        System.out.println("Es tu turno: " + myTurn);
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}