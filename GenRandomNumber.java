import java.io.IOException;
import java.io.PrintWriter;

public class GenRandomNumber {
	public static void main(String[] args){
		try{
		    PrintWriter writer = new PrintWriter("large.txt", "UTF-8");
		    for(int i=0;i<100000000;i++){
		    	writer.println(getRandom());
		    }
		    writer.close();
		} catch (IOException e) {
		   System.out.println(e);
		}
	}
	public static int getRandom(){
        int n1=0;
        int n2 = 999999;
        return (int) (n2 + (Math.random() * (n1 - n2)));
	}
}
