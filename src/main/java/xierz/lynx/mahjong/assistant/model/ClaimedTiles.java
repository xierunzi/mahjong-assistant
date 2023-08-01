package xierz.lynx.mahjong.assistant.model;

import java.util.List;

public class ClaimedTiles {
    private final TileKind suit;

    private final List<Tile> tiles;

    public ClaimedTiles(TileKind suit, List<Tile> tiles) {
        this.suit = suit;
        this.tiles = tiles;
    }

    public TileKind getType() {
        return suit;
    }

    public List<Tile> getCards() {
        return tiles;
    }
}
