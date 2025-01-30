package com.mycompany.ajedrez;

import com.mycompany.ajedrez.gameComponents.Board;
import com.mycompany.ajedrez.gameComponents.Hud;
import com.mycompany.ajedrez.gameComponents.Piece;
import com.mycompany.ajedrez.panels.CapturesPanel;
import com.mycompany.ajedrez.managers.AnimationManager;
import com.mycompany.ajedrez.managers.SpriteManager;
import com.mycompany.ajedrez.panels.BoardPanel;
import com.mycompany.ajedrez.panels.HudPanel;
import com.mycompany.ajedrez.server.Client;
import com.mycompany.ajedrez.server.Movement;
import com.mycompany.ajedrez.server.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

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
    private CapturesPanel capturesPanel; // Referencia al CapturesPanel
    private Room room;
    private String currentUser;
    private int posicionPiezaX;
    private int posicionPiezaY;
    private int posicionMovimientoX;
    private int posicionMovimientoY;
    private Client client;

    private Movement lastMovement;

    public GameController(Board board, BoardPanel boardPanel, HudPanel hudPanel, CapturesPanel capturesPanel, SpriteManager spriteManager, Room room, String usuario) {
        this.board = board;
        this.boardPanel = boardPanel;
        this.hudPanel = hudPanel;
        this.capturesPanel = capturesPanel; // Inicializar CapturesPanel
        this.spriteManager = spriteManager;
        this.animationManager = new AnimationManager(hudPanel); // Inicializar AnimationManager
        this.room = room;
        this.currentUser = usuario;
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
            selectPiece(y, x, true);
        } else {
            movePiece(y, x, true);
        }
    }

    public void selectPiece(int y, int x, boolean isMyPiece) {
        Piece piece = board.getPiece(y, x);
        if (piece == null) return;

        String miColor = room.getPlayers().get(currentUser);

        if (isMyPiece && piece.getColor().equals(miColor)) {
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
        }

        posicionPiezaX = x;
        posicionPiezaY = y;

        System.out.println("Pieza seleccionada: " + piece.getSymbol() + " en (" + y + ", " + x + ")");
    }

    public void movePiece(int y, int x, boolean isMyPiece) {
        // Verificar que las coordenadas estén dentro del rango válido
        if (y < 0 || y > 7 || x < 0 || x > 7) {
            System.err.println("Coordenadas inválidas: y=" + y + ", x=" + x);
            return;
        }

        if (isMyPiece) {
            if (!isValidMove(y, x)) {
                System.out.println("Movimiento inválido.");
                clearSelection();
                return;
            }
        }

        Piece selectedPiece = board.getPiece(selectedY, selectedX);
        if (selectedPiece == null) return;

        // Verificar si hay una pieza en la casilla de destino
        Piece targetPiece = board.getPiece(y, x);
        if (targetPiece != null && !targetPiece.getColor().equals(selectedPiece.getColor())) {
            // Incrementar el contador de capturas
            capturesPanel.getCaptures().incrementCaptureCount(targetPiece);
            capturesPanel.repaint();

            // Si la pieza capturada es un rey, terminar el juego
            if (targetPiece.getType() == Piece.KING) {
                board.movePiece(selectedY, selectedX, y, x);
                clearSelection();
                endGame(selectedPiece.getColor());
                return;
            }
        }

        // Mover la pieza
        board.movePiece(selectedY, selectedX, y, x);
        posicionMovimientoX = x;
        posicionMovimientoY = y;
        clearSelection();

        // Si es mi movimiento, lo envío al servidor
        if (isMyPiece) {
            lastMovement = new Movement(posicionPiezaX, posicionPiezaY, posicionMovimientoX, posicionMovimientoY);
            client.setMovimientoPieza(lastMovement);
        }
    }

    private void endGame(String winningColor) {
        // Crear un JDialog para mostrar la imagen de victoria
        JDialog victoryDialog = new JDialog();
        victoryDialog.setModal(false); // Diálogo no modal
        victoryDialog.setUndecorated(true); // Eliminar la barra de título
        victoryDialog.setBackground(new Color(0, 0, 0, 0)); // Fondo transparente

        // Cargar la imagen de victoria
        Image originalImage = spriteManager.getVictoryImage(winningColor);

        // Definir el nuevo tamaño deseado (por ejemplo, el doble del tamaño original)
        int newWidth = originalImage.getWidth(null) * 15; // Aumentar el ancho al doble
        int newHeight = originalImage.getHeight(null) * 15; // Aumentar la altura al doble

        // Escalar la imagen
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Crear un ImageIcon con la imagen escalada
        ImageIcon victoryIcon = new ImageIcon(scaledImage);
        JLabel victoryLabel = new JLabel(victoryIcon);
        victoryDialog.add(victoryLabel);

        // Centrar el diálogo en la pantalla
        victoryDialog.pack();
        victoryDialog.setLocationRelativeTo(null); // Centrar en la pantalla
        victoryDialog.setVisible(true);

        // Deshabilitar el tablero
        boardPanel.setEnabled(false);
        setMyTurn(false);
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

    public boolean getMyTurn() {
        return myTurn;
    }

    public Movement getLastMovement() {
        return lastMovement;
    }

    public void setClient(Client client) {
        this.client = client;
    }
}