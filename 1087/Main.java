import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Set;
import java.util.Enumeration;

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

		Hashtable<String, Hashtable<String, Boolean>> adaptersMap = new Hashtable<String, Hashtable<String, Boolean>>();
		for (int i = 0; i < adapters.size(); ++i)
		{
			Pair adapter = adapters.get(i);

			if (!adaptersMap.containsKey(adapter.key))
			{
				adaptersMap.put(adapter.key, new Hashtable<String, Boolean>());
			}

			Hashtable<String, Boolean> map = adaptersMap.get(adapter.key);
			map.put(adapter.value, true);
		}


		for (int k = 0; k < adapters.size(); ++k)
		{
			for (int i = 0; i < adapters.size(); ++i)
			{
				for (int j = 0; j < adapters.size(); ++j)
				{
					if (adapters.get(i).value.equals(adapters.get(j).key))
					{
						if (!adaptersMap.containsKey(adapters.get(i).key))
						{
							adaptersMap.put(adapters.get(i).key, new Hashtable<String, Boolean>());
						}

						Hashtable<String, Boolean> map = adaptersMap.get(adapters.get(i).key);
						map.put(adapters.get(j).value, true);
					}
				}
			}
		}

		/*
		for (int depth = 0; depth <= adapters.size(); depth++)
		{
			for (int i = 0; i < devices.size(); ++i)
			{
				Pair device = devices.get(i);
				if (device.isUsed)
					continue;

				Vector<Pair> adapterSequence = new Vector<Pair>();

				findTraverseWithDepth(device, device.value, depth, adapterSequence, 0, receptacles, adapters);
			}
		}

		int remainingDevices = 0;
		for (int i = 0; i < devices.size(); ++i)
		{
			if (devices.get(i).isUsed == false)
				remainingDevices++;
		}
		*/

		int maxPluggedDevices = findMaxPluggedDevices(devices, 0, receptacles, adapters, adaptersMap);

		int remainingDevices = devices.size() - maxPluggedDevices;

		System.out.println(remainingDevices);
	}

	private static int findMaxPluggedDevices(Vector<Pair> devices, int deviceIndex, Vector<Receptacle> receptacles, Vector<Pair> adapters, Hashtable<String, Hashtable<String, Boolean>> adaptersMap)
	{
		if (deviceIndex >= devices.size())
			return 0;

		Pair device = devices.get(deviceIndex);
		String deviceType = device.value;
		String pluginType = deviceType;
		int maxPluggedDevices = 0;

		Vector<String> nextTypes = new Vector<String>();
		if (adaptersMap.get(pluginType) != null)
		{
			Enumeration<String> matchedAdapters = adaptersMap.get(deviceType).keys();
			while (matchedAdapters.hasMoreElements())
			{
				String nextType = matchedAdapters.nextElement();

				nextTypes.add(nextType);
			}
		}

		// if device is plugged in into receptacle
		for (int i = 0; i < receptacles.size(); ++i)
		{
			Receptacle receptacle = receptacles.get(i);
			if (receptacle.isUsed)
				continue;

			if (!receptacle.type.equals(deviceType))
				continue;

			device.isUsed = true;
			receptacle.isUsed = true;
			int pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters, adaptersMap) + 1;
			if (maxPluggedDevices < pluggedDevices)
				maxPluggedDevices = pluggedDevices;

			device.isUsed = false;
			receptacle.isUsed = false;
		}

		// if device is plugged in into adapters and finally into one receptacle
		for (int i = 0; i < receptacles.size(); ++i)
		{
			Receptacle receptacle = receptacles.get(i);
			if (receptacle.isUsed)
				continue;

			boolean matchesReceptacle = false;
			for (int j = 0; j < nextTypes.size(); ++j)
			{
				if (nextTypes.get(j).equals(receptacle.type))
				{
					matchesReceptacle = true;
					break;
				}
			}

			if (matchesReceptacle == false)
				continue;

			receptacle.isUsed = true;
			device.isUsed = true;
			int pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters, adaptersMap) + 1;
			if (pluggedDevices > maxPluggedDevices)
				maxPluggedDevices = pluggedDevices;

			receptacle.isUsed = false;
			device.isUsed = false;
		}

		// if device is not plugged in at all
		int pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters, adaptersMap);
		if (maxPluggedDevices < pluggedDevices)
			maxPluggedDevices = pluggedDevices;

		return maxPluggedDevices;
	}

	private static int findMaxPluggedDevicesByAdapters(String pluginType, Vector<Pair> devices, int deviceIndex, Vector<Receptacle> receptacles, Vector<Pair> adapters, Hashtable<String, Hashtable<String, Boolean>> adaptersMap)
	{
		int maxPluggedDevices = 0;
		Pair device = devices.get(deviceIndex);



		return maxPluggedDevices;
	}

	private static void findTraverseWithDepth(Pair device, String plugType, int expectedDepth, Vector<Pair> adapterSequence, int currentDepth, Vector<Receptacle> receptacles, Vector<Pair> adapters)
	{
		Pair lastAdapter = null;
		if (adapterSequence.size() != 0)
		{
			lastAdapter = adapterSequence.get(adapterSequence.size() - 1);
		}

		if (device.isUsed)
			return;

		if (currentDepth == expectedDepth)
		{
			currentDepth = currentDepth;
			for (int i = 0; i < receptacles.size(); ++i)
			{
				Receptacle receptacle = receptacles.get(i);
				if (receptacle.isUsed)
					continue;

				if (receptacle.type.equals(plugType))
				{
					device.isUsed = true;
					receptacle.isUsed = true;
					for (int j = 0; j < adapterSequence.size(); ++j)
					{
						adapterSequence.get(j).isUsed = true;
					}

					return;
				}
			}
			return;
		}

		Vector<Pair> nextAdapters = new Vector<Pair>();
		for (int i = 0; i < adapters.size(); ++i)
		{
			if (adapters.get(i).isUsed)
				continue;

			// if used in current adapterSequence, continue
			boolean isUsedInAdapterSequence = false;
			for (int j = 0; j < adapterSequence.size(); ++j)
			{
				if (adapters.get(i) == adapterSequence.get(j))
				{
					isUsedInAdapterSequence = true;
					break;
				}
			}

			if (isUsedInAdapterSequence)
				continue;

			if (adapters.get(i).key.equals(plugType))
				nextAdapters.add(adapters.get(i));
		}

		for (int i = 0; i < nextAdapters.size(); ++i)
		{
			if (nextAdapters.get(i).isUsed)
				continue;

			adapterSequence.add(nextAdapters.get(i));
			findTraverseWithDepth(device, nextAdapters.get(i).value, expectedDepth, adapterSequence, currentDepth+1, receptacles, adapters);
			adapterSequence.remove(adapterSequence.size() - 1);

			if (device.isUsed)
				return;
		}
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


