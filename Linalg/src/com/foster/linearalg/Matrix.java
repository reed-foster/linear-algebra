package com.foster.linearalg;

/**Matrix class
 * provides structure for storing matrices of size nxm
 * supports various matrix arithmetic (add, matmpy, scalarmpy det, inv)
 * @author Reed2
 *
 */
public class Matrix
{
	private double[][] elements;
	private final int m, n;
	
	/**Constructor for matrices
	 * @param m = #rows
	 * @param n = #columns
	 * @param elements = list of matrix elements; unassigned elements set to 0
	 */
	Matrix(int m, int n, double...elements)
	{
		this.elements = new double[m][n];
		this.m = m;
		this.n = n;
		for (int i = 0; i < m; i++)
		{
			for (int j = 0; j < n; j++)
			{
				int idx = i * n + j;
				this.elements[i][j] = idx < elements.length ? elements[i * n + j] : 0;
			}
		}
	}
	
	/**Copy a matrix
	 * @return a copy of the matrix
	 */
	Matrix get()
	{
		double[] a = new double[elements.length * elements[0].length];
		for (int i = 0; i < this.m; i++)
		{
			for (int j = 0; j < this.n; j++)
			{
				a[i * this.n + j] = elements[i][j];
			}
		}
		return new Matrix(this.m, this.n, a);
	}
	
	/**Get the height of the matrix, in rows*/
	int getm() {return m;}
	/**Get the width of the matrix, in columns*/
	int getn() {return n;}
	
	/**Get the element corresponding to the index i,j*/
	double get(int i, int j) {return this.elements[i][j];}
	
	/**Get the row vector corresponding to the ith row*/
	Vector getr(int i)
	{
		return new Vector(this.elements[i]);
	}
	
	/**Get the column vector corresponding to the jth column*/
	Vector getc(int j)
	{
		double[] a = new double[this.n];
		for (int i = 0; i < this.n; i++)
		{
			a[i] = this.get(j, i);
		}
		return new Vector(a);
	}
	
	/**Get the subsection of a matrix when removing the ith row and jth column*/
	Matrix getsub(int i, int j)
	{
		double[] a = new double[(this.m - 1) * (this.n - 1)];
		for (int k = 0; k < this.m; k++)
		{
			for (int l = 0; l < this.n; l++)
			{
				if (k != i && l != j)
					a[(k > i ? k - 1 : k) * (this.n - 1) + (l > j ? l - 1: l)] = this.elements[k][l];
			}
		}
		return new Matrix(this.m - 1, this.n - 1, a);
	}
	
	/**Prints a formatted matrix*/
	void printmatrix()
	{
		for (int i = 0; i < this.m; i++)
		{
			System.out.print("[");
			for (int j = 0; j < this.n - 1; j++)
			{
				System.out.printf("%-5.4f,", this.elements[i][j]);
			}
			System.out.printf("%-5.4f]\n", this.elements[i][n - 1]);
		}
	}
	
	/**Recursively calculates the determinant of a matrix, returns NaN if matrix isn't square*/
	static double det(Matrix a)
	{
		if (a.m != a.n)
		{
			System.err.println("determinant is only defined for square matrices");
			return (double)Double.NaN;
		}
		if (a.m == 1)
		{
			return a.elements[0][0];
		}
		else if (a.m == 2)
		{
			return (a.get(0, 0) * a.get(1, 1) - a.get(0, 1) * a.get(1, 0));
		}
		else
		{
			double det = 0;
			for (int i = 0; i < a.m; i++)
			{
				det += (i % 2 == 1 ? -1 : 1) * a.get(i, 0) * det(a.getsub(i, 0));
			}
			return det;
		}
	}
	
	/**Calculates the transpose of the matrix a*/
	static Matrix trans(Matrix a)
	{
		double[] t = new double[a.m * a.n];
		for (int i = 0; i < a.m; i++)
		{
			for (int j = 0; j < a.n; j++)
				t[i * a.n + j] = a.get(j, i);
		}
		return new Matrix(a.n, a.m, t);
	}
	
	/**Generates an identity matrix of order size x size*/
	static Matrix I(int size)
	{
		Matrix identity = new Matrix(size, size);
		for (int i = 0; i < size; i++)
		{
			for (int j = 0; j < size; j++)
			{
				identity.elements[i][j] = (i == j ? 1 : 0);
			}
		}
		return identity;
	}
	
