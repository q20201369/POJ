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

		while (true)
		{
			int width = sc.nextInt();
			if (width == 0)
				break;

			Image image = parseImage(sc, width);
			Image edgedImage = image.getEdgedImage();
			LinkedList<RunLengthEncoding> encodedImage = edgedImage.toRunLengthEncoding();

			System.out.println(edgedImage.width);
			for (int i = 0; i < encodedImage.size(); ++i)
			{
				System.out.println(encodedImage.get(i).pixel + " " + encodedImage.get(i).length);
			}
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

			image.addPixels(new RunLengthEncoding(pixel, length));
		}

		return image;
	}
}

class ImageRow
{
	public ImageRow(int width, int count)
	{
		this.pixels = new int[width];
		this.count = count;
	}

	public int[] pixels;
	public int count;
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
	public LinkedList<ImageRow> imageRows = new LinkedList<ImageRow>();
	public int lastRowRemainingPixels = 0;
	public int width = 0;

	public Image(int width)
	{
		this.width = width;
	}

	public void addPixels(RunLengthEncoding data)
	{
		int pixel = data.pixel;
		int length = data.length;

		if (length <= 0)
			return;

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

	}

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
}


