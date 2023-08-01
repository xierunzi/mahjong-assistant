package xierz.lynx.mahjong.assistant.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import xierz.lynx.mahjong.assistant.model.Tile;
import xierz.lynx.mahjong.assistant.model.ClaimedTiles;
import xierz.lynx.mahjong.assistant.model.TileKind;

import java.util.*;
import java.util.function.Consumer;

public final class TilesStringUtils {
    private TilesStringUtils() {
    }

    public static List<ClaimedTiles> parseFixedTilesString(String fixedCardsString) {
        List<ClaimedTiles> res = new ArrayList<>();
        if (StringUtils.isNotEmpty(fixedCardsString)) {
            parseTilesString(fixedCardsString, pair -> res.add(new ClaimedTiles(pair.getKey(), pair.getValue())));
        }
        return res;
    }

    public static Map<TileKind, List<Tile>> parseHandString(String handString) {
        Map<TileKind, List<Tile>> map = new HashMap<>();
        if (StringUtils.isNotEmpty(handString)) {
            parseTilesString(handString, pair -> map.put(pair.getKey(), pair.getValue()));
        }
        return map;
    }

    public static List<Tile> parseTilesString(String tilesString) {
        List<Tile> tiles = new ArrayList<>();
        if (StringUtils.isNotEmpty(tilesString)) {
            parseTilesString(tilesString, pair -> tiles.addAll(pair.getValue()));
        }
        return tiles;
    }

    private static void parseTilesString(String tilesString, Consumer<Pair<TileKind, List<Tile>>> consumer) {
        char[] handArray = tilesString.toCharArray();
        int slow = 0;
        int fast = slow + 1;
        while (fast < handArray.length) {
            if (!isValidValue(handArray[fast])) {
                Pair<TileKind, List<Tile>> pair = parseTiles(handArray, slow, fast);
                slow = fast;
                consumer.accept(pair);
            }
            fast++;
        }
    }

    private static Pair<TileKind, List<Tile>> parseTiles(char[] chars, int start, int end) {
        List<Tile> tiles = new ArrayList<>();
        TileKind kind = TileKind.bySymbol(String.valueOf(chars[end]));
        while (start < end) {
            if (isValidValue(chars[start])) {
                tiles.add(new Tile(kind, chars[start] - '0'));
            }
            start++;
        }
        return Pair.of(kind, tiles);
    }

    private static boolean isValidValue(char c) {
        return c >= '1' && c <= '9';
    }
}
