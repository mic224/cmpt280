import lib280.list.LinkedList280;
import java.util.Random;

/**
 * Created by michael on 12/01/17.
 */
public class A1Q1 {

    private static Sack todaysPlunder[];
    private static LinkedList280<Sack> sackCollection[];


    public  static  Sack[]  generatePlunder(int  howMany) {
        Random  generator = new Random();
        Sack  grain[] = new  Sack[howMany ];
        for(int i=0; i < howMany; i++) {
            grain[i] = new  Sack(
                    Grain.values ()[ generator.nextInt(Grain.values (). length)],
                    generator.nextDouble () * 100 );
        }
        return  grain;
    }

    public static LinkedList280<Sack>[] sortSacks(Sack plunder[]) {
        LinkedList280<Sack>[] listOfSacks = new LinkedList280[Grain.values().length];

        for(int i = 0; i < listOfSacks.length; i++ ) {
            listOfSacks[i] = new LinkedList280<Sack>();
        }

        if(plunder != null) {
            for (Sack aPlunder : plunder) {
                if (aPlunder != null) {
                    if (aPlunder.getType() == Grain.WHEAT) {
                        listOfSacks[Grain.WHEAT.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.BARLEY) {
                        listOfSacks[Grain.BARLEY.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.OATS) {
                        listOfSacks[Grain.OATS.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.RYE) {
                        listOfSacks[Grain.RYE.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.OTHER) {
                        listOfSacks[Grain.OTHER.ordinal()].insert(aPlunder);
                    }
                }
            }
        }
        return listOfSacks;
    }
    public static void main(String[] args) {
        sackCollection = new LinkedList280[Grain.values().length];
        todaysPlunder = generatePlunder(10);

        sackCollection = sortSacks(todaysPlunder);

    }
}
