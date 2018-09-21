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

		int finalGroups = 0;
		int remainder = 0;
		for (int groups = 2; groups <= n-1; ++groups)
		{
			// calculate the sum of groups 2,3,4,5
			int sumStartsFromTwo = (2 + groups - 1 + 2) * groups / 2;
			int sumStartsFromThree = (3 + groups - 1 + 3) * groups / 2;
			int sumStartsFromTwoNextGroup = (2 + (groups+1) - 1 + 2) * (groups+1) / 2;

			if (sumStartsFromTwo <= n && n < sumStartsFromTwoNextGroup)
			{
				finalGroups = groups;
				remainder = n - sumStartsFromTwo;
				break;
			}
		}

		int[] groupSizes = new int[finalGroups];
		for (int i = 0; i < groupSizes.length; ++i)
		{
			groupSizes[i] = 2 + i;
		}

		int increaseIndex = groupSizes.length - 1;
		for (int i = 0; i < remainder; ++i)
		{
			groupSizes[increaseIndex]++;
			increaseIndex = (increaseIndex-1 + groupSizes.length) % groupSizes.length;
		}

		System.out.print(groupSizes[0]);
		for (int i = 1; i < groupSizes.length; ++i)
			System.out.print(" " + groupSizes[i]);
		System.out.println("");
	}
}
