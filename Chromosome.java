import java.util.Random;
import java.awt.image.BufferedImage;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Chromosome
{
	public static final int MAX_SHAPES = 50;
	public static final int MAX_POINTS = 6;

	public Shape[] DNA;
	public static int IMWIDTH;
	public static int IMHEIGHT;
	public static BufferedImage target;
	public static long NORM_COEFF;

	private Random random;

	public Chromosome()
	{
		random = new Random();
		DNA = new Shape[MAX_SHAPES];

		for(int i=0;i<MAX_SHAPES;i++)
		{
			int X_POINTS[] = new int[MAX_POINTS];
			int Y_POINTS[] = new int[MAX_POINTS];
			for(int j=0;j<MAX_POINTS;j++)
			{
				X_POINTS[j] = random.nextInt(IMWIDTH);
				Y_POINTS[j] = random.nextInt(IMHEIGHT);
			}
			Polygon POLYGON = new Polygon(X_POINTS, Y_POINTS, MAX_POINTS);
			Color COLOR = new Color(0,0,0,5);
			Shape shape = new Shape(COLOR, POLYGON);
			DNA[i] = shape;
		}
	}

	public Chromosome(Chromosome copy)
	{
		this.DNA = copy.DNA;
		this.random = new Random();
	}

	public static void passGeneMutation(Chromosome from, Chromosome to, int index)
	{
		int r = from.DNA[index].color.getRed();
		int g = from.DNA[index].color.getGreen();
		int b = from.DNA[index].color.getBlue();
		int a = from.DNA[index].color.getAlpha();

		to.DNA[index].color = new Color(r,g,b,a);

		for(int i=0;i<MAX_POINTS;i++)
		{
			to.DNA[index].polygon.X_POINTS[i] = from.DNA[index].polygon.X_POINTS[i];
			to.DNA[index].polygon.Y_POINTS[i] = from.DNA[index].polygon.Y_POINTS[i];
		}
	}


	public int mutate()
	{
		int change_index = random.nextInt(MAX_SHAPES);

		double wheel = random.nextDouble()*2;

		if(wheel<1)
		{
			int r = DNA[change_index].color.getRed();
			int g = DNA[change_index].color.getGreen();
			int b = DNA[change_index].color.getBlue();
			int a = DNA[change_index].color.getAlpha();
			if(wheel < 0.25)
			{
				r = random.nextInt(256);
			}
			else if(wheel < 0.5)
			{
				g = random.nextInt(256);	
			}
			else if(wheel < 0.75)
			{
				b = random.nextInt(256);
			}
			else
			{
				a = random.nextInt(256);
			}
			DNA[change_index].color = new Color(r,g,b,a);
		}
		else
		{
			int changed_point = random.nextInt(MAX_POINTS);
			if(wheel < 1.5)
			{
				DNA[change_index].polygon.X_POINTS[changed_point] = random.nextInt(IMWIDTH);
			}
			else
			{
				DNA[change_index].polygon.Y_POINTS[changed_point] = random.nextInt(IMHEIGHT);
			}
		}

		return change_index;
	}

	public static BufferedImage drawDNA(Chromosome ch)
	{
		BufferedImage image = new BufferedImage(IMWIDTH, IMHEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics graphics = image.getGraphics();

		for(int i=0;i<MAX_SHAPES;i++)
		{
			graphics.setColor(ch.DNA[i].color);
			graphics.fillPolygon(ch.DNA[i].polygon.X_POINTS, ch.DNA[i].polygon.Y_POINTS, ch.DNA[i].polygon.N_POINTS);
			//graphics.fillOval(ch.DNA[i].polygon.X_POINTS[0], ch.DNA[i].polygon.Y_POINTS[0], ch.DNA[i].polygon.X_POINTS[1], ch.DNA[i].polygon.Y_POINTS[1]);
		}

		return image;
	}

	public static long computeFitness(BufferedImage test)
	{
		long fitness = 0;
		for(int x=0;x<IMWIDTH;x++)
		{
			for(int y=0;y<IMHEIGHT;y++)
			{
				int color1 = test.getRGB(x,y);
				int color2 = target.getRGB(x,y);
				int a1 = (color1>>24)&0xff;
				int a2 = (color2>>24)&0xff;
				int r1 = (color1>>16)&0xff;
				int r2 = (color2>>16)&0xff;
				int g1 = (color1>>8)&0xff;
				int g2 = (color2>>8)&0xff;
				int b1 = (color1>>0)&0xff;
				int b2 = (color2>>0)&0xff;
				int pixel_error = Math.abs(a1-a2) + Math.abs(r1-r2) + Math.abs(g1-g2) + Math.abs(b1-b2);
				fitness += pixel_error;
			}
		}

		return fitness;
	}

	private static String inputFile;


	public static void setTarget(String file)
	{
		try
		{
			target = ImageIO.read(new File(file));
			inputFile = file;
		}
		catch(IOException ex){}
		IMWIDTH = target.getWidth();
		IMHEIGHT = target.getHeight();
		NORM_COEFF = IMHEIGHT*IMHEIGHT*3*255;
	}

	public static void saveToFile(Chromosome c, int gen)
	{


		BufferedImage image = new BufferedImage(IMWIDTH, IMHEIGHT,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics graphics = image.getGraphics();
		((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		

		for(int i=0;i<MAX_SHAPES;i++)
		{

		int[] xps = new int[c.DNA[i].polygon.N_POINTS];
		int[] yps = new int[c.DNA[i].polygon.N_POINTS];

		for(int j=0;j<c.DNA[i].polygon.N_POINTS;j++)
		{
			xps[j] = c.DNA[i].polygon.X_POINTS[j];
			yps[j] = c.DNA[i].polygon.Y_POINTS[j];
		}
			graphics.setColor(c.DNA[i].color);
			graphics.fillPolygon(xps, yps, c.DNA[i].polygon.N_POINTS);
			//graphics.fillOval(ch.DNA[i].polygon.X_POINTS[0], ch.DNA[i].polygon.Y_POINTS[0], ch.DNA[i].polygon.X_POINTS[1], ch.DNA[i].polygon.Y_POINTS[1]);
		}

		String dirName = new File(inputFile).getName().replaceAll("\\.\\w{2,}$","");

		File dir = new File("output\\"+dirName);
		if(!dir.exists())
		{
			dir.mkdirs();
		}

		String basepath = "output\\"+dirName+"\\";
		try
		{
			ImageIO.write(image, "PNG", new File(basepath+gen+".png"));
		}
		catch(IOException ex){}
	}

	public static void main(String[] args) 
	{
		new Chromosome();	
	}
}