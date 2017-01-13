import lib280.list.LinkedList280;
import java.util.Random;

/**
 * Created by michael on 12/01/17.
 */
public class A1Q1 {

    private LinkedList280<Sack> totalSacks[] = new LinkedList280[Grain.values().length];

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

}
