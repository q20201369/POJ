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

		while (true)
		{
			boolean allZeros = true;

			int[] packetsPerType = new int[6];
			for (int i = 0; i < packetsPerType.length; ++i)
			{
				packetsPerType[i] = sc.nextInt();
				if (packetsPerType[i] != 0)
					allZeros = false;
			}

			if (allZeros)
				break;

			int totalSize = 0;
			for (int i = 0; i < packetsPerType.length; ++i)
			{
				int width = i + 1;
				int size = width * width;
				totalSize += packetsPerType[i] * size;
			}

			int parcelSize = 6 * 6;

			int totalParcels = 0;

			// for 6*6 packet
			totalParcels += packetsPerType[5];
			packetsPerType[5] = 0;

			// for 5*5 packet
			totalParcels += packetsPerType[4];
			int maxOneSizedPacket = packetsPerType[4] * 11;
			if (packetsPerType[0] > maxOneSizedPacket)
				packetsPerType[0] -= maxOneSizedPacket;
			else
				packetsPerType[0] = 0;
			packetsPerType[4] = 0;

			// for 4*4 packet
			{
				totalParcels += packetsPerType[3];
				int maxTwoSizedPackets = packetsPerType[3] * 5;
				if (packetsPerType[1] > maxTwoSizedPackets)
				{
					packetsPerType[1] -= maxTwoSizedPackets;
				}
				else
				{
					maxTwoSizedPackets -= packetsPerType[1];
					packetsPerType[1] = 0;

					// fill remaining two sized spaces w/ one sized packets
					int remainingOneSized = maxTwoSizedPackets * 4;
					if (packetsPerType[0] > remainingOneSized)
					{
						packetsPerType[0] -= remainingOneSized;
					}
					else
					{
						packetsPerType[0] = 0;
					}
				}
				packetsPerType[3] = 0;
			}

			{
				// deal with 3*3 sized
				totalParcels += Math.ceil(packetsPerType[2] / 4.0);
				int remainingThreeSized = (4 - packetsPerType[2] % 4) % 4;
				int remainingTwoSized = 0;
				int remainingOneSized = 0;
				if (remainingThreeSized == 1)
				{
					remainingTwoSized = 1;
					remainingOneSized = 5;
				}
				else if (remainingThreeSized == 2)
				{
					remainingTwoSized = 3;
					remainingOneSized = 6;
				}
				else if (remainingThreeSized == 3)
				{
					remainingTwoSized = 5;
					remainingOneSized = 7;
				}

				if (packetsPerType[1] >= remainingTwoSized)
				{
					packetsPerType[1] -= remainingTwoSized;
					if (packetsPerType[0] >= remainingOneSized)
					{
						packetsPerType[0] -= remainingOneSized;
					}
					else
					{
						packetsPerType[0] = 0;
					}
				}
				else
				{
					remainingOneSized = (remainingTwoSized - packetsPerType[1]) * 4 + remainingOneSized;
					packetsPerType[1] = 0;

					if (packetsPerType[0] >= remainingOneSized)
					{
						packetsPerType[0] -= remainingOneSized;
					}
					else
					{
						packetsPerType[0] = 0;
					}
				}

				packetsPerType[2] = 0;
			}

			{
				// deal with 2*2 sized
				totalParcels += Math.ceil(packetsPerType[1] / 9.0);
				int remainingTwoSized = (9 - packetsPerType[1] % 9) % 9;
				int remainingOneSized = remainingTwoSized * 4;
				if (packetsPerType[0] >= remainingOneSized)
				{
					packetsPerType[0] -= remainingOneSized;
				}
				else
				{
					packetsPerType[0] = 0;
				}
			}

			{
				// deal with 1*1 sized
				totalParcels += Math.ceil(packetsPerType[0] / 36.0);
			}

			System.out.println(totalParcels);
		}
	}
}
