import java.util.Scanner;
import java.util.Vector;

public class Main
{
	private static void findRegion(Vector<Vertex> vertices, Vector<Vector<Integer>> regions, int startVertex, int regionSize, Vector<Integer> tempRegion)
	{
		int firstVertex = tempRegion.get(0);
		int lastVertex = tempRegion.get(tempRegion.size() - 1);

		if (tempRegion.size() >= regionSize)
		{
			// test if the first vertex in tempRegion is connected to the last vertex, if yes, the region is good for length
			for (int i = 0; i < tempRegion.size(); ++i)
			{
				System.out.print(i + ", ");
			}
			System.out.println(";");

			regions.add((Vector<Integer>)tempRegion.clone());
			return;
		}

		Vector<Integer> lastVertexNeighbor = vertices.get(lastVertex).neighbor;
		for (int i = 0; i < lastVertexNeighbor.size(); ++i)
		{
			int nextVertex = lastVertexNeighbor.get(i);
			tempRegion.add(nextVertex);
			findRegion(vertices, regions, startVertex, regionSize, tempRegion);
			tempRegion.remove(tempRegion.size() - 1);
		}
	}

	private static int findRegions(Vector<Vertex> vertices, int regionSize)
	{
		if (vertices.size() <= 2)
			return 0;

		Vector<Vector<Integer>> regions = new Vector<Vector<Integer>>();
		Vector<Integer> tempRegion = new Vector<Integer>();
		for (int i = 0; i < vertices.size(); ++i)
		{
			tempRegion.clear();
			tempRegion.add(i);
			findRegion(vertices, regions, i, regionSize, tempRegion);
		}

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

