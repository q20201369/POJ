import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int juryId = 1;

		while (true)
		{
			int n = sc.nextInt();
			int m = sc.nextInt();

			if (n == 0 && m == 0)
				break;
			else if (juryId != 1)
				System.out.println("");

			int[] defenceGrade = new int[n];
			int[] prosecutionGrade = new int[n];
			for (int i = 0; i < n; ++i)
			{
				prosecutionGrade[i] = sc.nextInt();
				defenceGrade[i] = sc.nextInt();
			}

			Vector<Integer> finalSelection = selectJury(prosecutionGrade, defenceGrade, m);

			System.out.println("Jury #" + juryId);

			int totalProsecutionGrade = 0;
			int totalDefenceGrade = 0;
			for (int i = 0; i < finalSelection.size(); ++i)
			{
				totalProsecutionGrade += prosecutionGrade[finalSelection.get(i)-1];
				totalDefenceGrade += defenceGrade[juryId-1];
			}

			System.out.println("Best jury has value " + totalProsecutionGrade + " for prosecution and value " + totalDefenceGrade + " for defence:");
			for (int i = 0; i < finalSelection.size(); ++i)
			{
				System.out.print(" " + (finalSelection.get(i) + 1));
			}
			System.out.println("");

			juryId++;
		}
	}

	public static Vector<Integer> selectJury(int[] prosecutionGrade, int[] defenceGrade, int m)
	{
		Vector<Integer> selection = new Vector<Integer>();

		// FIXME: need to implement
		return selection;
	}
}

