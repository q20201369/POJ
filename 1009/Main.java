import java.util.Scanner;
import java.util.Vector;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Arrays;

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

			ImageRow tempRow = new ImageRow(width);
			int tmpRowSize = 0;

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
					int pixelsLeftInCurrentRow = width - tmpRowSize;
					for (int i = tmpRowSize; i < width; ++i)
					{
						tempRow.pixels[i] = data.pixel;
					}
					rowBuffer.addLast(tempRow);
					tempRow = new ImageRow(width);
					tmpRowSize = 0;

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

			/*
			for (int i = 0; i < rowBuffer.size(); ++i)
			{
				System.out.println(rowBuffer.get(i).toString());
			}
			*/

			// calculate edge
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
				}

				edgeRows.addLast(row);
			}

			/*
			for (int i = 0; i < edgeRows.size(); ++i)
			{
				System.out.println(edgeRows.get(i).toString());
			}
			*/

			Vector<RunLengthEncoding> edgeRLEImage = new Vector<RunLengthEncoding>();

			int currentPixel = edgeRows.get(0).pixels[0];
			int currentLength = 0;
			for (int i = 0; i < edgeRows.size(); ++i)
			{
				for (int j = 0; j < width; ++j)
				{
					if (edgeRows.get(i).pixels[j] == currentPixel)
					{
						currentLength += edgeRows.get(i).count;
					}
					else
					{
						RunLengthEncoding rle = new RunLengthEncoding(currentPixel, currentLength);
						edgeRLEImage.add(rle);

						currentPixel = edgeRows.get(i).pixels[j];
						currentLength = 1;
					}
				}
			}

			if (currentLength != 0)
			{
				RunLengthEncoding rle = new RunLengthEncoding(currentPixel, currentLength);
				edgeRLEImage.add(rle);
			}

			for (int i = 0; i < edgeRLEImage.size(); ++i)
			{
				System.out.println(edgeRLEImage.get(i).pixel + " " + edgeRLEImage.get(i).length);
			}

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


