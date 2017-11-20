package game;

import javax.swing.*;
import java.awt.*;

public class Menu {

    public static boolean gameRunning;
    private volatile boolean readyToStart;

    private JFrame window;

    private JButton startGameButton;

    private JComboBox<Integer> numberOfBattleshipsComboBox, lengthOfBattleshipsComboBox;
    private JComboBox<Integer> numberOfCruisersComboBox, lengthOfCruisersComboBox;
    private JComboBox<Integer> numberOfDestroyersComboBox, lengthOfDestroyersComboBox;
    private JComboBox<Integer> numberOfSubmarinesComboBox, lengthOfSubmarinesComboBox;
    private JComboBox<Mode> gameModeComboBox;
    private JComboBox<Integer> boardSizeComboBox;

    private JLabel projectTitleText;
    private JLabel projectMembersText;
    private JLabel developedByText;
    private JLabel numberOfBattleshipsText, lengthOfBattleshipsText;
    private JLabel numberOfCruisersText, lengthOfCruiserText;
    private JLabel numberOfDestroyersText, lengthOfDestroyersText;
    private JLabel numberOfSubmarinesText, lengthOfSubmarinesText;
    private JLabel gameModeText;
    private JLabel boardSizeText;
    private JLabel errorMessageText;

    private final Integer[] numberOfShipValues = {1, 2, 3, 4, 5};
    private final Integer[] lengthOfShipValues = {1, 2, 3, 4, 5, 6, 7, 8};
    private final Integer[] boardSizeValues = {6, 8, 10, 12, 14, 16, 18};

    private final String fontName = "Calibri";
    private final String projectTitle = "AI Approaches to Battleship";
    private final String developedBy = "(Developed by humans)";
    private final String member1 = "Akshay Aravind";
    private final String member2 = "Arjun Koppal Manjunath";
    private final String member3 = "Dhruva Chakravarthi";
    private final String member4 = "Sneha Shankar";

    Menu(JFrame jFrame) {
        this.window = jFrame;
        this.window.setTitle(projectTitle);
        readyToStart = false;
    }

    public void setup() {
        gameRunning = true;
        setUpTitles();
        setUpErrorMessage();
        setUpButtons();
        setUpMenuWindow();
    }

    private void setUpButtons() {
        setUpStartGameButton();
        setUpBattleshipButtons();
        setUpCruiserButtons();
        setUpDestroyerButtons();
        setUpSubmarineButtons();
        setUpGameModeButton();
        setUpBoardSizeButton();
    }

    private void setUpTitles() {
        projectTitleText = new JLabel(projectTitle);
        projectTitleText.setFont(new Font(fontName, Font.BOLD + Font.ITALIC, 36));
        projectTitleText.setSize(550, 100);
        projectTitleText.setForeground(Color.WHITE);
        projectTitleText.setLocation(window.getWidth() / 2 - projectTitleText.getWidth() / 2, 1);
        projectTitleText.setVisible(true);

        developedByText = new JLabel(developedBy);
        developedByText.setFont(new Font(fontName, Font.PLAIN, 16));
        developedByText.setSize(200, 50);
        developedByText.setForeground(Color.WHITE);
        developedByText.setLocation(window.getWidth() / 2 - developedByText.getWidth() / 2, 80);
        developedByText.setVisible(true);

        projectMembersText = new JLabel(member1 + ", " + member2 + ", " + member3 + " and " + member4);
        projectMembersText.setFont(new Font(fontName, Font.PLAIN, 16));
        projectMembersText.setSize(650, 50);
        projectMembersText.setForeground(Color.WHITE);
        projectMembersText.setLocation(window.getWidth() / 2 - projectMembersText.getWidth() / 2, 100);
        projectMembersText.setVisible(true);
    }

