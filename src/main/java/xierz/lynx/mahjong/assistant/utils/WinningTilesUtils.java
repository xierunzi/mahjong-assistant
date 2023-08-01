package xierz.lynx.mahjong.assistant.utils;

import xierz.lynx.mahjong.assistant.model.Constants;
import xierz.lynx.mahjong.assistant.model.Tile;
import xierz.lynx.mahjong.assistant.model.TileKind;
import xierz.lynx.mahjong.assistant.model.WinningTiles;

import java.util.*;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public final class WinningTilesUtils {


    private WinningTilesUtils() {
    }

    public static List<WinningTiles> findWinningTiles(List<Tile> tiles) {
        tiles.sort(null);
        Map<TileKind, Integer> countByKind = tiles.stream()
                .collect(Collectors.groupingBy(Tile::getKind))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, it -> it.getValue().size()));
        // 先检查雀头个数
        int pairCount = findPairCount(countByKind);
        List<WinningTiles> winningTiles = new ArrayList<>();
        if (pairCount <= 1) {
            // 普通型或国士无双
            winningTiles.addAll(findInNormal(tiles, countByKind));
            if (winningTiles.isEmpty()) {
                winningTiles.addAll(findInThirteenOrphans(tiles));
            }
        } else if (pairCount == 2) {
            // 普通型，双碰/对倒
            winningTiles.addAll(findInNormal(tiles, countByKind));
        } else if (pairCount == 6) {
            // 七对
            findInSevenPairs(tiles).ifPresent(winningTiles::add);
        }
        return winningTiles;
    }

    private static int findPairCount(Map<TileKind, Integer> tiles) {
        int result = 0;
        for (Integer value : tiles.values()) {
            if (value == 2) {
                result++;
            }
        }
        return result;
    }

    /**
     * 计算普通牌型的待牌
     *
     * @param tiles
     * @return
     */
    private static List<WinningTiles> findInNormal(List<Tile> tiles, Map<TileKind, Integer> countByKind) {
        List<WinningTiles> result = new ArrayList<>();
        // 先通过张数判断有哪些花色可能造成胡牌，然后再穷举该花色下每一张牌
        Set<TileKind> possibleKinds = getPossibleKindInNormal(countByKind);
        if (possibleKinds.isEmpty()) {
            // 未听牌
            return Collections.emptyList();
        }
        for (TileKind kind : possibleKinds) {
            result.addAll(findInNormalByKind(kind, tiles));
        }
        return result;
    }

    /**
     * @param kind
     * @param tiles
     * @return
     */
    private static List<WinningTiles> findInNormalByKind(TileKind kind, List<Tile> tiles) {
        List<WinningTiles> winningTiles = new ArrayList<>();
        for (int i = 1; i <= kind.getAffectCount(); i++) {
            Tile tile = new Tile(kind, i);
            List<Tile> totalTiles = new ArrayList<>(tiles);
            totalTiles.add(tile);
            totalTiles.sort(null);
            // key name; value count
            Map<String, LongAdder> tileCountMap = new HashMap<>();
            for (Tile t : totalTiles) {
                tileCountMap.computeIfAbsent(t.getName(), k -> new LongAdder()).increment();
            }
            if (isWinning(totalTiles, tileCountMap, 0, false)) {
                winningTiles.add(new WinningTiles(tile));
            }
        }
        return winningTiles;
    }

    private static final int[][] SEQUENCE_DELTA = new int[][]{{-2, -1}, {-1, 1}, {1, 2}};

    private static boolean isWinning(List<Tile> tiles, Map<String, LongAdder> tileCountMap, int current,
                                     boolean hasPair) {
        if (current >= tiles.size()) {
            return true;
        }
        Tile tile = tiles.get(current);
        LongAdder currentCount = tileCountMap.get(tile.getName());
        if (currentCount.sum() == 0) {
            // 这种情况说明当前牌已被提前用掉
            return isWinning(tiles, tileCountMap, current + 1, hasPair);
        }
        // 先扣减当前牌的张数
        currentCount.decrement();
        // 如果还没有雀头，先尝试将当前牌固定雀头
        if (!hasPair && currentCount.sum() >= 1) {
            currentCount.decrement();
            if (isWinning(tiles, tileCountMap, current + 1, true)) {
                return true;
            }
            // 还原
            currentCount.increment();
        }
        // 再考虑当刻子的场景
        if (currentCount.sum() >= 2) {
            currentCount.add(-2);
            if (isWinning(tiles, tileCountMap, current + 1, hasPair)) {
                return true;
            }
            currentCount.add(2);
        }
        // 最后考虑面子的情况，只考虑非万、条、筒的花色
        if (TileKind.isSuitKind(tile.getKind())) {
            for (int[] delta : SEQUENCE_DELTA) {
                LongAdder[] adders = new LongAdder[delta.length];
                boolean invalidSequence = false;
                for (int i = 0; i < delta.length; i++) {
                    int rank = tile.getRank() + delta[i];
                    if (rank < 1 || rank > tile.getKind().getAffectCount()) {
                        invalidSequence = true;
                        break;
                    }
                    Tile temp = new Tile(tile.getKind(), tile.getRank() + delta[i]);
                    LongAdder counter = tileCountMap.get(temp.getName());
                    if (Objects.nonNull(counter) && counter.sum() >= 1) {
                        adders[i] = counter;
                    } else {
                        invalidSequence = true;
                    }
                }
                if (invalidSequence) {
                    continue;
                }
                for (LongAdder adder : adders) {
                    adder.decrement();
                }
                if (isWinning(tiles, tileCountMap, current + 1, hasPair)) {
                    return true;
                }
                for (LongAdder adder : adders) {
                    adder.increment();
                }
            }
        }
        currentCount.increment();
        return false;
    }

    /**
     * 从每个花色的张数来判断
     * 如果某一花色加了一张牌是否在张数的角度上可能和牌
     *
     * @param countByKind
     * @return
     */
    private static Set<TileKind> getPossibleKindInNormal(Map<TileKind, Integer> countByKind) {
        Set<TileKind> kinds = new HashSet<>();
        for (TileKind kind : TileKind.values()) {
            int afterAddOne = countByKind.getOrDefault(kind, 0) + 1;
            boolean allCountValid = true;
            for (Map.Entry<TileKind, Integer> entry : countByKind.entrySet()) {
                int count = entry.getValue();
                if (entry.getKey() == kind) {
                    count = afterAddOne;
                }
                if (!entry.getKey().getPossibleCountInWin().contains(count)) {
                    allCountValid = false;
                    break;
                }
            }
            if (allCountValid) {
                kinds.add(kind);
            }
        }
        return kinds;
    }

    /**
     * 计算七对待牌
     *
     * @param tiles
     * @return
     */
    private static Optional<WinningTiles> findInSevenPairs(List<Tile> tiles) {
        if (tiles.size() != Constants.SEVEN_PAIRS_READY_TO_WIN_COUNT) {
            return Optional.empty();
        }
        Set<String> existTiles = new HashSet<>();
        for (Tile tile : tiles) {
            if (!existTiles.add(tile.getName())) {
                existTiles.remove(tile.getName());
            }
        }
        if (existTiles.size() != 1) {
            throw new IllegalStateException("七对查询待牌时最后结果不为1张");
        }
        String name = new ArrayList<>(existTiles).get(0);
        return Optional.of(new WinningTiles(new Tile(name)));
    }

    /**
     * 计算国士无双的待牌
     *
     * @param tiles
     * @return
     */
    private static List<WinningTiles> findInThirteenOrphans(List<Tile> tiles) {
        if (tiles.size() != Constants.THIRTEEN_ORPHANS_READY_TO_WIN_COUNT) {
            return Collections.emptyList();
        }
        // 国士缺的牌
        Set<String> absentTiles = new HashSet<>();
        Set<String> existTiles = tiles.stream().map(Tile::getName).collect(Collectors.toSet());
        for (String name : Constants.THIRTEEN_ORPHANS_TILE_NAMES) {
            if (!existTiles.contains(name)) {
                absentTiles.add(name);
                if (absentTiles.size() > 1) {
                    return Collections.emptyList();
                }
            }
        }
        if (absentTiles.size() == 1) {
            String name = new ArrayList<>(absentTiles).get(0);
            return Collections.singletonList(new WinningTiles(new Tile(name)));
        }
        // 国士十三面
        return Constants.THIRTEEN_ORPHANS_TILE_NAMES.stream().map(Tile::new).map(WinningTiles::new).collect(Collectors.toList());
    }

}
