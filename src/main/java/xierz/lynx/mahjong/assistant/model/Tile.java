package xierz.lynx.mahjong.assistant.model;

/**
 * 描述一张卡牌
 */
public class Tile implements Comparable<Tile> {
    private final TileKind kind;
    private final int rank;
    private final String desc;
    private final String name;

    public Tile(TileKind kind, int rank) {
        this.kind = kind;
        this.rank = rank;
        this.desc = generateDesc(kind, rank);
        this.name = rank + kind.getSymbol();
    }

    public Tile(String name) {
        if (name.length() != Constants.TILE_NAME_LENGTH) {
            throw new IllegalArgumentException("牌的名字长度必须为2，第一位为rank，第二位为类别");
        }
        this.rank = name.charAt(0) - '0';
        this.kind = TileKind.bySymbol(String.valueOf(name.charAt(1)));
        this.desc = generateDesc(this.kind, this.rank);
        this.name = name;
    }

    public int getRank() {
        return rank;
    }

    public String getDesc() {
        return desc;
    }

    public TileKind getKind() {
        return kind;
    }

    public String getName() {
        return name;
    }

    private static String generateDesc(TileKind kind, int value) {
        String name = kind.getRankValues()[value - 1];
        if (kind == TileKind.CHARACTER || kind == TileKind.DOT || kind == TileKind.BAMBOO) {
            name = name + kind.getDesc();
        }
        return name;
    }

    @Override
    public int compareTo(Tile o) {
        if (this.kind != o.kind) {
            return Integer.compare(this.kind.ordinal(), o.kind.ordinal());
        }
        return Integer.compare(this.rank, o.rank);
    }

    @Override
    public String toString() {
        return this.desc;
    }
}
