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

		Stack<String> backwardStack = new Stack<String>();
		Stack<String> forwardStack = new Stack<String>();

		String currentUrl = "http://www.acm.org/";
		
		while (true)
		{
			String command = sc.next();
			String url = new String();

			if ("QUIT".equals(command))
				break;

			if ("VISIT".equals(command))
			{
				url = sc.nextLine().substring(1); // should remove the space
				backwardStack.push(currentUrl);
				forwardStack.clear();
				currentUrl = url;
				System.out.println(currentUrl);
			}
			else if ("BACK".equals(command))
			{
				if (backwardStack.empty())
				{
					System.out.println("Ignored");
				}
				else
				{
					forwardStack.push(currentUrl);
					currentUrl = backwardStack.pop();
					System.out.println(currentUrl);
				}
			}
			else if ("FORWARD".equals(command))
			{
				if (forwardStack.empty())
				{
					System.out.println("Ignored");
				}
				else
				{
					backwardStack.push(currentUrl);
					currentUrl = forwardStack.pop();
					System.out.println(currentUrl);
				}
			}
		}
	}
}
