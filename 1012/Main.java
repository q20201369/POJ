import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextInt())
		{
			int k = sc.nextInt();
			int n = 2 * k;

			if (k <= 0)
				break;

			long upperBound = 1;
			for (int i = 1; i <= (n+1); ++i)
			{
				upperBound *= i;
			}

			for (long m = k+1; m <= Long.MAX_VALUE; ++m) // try all the valid m
			{
				int startOfCircle = 0;
				boolean killingGoodGuy = false;

				Vector<Integer> circle = new Vector<Integer>();
				for (int i = 0; i < k; ++i)
				{
					circle.add(1);
				}
				for (int i = k; i < n; ++i)
				{
					circle.add(4); // bad guys
				}

				for (int i = 0; i < k; ++i)
				{
					// go to next m person
					long countingRemainder = m;
					int person = startOfCircle;
					for (long j = 0; j < m*n && countingRemainder > 0; ++j)
					{
						person = (int)((startOfCircle + j) % n);
						if (circle.get(person) != 0)
						{
							countingRemainder--;
							continue;
						}
					}

					// if killing a good guy, break
					if (circle.get(person) == 1)
					{
						killingGoodGuy = true;
						break;
					}

					// kill him
					circle.set(person, 0);
					
					// find the start of circle
					int nextStartOfCircle = person;
					for (int j = 1; j <= n; ++j)
					{
						nextStartOfCircle = (person + j) % n;
						if (circle.get(nextStartOfCircle) != 0)
						{
							break;
						}
					}
					startOfCircle = nextStartOfCircle;
				}

				if (killingGoodGuy)
				{
					continue;
				}
				else
				{
					System.out.println(m);
					break;
				}
			}
		}
	}
}

