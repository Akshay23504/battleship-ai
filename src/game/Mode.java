package game;

public enum Mode {
    HumanVsHuman ("Human vs Human"),
    HumanVsAI ("Human vs AI"),
    AIVsAI ("AI vs AI");

    public final String mode;

    Mode(String mode) {
        this.mode = mode;
    }
}
