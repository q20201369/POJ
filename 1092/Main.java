import java.util.Scanner;
import java.util.Vector;
import java.util.Set;
import java.util.HashSet;

public class Main
{
	private static boolean areTwoVerticesConnected(Vector<Vertex> vertices, int a, int b)
	{
		return vertices.get(a).neighbor.contains(b);
	}

	private static boolean hasRegionDuplicatedVertices(Vector<Integer> region)
	{
		Set<Integer> unique = new HashSet<Integer>();
		unique.addAll(region);

		if (unique.size() != region.size())
			return true;

		return false;
	}

	private static double getAngle(int basePosX, int basePosY, int newPosX, int newPosY)
	{
		int shiftedPosX = newPosX - basePosX;
		int shiftedPosY = newPosY - basePosY;

		if (shiftedPosX == 0)
		{
			if (shiftedPosY > 0)
				return Math.PI / 2;
			return - Math.PI / 2;
		}

		return Math.atan2(shiftedPosY, shiftedPosX);
	}

	private static void findRegion(Vector<Vertex> vertices, Vector<Vector<Integer>> regions, int startVertex, int regionSize, Vector<Integer> tempRegion)
	{
		int firstVertex = tempRegion.get(0);
		int secondVertex = tempRegion.get(1);
		int lastVertex = tempRegion.get(tempRegion.size() - 1);
		int lastLastVertex = tempRegion.get(tempRegion.size() - 2);

		if (tempRegion.size() >= regionSize)
		{
			for (int i = 0; i < tempRegion.size(); ++i)
			{
				System.out.print(tempRegion.get(i) + ", ");
			}
			System.out.println("#");

			if (!areTwoVerticesConnected(vertices, firstVertex, lastVertex))
				return;

			if (hasRegionDuplicatedVertices(tempRegion))
				return;

			for (int i = 0; i < tempRegion.size(); ++i)
			{
				System.out.print(tempRegion.get(i) + ", ");
			}
			System.out.println(";");

			regions.add((Vector<Integer>)tempRegion.clone());
			return;
		}
		for (int i = 0; i < tempRegion.size(); ++i)
		{
			System.out.print(tempRegion.get(i) + ", ");
		}
		System.out.println("*");

		Vector<Integer> lastVertexNeighbor = vertices.get(lastVertex).neighbor;
		// find a vertex so that it has smallest angle between last segment and the new segment
		int bestVertex = -1;
		double angle = getAngle(vertices.get(lastVertex).posX, vertices.get(lastVertex).posY, vertices.get(lastLastVertex).posX, vertices.get(lastLastVertex).posY);
		double bestAngle = 100000000;
		for (int i = 0; i < lastVertexNeighbor.size(); ++i)
		{
			int nextVertex = lastVertexNeighbor.get(i);
			if (bestVertex < 0)
				bestVertex = nextVertex;

			if (nextVertex == lastLastVertex)
				continue;

			System.out.println("last " + lastVertex + " neighbor " + nextVertex);

			double nextAngle = getAngle(vertices.get(lastVertex).posX, vertices.get(lastVertex).posY, vertices.get(nextVertex).posX, vertices.get(nextVertex).posY);
			double deltaAngle = Math.abs((angle - nextAngle) % (2*Math.PI));
			System.out.println("nextAngle: " + nextAngle + " angle: " + angle + " delta: " + deltaAngle);
			if (deltaAngle < bestAngle)
			{
				bestAngle = deltaAngle;
				bestVertex = nextVertex;
			}
		}

		tempRegion.add(bestVertex);
		findRegion(vertices, regions, startVertex, regionSize, tempRegion);
		tempRegion.remove(tempRegion.size() - 1);
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

			Vector<Integer> vertexNeighbor = vertices.get(i).neighbor;
			for (int j = 0; j < vertexNeighbor.size(); ++j)
			{
				int nextVertex = vertexNeighbor.get(j);
				if (nextVertex == i)
					continue;
				tempRegion.add(nextVertex);
				findRegion(vertices, regions, i, regionSize, tempRegion);
				tempRegion.remove(tempRegion.size() - 1);
			}
		}

		return regions.size();
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
				lineScanner.nextInt();
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

