package base;
/**
 * @author Kevan Buckley, maintained by __student
 * @version 2.0, 2014
 */
public class MultiLingualStringTable {
    private enum LanguageSetting {English, Klingon}
    private static LanguageSetting cl = LanguageSetting.English;
    private static String [] em = {"Enter your name:", "Welcome", "Have a good time playing Abominodo"};
    private static String [] km = {"'el lIj pong:", "nuqneH", "QaQ poH Abominodo"};
    public static String getMessage(int index){
        if(cl == LanguageSetting.English ){
            return em[index];
        } else {
            return km[index];
        }
    }
}