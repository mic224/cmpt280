import java.util.Random;

/**
 * Created by michael on 12/01/17.
 */
public class A1Q1 {


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
