import java.util.Scanner;
import java.util.Vector;

public class Main
{
	private static boolean existsPath(Vector<Vector<Integer>> matrix, int a, int b)
	{
		if (a == b)
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
					break;
				}

				// if not, check if the relation is already there
				if (!existsPath(matrix, smaller, larger))
				{
					// if not, create an edge between smaller node and larger node
					matrix.get(smaller).set(larger, 1);
				}

			}

			// find longest path
			int smallest = 0;
			for (int j = 0; j < matrix.size(); ++j)
			{
				for (int i = 0; i < matrix.size(); ++i)
				{
					if (matrix.get(i).get(smallest) == 1)
					{
						smallest = i;
					}
				}
			}
		}
	}
}

class Vertex
{
	public int id;
	public int posX;
	public int posY;
	public Vector<Integer> neighbor = new Vector<Integer>();
}

