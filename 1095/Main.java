import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;

public class Main
{
	private static Node findTree(Vector<Integer> treeNumberPerSize, int treeSize, int treeIndex)
	{
		if (treeSize == 0)
			return null;
		if (treeSize == 1)
			return new Node();

		int treeTotalNumber = 0;
		for (int leftSubTreeSize = 0; leftSubTreeSize <= treeSize - 1; ++leftSubTreeSize)
		{
			int rightSubTreeSize = treeSize - leftSubTreeSize - 1;

			int leftSubTreeIndex = 0;
			int rightSubTreeIndex = 0;
			boolean fixed = false;

			treeTotalNumber += treeNumberPerSize.get(leftSubTreeSize) * treeNumberPerSize.get(rightSubTreeSize);

			if (treeTotalNumber > treeIndex)
			{
				treeTotalNumber -= treeNumberPerSize.get(leftSubTreeSize) * treeNumberPerSize.get(rightSubTreeSize);

				int treeIndexStartsFromSpecificLeftTreeSize = treeIndex - treeTotalNumber;

				leftSubTreeIndex = treeIndexStartsFromSpecificLeftTreeSize / treeNumberPerSize.get(rightSubTreeSize);
				rightSubTreeIndex = treeIndexStartsFromSpecificLeftTreeSize % treeNumberPerSize.get(rightSubTreeSize);
				fixed = true;
			}

			if (fixed)
			{
				Node x = new Node();
				x.left = findTree(treeNumberPerSize, leftSubTreeSize, leftSubTreeIndex);
				x.right = findTree(treeNumberPerSize, rightSubTreeSize, rightSubTreeIndex);
				return x;
			}
		}

		return null;
	}

	private static String translateTree(Node root)
	{
		if (root == null)
			return "";

		String left = "";
		if (root.left != null)
			left = "(" + translateTree(root.left) + ")";

		String right = "";
		if (root.right != null)
			right = "(" + translateTree(root.right) + ")";

		return left + "X" + right;
	}

	public static void main(String[] args)
	{
		Vector<Integer> treeNumberPerSize = new Vector<Integer>();
		treeNumberPerSize.add(1);
		treeNumberPerSize.add(1);

		for (int treeSize = 2; treeSize < 20; ++treeSize)
		{
			int totalNumber = 0;
			for (int leftSubTreeSize = 0; leftSubTreeSize <= treeSize - 1; ++leftSubTreeSize) // should minus root node
			{
				int rightSubTreeSize = treeSize - leftSubTreeSize - 1;
				totalNumber += treeNumberPerSize.get(leftSubTreeSize) * treeNumberPerSize.get(rightSubTreeSize);
			}
			treeNumberPerSize.add(totalNumber);
		}


		Vector<Integer> treeIdLowerBound = new Vector<Integer>();
		treeIdLowerBound.add(0);
		int totalSize = 0;
		for (int treeSize = 0; treeSize < treeNumberPerSize.size(); ++treeSize)
		{
			totalSize += treeNumberPerSize.get(treeSize);
			treeIdLowerBound.add(totalSize);
		}

		Scanner sc = new Scanner(System.in);
		while (true)
		{
			int treeId = sc.nextInt();
			if (treeId == 0)
				break;

			int treeSize = 0;
			int treeIndexOfTheSameSize = 0;
			for (int i = 0; i < treeIdLowerBound.size(); ++i)
			{
				if (treeId < treeIdLowerBound.get(i))
				{
					treeSize = i - 1;
					treeIndexOfTheSameSize = treeId - treeIdLowerBound.get(i - 1);
					break;
				}
			}

			Node root = findTree(treeNumberPerSize, treeSize, treeIndexOfTheSameSize);
			String output = translateTree(root);
			System.out.println(output);
		}
	}
}

class Node
{
	public Node left;
	public Node right;
}

