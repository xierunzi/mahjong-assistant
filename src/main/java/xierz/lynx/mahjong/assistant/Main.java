package xierz.lynx.mahjong.assistant;

import xierz.lynx.mahjong.assistant.model.Tile;
import xierz.lynx.mahjong.assistant.utils.TilesStringUtils;
import xierz.lynx.mahjong.assistant.utils.WinningTilesUtils;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Tile> tiles = TilesStringUtils.parseTilesString("1112345678999p");
        System.out.println(WinningTilesUtils.findWinningTiles(tiles));
    }
}