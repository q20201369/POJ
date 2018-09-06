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

		int maxPluggedDevices = findMaxPluggedDevices(devices, 0, receptacles, adapters);

		int remainingDevices = devices.size() - maxPluggedDevices;

		System.out.println(remainingDevices);
	}

	private static int findMaxPluggedDevices(Vector<Pair> devices, int deviceIndex, Vector<Receptacle> receptacles, Vector<Pair> adapters)
	{
		if (deviceIndex >= devices.size())
			return 0;

		Pair device = devices.get(deviceIndex);
		String deviceType = device.value;
		int maxPluggedDevices = 0;

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
			int pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters) + 1;
			if (maxPluggedDevices < pluggedDevices)
				maxPluggedDevices = pluggedDevices;

			device.isUsed = false;
			receptacle.isUsed = false;
		}

		// if device is plugged in into adapters and finally into one receptacle
		int pluggedDevices = findMaxPluggedDevicesByAdapters(deviceType, devices, deviceIndex, receptacles, adapters);
		if (maxPluggedDevices < pluggedDevices)
			maxPluggedDevices = pluggedDevices;

		// if device is not plugged in at all
		pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters);
		if (maxPluggedDevices < pluggedDevices)
			maxPluggedDevices = pluggedDevices;

		return maxPluggedDevices;
	}

	private static int findMaxPluggedDevicesByAdapters(String pluginType, Vector<Pair> devices, int deviceIndex, Vector<Receptacle> receptacles, Vector<Pair> adapters)
	{
		int maxPluggedDevices = 0;
		Pair device = devices.get(deviceIndex);

		for (int i = 0; i < receptacles.size(); ++i)
		{
			Receptacle receptacle = receptacles.get(i);
			if (receptacle.isUsed)
				continue;

			if (!pluginType.equals(receptacle.type))
				continue;

			receptacle.isUsed = true;
			device.isUsed = true;
			int pluggedDevices = findMaxPluggedDevices(devices, deviceIndex + 1, receptacles, adapters) + 1;
			if (pluggedDevices > maxPluggedDevices)
				maxPluggedDevices = pluggedDevices;

			receptacle.isUsed = false;
			device.isUsed = false;
		}

		for (int i = 0; i < adapters.size(); ++i)
		{
			Pair adapter = adapters.get(i);
			if (adapter.isUsed)
				continue;

			if (adapter.key.equals(pluginType))
			{
				adapter.isUsed = true;
				int pluggedDevices = findMaxPluggedDevicesByAdapters(adapter.value, devices, deviceIndex, receptacles, adapters);
				if (pluggedDevices > maxPluggedDevices)
					maxPluggedDevices = pluggedDevices;
				adapter.isUsed = false;
			}
		}

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


