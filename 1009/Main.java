import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

public class Main
{
	private static boolean isDebug = false;

	public static void main(String[] args)
	{
		if (args.length > 0 && "-d".equals(args[0]))
			isDebug = true;

		Scanner sc = new Scanner(System.in);

		while (true)
		{
			int width = sc.nextInt();
			if (width == 0)
				break;

			Image image = parseImage(sc, width);

			System.out.println(width);

			Vector<Block> blocks = new Vector<Block>();
			Block currentBlock = new Block(width);
			int rowSize = 0;
			while (image.rleData.size() > 0)
			{
				RunLengthEncoding data = image.rleData.removeFirst();

				// fill the current row first
				if (rowSize > 0)
				{
					if (data.length + rowSize < width)
					{
						currentBlock.row.add(new RunLengthEncoding(data.pixel, data.length));
						rowSize += data.length;
						data.length = 0;
					}
					else
					{
						int pixelsToFillInCurrentRow = width - rowSize;
						currentBlock.row.add(new RunLengthEncoding(data.pixel, pixelsToFillInCurrentRow));
						blocks.add(currentBlock);

						data.length -= pixelsToFillInCurrentRow;
						currentBlock = new Block(width);
						rowSize = 0;
					}
				}

				if (data.length >= width)
				{
					currentBlock.row.add(new RunLengthEncoding(data.pixel, width));
					currentBlock.rowCount = data.length / width;
					blocks.add(currentBlock);

					data.length -= (data.length / width) * width;
					currentBlock = new Block(width);
					rowSize = 0;
				}

				if (data.length > 0)
				{
					int remainingWidth = data.length;
					currentBlock.row.add(new RunLengthEncoding(data.pixel, remainingWidth));

					rowSize += remainingWidth;
				}
			}

			if (isDebug)
			{
				System.out.println("--blocks---------");
				for (int i = 0; i < blocks.size(); ++i)
				{
					System.out.println(blocks.get(i).toString());
				}
			}

			// add fake blocks so that the calculation is simpler
			{
				Block fakeFirstBlock = blocks.get(0).clone();
				Block fakeLastBlock = blocks.get(blocks.size()-1).clone();

				blocks.add(0, fakeFirstBlock);
				blocks.add(fakeLastBlock);
			}

			LinkedList<RunLengthEncoding> edgeImage = new LinkedList<RunLengthEncoding>();
			for (int i = 1; i < blocks.size() - 1; ++i)
			{
				Block thisBlock = blocks.get(i);
				Block nextBlock = blocks.get(i+1);
				Block lastBlock = blocks.get(i-1);

				Vector<Integer> segments = thisBlock.getSegments();
				segments.addAll(lastBlock.getSegments());
				segments.addAll(nextBlock.getSegments());

				Integer[] sortedSegments = new HashSet<Integer>(segments).toArray(new Integer[0]);
				Arrays.sort(sortedSegments);

				thisBlock = thisBlock.split(sortedSegments);
				lastBlock = lastBlock.split(sortedSegments);
				nextBlock = nextBlock.split(sortedSegments);

				int rowCount = blocks.get(i).rowCount;

				if (rowCount == 1)
				{
					generateEdge(sortedSegments, edgeImage, lastBlock, thisBlock, nextBlock);
				}
				else
				{
					generateEdge(sortedSegments, edgeImage, lastBlock, thisBlock, thisBlock);

					rowCount--;

					if (rowCount > 1)
					{
						RunLengthEncoding thisRLE = thisBlock.getRLEByOffset(0);
						int thisPixel = thisRLE.pixel;
						edgeImage.add(new RunLengthEncoding(0, width * (rowCount-1)));
					}

					generateEdge(sortedSegments, edgeImage, thisBlock, thisBlock, nextBlock);
				}
			}

			if (isDebug)
			{
				System.out.println("---edgeImage--------");
				for (int i = 0; i < edgeImage.size(); ++i)
				{
					System.out.println(edgeImage.get(i).toString());
				}
			}

			RunLengthEncoding lastRLE = edgeImage.getFirst();
			for (int i = 1; i < edgeImage.size(); ++i)
			{
				RunLengthEncoding rle = edgeImage.get(i);
				if (rle.pixel == lastRLE.pixel)
				{
					lastRLE.length += rle.length;
					continue;
				}
				else
				{
					System.out.println(lastRLE.toString());
					lastRLE = rle;
				}
			}
			System.out.println(lastRLE.toString());

			System.out.println("0 0");
		}
		System.out.println("0");
	}

