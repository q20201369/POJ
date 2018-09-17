import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;

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

			Vector<Integer> finalJury = selectJury(prosecutionGrade, defenceGrade, m);

			System.out.println("Jury #" + juryId);

			int totalProsecutionGrade = 0;
			int totalDefenceGrade = 0;
			for (int i = 0; i < finalJury.size(); ++i)
			{
				totalProsecutionGrade += prosecutionGrade[finalJury.get(i)];
				totalDefenceGrade += defenceGrade[finalJury.get(i)];
			}

			System.out.println("Best jury has value " + totalProsecutionGrade + " for prosecution and value " + totalDefenceGrade + " for defence:");
			for (int i = 0; i < finalJury.size(); ++i)
			{
				System.out.print(" " + (finalJury.get(i) + 1));
			}
			System.out.println("");

			juryId++;
		}
	}

	public static Vector<Integer> selectJury(int[] prosecutionGrade, int[] defenceGrade, int m)
	{
		int n = prosecutionGrade.length;

		Vector<Integer> selection = new Vector<Integer>();

		Jury[] deltaGrade = new Jury[prosecutionGrade.length];
		for (int i = 0; i < prosecutionGrade.length; ++i)
		{
			Jury jury = new Jury();
			jury.delta = prosecutionGrade[i] - defenceGrade[i];
			jury.sum = prosecutionGrade[i] + defenceGrade[i];
			jury.id = i;

			deltaGrade[i] = jury;
		}

		Arrays.sort(deltaGrade, new Jury());

		int initialSumDelta = 0;
		int initialSumOfSum = 0;
		for (int i = 0; i < m; ++i)
		{
			initialSumDelta += deltaGrade[i].delta;
			initialSumOfSum += deltaGrade[i].sum;
		}

		JurySelection[] jurySelections = new JurySelection[n-m+1];
		jurySelections[0] = new JurySelection();
		jurySelections[0].sumOfDelta = initialSumDelta;
		jurySelections[0].sumOfSum = initialSumOfSum;
		for (int i = 0; i < m; ++i)
		{
			jurySelections[0].juries.add(deltaGrade[i].id);
		}
		
		for (int i = 1; i < n-m+1; ++i)
		{
			jurySelections[i] = new JurySelection();
			jurySelections[i].sumOfDelta = jurySelections[i-1].sumOfDelta + deltaGrade[i].delta - deltaGrade[i-1].delta;
			jurySelections[i].sumOfSum = jurySelections[i-1].sumOfSum + deltaGrade[i].sum - deltaGrade[i-1].sum;
			for (int j = i; j < i+m; ++j)
			{
				jurySelections[i].juries.add(deltaGrade[j].id);
			}
		}

		Arrays.sort(jurySelections, new JurySelection());

		/*
		for (int i = 0; i < m; ++i)
			selection.add(jurySelections[0].juries.get(i));
			*/
		selection.addAll(jurySelections[0].juries);

		/*
		int lastDelta = deltaGrade[m-1].delta;
		int extendedM = m;

		// extend
		for (int i = m; i < prosecutionGrade.length; ++i)
		{
			if (deltaGrade[i].delta != lastDelta)
			{
				extendedM = i + 1;
				break;
			}
		}
		*/

		return selection;
	}
}

class Jury implements Comparator<Jury>
{
	public int delta;
	public int sum;
	public int id;

	public int compare(Jury a, Jury b)
	{
		if (a.delta < b.delta)
		{
			return -1;
		}
		else if (a.delta > b.delta)
		{
			return 1;
		}
		else
		{
			if (a.sum > b.sum)
			{
				return -1;
			}
			else if (a.sum < b.sum)
			{
				return 1;
			}

			return 0;
		}
	}

	public boolean equals(Jury a)
	{
		if (a.id == this.id)
			return true;

		return false;
	}
}

class JurySelection implements Comparator<JurySelection>
{
	public Vector<Integer> juries = new Vector<Integer>();
	public int sumOfDelta;
	public int sumOfSum;

	public int compare(JurySelection a, JurySelection b)
	{
		int absDeltaOfA = Math.abs(a.sumOfDelta);
		int absDeltaOfB = Math.abs(b.sumOfDelta);

		if (absDeltaOfA < absDeltaOfB)
		{
			return -1;
		}
		else if (absDeltaOfA > absDeltaOfB)
		{
			return 1;
		}
		else
		{
			if (a.sumOfSum > b.sumOfSum)
			{
				return -1;
			}
			else if (a.sumOfSum < b.sumOfSum)
			{
				return 1;
			}

			return 0;
		}
	}
}

