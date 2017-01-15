package com.foster.linearalg;

/**Vector class
 * provides structure for storing vectors of size n
 * supports various vector arithmetic (add, scalarmpy, dot)
 * @author Reed2
 *
 */
public class Vector
{
	private double[] elements;
	public final int length;
	
	Vector(double...elements)
	{
		this.elements = new double[elements.length];
		this.length = elements.length;
		for (int i = 0; i < this.length; i++)
			this.elements[i] = elements[i];
	}
	
	Vector get()
	{
		return new Vector(this.elements);
	}
	
	double get(int i)
	{
		return this.elements[i];
	}
	
	double[] getelements()
	{
		return this.elements;
	}
	
	double[] getelements(int start, int stop)
	{
		double[] sub = new double[stop - start + 1];
		for (int i = start; i <= stop; i++)
		{
			sub[i - start] = this.elements[i];
		}
		return sub;
	}
	
	void printvector()
	{
		System.out.print("(");
		for (int i = 0; i < this.length; i++)
			System.out.printf(((i == this.length - 1) ? "%-2.1f)\n" : "%-2.1f, "), this.elements[i]);
	}
	
	static Vector add(Vector a, Vector b, Vector...c)
	{
		if (a.length != b.length)
		{
			System.err.println("attempt to add vectors of different length");
			return null;
		}
		for (Vector i : c)
		{
			if (i.length != a.length)
			{
				System.err.println("attempt to add vectors of different length");
				return null;
			}
		}
		Vector sum = a.get();
		for (int i = 0; i < a.length; i++)
			sum.elements[i] += b.elements[i];
		return sum;
	}
	
	static Vector mpy(Vector a, double b)
	{
		Vector p = a.get();
		for (int i = 0; i < a.length; i++)
			p.elements[i] *= b;
		return p;
	}
	
	static double dot(Vector a, Vector b)
	{
		if (a.length != b.length)
		{
			System.err.println("attempt to dot vectors of different length");
			return (double)Double.NaN;
		}
		double s = 0;
		for (int i = 0; i < a.length; i++)
			s += a.elements[i] * b.elements[i];
		return s;
	}
	
	static Vector concat(Vector a, Vector b)
	{
		double[] c = new double[a.length + b.length];
		for (int i = 0; i < a.length + b.length; i++)
		{
			c[i] = i >= a.length ? b.elements[i - a.length] : a.elements[i];
		}
		return new Vector(c);
	}
}
