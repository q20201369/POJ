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
	public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);

		while (true)
		{
			int width = sc.nextInt();
			if (width == 0)
				break;

			Image image = parseImage(sc, width);
			LinkedList<ImageRow> rowBuffer = new LinkedList<ImageRow>();

			System.out.println(width);

			ImageRow tempRow = new ImageRow(width);
			int tmpRowSize = 0;

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

				if (data.length > width)
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

			System.out.println("--blocks---------");
			for (int i = 0; i < blocks.size(); ++i)
			{
				System.out.println(blocks.get(i).toString());
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

			System.out.println("---edgeImage--------");
			for (int i = 0; i < edgeImage.size(); ++i)
			{
				System.out.println(edgeImage.get(i).toString());
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

			/*
			LinkedList<Block> segmentedBlocks = new LinkedList<Block>();
			for (int i = 0; i < blocks.size(); ++i)
			{
				Block thisBlock = blocks.get(i);
				Block nextBlock = null;
				Block lastBlock = null;
				if (i < blocks.size() -1)
					nextBlock = blocks.get(i+1);
				if (i > 0)
					lastBlock = blocks.get(i-1);

				Vector<Integer> segments = new Vector<Integer>();
				if (lastBlock != null)
				{
					int offset = 0;
					for (int j = 0; j < lastBlock.row.size(); ++j)
					{
						segments.add(offset);
						offset += lastBlock.row.get(j).length;
					}
				}
				if (nextBlock != null)
				{
					int offset = 0;
					for (int j = 0; j < nextBlock.row.size(); ++j)
					{
						segments.add(offset);
						offset += nextBlock.row.get(j).length;
					}
				}
				if (thisBlock != null)
				{
					int offset = 0;
					for (int j = 0; j < thisBlock.row.size(); ++j)
					{
						segments.add(offset);
						offset += thisBlock.row.get(j).length;
					}
				}

				Integer[] sortedSegments = new HashSet<Integer>(segments).toArray(new Integer[0]);
				Arrays.sort(sortedSegments);

				for (int j = 0; j < sortedSegments.length; ++j)
				{
					System.out.print(sortedSegments[j] + ",");
				}
				System.out.println("");

				if (thisBlock.rowCount > 1)
				{
					int rowCount = thisBlock.rowCount;
					if (lastBlock != null)
					{
						Block newBlock = new Block();
						for (int j = 1; j < sortedSegments.length; ++j)
						{
							newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, sortedSegments[j] - sortedSegments[j-1]));
						}
						newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, width - sortedSegments[sortedSegments.length-1]));

						segmentedBlocks.addLast(newBlock);
						rowCount--;
					}
					if (nextBlock != null)
					{
						Block newBlock = null;

						if (rowCount > 2)
						{
							newBlock = new Block();
							newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, width));
							newBlock.rowCount = rowCount - 1;
							segmentedBlocks.addLast(newBlock);
						}

						newBlock = new Block();
						for (int j = 1; j < sortedSegments.length; ++j)
						{
							newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, sortedSegments[j] - sortedSegments[j-1]));
						}
						newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, width - sortedSegments[sortedSegments.length-1]));
						segmentedBlocks.addLast(newBlock);
					}
					else
					{
						Block newBlock = new Block();
						newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(0).pixel, width));
						newBlock.rowCount = rowCount;
						segmentedBlocks.addLast(newBlock);
					}
				}
				else
				{
					Block newBlock = new Block();
					for (int j = 1; j < sortedSegments.length; ++j)
					{
						int pixel = thisBlock.row.get(0).pixel;
						int offset = 0;
						for (int k = 0; k < thisBlock.row.size(); ++k)
						{
							if (sortedSegments[j-1] >= offset && sortedSegments[j-1] < offset + thisBlock.row.get(k).length)
								pixel = thisBlock.row.get(k).pixel;
							offset += thisBlock.row.get(k).length;
						}

						newBlock.row.add(new RunLengthEncoding(pixel, sortedSegments[j] - sortedSegments[j-1]));
					}
					newBlock.row.add(new RunLengthEncoding(thisBlock.row.get(thisBlock.row.size()-1).pixel, width - sortedSegments[sortedSegments.length-1]));
					segmentedBlocks.addLast(newBlock);
				}
			}

			for (int i = 0; i < segmentedBlocks.size(); ++i)
			{
				System.out.println(segmentedBlocks.get(i).toString());
			}
			*/

			/*
			while (image.rleData.size() > 0)
			{
				RunLengthEncoding data = image.rleData.removeFirst();
				if (data.length + tmpRowSize < width)
				{
					for (int i = tmpRowSize; i < tmpRowSize + data.length; ++i)
						tempRow.pixels[i] = data.pixel;

					tmpRowSize += data.length;
				}
				else
				{
					int pixelsLeftInCurrentRow;
					if (tmpRowSize != 0)
					{
						pixelsLeftInCurrentRow = width - tmpRowSize;
						for (int i = tmpRowSize; i < width; ++i)
						{
							tempRow.pixels[i] = data.pixel;
						}
						rowBuffer.addLast(tempRow);
						tempRow = new ImageRow(width);
						tmpRowSize = 0;
					}
					else
					{
						pixelsLeftInCurrentRow = 0;
					}

					int leftPixelLength = data.length - pixelsLeftInCurrentRow;
					int pureRowsLeft = leftPixelLength / width;
					int pixelLengthAfterPureRows = leftPixelLength - pureRowsLeft * width;

					if (pureRowsLeft == 0)
					{
						tempRow = new ImageRow(width);
						tmpRowSize = pixelLengthAfterPureRows;
						for (int i = 0; i < tmpRowSize; ++i)
							tempRow.pixels[i] = data.pixel;
					}
					else if (pureRowsLeft < 3)
					{
						for (int i = 0; i < pureRowsLeft; ++i)
						{
							ImageRow pureRow = new ImageRow(width);
							Arrays.fill(pureRow.pixels, data.pixel);
							rowBuffer.addLast(pureRow);
						}

						tempRow = new ImageRow(width);
						tmpRowSize = pixelLengthAfterPureRows;
						for (int i = 0; i < tmpRowSize; ++i)
							tempRow.pixels[i] = data.pixel;
					}
					else
					{
						ImageRow pureRow = new ImageRow(width);
						Arrays.fill(pureRow.pixels, data.pixel);
						rowBuffer.addLast(pureRow);

						pureRow = new ImageRow(width, pureRowsLeft - 2);
						Arrays.fill(pureRow.pixels, data.pixel);
						rowBuffer.addLast(pureRow);

						pureRow = new ImageRow(width);
						Arrays.fill(pureRow.pixels, data.pixel);
						rowBuffer.addLast(pureRow);

						tempRow = new ImageRow(width);
						tmpRowSize = pixelLengthAfterPureRows;
						for (int i = 0; i < tmpRowSize; ++i)
							tempRow.pixels[i] = data.pixel;
					}
				}
			}

			System.out.println(width);

			for (int i = 0; i < rowBuffer.size(); ++i)
			{
				System.out.println(rowBuffer.get(i).toString());
			}

			Vector<RunLengthEncoding> edgeRLEImage = new Vector<RunLengthEncoding>();

			int currentPixel = 0;
			// int currentPixel = edgeRows.get(0).pixels[0];
			int currentLength = 0;

			// calculate edge
			int loopsToRLE = 0;
			Date lastRLEBegins = new Date();
			LinkedList<ImageRow> edgeRows = new LinkedList<ImageRow>();
			for (int i = 0; i < rowBuffer.size(); ++i)
			{
				ImageRow row = new ImageRow(width, rowBuffer.get(i).count);
				for (int j = 0; j < width; ++j)
				{
					int[] maxDelta = new int[1];

					if (j > 0)
					{
						if (i > 0)
							testMaxDelta(rowBuffer, i-1, j-1, i, j, maxDelta);

						testMaxDelta(rowBuffer, i, j-1, i, j, maxDelta);

						if (i < rowBuffer.size() - 1)
							testMaxDelta(rowBuffer, i+1, j-1, i, j, maxDelta);
					}

					if (i > 0)
						testMaxDelta(rowBuffer, i-1, j, i, j, maxDelta);

					if (i < rowBuffer.size() - 1)
						testMaxDelta(rowBuffer, i+1, j, i, j, maxDelta);

					if (j < width-1)
					{
						if (i > 0)
							testMaxDelta(rowBuffer, i-1, j+1, i, j, maxDelta);

						testMaxDelta(rowBuffer, i, j+1, i, j, maxDelta);

						if (i < rowBuffer.size() - 1)
							testMaxDelta(rowBuffer, i+1, j+1, i, j, maxDelta);
					}

					row.pixels[j] = maxDelta[0];
					if (i == 0 && j == 0)
					{
						currentPixel = row.pixels[j];
					}
				}

				// edgeRows.addLast(row);

				loopsToRLE++;

				for (int j = 0; j < width; ++j)
				{
					if (row.pixels[j] == currentPixel)
					{
						currentLength += row.count;
					}
					else
					{
						RunLengthEncoding rle = new RunLengthEncoding(currentPixel, currentLength);
						// edgeRLEImage.add(rle);
						System.out.println(rle.pixel + " " + rle.length);
						System.out.println(loopsToRLE);
						long gap = new Date().getTime() - lastRLEBegins.getTime();
						System.out.println(gap / 1000.0);
						loopsToRLE = 0;
						lastRLEBegins = new Date();

						currentPixel = row.pixels[j];
						currentLength = 1;
					}
				}
			}
			*/

			/*
			for (int i = 0; i < edgeRows.size(); ++i)
			{
				System.out.println(edgeRows.get(i).toString());
			}
			*/

			/*
			for (int i = 0; i < edgeRows.size(); ++i)
			{
			}
			*/

			/*
			if (currentLength != 0)
			{
				RunLengthEncoding rle = new RunLengthEncoding(currentPixel, currentLength);
				// edgeRLEImage.add(rle);
				System.out.println(rle.pixel + " " + rle.length);
			}
			*/

			/*
			for (int i = 0; i < edgeRLEImage.size(); ++i)
			{
				System.out.println(edgeRLEImage.get(i).pixel + " " + edgeRLEImage.get(i).length);
			}
			*/

			/*
			System.out.println(edgedImage.width);
			for (int i = 0; i < encodedImage.size(); ++i)
			{
				System.out.println(encodedImage.get(i).pixel + " " + encodedImage.get(i).length);
			}
			*/
			System.out.println("0 0");
		}
		System.out.println("0");
	}

	private static int findMaxDelta(Vector<Integer> pixels, int basePixel)
	{
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

	private static void testMaxDelta(LinkedList<ImageRow> rowBuffer, int row, int column, int baseRow, int baseColumn, int[] maxDelta)
	{
		int delta = Math.abs(rowBuffer.get(row).pixels[column] - rowBuffer.get(baseRow).pixels[baseColumn]);
		if (delta > maxDelta[0])
			maxDelta[0] = delta;
	}

	private static void generateEdge(Integer[] sortedSegments, LinkedList<RunLengthEncoding> edgeImage, Block lastBlock, Block thisBlock, Block nextBlock)
	{
		for (int j = 0; j < sortedSegments.length; ++j)
		{
			Vector<Integer> pixels = new Vector<Integer>();
			Vector<Integer> leftPixels = new Vector<Integer>();
			Vector<Integer> rightPixels = new Vector<Integer>();

			int offset = sortedSegments[j];
			RunLengthEncoding thisRLE = thisBlock.getRLEByOffset(offset);
			int thisPixel = thisRLE.pixel;
			int length = thisRLE.length;

			int lastLeftPixel = lastBlock.getPixelByOffset(offset-1, thisPixel);
			int lastMiddlePixel = lastBlock.getPixelByOffset(offset, thisPixel);
			int lastRightPixel = lastBlock.getPixelByOffset(offset+1, thisPixel);

			int leftPixel = thisBlock.getPixelByOffset(offset-1, thisPixel);
			int rightPixel = thisBlock.getPixelByOffset(offset+1, thisPixel);

			int nextLeftPixel = nextBlock.getPixelByOffset(offset-1, thisPixel);
			int nextMiddlePixel = nextBlock.getPixelByOffset(offset, thisPixel);
			int nextRightPixel = nextBlock.getPixelByOffset(offset+1, thisPixel);

			System.out.println("lastBlock: " + lastBlock.toString());
			System.out.println("thisBlock: " + thisBlock.toString());
			System.out.println("nextBlock: " + nextBlock.toString());
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
			}

			if (length > 2)
			{
				pixels.add(lastMiddlePixel);

				pixels.add(nextMiddlePixel);

				int pixel = findMaxDelta(pixels, thisPixel);
				edgeImage.add(new RunLengthEncoding(pixel, length-2));
			}

			if (length > 1)
			{
				if (length == 1)
					rightPixels.add(lastLeftPixel);
				rightPixels.add(lastMiddlePixel);
				rightPixels.add(lastRightPixel);

				rightPixels.add(rightPixel);
				if (length == 1)
					rightPixels.add(leftPixel);

				if (length == 1)
					rightPixels.add(nextLeftPixel);
				rightPixels.add(nextMiddlePixel);
				rightPixels.add(nextRightPixel);

				int pixel = findMaxDelta(rightPixels, thisPixel);
				edgeImage.add(new RunLengthEncoding(pixel, 1));
			}
		}
	}
}

