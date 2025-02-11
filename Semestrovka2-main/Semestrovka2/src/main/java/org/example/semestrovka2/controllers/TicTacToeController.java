package org.example.semestrovka2.controllers;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;

public class TicTacToeController {
    @FXML
    private Canvas gameCanvas;

    @FXML
    private Label timerLabel;

    @FXML
    private TextArea logArea;

    private boolean xTurn = true;
    private Image xImage;
    private Image oImage;
    private int[][] board = new int[3][3]; // 0 - пусто, 1 - X, 2 - O
    private double cellSize;
    private double padding;
    private double borderPadding = 30; // Отступы от границ канваса

    private long startTime;
    private AnimationTimer timer;

    @FXML
    public void initialize() {
        try {
            xImage = new Image(getClass().getResourceAsStream("/images/x.png"));
            oImage = new Image(getClass().getResourceAsStream("/images/o.png"));
        } catch (Exception e) {
            System.err.println("Ошибка загрузки изображений: " + e.getMessage());
            System.exit(1);
        }

        // Настройка канваса и отступов
        gameCanvas.widthProperty().addListener((obs, oldVal, newVal) -> drawBoard());
        gameCanvas.heightProperty().addListener((obs, oldVal, newVal) -> drawBoard());

        drawBoard();
        gameCanvas.setOnMouseClicked(this::handleClick);

        // Инициализация таймера
        startTime = System.currentTimeMillis();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long elapsedTime = System.currentTimeMillis() - startTime;
                long remainingTime = 120000 - elapsedTime; // 2 минуты
                if (remainingTime <= 0) {
                    timer.stop();
                    timerLabel.setText("Время вышло!");
                    resetGame();
                } else {
                    long minutes = remainingTime / 60000;
                    long seconds = (remainingTime % 60000) / 1000;
                    timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
                }
            }
        };
        timer.start();
    }

    private void drawBoard() {
        // Пересчитываем размер ячеек после изменения размера канваса с учетом отступов
        cellSize = (gameCanvas.getWidth() - 2 * borderPadding) / 3;
        padding = cellSize * 0.15; // 15% от размера ячейки для отступов

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight()); // Очищаем canvas
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5);

        // Рисуем вертикальные линии с отступами
        for (int i = 1; i < 3; i++) {
            double x = borderPadding + i * cellSize;
            gc.strokeLine(x, borderPadding, x, gameCanvas.getHeight() - borderPadding);
        }

        // Рисуем горизонтальные линии с отступами
        for (int i = 1; i < 3; i++) {
            double y = borderPadding + i * cellSize;
            gc.strokeLine(borderPadding, y, gameCanvas.getWidth() - borderPadding, y);
        }

        // Отображаем текущие X и O с учетом отступов
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board[row][col] != 0) {
                    // Если клетка занята, рисуем X или O
                    Image image = (board[row][col] == 1) ? xImage : oImage;
                    gc.drawImage(image,
                            borderPadding + col * cellSize + padding,
                            borderPadding + row * cellSize + padding,
                            cellSize - 2 * padding,
                            cellSize - 2 * padding);
                }
            }
        }
    }

    private void handleClick(MouseEvent e) {
        int col = (int) ((e.getX() - borderPadding) / cellSize);
        int row = (int) ((e.getY() - borderPadding) / cellSize);

        // Проверка на допустимость клика
        if (col >= 0 && col < 3 && row >= 0 && row < 3 && board[row][col] == 0) {
            board[row][col] = xTurn ? 1 : 2;
            xTurn = !xTurn;

            drawBoard(); // Перерисовываем поле

            if (checkWinner()) {
                String winner = board[row][col] == 1 ? "Крестики" : "Нолики";
                logArea.setText(logArea.getText() + "\nПобедитель: " + winner);
                resetGame();
            } else if (isBoardFull()) {
                logArea.setText(logArea.getText() + "\nНичья!");
                resetGame();
            }
        }
    }

    private boolean checkWinner() {
        // Проверка строк, столбцов и диагоналей
        for (int i = 0; i < 3; i++) {
            if (checkLine(board[i][0], board[i][1], board[i][2])) return true;
            if (checkLine(board[0][i], board[1][i], board[2][i])) return true;
        }
        if (checkLine(board[0][0], board[1][1], board[2][2])) return true;
        return checkLine(board[0][2], board[1][1], board[2][0]);
    }

    private boolean checkLine(int a, int b, int c) {
        return a != 0 && a == b && b == c;
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    @FXML
    private void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = 0;
            }
        }
        xTurn = true;
        drawBoard(); // Перерисовываем поле
        startTime = System.currentTimeMillis(); // Сброс таймера
    }
}