	private static int findMaxDelta(Vector<Integer> pixels, int basePixel)
	{
		if (isDebug)
		{
			System.out.print("findMaxDelta: ");
			for (int i = 0; i < pixels.size(); ++i)
			{
				System.out.print(pixels.get(i) + " ");
			}
			System.out.println("");
		}

		int maxDelta = 0;
		for (int i = 0; i < pixels.size(); ++i)
		{
			int delta = Math.abs(pixels.get(i) - basePixel);
			if (delta > maxDelta)
				maxDelta = delta;
		}

		return maxDelta;
	}

	private static Image parseImage(Scanner sc, int width)
	{
		Image image = new Image(width);

		while (true)
		{
			int pixel = sc.nextInt();
			int length = sc.nextInt();
			if (pixel == 0 && length == 0)
				break;

			image.addRunLengthEncodingData(new RunLengthEncoding(pixel, length));
		}

		return image;
	}

	private static void generateEdge(Integer[] sortedSegments, LinkedList<RunLengthEncoding> edgeImage, Block lastBlock, Block thisBlock, Block nextBlock)
	{
		if (isDebug)
		{
			System.out.println("lastBlock: " + lastBlock.toString());
			System.out.println("thisBlock: " + thisBlock.toString());
			System.out.println("nextBlock: " + nextBlock.toString());
		}

		for (int j = 0; j < sortedSegments.length; ++j)
		{
			if (isDebug)
				System.out.println("segment: " + j);

			Vector<Integer> pixels = new Vector<Integer>();
			Vector<Integer> leftPixels = new Vector<Integer>();
			Vector<Integer> rightPixels = new Vector<Integer>();

			int offset = sortedSegments[j];
			RunLengthEncoding thisRLE = thisBlock.getRLEByOffset(offset);
			int thisPixel = thisRLE.pixel;
			int length = thisRLE.length;

			int lastLeftPixel = lastBlock.getPixelByOffset(offset-1, thisPixel);
			int lastMiddlePixel = lastBlock.getPixelByOffset(offset, thisPixel);
			int lastRightPixel = lastBlock.getPixelByOffset(offset+length, thisPixel);

			int leftPixel = thisBlock.getPixelByOffset(offset-1, thisPixel);
			int rightPixel = thisBlock.getPixelByOffset(offset+length, thisPixel);

			int nextLeftPixel = nextBlock.getPixelByOffset(offset-1, thisPixel);
			int nextMiddlePixel = nextBlock.getPixelByOffset(offset, thisPixel);
			int nextRightPixel = nextBlock.getPixelByOffset(offset+length, thisPixel);

			int remainingLength = length;
			if (remainingLength > 0)
			{
				leftPixels.add(lastLeftPixel);
				leftPixels.add(lastMiddlePixel);
				if (length == 1)
					leftPixels.add(lastRightPixel);

				leftPixels.add(leftPixel);
				if (length == 1)
					leftPixels.add(rightPixel);

				leftPixels.add(nextLeftPixel);
				leftPixels.add(nextMiddlePixel);
				if (length == 1)
					leftPixels.add(nextRightPixel);

				int pixel = findMaxDelta(leftPixels, thisPixel);

				edgeImage.add(new RunLengthEncoding(pixel, 1));

				remainingLength--;

				if (isDebug)
					System.out.println("edge: " + pixel + " " + 1);
			}

			if (remainingLength > 1)
			{
				pixels.add(lastMiddlePixel);

				pixels.add(nextMiddlePixel);

				int pixel = findMaxDelta(pixels, thisPixel);
				edgeImage.add(new RunLengthEncoding(pixel, remainingLength-1));

				remainingLength = 1;

				if (isDebug)
					System.out.println("edge: " + pixel + " " + (remainingLength-1));
			}

			if (remainingLength > 0)
			{
				if (length == 1)
					rightPixels.add(lastLeftPixel);
				rightPixels.add(lastMiddlePixel);
				rightPixels.add(lastRightPixel);

				if (length == 1)
					rightPixels.add(leftPixel);
				rightPixels.add(rightPixel);

				if (length == 1)
					rightPixels.add(nextLeftPixel);
				rightPixels.add(nextMiddlePixel);
				rightPixels.add(nextRightPixel);

				int pixel = findMaxDelta(rightPixels, thisPixel);
				edgeImage.add(new RunLengthEncoding(pixel, 1));

				if (isDebug)
					System.out.println("edge: " + pixel + " " + 1);
			}
		}
	}
}

