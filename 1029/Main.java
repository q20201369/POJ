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

		int totalCoins = sc.nextInt();
		int totalWeightings = sc.nextInt();

		Vector<Integer> coinsScore = new Vector<Integer>();
		for (int i = 0; i < totalCoins; ++i)
		{
			coinsScore.add(0);
		}

		Vector<Vector<Integer>> leftSideResults = new Vector<Vector<Integer>>();
		Vector<Vector<Integer>> rightSideResults = new Vector<Vector<Integer>>();
		Vector<String> weightingResults = new Vector<String>();
		for (int i = 0; i < totalWeightings; ++i)
		{
			int coinsEachSide = sc.nextInt();
			Vector<Integer> leftSide = new Vector<Integer>();
			Vector<Integer> rightSide = new Vector<Integer>();

			for (int j = 0; j < coinsEachSide; ++j)
			{
				leftSide.add(sc.nextInt());
			}

			for (int j = 0; j < coinsEachSide; ++j)
			{
				rightSide.add(sc.nextInt());
			}

			String weightingResult = sc.next();

			leftSideResults.add(leftSide);
			rightSideResults.add(rightSide);
			weightingResults.add(weightingResult);
		}

		for (int i = 0; i < totalWeightings; ++i)
		{
			if ("<".equals(weightingResults.get(i)))
			{
				for (int j = 0; j < leftSideResults.get(i).size(); ++j)
				{
					int coinId = leftSideResults.get(i).get(j);
					coinsScore.set(coinId-1, coinsScore.get(coinId-1)+1);
				}

				for (int j = 0; j < rightSideResults.get(i).size(); ++j)
				{
					int coinId = rightSideResults.get(i).get(j);
					coinsScore.set(coinId-1, coinsScore.get(coinId-1)-1);
				}
			}
			else if (">".equals(weightingResults.get(i)))
			{
				for (int j = 0; j < leftSideResults.get(i).size(); ++j)
				{
					int coinId = leftSideResults.get(i).get(j);
					coinsScore.set(coinId-1, coinsScore.get(coinId-1)-1);
				}

				for (int j = 0; j < rightSideResults.get(i).size(); ++j)
				{
					int coinId = rightSideResults.get(i).get(j);
					coinsScore.set(coinId-1, coinsScore.get(coinId-1)+1);
				}
			}
		}

		for (int i = 0; i < totalWeightings; ++i)
		{
			if ("=".equals(weightingResults.get(i)))
			{
				for (int j = 0; j < leftSideResults.get(i).size(); ++j)
				{
					int coinId = leftSideResults.get(i).get(j);
					coinsScore.set(coinId-1, 0);
				}

				for (int j = 0; j < rightSideResults.get(i).size(); ++j)
				{
					int coinId = rightSideResults.get(i).get(j);
					coinsScore.set(coinId-1, 0);
				}
			}
		}

		Vector<Integer> absCoinsScore = new Vector<Integer>();
		for (int i = 0; i < coinsScore.size(); ++i)
		{
			absCoinsScore.add(Math.abs(coinsScore.get(i)));
		}

		Collections.sort(absCoinsScore);

		if (absCoinsScore.get(absCoinsScore.size()-1) == absCoinsScore.get(absCoinsScore.size()-2))
		{
			System.out.println("0");
		}
		else
		{
			for (int i = 0; i < coinsScore.size(); ++i)
			{
				if (Math.abs(coinsScore.get(i)) == absCoinsScore.get(absCoinsScore.size()-1))
				{
					System.out.println(i+1);
				}
			}
		}
	}
}
