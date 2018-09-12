import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;

public class Main
{
	public static void judge(int smallStones, int largeStones)
	{
		long patternSizes[][] = new long[11][2];
		patternSizes[0] = new long[] {21, 13};
		{
			long a = patternSizes[0][0]; long b = patternSizes[0][1];
			for (int i = 1; i < patternSizes.length; ++i)
			{
				long x = 5 * a + 3 * b;
				long y = 3 * a + 2 * b;
				patternSizes[i] = new long[2];
				patternSizes[i][0] = x;
				patternSizes[i][1] = y;
				a = x; b = y;
			}
		}

		long patternDeltas[][] = new long[patternSizes.length][2];
		patternDeltas[0] = new long[] {13, 8};
		{
			long a = patternDeltas[0][0]; long b = patternDeltas[0][1];
			for (int i = 1; i < patternDeltas.length; ++i)
			{
				long x = 5 * a + 3 * b;
				long y = 3 * a + 2 * b;
				patternDeltas[i] = new long[2];
				patternDeltas[i][0] = x;
				patternDeltas[i][1] = y;
				a = x; b = y;
			}
		}

		/*
		long patternDeltas[][] = {
			{13, 8,},
			{89, 55},
			{610, 377},
			{4181, 2584},
			{28657, 17711},
			{196418, 121393},
			{1346269, 832040},
			{9227465, 5702887},
			{63245986, 39088169},
			{433494437, 267914296},
			{2971215073L, 1836311903L},
		};
		*/

		int patternA[] = {1, 2, 1, 2, 2, 1, 2, 1, 2, 2, 1, 2, 2};
		int patternB[] = {1, 2, 1, 2, 2, 1, 2, 2};

		long number = smallStones;
		long step = 0;
		int patternType = 0;

		Vector<Integer> path = new Vector<Integer>();
		for (int i = patternSizes.length - 1; i >= 0; --i)
		{
			int node = 0;
			long a = patternSizes[i][0];
			long b = patternSizes[i][1];
			long da = patternDeltas[i][0];
			long db = patternDeltas[i][1];

			if (patternType == 0)
			{
				if (number <= a)
				{
					node = 0;
					patternType = 0;
				}
				else if (number <= a + a)
				{
					node = 1;
					number -= a;
					step += da;
					patternType = 0;
				}
				else if (number <= a + a + b)
				{
					node = 2;
					number -= a + a;
					step += da + da;
					patternType = 1;
				}
				else if (number <= a + a + b + a)
				{
					node = 3;
					number -= a + a + b;
					step += da + da + db;
					patternType = 0;
				}
				else if (number <= a + a + b + a + a)
				{
					node = 4;
					number -= a + a + b + a;
					step += da + da + db + da;
					patternType = 0;
				}
				else if (number <= a + a + b + a + a + b)
				{
					node = 5;
					number -= a + a + b + a + a;
					step += da + da + db + da + da;
					patternType = 1;
				}
				else if (number <= a + a + b + a + a + b + a)
				{
					node = 6;
					number -= a + a + b + a + a + b;
					step += da + da + db + da + da + db;
					patternType = 0;
				}
				else if (number <= a + a + b + a + a + b + a + b)
				{
					node = 7;
					number -= a + a + b + a + a + b + a;
					step += da + da + db + da + da + db + da;
					patternType = 1;
				}
			}
			else
			{
				if (number <= a)
				{
					node = 0;
					patternType = 0;
				}
				else if (number <= a + a)
				{
					node = 1;
					number -= a;
					step += da;
					patternType = 0;
				}
				else if (number <= a + a + b)
				{
					node = 2;
					number -= a + a;
					step += da + da;
					patternType = 1;
				}
				else if (number <= a + a + b + a)
				{
					node = 3;
					number -= a + a + b;
					step += da + da + db;
					patternType = 0;
				}
				else if (number <= a + a + b + a + b)
				{
					node = 4;
					number -= a + a + b + a;
					step += da + da + db + da;
					patternType = 1;
				}
			}


			path.add(node);
		}

		if (patternType == 0)
		{
			int sumPatternSize = 0;
			for (int i = 0; i < patternA.length; ++i)
			{
				sumPatternSize += patternA[i];
				if (sumPatternSize == number)
				{
					number = 0;
					step += i + 1;
					break;
				}
			}
		}
		else
		{
			int sumPatternSize = 0;
			for (int i = 0; i < patternB.length; ++i)
			{
				sumPatternSize += patternB[i];
				if (sumPatternSize == number)
				{
					number = 0;
					step += i + 1;
					break;
				}
			}
		}

		if (number == 0 && largeStones == step + smallStones)
		{
			System.out.println("0");
		}
		else
		{
			System.out.println("1");
		}
	}

	public static void test(String[] args)
	{
		HashMap<Integer, Boolean> keys = new HashMap<Integer, Boolean>();
		Vector<Integer> baseDeltas = new Vector<Integer>();

		int base = 1;
		int oldBase = 0;
		for (int i = 1; i < 100000000; ++i)
		{
			// find a valid base
			while (keys.containsKey(base))
			{
				keys.remove(base);
				base++;
			}

			int newNumber = base + i;
			int baseDelta = base - oldBase;
			// baseDeltas.add(baseDelta);
			oldBase = base;

			// System.out.println(base + ", " + newNumber + ": " + baseDelta);
			System.out.println(baseDelta);

			base++;
			keys.put(newNumber, true);
		}
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextInt())
		{
			int leftStones = sc.nextInt();
			int rightStones = sc.nextInt();

			if (leftStones < rightStones)
			{
				judge(leftStones, rightStones);
			}
			else
			{
				judge(rightStones, leftStones);
			}
		}
	}
}

