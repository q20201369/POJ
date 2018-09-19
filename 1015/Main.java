import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

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

		Vector<Jury> allJuries = new Vector<Jury>();
		for (int i = 0; i < prosecutionGrade.length; ++i)
		{
			Jury jury = new Jury();
			jury.prosecutionGrade = prosecutionGrade[i];
			jury.defenceGrade = defenceGrade[i];
			jury.delta = prosecutionGrade[i] - defenceGrade[i];
			jury.sum = prosecutionGrade[i] + defenceGrade[i];
			jury.id = i;

			allJuries.add(jury);
		}

		Collections.sort(allJuries, new Jury());

		HashMap<Integer, Vector<Jury>> deltaMap = new HashMap<Integer, Vector<Jury>>();
		for (int i = 0; i < allJuries.size(); ++i)
		{
			int key = allJuries.get(i).delta;
			if (deltaMap.containsKey(key))
			{
				Vector<Jury> juries = deltaMap.get(key);
				juries.add(allJuries.get(i));
			}
			else
			{
				Vector<Jury> juries = new Vector<Jury>();
				juries.add(allJuries.get(i));

				deltaMap.put(key, juries);
			}
		}

		Vector<JurySelection> jurySelections = new Vector<JurySelection>();

		Vector<Jury> tempJuries = new Vector<Jury>();
		tempJuries.addAll(allJuries);

		Vector<Jury> juriesWithMinDelta = new Vector<Jury>();

		Vector<Vector<Jury>> juriesSelected = findJuries(allJuries, m);
		for (int i = 0; i < juriesSelected.size(); ++i)
		{
			JurySelection selected = new JurySelection();
			selected.juries.addAll(juriesSelected.get(i));
			// FIXME: calculate other fields of jury selection
			int sumOfDelta = 0;
			int sumOfSum = 0;
			for (int j = 0; j < selected.juries.size(); ++j)
			{
				sumOfDelta += selected.juries.get(j).delta;
				sumOfSum += selected.juries.get(j).sum;
			}
			selected.sumOfDelta = sumOfDelta;
			selected.sumOfSum = sumOfSum;

			jurySelections.add(selected);
		}

		/*
		for (int i = 0; i < n-m+1; ++i)
		{
			JurySelection currentSelection = new JurySelection();

			if (i == 0)
			{
				int deltaSum = 0;
				int sumOfSum = 0;

				for (int j = 0; j < m; ++j)
				{
					deltaSum += allJuries.get(j).delta;
					sumOfSum += allJuries.get(j).sum;
				}

				currentSelection.sumOfDelta = deltaSum;
				currentSelection.sumOfSum = sumOfSum;
			}
			else
			{
				JurySelection lastSelection = jurySelections.get(i-1);
				currentSelection.sumOfDelta = lastSelection.sumOfDelta + allJuries.get(i+m-1).delta - allJuries.get(i-1).delta;
				currentSelection.sumOfSum = lastSelection.sumOfSum + allJuries.get(i+m-1).sum - allJuries.get(i-1).sum;
			}

			for (int j = i; j < i+m; ++j)
				currentSelection.juries.add(allJuries.get(j));

			jurySelections.set(i, currentSelection);
		}
		*/

		Collections.sort(jurySelections, new JurySelection());

		// expand selections w/ selections w/ the same delta
		// e.g. expand {delta:1, juries:[1 2]} to {delta:1, juries:[1 3]} if jury #3 has the same delta as #2
		Vector<JurySelection> sameDeltaJurySelections = new Vector<JurySelection>();
		for (int i = 0; i < jurySelections.size(); ++i)
		{
			if (Math.abs(jurySelections.get(i).sumOfDelta) == Math.abs(jurySelections.get(0).sumOfDelta))
			{
				sameDeltaJurySelections.add(jurySelections.get(i));

				Vector<Jury> juries = jurySelections.get(i).juries;

				for (int j = 0; j < juries.size(); ++j)
				{
					Vector<Jury> sameDeltaJuries = deltaMap.get(juries.get(j).delta);
					if (sameDeltaJuries == null)
						continue;

					for (int k = 0; k < sameDeltaJuries.size(); ++k)
					{
						// remove the same Jury used in the base jury selection
						if (sameDeltaJuries.get(k).id == juries.get(j).id)
							continue;

						Jury newJury = sameDeltaJuries.get(k);
						Vector<Jury> newJuries = new Vector<Jury>();
						for (int x = 0; x < j; ++x)
						{
							newJuries.add(juries.get(x));
						}
						newJuries.add(newJury);
						for (int x = j+1; x < m; ++x)
						{
							newJuries.add(juries.get(x));
						}

						JurySelection newJurySelection = new JurySelection();
						newJurySelection.sumOfDelta = jurySelections.get(i).sumOfDelta;
						newJurySelection.sumOfSum = jurySelections.get(i).sumOfSum + newJury.sum - juries.get(j).sum;
						newJurySelection.juries = newJuries;

						boolean hasDup = false;
						HashSet<Integer> juriesIdSet = new HashSet<Integer>();
						for (int x = 0; x < m; ++x)
						{
							int id = newJuries.get(x).id;
							if (juriesIdSet.contains(id))
							{
								hasDup = true;
								break;
							}

							juriesIdSet.add(id);
						}

						if (!hasDup)
						{
							sameDeltaJurySelections.add(newJurySelection);
						}
					}
				}
			}
		}

		Collections.sort(sameDeltaJurySelections, new JurySelection());

		JurySelection finalSelection = sameDeltaJurySelections.get(0);

		Vector<Integer> finalJuries = new Vector<Integer>();
		for (int i = 0; i < finalSelection.juries.size(); ++i)
		{
			finalJuries.add(finalSelection.juries.get(i).id);
		}
		Collections.sort(finalJuries);

		selection.addAll(finalJuries);

		/*
		int lastDelta = allJuries.get(m-1).delta;
		int extendedM = m;

		// extend
		for (int i = m; i < prosecutionGrade.length; ++i)
		{
			if (allJuries.get(i).delta != lastDelta)
			{
				extendedM = i + 1;
				break;
			}
		}
		*/

		return selection;
	}

	/*
	public Vector<JurySelection> findJurySelectionsWithMinimalDelta(int m, Vector<Jury> allJuries)
	{
		while (allJuries.size() > 1)
		{
			int minSum = Integer.MAX_VALUE;
			int jWithMinSum = 0;
			int kWithMinSum = 0;
			for (int j = 0; j < tempJuries.size() - 1; ++j)
			{
				for (int k = j+1; k < tempJuries(); ++k)
				{
					int sum = tempJuries.get(j) + tempJuries.get(k);
					if (sum < minSum)
					{
						minSum = sum;
						jWithMinSum = j;
						kWithMinSum = k;
					}
				}
			}

			juriesWithMinDelta.add(tempJuries.get(jWithMinSum));
			juriesWithMinDelta.add(tempJuries.get(kWithMinSum));
			if (jWithMinSum < kWithMinSum)
			{
				tempJuries.remove(kWithMinSum);
				tempJuries.remove(jWithMinSum);
			}
			else
			{
				tempJuries.remove(jWithMinSum);
				tempJuries.remove(kWithMinSum);
			}

			Jury fakeJury = new Jury();
			jury.id = -1;
			jury.delta = minSum;
		}

		for (int i = juriesWithMinDelta.size() - 1; i >= 0; --i)
		{
			if (juriesWithMinDelta.get(i).id < 0)
				juriesWithMinDelta.remove(i);
		}
	}
	*/

	public static Vector<Vector<Jury>> findJuries(Vector<Jury> allJuries, int m)
	{
		Vector<Vector<Jury>> resultJuries = new Vector<Vector<Jury>>();

		if (m <= 0)
		{
			resultJuries.add(new Vector<Jury>());
			return resultJuries;
		}

		if (m == 1)
		{
			int minDelta = Integer.MAX_VALUE;
			int iMin = -1;
			for (int i = 0; i < allJuries.size(); ++i)
			{
				if (allJuries.get(i).id >= 0 && allJuries.get(i).delta < minDelta)
				{
					minDelta = allJuries.get(i).delta;
					iMin = i;
				}
			}

			for (int i = 0; i < allJuries.size(); ++i)
			{
				if (allJuries.get(i).id >= 0 && allJuries.get(i).delta == minDelta)
				{
					Vector<Jury> tempJuries = new Vector<Jury>();
					tempJuries.add(allJuries.get(i));
					resultJuries.add(tempJuries);
				}
			}

			return resultJuries;
		}

		int standaloneJuries = 0;
		for (int i = 0; i < allJuries.size(); ++i)
		{
			if (allJuries.get(i).id >= 0)
				standaloneJuries++;
		}

		int minDeltaSum = Integer.MAX_VALUE;
		int iMin = 0;
		int jMin = 0;
		for (int i = 0; i < allJuries.size() - 1; ++i)
		{
			for (int j = i + 1; j < allJuries.size(); ++j)
			{
				int sum = allJuries.get(i).delta + allJuries.get(j).delta;
				sum = Math.abs(sum);
				if (sum < minDeltaSum)
				{
					minDeltaSum = sum;
					iMin = i;
					jMin = j;
				}
			}
		}

		Vector<Vector<Jury>> possibleJuries = new Vector<Vector<Jury>>();
		Vector<Vector<Jury>> possibleAddedJuries = new Vector<Vector<Jury>>();
		for (int i = 0; i < allJuries.size() - 1; ++i)
		{
			for (int j = i + 1; j < allJuries.size(); ++j)
			{
				int sumOfDelta = allJuries.get(i).delta + allJuries.get(j).delta;
				sumOfDelta = Math.abs(sumOfDelta);
				if (sumOfDelta == minDeltaSum)
				{
					Vector<Jury> juriesToBeAdded = new Vector<Jury>();
					if (allJuries.get(i).id >= 0)
						juriesToBeAdded.add(allJuries.get(i));
					if (allJuries.get(j).id >= 0)
						juriesToBeAdded.add(allJuries.get(j));
					possibleAddedJuries.add(juriesToBeAdded);

					Vector<Jury> newJuries = new Vector<Jury>();
					newJuries.addAll(allJuries);
					JuryPair juries = new JuryPair(allJuries.get(i), allJuries.get(j));
					newJuries.remove(j);
					newJuries.remove(i);
					newJuries.add(juries);

					possibleJuries.add(newJuries);
				}
			}
		}
		

		int maxSumOfSum = -1;
		for (int i = 0; i < possibleAddedJuries.size(); ++i)
		{
			Vector<Jury> juries = possibleAddedJuries.get(i);

			int sum = 0;
			for (int j = 0; j < juries.size(); ++j)
			{
				sum += juries.get(j).sum;
			}

			if (sum > maxSumOfSum)
			{
				maxSumOfSum = sum;
			}
		}

		Vector<Vector<Jury>> finalJuries = new Vector<Vector<Jury>>();
		Vector<Vector<Jury>> finalAddedJuries = new Vector<Vector<Jury>>();
		for (int i = 0; i < possibleAddedJuries.size(); ++i)
		{
			Vector<Jury> juries = possibleAddedJuries.get(i);

			int sum = 0;
			for (int j = 0; j < juries.size(); ++j)
			{
				sum += juries.get(j).sum;
			}

			if (sum == maxSumOfSum)
			{
				finalJuries.add(possibleJuries.get(i));
				finalAddedJuries.add(possibleAddedJuries.get(i));
			}
		}

		for (int i = 0; i < finalJuries.size(); ++i)
		{
			Vector<Jury> juries = finalJuries.get(i);
			int newStandaloneJuries = 0;
			for (int j = 0; j < juries.size(); ++j)
			{
				if (juries.get(j).id >= 0)
					newStandaloneJuries++;
			}

			Vector<Vector<Jury>> returnedJuries = findJuries(juries, m-(standaloneJuries-newStandaloneJuries));
			for (int j = 0; j < returnedJuries.size(); ++j)
			{
				Vector<Jury> combinedJuries = new Vector<Jury>();
				combinedJuries.addAll(finalAddedJuries.get(i));
				combinedJuries.addAll(returnedJuries.get(j));
				resultJuries.add(combinedJuries);
			}
		}

		return resultJuries;
	}
}

