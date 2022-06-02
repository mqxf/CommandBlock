package dev.mqxf.command;

import java.nio.CharBuffer;
import java.util.Formatter;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    final static String ending = "]}";
    final static String passengerP1 = "{id:command_block_minecart,Command:'";
    final static String passengerP2 = "'},";

    static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);

        if (args.length > 0) {
            oneCommand(args[0]);
            return;
        }

        int p = menu();

        while (p != 2) {
            if (p == 1) summonBlock();
            p = menu();
        }
    }

    public static void oneCommand(String name) {
        String fullCommand = "summon falling_block ~ ~1 ~ {Time:1,BlockState:{Name:\"activator_rail\"},Passengers:[";

        System.out.println("Please input filename of file with commands:");

        for (String commandInput : readCommands(name)){
            fullCommand += passengerP1 + commandInput + passengerP2;
        }
        fullCommand += passengerP1 + "setblock ~0 ~0 ~ lava[level=6]" + passengerP2;
        fullCommand += ending;
        System.out.println(fullCommand);
    }

    public static void summonBlock() {
        String fullCommand = "summon falling_block ~%d ~%d ~%d {Time:1,BlockState:{Name:\"%s\",Properties:{facing\"%s\"}},TileEntityData:{Command:\"%s\"}}\n";
        //order: x, y, z, command_block/chain_command_block, facing, command
        int x = 0;
        int y = 2;
        int z = 0;
        String type = "command_block";
        String facing = "south";
        String command;
        System.out.println("Input the x offset you want your command block to be summoned at.");
        x = scanner.nextInt();
        System.out.println("Input the y extra offset you want your command block to be summoned at.");
        y += scanner.nextInt();
        System.out.println("Input the z offset you want your command block to be summoned at.");
        z = scanner.nextInt();
        System.out.println("If you want it to be a custom command block, type 'C' for chain, or type 'R' for repeating. Otherwise, don't type anything");
        String c = scanner.next();
        scanner.nextLine();
        type = c.equalsIgnoreCase("R") ? "repeating_command_block" : (c.equalsIgnoreCase("C") ? "chain_command_block" : "command_block");
        System.out.println("Type the direction you want the command block to be facing.");
        facing = scanner.nextLine();
        System.out.println("Finally type the command from the command block, all on one line. All the '\"' and '\\\"' will be incremented accordingly.");
        command = scanner.nextLine();
        command = command.replaceAll("\\\\", "\\\\\\\\");
        command = command.replaceAll("\"", "\\\\\"");
        System.out.println("");
        System.out.println(String.format(fullCommand, x, y, z, type, facing, command));
    }

    public static int menu() {
        System.out.println("Welcome to Java Command Block, please input an option:\n");
        System.out.println("1. Turn command into summon command block with command\n2. Exit");
        System.out.println("Use command <filename> to turn all commands in the file into one command block\n\nPlease pick an option:");
        return scanner.nextInt();
    }

    public static List<String> readCommands(String file) {
        List<String> result = new ArrayList<String>();

        try (FileReader f = new FileReader(file)) {
            StringBuffer sb = new StringBuffer();
            while (f.ready()) {
                char c = (char) f.read();
                if (c == '\n') {
                    result.add(sb.toString());
                    sb = new StringBuffer();
                } else {
                    sb.append(c);
                }
            }
            if (sb.length() > 0) {
                result.add(sb.toString());
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
