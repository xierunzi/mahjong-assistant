package xierz.lynx.mahjong.assistant.component;

import xierz.lynx.mahjong.assistant.model.Tile;
import xierz.lynx.mahjong.assistant.model.ClaimedTiles;
import xierz.lynx.mahjong.assistant.model.TileKind;
import xierz.lynx.mahjong.assistant.utils.TilesStringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class Hand {
    private final List<Tile> characters = new ArrayList<>();
    private final List<Tile> bamboos = new ArrayList<>();
    private final List<Tile> circles = new ArrayList<>();
    private final List<Tile> letters = new ArrayList<>();
    private final List<ClaimedTiles> claimedCards = new ArrayList<>();

    public Hand(Map<TileKind, List<Tile>> hand, List<ClaimedTiles> claimedCards) {
        hand.forEach((type, cards) -> {
            List<Tile> list = selectList(type);
            list.addAll(cards);
            list.sort(null);
        });
        this.claimedCards.addAll(claimedCards);
    }

    public Hand(String handString, String fixedString) {
        this(TilesStringUtils.parseHandString(handString), TilesStringUtils.parseFixedTilesString(fixedString));
    }

    public void print() {
        System.out.println("手牌信息:");
        System.out.println("万:" + printInfo(TileKind.CHARACTER, characters));
        System.out.println("条:" + printInfo(TileKind.BAMBOO, bamboos));
        System.out.println("筒:" + printInfo(TileKind.DOT, circles));
        System.out.println("字:" + printInfo(TileKind.WIND, letters));
        if (claimedCards.size() > 0) {
            List<String> infos = claimedCards.stream()
                    .map(it -> printInfo(it.getType(), it.getCards()))
                    .collect(Collectors.toList());
            System.out.println("鸣牌:" + infos);
        }
    }

    private String printInfo(TileKind suit, List<Tile> tiles) {
        StringBuilder builder = new StringBuilder();
        if (suit != TileKind.WIND) {
            tiles.forEach(it -> builder.append(it.getRank()));
            builder.append(suit.getDesc());
        } else {
            tiles.forEach(it -> builder.append(it.getDesc()));
        }
        return builder.toString();
    }

    private List<Tile> selectList(TileKind suit) {
        if (suit == TileKind.CHARACTER) {
            return this.characters;
        } else if (suit == TileKind.DOT) {
            return this.circles;
        } else if (suit == TileKind.WIND) {
            return this.letters;
        } else {
            return bamboos;
        }
    }
}
