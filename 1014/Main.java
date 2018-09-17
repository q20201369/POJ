import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int collectionId = 1;

		while (true)
		{
			int[] marblesPerValue = new int[6];
			for (int i = 0; i < 6; ++i)
			{
				marblesPerValue[i] = sc.nextInt();
			}

			if (shallEnd(marblesPerValue))
				break;
			else if (collectionId != 1)
				System.out.println("");

			System.out.println("Collection #" + collectionId + ":");
			if (canBeDivided(marblesPerValue))
			{
				System.out.println("Can be divided.");
			}
			else
			{
				System.out.println("Can't be divided.");
			}

			collectionId++;
		}
	}

	public static boolean shallEnd(int[] marblesPerValue)
	{
		boolean hasNonZeroMarbles = false;
		for (int i = 0; i < marblesPerValue.length; ++i)
		{
			if (marblesPerValue[i] != 0)
			{
				hasNonZeroMarbles = true;
				return false;
			}
		}

		return true;
	}

	public static boolean canBeDivided(int[] marblesPerValue)
	{
		int totalValue = totalValue(marblesPerValue);
		if (totalValue % 2 != 0)
			return false;

		int halfValue = totalValue / 2;

		int[] marblesUsage = new int[marblesPerValue.length];

		/*
		int[] marblesToBeSaved = new int[6] {
			2*3*4*5*6,
			1*3*4*5*6,
			1*2*4*5*6,
			1*2*3*5*6,
			1*2*3*4*6,
			1*2*3*4*5,
		};

		int[] marblesForPreprocessing = new int[6];
		for (int i = 0; i < 6; ++i)
		{
			if (marblesToBeSaved[i] < marblesPerValue[i])
			{
				marblesForPreprocessing[i] = marblesPerValue[i] - marblesToBeSaved[i];
			}
			else
			{
				marblesForPreprocessing[i] = 0;
			}
		}
		*/

		int[] maxValues = maxValueFromCurrentValueToLowestValue(marblesPerValue);

		return canPack(marblesPerValue, marblesUsage, maxValues, halfValue, 6);
	}

	public static int totalValue(int[] marblesPerValue)
	{
		int totalValue = 0;
		for (int i = 0; i < marblesPerValue.length; ++i)
		{
			totalValue += marblesPerValue[i] * (i+1);
		}

		return totalValue;
	}

	public static int[] maxValueFromCurrentValueToLowestValue(int[] marblesPerValue)
	{
		int totalValue = 0;
		int[] maxValues = new int[marblesPerValue.length];
		for (int i = 0; i < marblesPerValue.length; ++i)
		{
			totalValue += marblesPerValue[i] * (i+1);
			maxValues[i] = totalValue;
		}

		return maxValues;
	}

	public static boolean canPack(int[] marblesPerValue, int[] marblesUsage, int[] maxValues, int targetValue, int currentValue)
	{
		if (targetValue == 0)
			return true;

		if (maxValues[currentValue-1] < targetValue)
			return false;

		int neededMarbles = targetValue / currentValue;
		if (neededMarbles > 0 && neededMarbles <= marblesPerValue[currentValue-1] && neededMarbles * currentValue == targetValue)
		{
			return true;
		}

		int maxMarblesForCurrentValue = targetValue / currentValue;
		if (maxMarblesForCurrentValue > marblesPerValue[currentValue-1])
			maxMarblesForCurrentValue = marblesPerValue[currentValue-1];

		int[] lowerBound = new int[]
		{
			0,
			1,
			2,
			4,
			6,
			10,
		};

		int minMarblesForCurrentValue = maxMarblesForCurrentValue - lowerBound[currentValue-1];
		if (minMarblesForCurrentValue < 0)
			minMarblesForCurrentValue = 0;

		for (int i = maxMarblesForCurrentValue; i >= minMarblesForCurrentValue; --i)
		{
			int newTargetValue = targetValue - currentValue * i;

			marblesPerValue[currentValue-1] -= i;
			if (canPack(marblesPerValue, marblesUsage, maxValues, newTargetValue, currentValue-1))
				return true;
			marblesUsage[currentValue-1] += i;
		}

		return false;
	}
}

