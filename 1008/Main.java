import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		int inputs = sc.nextInt();
		System.out.println(inputs);

		Hashtable<String, Integer> haabMonthNameToIndex = new Hashtable<String, Integer>();
		haabMonthNameToIndex.put("pop",0);
		haabMonthNameToIndex.put("no",1);
		haabMonthNameToIndex.put("zip",2);
		haabMonthNameToIndex.put("zotz",3);
		haabMonthNameToIndex.put("tzec",4);
		haabMonthNameToIndex.put("xul",5);
		haabMonthNameToIndex.put("yoxkin",6);
		haabMonthNameToIndex.put("mol",7);
		haabMonthNameToIndex.put("chen",8);
		haabMonthNameToIndex.put("yax",9);
		haabMonthNameToIndex.put("zac",10);
		haabMonthNameToIndex.put("ceh",11);
		haabMonthNameToIndex.put("mac",12);
		haabMonthNameToIndex.put("kankin",13);
		haabMonthNameToIndex.put("muan",14);
		haabMonthNameToIndex.put("pax",15);
		haabMonthNameToIndex.put("koyab",16);
		haabMonthNameToIndex.put("cumhu",17);
		haabMonthNameToIndex.put("uayet",18);

		String[] tzolkinDayNames = new String[]
		{
			"imix", "ik", "akbal", "kan", "chicchan", "cimi", "manik", "lamat", "muluk", "ok", "chuen", "eb", "ben", "ix", "mem", "cib", "caban", "eznab", "canac", "ahau", "uayet"
		};

		for (int i = 0; i < inputs; ++i)
		{
			double haabDayDouble = sc.nextDouble();
			int haabDay = (int)Math.round(haabDayDouble);
			String haabMonthName = sc.next("[a-z]*");
			int haabYear = sc.nextInt();

			int haabMonth = haabMonthNameToIndex.get(haabMonthName);

			int dayFromWorldBeginning = haabYear * 365 + haabMonth * 20 + haabDay;
			int tzolkinYear = dayFromWorldBeginning / (13 * 20);
			int tzolkinDaysInYear = dayFromWorldBeginning % (13 * 20);
			int tzolkinPeriod = (tzolkinDaysInYear % 13) + 1;
			int tzolkinDay = tzolkinDaysInYear % 20;
			String tzolkinDayName = tzolkinDayNames[tzolkinDay];

			System.out.println("" + tzolkinPeriod + " " + tzolkinDayName + " " + tzolkinYear);
		}
	}
}