class Jury implements Comparator<Jury>
{
	public int prosecutionGrade;
	public int defenceGrade;
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

	public String toString()
	{
		return "{"
			+ "id:" + this.id
			+ " "
			+ "pg:" + this.prosecutionGrade
			+ " "
			+ "dg:" + this.defenceGrade
			+ " "
			+ "delta:" + this.delta
			+ " "
			+ "sum:" + this.sum
			+ "}";
	}
}

class JurySelection implements Comparator<JurySelection>
{
	public Vector<Jury> juries = new Vector<Jury>();
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

	public String toString()
	{
		String str = "(";
		str += "sumOfDelta: " + this.sumOfDelta;
		str += " ";
		str += "sumOfSum: " + this.sumOfSum;
		str += " ";
		for (int i = 0; i < juries.size(); ++i)
		{
			str += juries.get(i) + " ";
		}
		str += ")";
		return str;
	}
}

class JuryPair extends Jury
{
	public Jury left;
	public Jury right;

	public JuryPair(Jury left, Jury right)
	{
		this.id = -1;
		this.left = left;
		this.right = right;
		this.delta = this.left.delta + this.right.delta;
		this.sum = this.left.sum + this.right.sum;
	}

	public String toString()
	{
		return "{"
			+ "delta:" + this.delta + " "
			+ "sum:" + this.sum + " "
			+ "(" + this.left.toString() + " | " + this.right.toString() + ")"
			+ "}";
	}
}


