import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		while (true)
		{
			String digits = sc.next();
			if ("-1".equals(digits))
				break;

			Vector<String> loop = new Vector<String>();
			testSelfInventorying(digits, loop, digits, 0);
		}
	}

	public static void testSelfInventorying(String originalDigits, Vector<String> loop, String digits, int step)
	{
		if (step >= 15)
		{
			System.out.println(originalDigits + " can not be classified after 15 iterations");
			return;
		}

		int[] frequencyTable = new int[10];
		for (int i = 0; i < digits.length(); ++i)
		{
			int digit = digits.charAt(i) - '0';
			frequencyTable[digit]++;
		}

		String inventory = new String();
		for (int i = 0; i < frequencyTable.length; ++i)
		{
			if (frequencyTable[i] != 0)
			{
				inventory += frequencyTable[i];
				inventory += i;
			}
		}

		if (digits.equals(inventory))
		{
			if (step == 0)
			{
				System.out.println(originalDigits + " is self-inventorying");
			}
			else
			{
				System.out.println(originalDigits + " is self-inventorying after " + step + " steps");
			}
			return;
		}

		for (int i = 0; i < loop.size(); ++i)
		{
			if (loop.get(i).equals(inventory))
			{
				int k = loop.size() - 1 - i + 2;
				System.out.println(originalDigits + " enters an inventory loop of length " + k);
				return;
			}
		}

		loop.add(digits);
		testSelfInventorying(originalDigits, loop, inventory, step+1);
	}

}
