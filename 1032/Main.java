import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.math.BigInteger;
import java.util.Stack;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int n = sc.nextInt();

		int numberOfThrees = n / 3;
		int numberOfTwos = 0;
		int numberOfFours = 0;
		int remainder = n % 3;
		if (remainder == 1)
		{
			numberOfFours = 1;
			numberOfThrees--;
		}
		else if (remainder == 2)
		{
			numberOfTwos = 1;
		}

		if (numberOfTwos == 1)
		{
			System.out.print(2);
			for (int i = 0; i < numberOfThrees; ++i)
			{
				System.out.print(" " + 3);
			}
		}
		else if (numberOfFours == 1)
		{
			for (int i = 0; i < numberOfThrees; ++i)
			{
				System.out.print(3 + " ");
			}
			System.out.print(4);
		}
		else
		{
			for (int i = 0; i < numberOfThrees-1; ++i)
			{
				System.out.print(3 + " ");
			}
			System.out.print(3);
		}

		System.out.println("");
	}
}
