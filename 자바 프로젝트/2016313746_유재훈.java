import java.util.Scanner;

public class practice {
	
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		
		int number = s.nextInt();
		
		System.out.print("Binary number : ");
		System.out.println(Integer.toBinaryString(number));
		System.out.print("Octal number : ");
		System.out.println(Integer.toOctalString(number));
		System.out.print("Hexadecial number : ");
		System.out.println(Integer.toHexString(number));
	}

}
