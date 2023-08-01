package xierz.lynx.mahjong.assistant.model;

import java.util.*;

/**
 * 牌类别，包含一些元信息
 */
public enum TileKind {
    CHARACTER("万", 9, "m", Constants.SUIT_VALUE_DESC, Constants.SUIT_POSSIBLE_COUNT_IN_WIN),
    BAMBOO("条", 9, "s", Constants.SUIT_VALUE_DESC, Constants.SUIT_POSSIBLE_COUNT_IN_WIN),

    DOT("筒", 9, "p", Constants.SUIT_VALUE_DESC, Constants.SUIT_POSSIBLE_COUNT_IN_WIN),
    /**
     * 1-4
     * 东南西北
     */
    WIND("风", 4, "w", Constants.WIND_VALUE_DESC, Constants.WIND_POSSIBLE_COUNT_IN_WIN),
    /**
     * 1-3
     * 中发白
     */
    DRAGON("元", 3, "d", Constants.DRAGON_VALUE_DESC, Constants.DRAGON_POSSIBLE_COUNT_IN_WIN);

    private final String desc;
    private final int affectCount;
    private final String symbol;
    private final String[] rankValues;
    /**
     * 胡牌时可能的张数
     */
    private final Set<Integer> possibleCountInWin;

    TileKind(String desc, int affectCount, String symbol, String[] rankValues, Set<Integer> possibleCountInWin) {
        this.desc = desc;
        this.affectCount = affectCount;
        this.symbol = symbol;
        this.rankValues = rankValues;
        this.possibleCountInWin = possibleCountInWin;
    }

    public static final int SIZE = TileKind.values().length;
    private static final Map<String, TileKind> SYMBOL_MAP;

    static {
        Map<String, TileKind> temp = new HashMap<>();
        for (TileKind value : TileKind.values()) {
            temp.put(value.getSymbol(), value);
        }
        SYMBOL_MAP = Collections.unmodifiableMap(temp);
    }

    public static boolean isSuitKind(TileKind kind) {
        return kind == BAMBOO || kind == CHARACTER || kind == DOT;
    }

    public static TileKind bySymbol(String s) {
        return Optional.ofNullable(SYMBOL_MAP.get(s))
                .orElseThrow(() -> new IllegalArgumentException("非法花色标示:" + s));
    }

    public String getDesc() {
        return desc;
    }

    public int getAffectCount() {
        return affectCount;
    }

    public String getSymbol() {
        return symbol;
    }

    public String[] getRankValues() {
        return rankValues;
    }

    public Set<Integer> getPossibleCountInWin() {
        return possibleCountInWin;
    }
}
