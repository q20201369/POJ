import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.math.BigInteger;

public class Main
{
	public static BigInteger findMinimumMovesSetLastToOne(Vector<Integer> numbers, int vectorUpperBound)
	{
		if (vectorUpperBound < 0)
		{
			return BigInteger.ZERO;
		}
		else if (vectorUpperBound == 0)
		{
			return numbers.get(0) == 0 ? BigInteger.ONE : BigInteger.ZERO;
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
					return findMinimumMoves(numbers, vectorUpperBound - 2).add(BigInteger.ONE).add(BigInteger.ONE.shiftLeft(vectorUpperBound)).subtract(BigInteger.ONE);
				}
				else
				{
					return findMinimumMovesSetLastToOne(numbers, vectorUpperBound-1).add(BigInteger.ONE).add(BigInteger.ONE.shiftLeft(vectorUpperBound)).subtract(BigInteger.ONE);
				}
			}
		}
	}

	public static BigInteger findMinimumMoves(Vector<Integer> numbers, int vectorUpperBound)
	{
		if (vectorUpperBound < 0)
		{
			return BigInteger.ZERO;
		}
		else if (vectorUpperBound == 0)
		{
			return numbers.get(0) == 0 ? BigInteger.ZERO : BigInteger.ONE;
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
					BigInteger subResult = findMinimumMoves(numbers, vectorUpperBound-2);
					BigInteger clearResult = BigInteger.ONE.shiftLeft(vectorUpperBound).subtract(BigInteger.ONE);
					return  subResult.add(BigInteger.ONE).add(clearResult);
				}
				else
				{
					return findMinimumMovesSetLastToOne(numbers, vectorUpperBound-1).add(BigInteger.ONE).add(BigInteger.ONE.shiftLeft(vectorUpperBound)).subtract(BigInteger.ONE);
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
