package xierz.lynx.mahjong.assistant.model;

public class CleanCalculateResult {
    private final TileKind suit;
    private final boolean success;

    public CleanCalculateResult(TileKind suit, boolean success) {
        this.suit = suit;
        this.success = success;
    }

    public TileKind getType() {
        return suit;
    }

    public boolean isSuccess() {
        return success;
    }
}