    private void setUpStartGameButton() {
        startGameButton = new JButton("Start Game");
        startGameButton.setSize(100, 40);
        startGameButton.setLocation(window.getWidth() / 2 - startGameButton.getWidth() / 2,
                window.getHeight() / 2 - 100);
        startGameButton.setVisible(true);
        startGameButton.addActionListener(e -> {
            if (!canSelectedShipsFitOnBoard()) {
                errorMessageText.setVisible(true);
            } else {
                errorMessageText.setVisible(false);
                removeMenuWindows();
                Window.numberOfBattleships = (int) numberOfBattleshipsComboBox.getSelectedItem();
                Window.lengthOfBattleships = (int) lengthOfBattleshipsComboBox.getSelectedItem();
                Window.numberOfCruisers = (int) numberOfCruisersComboBox.getSelectedItem();
                Window.lengthOfCruisers = (int) lengthOfCruisersComboBox.getSelectedItem();
                Window.numberOfDestroyers = (int) numberOfDestroyersComboBox.getSelectedItem();
                Window.lengthOfDestroyers = (int) lengthOfDestroyersComboBox.getSelectedItem();
                Window.numberOfSubmarines = (int) numberOfSubmarinesComboBox.getSelectedItem();
                Window.lengthOfSubmarines = (int) lengthOfSubmarinesComboBox.getSelectedItem();
                Window.gameMode = (Mode) gameModeComboBox.getSelectedItem();
                Window.boardSize = (int) boardSizeComboBox.getSelectedItem();
                readyToStart = true;
            }
        });
    }

    private void setUpBattleshipButtons() {
        numberOfBattleshipsComboBox = new JComboBox<>(numberOfShipValues);
        numberOfBattleshipsComboBox.setSize(70, 50);
        numberOfBattleshipsComboBox.setLocation(
                window.getWidth() / 2 - numberOfBattleshipsComboBox.getWidth() - 10,
                window.getHeight() / 2 - 90 + startGameButton.getHeight());
        numberOfBattleshipsComboBox.setVisible(true);

        numberOfBattleshipsText = new JLabel("Number of Battleships: ");
        numberOfBattleshipsText.setFont(new Font(fontName, Font.PLAIN, 16));
        numberOfBattleshipsText.setSize(200, 50);
        numberOfBattleshipsText.setForeground(Color.WHITE);
        numberOfBattleshipsText.setLocation(
                window.getWidth() / 2 - numberOfBattleshipsComboBox.getWidth() - numberOfBattleshipsText.getWidth(),
                window.getHeight() / 2 - 93 + startGameButton.getHeight());
        numberOfBattleshipsText.setVisible(true);

        lengthOfBattleshipsText = new JLabel("Length of Battleships: ");
        lengthOfBattleshipsText.setFont(new Font(fontName, Font.PLAIN, 16));
        lengthOfBattleshipsText.setSize(200, 50);
        lengthOfBattleshipsText.setForeground(Color.WHITE);
        lengthOfBattleshipsText.setLocation(
                window.getWidth() / 2 + 20,
                window.getHeight() / 2 - 93 + startGameButton.getHeight());
        lengthOfBattleshipsText.setVisible(true);

        lengthOfBattleshipsComboBox = new JComboBox<>(lengthOfShipValues);
        lengthOfBattleshipsComboBox.setSize(70, 50);
        lengthOfBattleshipsComboBox.setLocation(
                window.getWidth() / 2 + lengthOfBattleshipsText.getWidth(),
                window.getHeight() / 2 - 90 + startGameButton.getHeight());
        lengthOfBattleshipsComboBox.setVisible(true);
    }

    private void setUpCruiserButtons() {
        numberOfCruisersComboBox = new JComboBox<>(numberOfShipValues);
        numberOfCruisersComboBox.setSize(70, 50);
        numberOfCruisersComboBox.setLocation(
                window.getWidth() / 2 - numberOfCruisersComboBox.getWidth() - 10,
                window.getHeight() / 2 - 100 + startGameButton.getHeight() + numberOfBattleshipsComboBox.getHeight());
        numberOfCruisersComboBox.setVisible(true);

        numberOfCruisersText = new JLabel("Number of Cruisers: ");
        numberOfCruisersText.setFont(new Font(fontName, Font.PLAIN, 16));
        numberOfCruisersText.setSize(200, 50);
        numberOfCruisersText.setForeground(Color.WHITE);
        numberOfCruisersText.setLocation(
                window.getWidth() / 2 - numberOfBattleshipsComboBox.getWidth() - numberOfCruisersText.getWidth(),
                window.getHeight() / 2 - 103 + startGameButton.getHeight() + numberOfBattleshipsText.getHeight());
        numberOfCruisersText.setVisible(true);

        lengthOfCruiserText = new JLabel("Length of Cruisers: ");
        lengthOfCruiserText.setFont(new Font(fontName, Font.PLAIN, 16));
        lengthOfCruiserText.setSize(200, 50);
        lengthOfCruiserText.setForeground(Color.WHITE);
        lengthOfCruiserText.setLocation(
                window.getWidth() / 2 + 20,
                window.getHeight() / 2 - 103 + startGameButton.getHeight() + lengthOfBattleshipsText.getHeight());
        lengthOfCruiserText.setVisible(true);

        lengthOfCruisersComboBox = new JComboBox<>(lengthOfShipValues);
        lengthOfCruisersComboBox.setSize(70, 50);
        lengthOfCruisersComboBox.setLocation(
                window.getWidth() / 2 + lengthOfCruiserText.getWidth(),
                window.getHeight() / 2 - 100 + startGameButton.getHeight() + lengthOfBattleshipsComboBox.getHeight());
        lengthOfCruisersComboBox.setVisible(true);
    }

