import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;

public class Main
{
	private static void exploreGrid(int[][][] grid, int x, int y, int z, int xUpperBound, int yUpperBound, int zUpperBound)
	{
		if (x < 0 || y < 0 || z < 0)
			return;

		if (x > xUpperBound || y > yUpperBound || z > zUpperBound)
			return;

		if (grid[z][y][x] != 0)
			return;

		grid[z][y][x] = 1;

		exploreGrid(grid, x+1, y, z, xUpperBound, yUpperBound, zUpperBound);
		exploreGrid(grid, x-1, y, z, xUpperBound, yUpperBound, zUpperBound);
		exploreGrid(grid, x, y+1, z, xUpperBound, yUpperBound, zUpperBound);
		exploreGrid(grid, x, y-1, z, xUpperBound, yUpperBound, zUpperBound);
		exploreGrid(grid, x, y, z+1, xUpperBound, yUpperBound, zUpperBound);
		exploreGrid(grid, x, y, z-1, xUpperBound, yUpperBound, zUpperBound);
	}

	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		while (true)
		{
			int n = sc.nextInt();
			int m = sc.nextInt();
			int k = sc.nextInt();
			int l = sc.nextInt();
			if (n == 0 && m == 0 && k == 0 && l == 0)
				break;

			int[][][] extendedGrid = new int[k+2][m+2][n+2];
			Vector<Integer> points = new Vector<Integer>();

			for (int i = 0; i < l; ++i)
			{
				int moduleId = sc.nextInt();
				int nId = moduleId % n;
				int mId = (moduleId % (m*n)) / n;
				int kId = moduleId / (m*n);

				int extendedNId = nId + 1;
				int extendedMId = mId + 1;
				int extendedKId = kId + 1;

				points.add(extendedNId);
				points.add(extendedMId);
				points.add(extendedKId);

				extendedGrid[extendedKId][extendedMId][extendedNId] = 2;
			}

			exploreGrid(extendedGrid, 0, 0, 0, n+1, m+1, k+1);

			int faces = 0;
			for (int iK = 1; iK < k+1; iK++)
			{
				for (int iM = 1; iM < m+1; iM++)
				{
					for (int iN = 1; iN < n+1; iN++)
					{
						System.out.print(extendedGrid[iK][iM][iN]);
					}
					System.out.println("");
				}
				System.out.println("----");
			}

			for (int i = 0; i < points.size() / 3; ++i)
			{
				int iN = points.get(3 * i);
				int iM = points.get(3 * i + 1);
				int iK = points.get(3 * i + 2);

				if (extendedGrid[iK][iM][iN+1] == 1)
					faces++;
				if (extendedGrid[iK][iM][iN-1] == 1)
					faces++;
				if (extendedGrid[iK][iM+1][iN] == 1)
					faces++;
				if (extendedGrid[iK][iM-1][iN] == 1)
					faces++;
				if (extendedGrid[iK+1][iM][iN] == 1)
					faces++;
				if (extendedGrid[iK-1][iM][iN] == 1)
					faces++;
			}

			System.out.println("The number of faces needing shielding is " + faces + ".");
		}
	}
}
