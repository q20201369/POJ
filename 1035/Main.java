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

		Vector<String> dict = readWords(sc);
		Vector<String> wordsToCorrect = readWords(sc);

		HashMap<String, Integer> dictMap = new HashMap<String, Integer>();
		HashMap<String, Vector<Integer>> replaceMap = new HashMap<String, Vector<Integer>>();

		for (int i = 0; i < dict.size(); ++i)
		{
			String word = dict.get(i);

			dictMap.put(word, i);

			for (int j = 0; j < word.length(); ++j)
			{
				String newWord = word.substring(0, j);
				newWord += "_";
				newWord += word.substring(j+1);

				if (!replaceMap.containsKey(newWord))
				{
					replaceMap.put(newWord, new Vector<Integer>());
				}

				Vector<Integer> relatedWords = replaceMap.get(newWord);
				relatedWords.add(i);

			}
		}

		for (int i = 0; i < wordsToCorrect.size(); ++i)
		{
			String word = wordsToCorrect.get(i);

			HashSet<Integer> relatedWords = new HashSet<Integer>();

			if (dictMap.containsKey(word))
			{
				System.out.println(word + " is correct");
				continue;
			}

			// by removing a letter
			for (int j = 0; j < word.length(); ++j)
			{
				String newWord = word.substring(0, j);
				newWord += word.substring(j+1);

				if (dictMap.containsKey(newWord))
				{
					relatedWords.add(dictMap.get(newWord));
				}
			}

			// by adding a letter
			for (int j = 0; j <= word.length(); ++j)
			{
				String newWord = word.substring(0, j);
				newWord += "_";
				newWord += word.substring(j);

				if (replaceMap.containsKey(newWord))
				{
					relatedWords.addAll(replaceMap.get(newWord));
				}
			}

			// by replacing a letter
			for (int j = 0; j < word.length(); ++j)
			{
				String newWord = word.substring(0, j);
				newWord += "_";
				newWord += word.substring(j+1);

				if (replaceMap.containsKey(newWord))
				{
					relatedWords.addAll(replaceMap.get(newWord));
				}
			}

			Vector<Integer> sortedRelatedWords = new Vector<Integer>();
			sortedRelatedWords.addAll(relatedWords);
			Collections.sort(sortedRelatedWords);

			System.out.print(word + ":");
			for (int j = 0; j < sortedRelatedWords.size(); ++j)
			{
				System.out.print(" " + dict.get(sortedRelatedWords.get(j)));
			}
			System.out.println("");
		}
	}

	public static Vector<String> readWords(Scanner sc)
	{
		Vector<String> words = new Vector<String>();
		while (true)
		{
			String word = sc.next();
			if ("#".equals(word))
				break;

			words.add(word);
		}

		return words;
	}
}
