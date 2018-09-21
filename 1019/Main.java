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
			int position = sc.nextInt();
			int digit = 0;

			int upperBound = 0;
			int lowerBound = 0;
			for (int width = 1; width < 5; ++width)
			{
				upperBound = pow(width) - 1;
				lowerBound = pow(width-1) - 1;
				long upperBoundLength = totalDigits(upperBound);
				if (position <= upperBoundLength)
					break;
			}

			// find a number between lowerBound and upperBound
			int number = 0;
			while (true)
			{
				int middle = (lowerBound + upperBound) / 2;
				long middleLength = totalDigits(middle);

				if (position <= middleLength && totalDigits(middle-1) < position)
				{
					number = middle;
					break;
				}

				if (middleLength < position)
				{
					lowerBound = middle + 1;
				}
				else if (position < middleLength)
				{
					upperBound = middle - 1;
				}
			}

			int positionInNumber = position - (int)totalDigits(number-1);

			upperBound = number;
			lowerBound = 0;
			int numberHit = 0;
			while (true)
			{
				int middle = (upperBound + lowerBound) / 2;
				int middleNumberDigits = numberDigits(middle);
				if (numberDigits(middle-1) < positionInNumber && positionInNumber <= middleNumberDigits)
				{
					numberHit = middle;
					break;
				}
				if (positionInNumber < middleNumberDigits)
					upperBound = middle - 1;
				else
					lowerBound = middle + 1;
			}

			int offsetInNumberHit = positionInNumber - numberDigits(numberHit-1) - 1;

			/*
			int widthOfPositionInNumber = ("" + positionInNumber).length();
			for (int i = 1; i < widthOfPositionInNumber; ++i)
			{
				positionInNumber -= 9 * pow(i-1) * i;
			}

			int numberHit = (positionInNumber-1) / widthOfPositionInNumber;
			if (widthOfPositionInNumber == 1)
			{
				// single digits starts from 1, but double digits start from 0
				numberHit = positionInNumber / widthOfPositionInNumber;
			}

			for (int i = 1; i < widthOfPositionInNumber; ++i)
			{
				numberHit += pow(i-1) * 10;
			}

			int positionInNumberHit = (positionInNumber-1) % widthOfPositionInNumber;
			*/

			String numberHitString = "" + numberHit;
			digit = numberHitString.charAt(offsetInNumberHit) - '0';

			System.out.println(digit);

			/*
			if (positionInNumber == 0)
			{
				digit = 1;
			}
			if (0 <= positionInNumber && positionInNumber < 9)
			{
				digit = positionInNumber + 1;
			}
			if (9 <= positionInNumber && positionInNumber < 99)
			{
				positionInNumber -= 9 * 1;
				int numberHit = positionInNumber / 2;
				int positionInNumberHit = positionInNumber % 2;
				if (positionInNumberHit == 0)
					digit = (numberHit + 10) / 10;
				else
					digit = (numberHit + 10) % 10;
			}
			if (9 <= positionInNumber && positionInNumber < 99)
			{
				positionInNumber -= 9 * 1;
				positionInNumber -= 90 * 2;
				int numberHit = positionInNumber / 3;
				int positionInNumberHit = positionInNumber % 3;
			}
			*/
		}
	}

	public static HashMap<Integer, Long> totalDigitsMap = new HashMap<Integer, Long>();
	public static HashMap<Integer, Integer> numberDigitsMap = new HashMap<Integer, Integer>();

	public static long totalDigits(int number)
	{
		if (totalDigitsMap.containsKey(number))
			return totalDigitsMap.get(number);

		long result = 0;

		int numberWidth = ("" + number).length();
		int upperBoundOfLowerSegment = pow(numberWidth-1)-1;

		if (number <= 0)
			return 0;

		result = totalDigits(upperBoundOfLowerSegment) + (number - upperBoundOfLowerSegment) * numberDigits(upperBoundOfLowerSegment) + (number - upperBoundOfLowerSegment) * (number - upperBoundOfLowerSegment + 1) / 2 * numberWidth;

		totalDigitsMap.put(number, result);

		return result;

		/*
		if (number < 10)
		{
			result = number * (number + 1) / 2;
		}
		if (number >= 10 && number < 100)
		{
			return totalDigits(9) + (number - 9) * totalDigits(9) + (number - 9) * (number - 9 + 1) / 2 * 2;
		}
		if (number >= 100 & number < 1000)
		{
			return totalDigits(99) + (number - 99) * totalDigits(99) + (number - 99) * (number - 99 + 1) / 2 * 3;
		}
		*/
	}

	public static int numberDigits(int number)
	{
		if (numberDigitsMap.containsKey(number))
			return numberDigitsMap.get(number);

		if (number <= 0)
			return 0;

		int numberWidth = ("" + number).length();
		int upperBoundOfLowerSegment = pow(numberWidth-1) - 1;

		int result = numberDigits(upperBoundOfLowerSegment) + (number-upperBoundOfLowerSegment) * numberWidth;

		numberDigitsMap.put(number, result);

		return result;
	}

	public static int pow(int x)
	{
		int result = 1;
		for (int i = 0; i < x; ++i)
			result *= 10;

		return result;
	}
}
