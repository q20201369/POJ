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

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int testCases = sc.nextInt();
		for (int c = 0; c < testCases; ++c)
		{
			int k = sc.nextInt();
			String numberSystem = sc.next();
			long numberToFit = sc.nextLong();

			if (fitNumber(numberToFit, numberSystem) == false)
			{
				System.out.println("Impossible");
			}
			else
			{
				String factors = new String();
				for (int i = 0; i < k-1; ++i)
				{
					String subNumberSystem = numberSystem.substring(i + 1);
					if (fitNumber(numberToFit, subNumberSystem))
					{
						factors += "0";
					}
					else
					{
						factors += "1";
						if (numberSystem.charAt(i) == 'p')
						{
							numberToFit -= 1<<(k-1-i);
						}
						else
						{
							numberToFit += 1<<(k-1-i);
						}
					}
				}

				if (numberToFit != 0)
				{
					factors += "1";
				}
				else
				{
					factors += "0";
				}

				System.out.println(factors);
			}
		}
	}

	public static boolean fitNumber(long numberToFit, String numberSystem)
	{
		long lowerBound = 0;
		long upperBound = 0;

		int k = numberSystem.length();
		for (int i = 0; i < k; ++i)
		{
			char direction = numberSystem.charAt(k-1 - i);

			if (direction == 'p')
			{
				upperBound += 1 << i;
			}
			else
			{
				lowerBound -= 1 << i;
			}
		}

		if (lowerBound <= numberToFit && numberToFit <= upperBound)
			return true;

		return false;
	}
}
