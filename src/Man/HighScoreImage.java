package Man;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

class PlayerData {
    String name;
    int score;

    public PlayerData(String name, int score) {
        this.name = name;
        this.score = score;
    }
}

public class HighScoreImage {

    private static final String FILE_PATH = "playersNamesAndTheirScore.txt";

    // Step 1: Read data from file and parse it
    public static List<PlayerData> readPlayerDataFromFile() {
        List<PlayerData> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("Player1:") || line.startsWith("Player2:")) {
                    String[] parts = line.split(", Score: ");
                    String name = parts[0].substring(parts[0].indexOf(":") + 1).trim();
                    int score = Integer.parseInt(parts[1].trim());
                    players.add(new PlayerData(name, score));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Step 2: Get the highest score from the list
    public static PlayerData getHighScore(List<PlayerData> players) {
        return players.stream().max(Comparator.comparingInt(p -> p.score)).orElse(null);
    }

    // Step 3: Generate an image with player names and scores
    public static BufferedImage createImageWithScores(List<PlayerData> players, PlayerData highScorePlayer) {
        // Reverse the list so the last player in the file appears first
        Collections.reverse(players);

        int width = 400;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        // Set background color
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        // Set text font
        g.setFont(new Font("Arial", Font.BOLD, 16));

        // Start rendering from the top of the image
        int yPosition = 30;

        // Draw high-score player first
        if (highScorePlayer != null) {
            g.setColor(Color.RED);
            g.drawString("Name: " + highScorePlayer.name + " Score: " + highScorePlayer.score, 30, yPosition);
            yPosition += 30; // Move down for the next player
        }

        // Draw the rest of the players, skipping the high-score player
        for (PlayerData player : players) {
            if (player != highScorePlayer) {
                g.setColor(Color.WHITE);
                g.drawString("Name: " + player.name + " Score: " + player.score, 30, yPosition);
                yPosition += 30; // Move down for the next player
            }
        }

        // Dispose of the graphics object
        g.dispose();
        return image;
    }

    // Step 4: Save the image to a file
    public static void saveImageToFile(BufferedImage image, String filePath) {
        try {
            File outputfile = new File(filePath);
            ImageIO.write(image, "PNG", outputfile);
            System.out.println("Image saved as " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Read player data from file
        List<PlayerData> players = readPlayerDataFromFile();
        if (players.isEmpty()) {
            System.out.println("No data found in the file.");
            return;
        }

        // Get the player with the highest score
        PlayerData highScorePlayer = getHighScore(players);

        // Create an image with player data
        BufferedImage image = createImageWithScores(players, highScorePlayer);

        // Save the image to a file
        saveImageToFile(image, "C:\\Users\\user\\Documents\\graph1\\mainProject_304\\Assets\\high_scores.png");
    }
}
