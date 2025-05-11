package base;

public class MenuController {


    public String greetPlayer() {
        IOSpecialist io = new IOSpecialist();
        System.out.println("Welcome To Abominodo - The Best Dominoes Puzzle Game in the Universe");
        System.out.println("Version 2.1 (c), Kevan Buckley, 2014");
        System.out.println();
        System.out.println(MultiLingualStringTable.getMessage(0));
        String playerName = io.getString();
        System.out.printf("%s %s. %s", MultiLingualStringTable.getMessage(1),
                playerName, MultiLingualStringTable.getMessage(2));
        return playerName;
    }

}
