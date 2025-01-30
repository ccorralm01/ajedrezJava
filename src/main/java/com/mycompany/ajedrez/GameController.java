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

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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
    private boolean winMove = false;
    private Movement lastMovement;
    private String miColor;
    private boolean gameOver = false;

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
        if (gameOver) return; // Evitar interacción si el juego terminó
        if (selectedX == -1 && selectedY == -1) {
            selectPiece(y, x);
        } else {
            movePiece(y, x, true);
        }
    }

    public void selectPiece(int y, int x) {
        Piece piece = board.getPiece(y, x);
        miColor = room.getPlayers().get(currentUser);
        if (piece != null && piece.getColor().equals(miColor)) {
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
            posicionPiezaX = x;
            posicionPiezaY = y;
            System.out.println("Pieza seleccionada: " + piece.getSymbol() + " en (" + y + ", " + x + ")");
        }
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

        System.out.println("Coordenada y=" + y + ", x=" + x);
        posicionMovimientoX = x;
        posicionMovimientoY = y;

        Piece targetPiece = board.getPiece(y, x);
        if (targetPiece != null && !targetPiece.getColor().equals(selectedPiece.getColor())) {
            // Incrementar el contador de capturas
            capturesPanel.getCaptures().incrementCaptureCount(targetPiece);
            capturesPanel.repaint();

            // Si la pieza capturada es un rey, marcar la victoria
            if (targetPiece.getType() == Piece.KING) {
                winMove = true; // Se ha capturado al rey
                System.out.println("¡El rey ha sido capturado! Fin del juego.");
                // Mover la pieza antes de finalizar el juego
                board.movePiece(selectedY, selectedX, y, x);
                clearSelection();
                endGame(selectedPiece.getColor());

                // Enviar el movimiento al servidor indicando victoria
                if (isMyPiece) {
                    lastMovement = new Movement(posicionPiezaX, posicionPiezaY, posicionMovimientoX, posicionMovimientoY);
                    lastMovement.setWinMove(true); // Suponiendo que Movement tenga este atributo
                    client.setMovimientoPieza(lastMovement);
                }
                return;
            }
        }

        // Mover la pieza
        board.movePiece(selectedY, selectedX, y, x);
        clearSelection();

        // Si es mi movimiento, lo envío al servidor
        if (isMyPiece) {
            lastMovement = new Movement(posicionPiezaX, posicionPiezaY, posicionMovimientoX, posicionMovimientoY);
            lastMovement.setWinMove(winMove); // Enviar estado de victoria al servidor
            client.setMovimientoPieza(lastMovement);
        }
    }


    public void endGame(String capturedKingColor) {
        gameOver = true; // Marcar el juego como finalizado
        boardPanel.setEnabled(false); // Deshabilitar la interacción del tablero
        setMyTurn(false);

        // Mostrar mensaje de victoria o derrota
        boolean isVictory = !miColor.equals(capturedKingColor);
        Image originalImage = spriteManager.getVictoryImage(isVictory ? "lose" : "win");

        // Escalar la imagen
        int newWidth = originalImage.getWidth(null) * 15;
        int newHeight = originalImage.getHeight(null) * 15;
        Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

        // Crear JLabel para mostrar la imagen de resultado
        JLabel resultLabel = new JLabel(new ImageIcon(scaledImage));
        resultLabel.setVisible(true);

        // Obtener JLayeredPane y posicionar la imagen
        JLayeredPane layeredPane = (JLayeredPane) boardPanel.getParent();
        int imageX = (layeredPane.getWidth() - newWidth) / 2;
        int imageY = (layeredPane.getHeight() - newHeight) / 2;
        resultLabel.setBounds(imageX, imageY, newWidth, newHeight);

        // Añadir al panel y actualizar
        layeredPane.add(resultLabel, JLayeredPane.POPUP_LAYER);
        layeredPane.revalidate();
        layeredPane.repaint();
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

    public void playSound(String soundFile) {
        try {
            File soundPath = new File(soundFile);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
            } else {
                System.err.println("El archivo de sonido no existe: " + soundFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void setSelectedX(int selectedX) {
        this.selectedX = selectedX;
    }

    public void setSelectedY(int selectedY) {
        this.selectedY = selectedY;
    }
}