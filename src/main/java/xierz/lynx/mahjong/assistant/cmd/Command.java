package xierz.lynx.mahjong.assistant.cmd;

import java.util.Scanner;

public interface Command {
    String command();

    String desc();

    void execute(Scanner scanner);
}