class RunLengthEncoding
{
	public RunLengthEncoding(int pixel, int length)
	{
		this.pixel = pixel;
		this.length = length;
	}

	public int pixel;
	public int length;

	public String toString()
	{
		return this.pixel + " " + this.length;
	}
}

class Block
{
	public Vector<RunLengthEncoding> row = new Vector<RunLengthEncoding>();
	public int rowCount = 1;
	public int width;

	public Block(int width)
	{
		this.width = width;
	}

	public String toString()
	{
		String str = new String();
		for (int i = 0; i < this.row.size(); ++i)
			str +=  "(" + this.row.get(i).toString() + ") ";
		if (this.rowCount > 1)
			str += "@ " + this.rowCount;

		return str;
	}

	public Vector<Integer> getSegments()
	{
		Vector<Integer> segments = new Vector<Integer>();
		int offset = 0;
		for (int j = 0; j < this.row.size(); ++j)
		{
			segments.add(offset);
			offset += this.row.get(j).length;
		}

		return segments;
	}

	public Block clone()
	{
		Block newBlock = new Block(this.width);
		for (int i = 0; i < this.row.size(); ++i)
		{
			newBlock.row.add(new RunLengthEncoding(this.row.get(i).pixel, this.row.get(i).length));
		}

		return newBlock;
	}

	public int getPixelByOffset(int offset, int basePixel)
	{
		RunLengthEncoding rle = this.getRLEByOffset(offset);
		if (rle == null)
			return basePixel;

		return rle.pixel;
	}

	public RunLengthEncoding getRLEByOffset(int offset)
	{
		if (offset < 0)
			return null;
		if (offset >= this.width)
			return null;

		int currentOffset = 0;
		for (int i = 0; i < this.row.size(); ++i)
		{
			if (offset >= currentOffset && offset < currentOffset + this.row.get(i).length)
				return this.row.get(i);

			currentOffset += this.row.get(i).length;
		}

		return null;
	}

	public Block split(Integer[] segments)
	{
		Block newBlock = new Block(this.width);
		for (int i = 0; i < segments.length; ++i)
		{
			int offset = segments[i];
			int nextOffset = (i == segments.length-1) ? this.width : segments[i+1];
			int length = nextOffset - offset;
			int pixel = this.getPixelByOffset(offset, 0);
			newBlock.row.add(new RunLengthEncoding(pixel, length));
		}

		return newBlock;
	}
}

class Image
{
	public LinkedList<RunLengthEncoding> rleData = new LinkedList<RunLengthEncoding>();
	public int width = 0;

	public Image(int width)
	{
		this.width = width;
	}

	public void addRunLengthEncodingData(RunLengthEncoding data)
	{
		int pixel = data.pixel;
		int length = data.length;

		if (length <= 0)
			return;

		this.rleData.addLast(data);

	}
}


