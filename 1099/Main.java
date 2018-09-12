import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Arrays;

public class Main
{
	public static int getOxygenState(int[][] matrix, int m, int row, int column)
	{
		if (matrix[row][column] != 0)
			return matrix[row][column];

		boolean[] possibleStates = new boolean[] {true, true, true, true};
		if (row == 0)
		{
			possibleStates[0] = false;
			possibleStates[1] = false;
		}
		else if (row == m - 1)
		{
			possibleStates[2] = false;
			possibleStates[3] = false;
		}

		/*
		if (column == 0)
		{
			possibleStates[1] = false;
			possibleStates[2] = false;
		}
		else if (column == m - 1)
		{
			possibleStates[0] = false;
			possibleStates[3] = false;
		}
		*/

		if (0 < column && (matrix[row][column-1] == 11 || matrix[row][column-1] == 14 || matrix[row][column-1] == 1))
		{
			possibleStates[1] = false;
			possibleStates[2] = false;
		}

		if (column < m-1 && (matrix[row][column+1] == 12 || matrix[row][column+1] == 13 || matrix[row][column+1] == 1))
		{
			possibleStates[0] = false;
			possibleStates[3] = false;
		}

		if (0 < row && (matrix[row-1][column] == 13 || matrix[row-1][column] == 14 || matrix[row-1][column] == -1))
		{
			possibleStates[0] = false;
			possibleStates[1] = false;
		}

		if (row < m-1 && (matrix[row+1][column] == 11 || matrix[row+1][column] == 12 || matrix[row+1][column] == -1))
		{
			possibleStates[2] = false;
			possibleStates[3] = false;
		}

		int remainingPossibleStates = 0;
		int state = 0;
		for (int i = 0; i < possibleStates.length; ++i)
		{
			if (possibleStates[i] == true)
			{
				remainingPossibleStates++;
				state = i;
			}
		}

		if (remainingPossibleStates == 1)
		{
			matrix[row][column] = state + 11;
			return state + 11;
		}

		return 0;
	}

	public static void main(String[] args)
	{
		int caseNumber = 0;
		Scanner sc = new Scanner(System.in);
		while (sc.hasNextInt())
		{
			int m = sc.nextInt();
			if (m <= 0)
				break;

			caseNumber++;

			int[][] matrix = new int[m][m];
			for (int i = 0; i < m; ++i)
			{
				matrix[i] = new int[m];
				for (int j = 0; j < m; ++j)
				{
					int d = sc.nextInt();
					matrix[i][j] = d;
				}
			}

			LinkedList<Pair<Integer, Integer>> queue = new LinkedList<Pair<Integer, Integer>>();
			for (int i = 0; i < m; ++i)
			{
				for (int j = 0; j < m; ++j)
				{
					queue.add(new Pair<Integer, Integer>(i, j));
				}
			}

			while (queue.size() > 0)
			{
				Pair<Integer, Integer> oxygen = queue.removeFirst();
				int state = getOxygenState(matrix, m, oxygen.key, oxygen.value);
				if (state == 0)
					queue.addLast(oxygen);
			}

			/*
			for (int i = 0; i < m; ++i)
			{
				for (int j = 0; j < m; ++j)
				{
					System.out.print("" + matrix[i][j] + " ");
				}
				System.out.println("");
			}
			*/

			int squareWidth = 5 + 4 * (m-1);
			int squareHeight = squareWidth - 4;
			char[][] square = new char[squareHeight][squareWidth];
			for (int i = 0; i < squareHeight; ++i)
			{
				square[i] = new char[squareWidth];
				Arrays.fill(square[i], ' ');
			}

			for (int i = 0; i < m; ++i)
			{
				for (int j = 0; j < m; ++j)
				{
					int oRow = 0 + i * 4;
					int oColumn = 2 + j * 4;

					square[oRow][oColumn] = 'O';

					int state = matrix[i][j];
					if (state == 1)
					{
						square[oRow][oColumn-2] = 'H';
						square[oRow][oColumn-1] = '-';
						square[oRow][oColumn+1] = '-';
						square[oRow][oColumn+2] = 'H';
					}
					else if (state == -1)
					{
						square[oRow-2][oColumn] = 'H';
						square[oRow-1][oColumn] = '|';
						square[oRow+1][oColumn] = '|';
						square[oRow+2][oColumn] = 'H';
					}
					else if (state == 11)
					{
						square[oRow][oColumn+2] = 'H';
						square[oRow][oColumn+1] = '-';
						square[oRow-1][oColumn] = '|';
						square[oRow-2][oColumn] = 'H';
					}
					else if (state == 12)
					{
						square[oRow-2][oColumn] = 'H';
						square[oRow-1][oColumn] = '|';
						square[oRow][oColumn-1] = '-';
						square[oRow][oColumn-2] = 'H';
					}
					else if (state == 13)
					{
						square[oRow][oColumn-1] = '-';
						square[oRow][oColumn-2] = 'H';
						square[oRow+1][oColumn] = '|';
						square[oRow+2][oColumn] = 'H';
					}
					else if (state == 14)
					{
						square[oRow+1][oColumn] = '|';
						square[oRow+2][oColumn] = 'H';
						square[oRow][oColumn+1] = '-';
						square[oRow][oColumn+2] = 'H';
					}
				}
			}

			System.out.println("Case " + caseNumber + ":");
			System.out.println("");

			// print header
			for (int i = 0; i < square[0].length + 2; ++i)
			{
				System.out.print("*");
			}
			System.out.println("");

			// print body
			for (int i = 0; i < square.length; ++i)
			{
				System.out.print("*");
				for (int j = 0; j < square[i].length; ++j)
					System.out.print(square[i][j]);
				System.out.println("*");
			}

			// print footer
			for (int i = 0; i < square[0].length + 2; ++i)
			{
				System.out.print("*");
			}
			System.out.println("");
		}
	}
}

class Pair<KeyType, ValueType>
{
	public KeyType key;
	public ValueType value;

	public Pair(KeyType key, ValueType value)
	{
		this.key = key;
		this.value = value;
	}
}