class ImageRow
{
	public ImageRow(int width, int count)
	{
		this.pixels = new int[width];
		this.count = count;
	}

	// FIXME: shall we call the first constructor instead?
	public ImageRow(int width)
	{
		this.pixels = new int[width];
		this.count = 1;
	}

	public int[] pixels;
	public int count;

	public String toString()
	{
		String str = new String();
		for (int i = 0; i < this.pixels.length; ++i)
		{
			str += this.pixels[i] + ", ";
		}

		if (this.count > 1)
		{
			str += "@" + this.count;
		}

		return str;
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

		/*
		int remainingLength = length;

		if (remainingLength <= this.lastRowRemainingPixels)
		{
			for (int i = 0; i < remainingLength; ++i)
			{
				int offset = i + width - this.lastRowRemainingPixels;
				this.imageRows.getLast().pixels[offset] = pixel;
			}

			this.lastRowRemainingPixels -= remainingLength;
			remainingLength = 0;
			return;
		}
		else if (remainingLength > this.lastRowRemainingPixels)
		{
			if (this.lastRowRemainingPixels == 0)
			{
				int rows = remainingLength / this.width;
				if (rows == 0)
				{
					imageRows.add(new ImageRow(this.width, 1));
					this.lastRowRemainingPixels = this.width;
					this.addPixels(new RunLengthEncoding(pixel, remainingLength));
					return;
				}

				imageRows.add(new ImageRow(this.width, rows));
				for (int i = 0; i < this.width; ++i)
				{
					int offset = i;
					this.imageRows.getLast().pixels[offset] = pixel;
				}

				this.addPixels(new RunLengthEncoding(pixel, remainingLength % this.width));
				return;
			}

			int firstPart = this.lastRowRemainingPixels;
			int secondPart = remainingLength - firstPart;
			this.addPixels(new RunLengthEncoding(pixel, firstPart));
			this.addPixels(new RunLengthEncoding(pixel, secondPart));
			return;
		}
		*/


	/*
	public Image getEdgedImage()
	{
		Image edgedImage = new Image(this.width);

		// trick to simplify the computing, by adding empty rows to both beginning and end of the image
		this.imageRows.addFirst(new ImageRow(this.width, 1));
		this.imageRows.add(new ImageRow(this.width, 1));

		for (int i = 1; i < imageRows.size() - 1; ++i)
		{
			ImageRow row = imageRows.get(i);
			ImageRow newRow = new ImageRow(this.width, 1);
			for (int j = 0; j < this.width; ++j)
			{
				newRow.pixels[j] = 
			}

			if (row.count >= 3)
		}

		return edgedImage;
	}

	public LinkedList<RunLengthEncoding> toRunLengthEncoding()
	{
		LinkedList<RunLengthEncoding> encoded = new LinkedList<RunLengthEncoding>();
		return encoded;
	}
	*/
}


