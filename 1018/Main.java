import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class Main
{
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		int testCases = sc.nextInt();
		for (int c = 0; c < testCases; ++c)
		{
			int totalDevices = sc.nextInt();

			Vector<Vector<Device>> devices = new Vector<Vector<Device>>();
			HashSet<Integer> bandwidths = new HashSet<Integer>();

			for (int d = 0; d < totalDevices; ++d)
			{
				int totalManufacturers = sc.nextInt();

				Vector<Device> manufacturerDevices = new Vector<Device>();
				for (int m = 0; m < totalManufacturers; ++m)
				{
					int bandwidth = sc.nextInt();
					int price = sc.nextInt();

					Device device = new Device();
					device.bandwidth = bandwidth;
					device.price = price;

					bandwidths.add(bandwidth);

					manufacturerDevices.add(device);
				}

				Collections.sort(manufacturerDevices, new Device());
				devices.add(manufacturerDevices);
			}

			double maxBandwidthPriceRatio = 0;

			Iterator<Integer> bandwidthsIterator = bandwidths.iterator();
			while (bandwidthsIterator.hasNext())
			{
				int bandwidth = bandwidthsIterator.next();
				int totalPrice = 0;

				boolean isValidBandwidth = true;
				for (int d = 0; d < devices.size(); ++d)
				{
					Vector<Device> manufacturerDevices = devices.get(d);

					int lowestPrice = 99999;
					boolean deviceFound = false;
					for (int md = 0; md < manufacturerDevices.size(); ++md)
					{
						if (manufacturerDevices.get(md).bandwidth >= bandwidth)
						{
							int price = manufacturerDevices.get(md).price;
							if (price < lowestPrice)
							{
								lowestPrice = price;
								deviceFound = true;
							}
						}
					}

					if (deviceFound == false)
					{
						// no valid device found
						isValidBandwidth = false;
						break;
					}

					totalPrice += lowestPrice;
				}

				if (isValidBandwidth == false)
					continue;

				double bandwidthPriceRatio = ((double)bandwidth) / totalPrice;
				if (bandwidthPriceRatio > maxBandwidthPriceRatio)
				{
					maxBandwidthPriceRatio = bandwidthPriceRatio;
				}
			}

			System.out.println(String.format("%.3f", maxBandwidthPriceRatio));
		}
	}
}

class Device implements Comparator<Device>
{
	public int bandwidth;
	public int price;

	public int compare(Device a, Device b)
	{
		if (a.bandwidth < b.bandwidth)
			return -1;
		if (a.bandwidth > b.bandwidth)
			return 1;
		return 0;
	}

	public String toString()
	{
		return "b:" + this.bandwidth + "," + "p:" + this.price;
	}
}