    private void setUpDestroyerButtons() {
        numberOfDestroyersComboBox = new JComboBox<>(numberOfShipValues);
        numberOfDestroyersComboBox.setSize(70, 50);
        numberOfDestroyersComboBox.setLocation(
                window.getWidth() / 2 - numberOfDestroyersComboBox.getWidth() - 10,
                window.getHeight() / 2 - 110 + startGameButton.getHeight() + numberOfBattleshipsComboBox.getHeight() +
                        numberOfCruisersComboBox.getHeight());
        numberOfDestroyersComboBox.setVisible(true);

        numberOfDestroyersText = new JLabel("Number of Destroyers: ");
        numberOfDestroyersText.setFont(new Font(fontName, Font.PLAIN, 16));
        numberOfDestroyersText.setSize(200, 50);
        numberOfDestroyersText.setForeground(Color.WHITE);
        numberOfDestroyersText.setLocation(
                window.getWidth() / 2 - numberOfBattleshipsComboBox.getWidth() - numberOfDestroyersText.getWidth(),
                window.getHeight() / 2 - 113 + startGameButton.getHeight() + numberOfBattleshipsText.getHeight() +
                        numberOfCruisersText.getHeight());
        numberOfDestroyersText.setVisible(true);

        lengthOfDestroyersText = new JLabel("Length of Destroyers: ");
        lengthOfDestroyersText.setFont(new Font(fontName, Font.PLAIN, 16));
        lengthOfDestroyersText.setSize(200, 50);
        lengthOfDestroyersText.setForeground(Color.WHITE);
        lengthOfDestroyersText.setLocation(
                window.getWidth() / 2 + 20,
                window.getHeight() / 2 - 113 + startGameButton.getHeight() + lengthOfBattleshipsText.getHeight() +
                        lengthOfCruiserText.getHeight());
        lengthOfDestroyersText.setVisible(true);

        lengthOfDestroyersComboBox = new JComboBox<>(lengthOfShipValues);
        lengthOfDestroyersComboBox.setSize(70, 50);
        lengthOfDestroyersComboBox.setLocation(
                window.getWidth() / 2 + lengthOfDestroyersText.getWidth(),
                window.getHeight() / 2 - 110 + startGameButton.getHeight() + lengthOfBattleshipsComboBox.getHeight() +
                        lengthOfCruisersComboBox.getHeight());
        lengthOfDestroyersComboBox.setVisible(true);
    }

