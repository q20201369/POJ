import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class Main
{
	private static boolean isDebug = false;

	public static void main(String[] args)
	{
		if (args.length > 0 && "-d".equals(args[0]))
			isDebug = true;

		Scanner sc = new Scanner(System.in);

		while (true)
		{
			int sticksCount = sc.nextInt();
			if (sticksCount == 0)
				break;

			int[] sticks = new int[sticksCount];

			int totalLength = 0;
			int maxLength = 0;
			for (int i = 0; i < sticks.length; ++i)
			{
				int length = sc.nextInt();
				sticks[i] = length;

				totalLength += length;
				if (length > maxLength)
					maxLength = length;
			}

			Arrays.sort(sticks);

			for (int i = maxLength; i <= totalLength; ++i)
			{
				// if n i-length stick can't make total length, the i-length is not valid
				if ((totalLength / i) * i != totalLength)
					continue;

				int originalSticksCount = totalLength / i;
				boolean[] sticksUsage = new boolean[sticksCount];

				boolean canPack = true;
				for (int j = 0; j < originalSticksCount; ++j)
				{
					if (packSticks(sticks, sticksUsage, i, 0) == false)
					{
						canPack = false;
						break;
					}
				}

				if (canPack)
				{
					System.out.println(i);
					break;
				}
			}
		}

	}

	private static boolean packSticks(int[] ascSortedSticks, boolean[] sticksUsage, int target, int currentTotal)
	{
		assert(ascSortedSticks.length == sticksUsage.length);

		if (currentTotal == target)
			return true;

		for (int i = ascSortedSticks.length-1; i >= 0; --i)
		{
			int newTotal = ascSortedSticks[i] + currentTotal;
			if (newTotal <= target && sticksUsage[i] == false)
			{
				sticksUsage[i] = true;
				boolean canPack = packSticks(ascSortedSticks, sticksUsage, target, newTotal);
				if (canPack)
					return true;

				sticksUsage[i] = false;
			}
		}

		return false;
	}
}