	/**Calculates the inverse of matrix a*/
	static Matrix inv(Matrix a)
	{
		double deta = det(a);
		if (deta == 0 || ((Double)deta).isNaN())
		{
			System.err.println("matrix is not invertible");
			return null;
		}
		double deta_inv = 1 / det(a);
		if (a.m == 1)
		{
			return new Matrix(1, 1, 1 / a.get(0, 0));
		}
		else if (a.m == 2)
		{
			return new Matrix(2, 2, deta_inv * a.get(1, 1), -deta_inv * a.get(0, 1), -deta_inv * a.get(1, 0), deta_inv * a.get(0, 0));
		}
		else
		{
			Vector[] r = new Vector[a.m];
			Matrix identity = I(a.m).get();
			for (int i = 0; i < a.m; i++)
				r[i] = Vector.concat(a.getr(i), identity.getr(i));
			for (int i = 0; i < a.m; i++)
			{
				r[i] = Vector.mpy(r[i], 1 / r[i].get(i));
				for (int j = 0; j < a.m; j++)
				{
					if (i != j)
						r[j] = Vector.add(Vector.mpy(r[i], -1 * r[j].get(i)).get(), r[j]).get();
				}
			}
			double[] inv = new double[a.m * a.n];
			for (int i = 0; i < a.m; i++)
			{
				double[] invrows = r[i].getelements(a.m, a.m * 2 - 1);
				for (int j = 0; j < a.m; j++)
				{
					inv[i * a.m + j] = invrows[j];
				}
			}
			return new Matrix(a.m, a.n, inv);
		}
	}
	
	/**Multiplies matrix a by scalar b*/
	static Matrix mpy(Matrix a, double b)
	{
		double[] product = new double[a.m * a.n];
		for (int i = 0; i < a.m; i++)
		{
			for (int j = 0; j < a.n; j++)
				product[i * a.n + j] = a.get(i, j) * b;
		}
		return new Matrix(a.m, a.n, product);
	}
	
	/**Multiplies a variable number of matrices*/
	static Matrix mpy(Matrix a, Matrix b, Matrix...c)
	{
		if (a.n != b.m)
		{
			System.err.printf("matrix multiplication for %dx%d and %dx%d matrices undefined\n", a.m, a.n, b.m, b.n);
			return null;
		}
		int last = b.n;
		for (Matrix i : c)
		{
			if (i.m != last)
			{
				System.err.printf("matrix multiplication for nx%d and %dx%d matrices undefined\n", last, i.m, i.n);
				return null;
			}
			last = i.n;
		}
		
		double[] elements = new double[a.m * c[c.length - 1].n];
		
		for (int i = 0; i < a.m; i++)
		{
			for (int j = 0; j < b.n; j++)
				elements[i * a.n + j] = Vector.dot(a.getr(i), b.getc(j));
		}
		return new Matrix(a.m, (c.length == 0 ? b.n : c[c.length - 1].n), elements);
	}
	
	/**Adds a variable number of matrices*/
	static Matrix add(Matrix a, Matrix b, Matrix...c)
	{
		if (a.m != b.m || a.n != b.n)
		{
			System.err.printf("matrix addition for %dx%d and %dx%d matrices undefined\n", a.m, a.n, b.m, b.n);
			return null;
		}
		for (Matrix i : c)
		{
			if (i.m != a.m || i.n != b.n)
			{
				System.err.printf("matrix addition for %dx%d and %dx%d matrices undefined\n", a.m, a.n, b.m, b.n);
				return null;
			}
		}
		
		double[] sum = new double[a.m * a.n];
		
		if (c.length > 0)
		{
			for (int i = 0; i < a.m; i++)
			{
				for (int j = 0; j < a.n; j++)
				{
					double s = 0;
					for (Matrix m : c)
						s += m.get(i, j);
					sum[i * a.n + j] = a.get(i, j) + b.get(i, j) + s;
				}
			}
		}
		else
		{
			for (int i = 0; i < a.m; i++)
			{
				for (int j = 0; j < a.n; j++)
				{
					sum[i * a.n + j] = a.get(i, j) + b.get(i, j);
				}
			}
		}
		return new Matrix(a.m, a.n, sum);
	}
}
