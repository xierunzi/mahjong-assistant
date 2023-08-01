package xierz.lynx.mahjong.assistant.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Constants {
    public static final String[] SUIT_VALUE_DESC = new String[]{"一", "二", "三", "四", "五", "六", "七", "八", "九"};
    public static final String[] WIND_VALUE_DESC = new String[]{"东", "南", "西", "北"};
    public static final String[] DRAGON_VALUE_DESC = new String[]{"中", "发", "白"};

    /**
     * 万条筒花色在胡牌时可能的张数
     */
    public static final Set<Integer> SUIT_POSSIBLE_COUNT_IN_WIN = new HashSet<>(Arrays.asList(0, 2, 3, 5, 6, 8, 9, 11, 12, 14));
    /**
     * 风向在胡牌时可能的张数
     */
    public static final Set<Integer> WIND_POSSIBLE_COUNT_IN_WIN = new HashSet<>(Arrays.asList(0, 2, 3, 5, 6, 8, 9, 11, 12));
    /**
     * 三元牌在胡牌时可能的张数
     */
    public static final Set<Integer> DRAGON_POSSIBLE_COUNT_IN_WIN = new HashSet<>(Arrays.asList(0, 2, 3, 5, 6, 8, 9));
    public static final int TILE_NAME_LENGTH = 2;

    /**
     * 国士需要的牌
     */
    public static final List<String> THIRTEEN_ORPHANS_TILE_NAMES = Arrays.asList("1m", "9m", "1p", "9p", "1s", "9s", "1w", "2w", "3w",
            "4w", "1d", "2d", "3d");

    public static final int THIRTEEN_ORPHANS_READY_TO_WIN_COUNT = 13;
    public static final int SEVEN_PAIRS_READY_TO_WIN_COUNT = 13;

    private Constants() {
    }
}
