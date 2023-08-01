package xierz.lynx.mahjong.assistant.component;


import xierz.lynx.mahjong.assistant.model.Tile;
import xierz.lynx.mahjong.assistant.model.TileKind;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Storage {
    /**
     * sub key is value
     */
    private final Map<TileKind, Map<Integer, Tile>> cache = new ConcurrentHashMap<>();

    public Storage() {
        init();
    }

    public void printRemain(boolean filterEmpty) {
//        cache.forEach((type, map) -> {
//            List<String> remainInfos = map.values().stream()
//                    .map(it -> it.remainInfo(filterEmpty))
//                    .filter(Optional::isPresent)
//                    .map(Optional::get)
//                    .collect(Collectors.toList());
//            int remainTotal = map.values().stream()
//                    .map(Card::remainCount)
//                    .reduce(Integer::sum)
//                    .orElse(0);
//            String info = String.format("%s牌总剩余%d张，剩余信息:%s", type.getDesc(), remainTotal, remainInfos);
//            System.out.println(info);
//        });
    }

    private void init() {
        cache.clear();
        for (TileKind value : TileKind.values()) {
            Map<Integer, Tile> inner = new ConcurrentHashMap<>();
            for (int i = 0; i < value.getAffectCount(); i++) {
                int val = i + 1;
                inner.put(val, new Tile(value, i + 1));
            }
            cache.put(value, inner);
        }
    }
}
