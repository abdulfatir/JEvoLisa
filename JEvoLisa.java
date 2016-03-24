import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.io.*;

public class JEvoLisa
{
	public static void main(String args[])
	{
		if(args.length == 0)
		{
			System.out.println("Usage:");
			System.out.println(" java JEvoLisa <image-file>");
			System.exit(1);
		}
		Chromosome.setTarget(args[0]);
		new JEvoLisa().paint();
	}

	private void paint()
	{
		Chromosome DNA_BEST = new Chromosome();
		Chromosome DNA_TEST = new Chromosome();

		for(int x=0;x<Chromosome.MAX_SHAPES;x++)
		{
			Chromosome.passGeneMutation(DNA_BEST, DNA_TEST, x);
		}

		long FITNESS_MAX = Long.MAX_VALUE;
		long FITNESS_TEST = FITNESS_MAX;
		long FITNESS_BEST = FITNESS_MAX;
		double PERC_FITNESS = 0;

		int beneficial=0,total=0;

		long startTimestamp = System.currentTimeMillis();

		while(true)
		{
			int change_index = DNA_TEST.mutate();
			FITNESS_TEST = Chromosome.computeFitness(Chromosome.drawDNA(DNA_TEST));

			if(FITNESS_TEST < FITNESS_BEST)
			{
				Chromosome.passGeneMutation(DNA_TEST, DNA_BEST, change_index);

				FITNESS_BEST = FITNESS_TEST;
				PERC_FITNESS = 100*(1-((double)FITNESS_BEST/Chromosome.NORM_COEFF));
				//System.out.println(FITNESS_BEST+","+Chromosome.NORM_COEFF);
				beneficial++;
				total++;
			}
			else
			{
				Chromosome.passGeneMutation(DNA_BEST, DNA_TEST, change_index);
				total++;
			}
			if(total%1000 == 0)
			{
				Chromosome.saveToFile(DNA_BEST,total);
				System.out.printf("Improvements: %d, Fitness: %f, Time: %ds%n",beneficial,PERC_FITNESS, (System.currentTimeMillis()-startTimestamp)/1000);
			}
		}
	}
}