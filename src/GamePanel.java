import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

class GamePanel extends JPanel implements Runnable {
    int width = 350, height = 622;
    int fps = 30;
    int score = 0;
    int highScore = score;
    Thread gameThread = new Thread(this);
    BufferedImage backgroundImage, logoImage, restartBtnImage;
    GameState gameState = GameState.TITLE;
    Font smallFont = new Font("Arial", Font.PLAIN, 20);
    Font bigFont = new Font("Arial", Font.PLAIN, 30);
    Ground ground = new Ground(this);
    List<Pipe> pipes = new CopyOnWriteArrayList<>();
    Bird bird = new Bird(this);
    Sound sound = new Sound();

    GamePanel() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
                "SPACE_PRESSED"
        );
        getActionMap().put("SPACE_PRESSED", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Objects.equals(gameState, GameState.TITLE)) {
                    gameState = GameState.PLAY;
                } else if (Objects.equals(gameState, GameState.PLAY) && bird != null) {
                    bird.jump();
                } else if (Objects.equals(gameState, GameState.GAME_OVER)) {
                    try {
                        score = 0;
                        bird = new Bird(GamePanel.this);
                        pipes.add(new Pipe(GamePanel.this, width + 100));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    gameState = GameState.PLAY;
                }
            }
        });

        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setDoubleBuffered(true);
        setFocusable(true);
        requestFocusInWindow();

        gameThread.start();
        backgroundImage = ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/background.png")
        )));
        logoImage = ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.png")
        )));
        restartBtnImage = ImageIO.read(new BufferedInputStream(
                Objects.requireNonNull(getClass().getResourceAsStream("/images/restart-button.png")
        )));

        pipes.add(new Pipe(this, width + 100));
        sound.playBackgroundLoop();
    }

    int getXToCenterAlign(String text, Graphics2D g2d) {
        return (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth() / 2;
    }

    void update() throws IOException {
        if (Objects.equals(gameState, GameState.PLAY) || Objects.equals(gameState, GameState.GAME_OVER)) {
            if (bird != null && (bird.y + bird.image.getHeight() > ground.y || bird.y < 0)) {
                bird = null;
            }

            pipes.removeIf(pipe -> pipe.x + pipe.topPipeImage.getWidth() < 0);

            boolean addPipe = false;

            for (Pipe pipe : pipes) {
                if (bird != null && pipe.collide(bird)) {
                    bird = null;
                }

                if (!pipe.birdPassed && bird != null && bird.x > pipe.x) {
                    pipe.birdPassed = true;
                    addPipe = true;
                }

                pipe.update();
            }

            if (addPipe) {
                pipes.add(new Pipe(this, width));
                score++;
                sound.playSuccessSound();
            }

            if (bird != null) {
                bird.update();
            } else if (pipes.isEmpty()) {
                gameState = GameState.GAME_OVER;

                if (score > highScore) {
                    highScore = score;
                }
            }

            ground.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        g2d.drawImage(backgroundImage, 0, 0, null);
        g2d.setFont(smallFont);
        g2d.setColor(Color.WHITE);

        if (Objects.equals(gameState, GameState.TITLE)) {
            g2d.drawImage(logoImage, width / 2 - logoImage.getWidth() / 2, 100, null);

            String text = "Press SPACE to play";
            g2d.drawString(text, width / 2 - getXToCenterAlign(text, g2d), 250);
        } else if (Objects.equals(gameState, GameState.PLAY) || Objects.equals(gameState, GameState.GAME_OVER)) {
            for (Pipe pipe : pipes) {
                pipe.draw(g2d);
            }

            String showScore = "SCORE: " + score;
            g2d.setFont(bigFont);
            g2d.drawString(showScore, width - 90 - getXToCenterAlign(showScore, g2d), 40);

            if (bird != null) {
                bird.draw(g2d);
            }

            if (Objects.equals(gameState, GameState.GAME_OVER)) {
                g2d.setFont(smallFont);
                String highScoreText = "High Score: " + highScore;
                g2d.drawString(highScoreText, width - 80 - getXToCenterAlign(highScoreText, g2d), 70);

                String restartText = "Press SPACE to restart";
                g2d.drawString(restartText, width / 2 - getXToCenterAlign(restartText, g2d),250);

                g2d.drawImage(
                        restartBtnImage,
                        width / 2 - restartBtnImage.getWidth() / 2,
                        height / 2 - restartBtnImage.getHeight() / 2,
                        null
                );
            }
        }

        ground.draw(g2d);
        repaint();
    }

    @Override
    public void run() {
        if (!isFocusOwner()) {
            requestFocusInWindow();
        }

        double drawInterval = (double) 1000000000 / fps;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if (delta >= 1) {
                try {
                    update();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                delta--;
            }
        }
    }
}
