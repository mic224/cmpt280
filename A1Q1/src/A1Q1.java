// Assignment #1
//
//		Class:				CMPT 280
//		Name:				Michael Coquet
//		NSID:				mic224
//		Student #:			11164232
//		Lecture Section:	02
//		Tutorial Section:	T04

import lib280.list.LinkedList280;
import java.util.Random;

/**
 * Created by michael on 12/01/17.
 */
public class A1Q1 {

    private static Sack todaysPlunder[];
    private static LinkedList280<Sack> sackCollection[];
    private static LinkedList280<Sack> totalPlunder;


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
                    if (aPlunder.getType() == Grain.OTHER) {
                        listOfSacks[Grain.OTHER.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.RYE) {
                        listOfSacks[Grain.RYE.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.OATS) {
                        listOfSacks[Grain.OATS.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.BARLEY) {
                        listOfSacks[Grain.BARLEY.ordinal()].insert(aPlunder);
                    }
                    if (aPlunder.getType() == Grain.WHEAT) {
                        listOfSacks[Grain.WHEAT.ordinal()].insert(aPlunder);
                    }
                }
            }
        }
        return listOfSacks;
    }

    public static LinkedList280<Sack> sumTotalPlunder(LinkedList280<Sack>[] sortedSacks) {
        LinkedList280<Sack> total = new LinkedList280();

        for(int i = (Grain.values().length - 1); i >= 0; i--) {
            double sum = 0;

            if(!sortedSacks[i].isEmpty()) {
                total.insert(new Sack(sortedSacks[i].firstItem().getType(),sum));
                total.goFirst();

                sortedSacks[i].goFirst();
                sum += sortedSacks[i].item().getWeight();
                sortedSacks[i].goForth();

                while(!sortedSacks[i].after()) {
                    sum += sortedSacks[i].item().getWeight();
                    sortedSacks[i].goForth();
                }
            }
            else {
                total.insert(new Sack(Grain.values()[i],sum));
                total.goFirst();
            }
            total.item().setWeight(sum);
        }

        return total;
    }

    public static void printList(LinkedList280<Sack> list) {
        if(!list.isEmpty()) {
            list.goFirst();

            while(!list.after()) {
                System.out.println("Jack plundered " + list.item().getWeight() + " pounds of " + list.item().getType());
                list.goForth();
            }
        }
    }

    public static void main(String[] args) {
        sackCollection = new LinkedList280[Grain.values().length];
        todaysPlunder = generatePlunder(10);

        sackCollection = sortSacks(todaysPlunder);

        totalPlunder = sumTotalPlunder(sackCollection);

        printList(totalPlunder);

    }
}
