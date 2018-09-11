import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;

public class Main
{
	public static int findMinimumMovesSetLastToOne(Vector<Integer> numbers, int vectorUpperBound)
	{
		if (vectorUpperBound < 0)
		{
			return 0;
		}
		else if (vectorUpperBound == 0)
		{
			return numbers.get(0) == 0 ? 1 : 0;
		}
		else
		{
			if (numbers.get(vectorUpperBound) == 1)
			{
				return findMinimumMoves(numbers, vectorUpperBound-1);
			}
			else
			{
				if (numbers.get(vectorUpperBound-1) == 1)
				{
					return findMinimumMoves(numbers, vectorUpperBound - 2) + 1 + ((1<<(vectorUpperBound))-1);
				}
				else
				{
					return findMinimumMovesSetLastToOne(numbers, vectorUpperBound-1) + 1 + ((1<<(vectorUpperBound))-1);
				}
			}
		}
	}

	public static int findMinimumMoves(Vector<Integer> numbers, int vectorUpperBound)
	{
		if (vectorUpperBound < 0)
		{
			return 0;
		}
		else if (vectorUpperBound == 0)
		{
			return numbers.get(0);
		}
		else
		{
			if (numbers.get(vectorUpperBound) == 0)
			{
				return findMinimumMoves(numbers, vectorUpperBound-1);
			}
			else
			{
				if (numbers.get(vectorUpperBound-1) == 1)
				{
					int subResult = findMinimumMoves(numbers, vectorUpperBound-2);
					int clearResult = ((1<<(vectorUpperBound)) - 1);
					return  subResult + 1 + clearResult;
				}
				else
				{
					return findMinimumMovesSetLastToOne(numbers, vectorUpperBound-1) + 1 + ((1<<vectorUpperBound) - 1);
				}
			}
		}
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int totalNumbers = sc.nextInt();
		Vector<Integer> numbers = new Vector<Integer>();
		for (int i = 0; i < totalNumbers; ++i)
		{
			numbers.add(sc.nextInt());
		}

		System.out.println(findMinimumMoves(numbers, numbers.size() - 1));
	}
}