    private void setUpSubmarineButtons() {
        numberOfSubmarinesComboBox = new JComboBox<>(numberOfShipValues);
        numberOfSubmarinesComboBox.setSize(70, 50);
        numberOfSubmarinesComboBox.setLocation(
                window.getWidth() / 2 - numberOfSubmarinesComboBox.getWidth() - 10,
                window.getHeight() / 2 - 120 + startGameButton.getHeight() + numberOfBattleshipsComboBox.getHeight() +
                        numberOfCruisersComboBox.getHeight() + numberOfDestroyersComboBox.getHeight());
        numberOfSubmarinesComboBox.setVisible(true);

        numberOfSubmarinesText = new JLabel("Number of Submarines: ");
        numberOfSubmarinesText.setFont(new Font(fontName, Font.PLAIN, 16));
        numberOfSubmarinesText.setSize(200, 50);
        numberOfSubmarinesText.setForeground(Color.WHITE);
        numberOfSubmarinesText.setLocation(
                window.getWidth() / 2 - numberOfBattleshipsComboBox.getWidth() - numberOfSubmarinesText.getWidth(),
                window.getHeight() / 2 - 123 + startGameButton.getHeight() + numberOfBattleshipsText.getHeight() +
                        numberOfCruisersText.getHeight() + numberOfDestroyersText.getHeight());
        numberOfSubmarinesText.setVisible(true);

        lengthOfSubmarinesText = new JLabel("Length of Submarines: ");
        lengthOfSubmarinesText.setFont(new Font(fontName, Font.PLAIN, 16));
        lengthOfSubmarinesText.setSize(200, 50);
        lengthOfSubmarinesText.setForeground(Color.WHITE);
        lengthOfSubmarinesText.setLocation(
                window.getWidth() / 2 + 20,
                window.getHeight() / 2 - 123 + startGameButton.getHeight() + lengthOfBattleshipsText.getHeight() +
                        lengthOfCruiserText.getHeight() + lengthOfDestroyersText.getHeight());
        lengthOfSubmarinesText.setVisible(true);

        lengthOfSubmarinesComboBox = new JComboBox<>(lengthOfShipValues);
        lengthOfSubmarinesComboBox.setSize(70, 50);
        lengthOfSubmarinesComboBox.setLocation(
                window.getWidth() / 2 + lengthOfDestroyersText.getWidth(),
                window.getHeight() / 2 - 120 + startGameButton.getHeight() + lengthOfBattleshipsComboBox.getHeight() +
                        lengthOfCruisersComboBox.getHeight() + lengthOfDestroyersComboBox.getHeight());
        lengthOfSubmarinesComboBox.setVisible(true);
    }

    private void setUpGameModeButton() {
        gameModeComboBox = new JComboBox<>(Mode.values());
        gameModeComboBox.setSize(200, 50);
        gameModeComboBox.setLocation(
                window.getWidth() / 2,
                window.getHeight() / 2 - 120 + startGameButton.getHeight() + numberOfBattleshipsComboBox.getHeight() +
                        numberOfCruisersComboBox.getHeight() +
                        numberOfDestroyersComboBox.getHeight() + numberOfSubmarinesComboBox.getHeight());
        gameModeComboBox.setVisible(true);

        gameModeText = new JLabel("Game mode: ");
        gameModeText.setFont(new Font(fontName, Font.PLAIN, 16));
        gameModeText.setSize(110, 50);
        gameModeText.setForeground(Color.WHITE);
        gameModeText.setLocation(
                window.getWidth() / 2 - gameModeText.getWidth(),
                window.getHeight() / 2 - 123 + startGameButton.getHeight() + numberOfBattleshipsText.getHeight() +
                        numberOfCruisersText.getHeight() +
                        numberOfDestroyersText.getHeight() + numberOfSubmarinesText.getHeight());
        gameModeText.setVisible(true);
    }

    private void setUpBoardSizeButton() {
        boardSizeComboBox = new JComboBox<>(boardSizeValues);
        boardSizeComboBox.setSize(200, 50);
        boardSizeComboBox.setLocation(
                window.getWidth() / 2,
                window.getHeight() / 2 - 120 + startGameButton.getHeight() + numberOfBattleshipsComboBox.getHeight() +
                        numberOfCruisersComboBox.getHeight() + numberOfDestroyersComboBox.getHeight() +
                        numberOfSubmarinesComboBox.getHeight() + gameModeComboBox.getHeight());
        boardSizeComboBox.setVisible(true);

        boardSizeText = new JLabel("Board Size: ");
        boardSizeText.setFont(new Font(fontName, Font.PLAIN, 16));
        boardSizeText.setSize(100, 50);
        boardSizeText.setForeground(Color.WHITE);
        boardSizeText.setLocation(window.getWidth() / 2 - boardSizeText.getWidth(),
                window.getHeight() / 2 - 123 + startGameButton.getHeight() + numberOfBattleshipsText.getHeight() +
                        numberOfCruisersText.getHeight() + numberOfDestroyersText.getHeight() +
                        numberOfSubmarinesText.getHeight() + gameModeText.getHeight());
        boardSizeText.setVisible(true);
    }

    private void setUpErrorMessage() {
        errorMessageText = new JLabel("Board is too small to fit the selected ships");
        errorMessageText.setForeground(Color.YELLOW);
        errorMessageText.setFont(new Font(fontName, Font.PLAIN, 20));
        errorMessageText.setSize(400, 40);
        errorMessageText.setLocation(window.getWidth() / 2 - errorMessageText.getWidth() / 2,
                window.getHeight() - errorMessageText.getHeight() - 30);
        errorMessageText.setVisible(false);
    }

