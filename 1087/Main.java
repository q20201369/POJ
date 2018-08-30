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

		Vector<Receptacle> receptacles = new Vector<Receptacle>();
		int numOfReceptacles = sc.nextInt();
		for (int i = 0; i < numOfReceptacles; ++i)
		{
			receptacles.add(new Receptacle(sc.next()));
		}

		Vector<Pair> devices = new Vector<Pair>();
		int numOfDevices = sc.nextInt();
		for (int i = 0; i < numOfDevices; ++i)
		{
			String deviceName = sc.next();
			String devicePlugType = sc.next();
			Pair pair = new Pair(deviceName, devicePlugType);
			devices.add(pair);
		}

		Vector<Pair> adapters = new Vector<Pair>();
		int numOfAdapters = sc.nextInt();
		for (int i = 0; i < numOfAdapters; ++i)
		{
			String adapterReceptacleType = sc.next();
			String adapterPlugType = sc.next();

			adapters.add(new Pair(adapterReceptacleType, adapterPlugType));
		}

		for (int i = 0; i < devices.size(); ++i)
		{
			Pair device = devices.get(i);
			if (device.isUsed)
				continue;

			for (int j = 0; j < receptacles.size(); ++j)
			{
				Receptacle receptacle = receptacles.get(j);
				if (receptacle.isUsed)
					continue;

				if (device.value.equals(receptacle.type))
				{
					device.isUsed = true;
					receptacle.isUsed = true;
					break;
				}
			}
		}

		int remainingDevices = 0;
		for (int i = 0; i < devices.size(); ++i)
		{
			if (devices.get(i).isUsed == false)
				remainingDevices++;
		}

		System.out.println(remainingDevices);
	}
}

class Pair
{
	public Pair(String key, String value)
	{
		this.key = key;
		this.value = value;
	}

	public String key;
	public String value;
	public boolean isUsed;
}

class Receptacle
{
	public Receptacle(String type)
	{
		this.type = type;
	}

	public String type;
	public boolean isUsed;
}


