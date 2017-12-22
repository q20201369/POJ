import java.util.Scanner;
import java.util.Vector;

public class Main
{
	public static int findRegions(Vector<Vertex> vertices, int regionSize)
	{
		return 0;
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int testCases = sc.nextInt();

		for (int i = 0; i < testCases; ++i)
		{
			Vector<Vertex> vertices = new Vector<Vertex>();

			int verticesCount = sc.nextInt();
			String line = sc.nextLine(); // consume the next line separator
			for (int v = 0; v < verticesCount; ++v)
			{
				line = sc.nextLine();
				Scanner lineScanner = new Scanner(line);
				Vertex vertex = new Vertex();
				vertex.id = lineScanner.nextInt() - 1;
				vertex.posX = lineScanner.nextInt();
				vertex.posY = lineScanner.nextInt();
				while (lineScanner.hasNextInt())
				{
					int neighbor = lineScanner.nextInt() - 1;
					vertex.neighbor.add(neighbor);
				}

				vertices.add(vertex);
			}

			int regionSize = sc.nextInt();

			int regions = findRegions(vertices, regionSize);
			System.out.println(regions);
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