    private boolean canSelectedShipsFitOnBoard() {
        int totalShipSize =
                ((int) numberOfBattleshipsComboBox.getSelectedItem() *
                        (int) lengthOfBattleshipsComboBox.getSelectedItem()) +
                ((int) numberOfCruisersComboBox.getSelectedItem() *
                        (int) lengthOfCruisersComboBox.getSelectedItem()) +
                ((int) numberOfDestroyersComboBox.getSelectedItem() *
                        (int) lengthOfDestroyersComboBox.getSelectedItem()) +
                ((int) numberOfSubmarinesComboBox.getSelectedItem() *
                        (int) lengthOfSubmarinesComboBox.getSelectedItem());

        return totalShipSize <= (int) boardSizeComboBox.getSelectedItem() * (int) boardSizeComboBox.getSelectedItem();
    }

    private void setUpMenuWindow() {
        window.getContentPane().add(projectTitleText);
        window.getContentPane().add(developedByText);
        window.getContentPane().add(projectMembersText);

        window.getContentPane().add(errorMessageText);

        window.getContentPane().add(startGameButton);

        window.getContentPane().add(numberOfBattleshipsText);
        window.getContentPane().add(numberOfBattleshipsComboBox);
        window.getContentPane().add(lengthOfBattleshipsText);
        window.getContentPane().add(lengthOfBattleshipsComboBox);

        window.getContentPane().add(numberOfCruisersText);
        window.getContentPane().add(numberOfCruisersComboBox);
        window.getContentPane().add(lengthOfCruiserText);
        window.getContentPane().add(lengthOfCruisersComboBox);

        window.getContentPane().add(numberOfDestroyersText);
        window.getContentPane().add(numberOfDestroyersComboBox);
        window.getContentPane().add(lengthOfDestroyersText);
        window.getContentPane().add(lengthOfDestroyersComboBox);

        window.getContentPane().add(numberOfSubmarinesText);
        window.getContentPane().add(numberOfSubmarinesComboBox);
        window.getContentPane().add(lengthOfSubmarinesText);
        window.getContentPane().add(lengthOfSubmarinesComboBox);

        window.getContentPane().add(gameModeComboBox);
        window.getContentPane().add(gameModeText);

        window.getContentPane().add(boardSizeComboBox);
        window.getContentPane().add(boardSizeText);

        window.getContentPane().setBackground(Color.BLACK);
        window.setVisible(true);
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }

    private void removeMenuWindows() {
        window.getContentPane().remove(projectTitleText);
        window.getContentPane().remove(developedByText);
        window.getContentPane().remove(projectMembersText);

        window.getContentPane().remove(errorMessageText);

        window.getContentPane().remove(startGameButton);

        window.getContentPane().remove(numberOfBattleshipsText);
        window.getContentPane().remove(numberOfBattleshipsComboBox);
        window.getContentPane().remove(lengthOfBattleshipsText);
        window.getContentPane().remove(lengthOfBattleshipsComboBox);

        window.getContentPane().remove(numberOfCruisersText);
        window.getContentPane().remove(numberOfCruisersComboBox);
        window.getContentPane().remove(lengthOfCruiserText);
        window.getContentPane().remove(lengthOfCruisersComboBox);

        window.getContentPane().remove(numberOfDestroyersText);
        window.getContentPane().remove(numberOfDestroyersComboBox);
        window.getContentPane().remove(lengthOfDestroyersText);
        window.getContentPane().remove(lengthOfDestroyersComboBox);

        window.getContentPane().remove(numberOfSubmarinesText);
        window.getContentPane().remove(numberOfSubmarinesComboBox);
        window.getContentPane().remove(lengthOfSubmarinesText);
        window.getContentPane().remove(lengthOfSubmarinesComboBox);

        window.getContentPane().remove(gameModeComboBox);
        window.getContentPane().remove(gameModeText);

        window.getContentPane().remove(boardSizeComboBox);
        window.getContentPane().remove(boardSizeText);

        window.getContentPane().setBackground(Color.BLACK);
        window.getContentPane().revalidate();
        window.getContentPane().repaint();
    }

    public boolean isReadyToStart() {
        return readyToStart;
    }
}
