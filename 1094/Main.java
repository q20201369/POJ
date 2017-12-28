import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;

public class Main
{
	private static boolean existsPath(Vector<Vector<Integer>> matrix, int a, int b)
	{
		if (a == b)
			return true;

		if (matrix.get(a).get(b) == 1)
			return true;

		for (int i = 0; i < matrix.size(); ++i)
		{
			if (matrix.get(a).get(i) == 1)
			{
				if (existsPath(matrix, i, b))
					return true;
			}
		}

		return false;
	}


	private static Vector<Integer> findRoots(Vector<Vector<Integer>> matrix)
	{
		// find all the roots in the forest
		Vector<Boolean> roots = new Vector<Boolean>();
		for (int i = 0; i < matrix.size(); ++i)
		{
			roots.add(true);
		}

		for (int j = 0; j < matrix.size(); ++j)
		{
			for (int i = 0; i < matrix.size(); ++i)
			{
				if (matrix.get(i).get(j) == 1)
				{
					// any vertex is the end of an edge is not a root
					roots.set(j, false);
				}
			}
		}

		int rootsCount = 0;
		Vector<Integer> rootsList = new Vector<Integer>();
		for (int i = 0; i < matrix.size(); ++i)
		{
			if (roots.get(i) == true)
			{
				rootsList.add(i);
				rootsCount++;
			}
		}

		return rootsList;
	}

	private static Vector<Integer> findLongestPathStartFromVertex(Vector<Vector<Integer>> matrix, int root)
	{
		Vector<Integer> longestPath = new Vector<Integer>();
		for (int i = 0; i < matrix.size(); ++i)
		{
			if (matrix.get(root).get(i) == 1)
			{
				Vector<Integer> subPath = findLongestPathStartFromVertex(matrix, i);
				if (subPath.size() > longestPath.size())
					longestPath = subPath;
			}
		}

		longestPath.add(0, root);
		return longestPath;
	}

	private static Vector<Integer> findLongestPath(Vector<Vector<Integer>> matrix)
	{
		Vector<Integer> roots = findRoots(matrix);
		if (roots.size() > 1)
			return new Vector<Integer>();

		int root = roots.get(0);

		Vector<Integer> path = findLongestPathStartFromVertex(matrix, root);
		return path;
	}

	private static Vector<Integer> findPrecedents(Vector<Vector<Integer>> matrix, int vertex)
	{
		Vector<Integer> precedents = new Vector<Integer>();

		for (int i = 0; i < matrix.size(); ++i)
		{
			if (matrix.get(i).get(vertex) == 1)
				precedents.add(i);
		}

		return precedents;
	}

	private static Vector<Integer> findSubsequents(Vector<Vector<Integer>> matrix, int vertex)
	{
		Vector<Integer> subsequents = new Vector<Integer>();

		for (int i = 0; i < matrix.size(); ++i)
		{
			if (matrix.get(vertex).get(i) == 1)
				subsequents.add(i);
		}

		return subsequents;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextLine())
		{
			int numberOfObjects = sc.nextInt();
			int numberOfRelations = sc.nextInt();

			if (numberOfObjects == 0 && numberOfRelations == 0)
				break;

			Vector<Vector<Integer>> matrix = new Vector<Vector<Integer>>();
			for (int i = 0; i < numberOfObjects; ++i)
			{
				Vector<Integer> x = new Vector<Integer>();
				for (int j = 0; j < numberOfObjects; ++j)
				{
					x.add(0);
				}
				matrix.add(x);
			}

			boolean isInconsistent = false;
			boolean isSequenceDetermined = false;

			for (int i = 0; i < numberOfRelations; ++i)
			{
				String relation = sc.next("[A-Z][><][A-Z]");
				char smallerChar = relation.charAt(0);
				char largerChar = relation.charAt(2);
				int smaller = smallerChar - 'A';
				int larger = largerChar - 'A';

				// check if there is a loop after adding this relation
				// i.e. check if there is a path from larger to smaller
				if (existsPath(matrix, larger, smaller))
				{
					System.out.println("Inconsistency found after " + (i+1) + " relations.");
					isInconsistent = true;
					for (int j = 0; j < numberOfRelations-i-1; ++j)
						sc.next("[A-Z][><][A-Z]");
					break;
				}

				// if not, check if the relation is already there
				if (!existsPath(matrix, smaller, larger))
				{
					// if not, create an edge between smaller node and larger node
					matrix.get(smaller).set(larger, 1);
					// create edges between precedents and subsequents
					Vector<Integer> precedents = findPrecedents(matrix, smaller);
					Vector<Integer> subsequents = findSubsequents(matrix, larger);
					precedents.add(smaller);
					subsequents.add(larger);
					for (int p = 0; p < precedents.size(); ++p)
					{
						for (int s = 0; s < subsequents.size(); ++s)
						{
							matrix.get(precedents.get(p)).set(subsequents.get(s), 1);
						}
					}
				}

				Vector<Integer> sizes = new Vector<Integer>();
				for (int r = 0; r < matrix.size(); ++r)
				{
					int count = 0;
					Vector<Integer> row = matrix.get(r);
					for (int c = 0; c < row.size(); ++c)
					{
						if (row.get(c) == 1)
							count++;
					}

					sizes.add(count);
				}
				Collections.sort(sizes);
				boolean isIncremental = true;
				for (int r = 0; r < sizes.size(); ++r)
				{
					if (sizes.get(r) != r)
						isIncremental = false;
				}

				if (isIncremental)
				{
					Vector<Integer> longestPath = findLongestPath(matrix);
					System.out.print("Sorted sequence determined after " + (i+1) + " relations: ");
					for (int j = 0; j < longestPath.size(); ++j)
					{
						System.out.print("" + (char)('A' + longestPath.get(j)));
					}
					System.out.println(".");

					for (int j = 0; j < numberOfRelations-i-1; ++j)
						sc.next("[A-Z][><][A-Z]");

					isSequenceDetermined = true;
					break;

				}

			}

			if (isInconsistent || isSequenceDetermined)
				continue;

			System.out.println("Sorted sequence cannot be determined.");
		}
	}
}
