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

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		while (true)
		{
			int n = sc.nextInt();
			if (n <= 0)
				break;

			Vector<Integer> encryptTable = new Vector<Integer>();
			for (int i = 0; i < n; ++i)
			{
				encryptTable.add(sc.nextInt()-1);
			}

			Vector<Vector<Integer>> multiEncryptTable = new Vector<Vector<Integer>>();
			for (int i = 0; i < n; ++i)
			{
				Vector<Integer> multiEncrypt = new Vector<Integer>();
				multiEncrypt.add(i);

				int currentPosition = i;
				while (true)
				{
					int nextPosition = encryptTable.get(currentPosition);
					if (nextPosition == i)
						break;

					multiEncrypt.add(nextPosition);
					currentPosition = nextPosition;
				}

				multiEncryptTable.add(multiEncrypt);
			}

			while (true)
			{
				int k = sc.nextInt();
				if (k <= 0)
					break;

				String message = sc.nextLine().substring(1);

				char[] messageFilledSpace = new char[n];
				Arrays.fill(messageFilledSpace, ' ');
				for (int i = 0; i < message.length(); ++i)
					messageFilledSpace[i] = message.charAt(i);

				char[] newMessage = new char[n];
				for (int i = 0; i < n; ++i)
				{
					char c = messageFilledSpace[i];
					Vector<Integer> encrypt = multiEncryptTable.get(i);
					int finalPosition = encrypt.get(k % encrypt.size());
					newMessage[finalPosition] = c;
				}

				String finalMessage = new String();
				for (int i = 0; i < n; ++i)
					finalMessage += newMessage[i];

				System.out.println(finalMessage);
			}

			System.out.println("");
		}
	}
}
