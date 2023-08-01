package xierz.lynx.mahjong.assistant.model;

public class WinningTiles {
    private final Tile tile;

    public WinningTiles(Tile tile) {
        this.tile = tile;
    }

    public Tile getTile() {
        return tile;
    }

    @Override
    public String toString() {
        return "WinningTiles{" +
                "tile=" + tile +
                '}';
    }
}
