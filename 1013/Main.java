import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;

public class Main
{
	public static Vector<Integer> convert_coins(String coinsString)
	{
		Vector<Integer> coins = new Vector<Integer>();

		for (int c = 0; c < coinsString.length(); ++c)
		{
			int coin = coinsString.charAt(c) - 'A';
			coins.add(coin);
		}

		return coins;
	}

	public static boolean checkAssumption(Vector<Weigh> weighs, Vector<Integer> coinsAssumption)
	{
		boolean isValid = true;

		for (int k = 0; k < 3; ++k)
		{
			Weigh weigh = weighs.get(k);

			int leftSum = 0;
			for (int c = 0; c < weigh.leftCoins.size(); ++c)
			{
				leftSum += coinsAssumption.get(weigh.leftCoins.get(c));
			}

			int rightSum = 0;
			for (int c = 0; c < weigh.rightCoins.size(); ++c)
			{
				rightSum += coinsAssumption.get(weigh.rightCoins.get(c));
			}

			if (leftSum == rightSum && !"even".equals(weigh.weighString))
			{
				isValid = false;
				break;
			}
			if (leftSum > rightSum && !"up".equals(weigh.weighString))
			{
				isValid = false;
				break;
			}
			if (leftSum < rightSum && !"down".equals(weigh.weighString))
			{
				isValid = false;
				break;
			}
		}

		return isValid;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int cases = sc.nextInt();
		for (int i = 0; i < cases; ++i)
		{
			Vector<Integer> coins = new Vector<Integer>();
			for (int j = 0; j < 12; ++j)
			{
				coins.add(0);
			}

			Vector<Weigh> weighs = new Vector<Weigh>();
			for (int j = 0; j < 3; ++j)
			{
				String leftCoinsString = sc.next();
				String rightCoinsString = sc.next();
				String weighString = sc.next();
				Vector<Integer> leftCoins = convert_coins(leftCoinsString);
				Vector<Integer> rightCoins = convert_coins(rightCoinsString);

				Weigh weigh = new Weigh();
				weigh.leftCoins = leftCoins;
				weigh.rightCoins = rightCoins;
				weigh.weighString = weighString;

				weighs.add(weigh);
			}

			for (int j = 0; j < 3; ++j)
			{
				Weigh weigh = weighs.get(j);
				if ("even".equals(weigh.weighString))
				{
					for (int c = 0; c < weigh.leftCoins.size(); ++c)
					{
						coins.set(weigh.leftCoins.get(c), 1);
					}

					for (int c = 0; c < weigh.rightCoins.size(); ++c)
					{
						coins.set(weigh.rightCoins.get(c), 1);
					}
				}

			}

			Vector<Integer> coinsAssumption = new Vector<Integer>();
			for (int k = 0; k < 12; ++k)
			{
				coinsAssumption.add(1);
			}

			for (int j = 0; j < 12; ++j)
			{
				if (coins.get(j) == 1)
					continue;


				// if j is ligher
				coinsAssumption.set(j, 0);
				if (checkAssumption(weighs, coinsAssumption))
				{
					char coinChar = (char)('A' + j);
					System.out.println("" + coinChar + " is the counterfeit coin and it is light.");
				}

				// if j is ligher
				coinsAssumption.set(j, 2);
				if (checkAssumption(weighs, coinsAssumption))
				{
					char coinChar = (char)('A' + j);
					System.out.println("" + coinChar + " is the counterfeit coin and it is heavy.");
				}

				coinsAssumption.set(j, 1);
			}
		}
	}
}

class Weigh
{
	public Vector<Integer> leftCoins;
	public Vector<Integer> rightCoins;
	public String weighString;
}

