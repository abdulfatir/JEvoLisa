# JEvoLisa
A Java reimplementation of Roger Alsing's EvoLisa which approximates an image using a set number of shapes by using an optimization algorithm. The co-ordinates and ARGB values of the shapes are evolved and at any instant a chromosome represents a collection of such shapes.
### Build
`$ javac JEvoLisa.java Chromosome.java Polygon.java Shape.java`
### Usage
`$ java JEvoLisa images/mona.png`
### Editing Properties
To change the number and type of shapes, change the following code in `Chromosome.java`  
```java
public static final int MAX_SHAPES = 50;
public static final int MAX_POINTS = 6;
```
The code currently writes to an image file every `1000` iterations, to change that, change the following lines in `JEvoLisa.java`  
```java
if(total%1000 == 0)
{
	Chromosome.saveToFile(DNA_BEST,total);
	System.out.printf("Improvements: %d, Fitness: %f, Time: %ds%n", beneficial,
	PERC_FITNESS, (System.currentTimeMillis()-startTimestamp)/1000);
}
```
### Note
The code does not comply entirely with Java design guidelines, fork it to make it better.
#### Possible Features
* Chromosome serialization so that best chromosome can be saved and later loaded to resume computation.
* Command Line arguments to set the number and type of shapes.  

### Further Reading
[Roger Alsing's Article](http://rogeralsing.com/2008/12/07/genetic-programming-evolution-of-mona-lisa/)  
[Online Image Evolution](http://alteredqualia.com/visualization/evolve/)
