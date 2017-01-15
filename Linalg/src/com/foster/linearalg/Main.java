package com.foster.linearalg;

import java.io.*;
import java.util.ArrayList;

public class Main
{
	private static Matrix getmatrix(String str)
	{
		String[] rows = str.split("/");
		ArrayList<String> elements = new ArrayList<String>();
		for (String i : rows)
		{
			String[] r_ele = i.split(",");
			for (String j : r_ele)
			{
				elements.add(j);
			}
		}
		double[] matr_data = new double[elements.size()];
		for (int i = 0; i < matr_data.length; i++)
		{
			try
			{
				matr_data[i] = Double.parseDouble(elements.get(i));
			}
			catch (NumberFormatException exc)
			{
				System.out.println("invalid matrix");
				return null;
			}
		}
		return new Matrix(rows.length, matr_data.length/rows.length, matr_data);
	}
	
	public static void main(String[] args) throws IOException
	{
		BufferedReader br = new BufferedReader (new InputStreamReader(System.in));
		String str;
		System.out.print("Enter a matrix (A): ");
		str = br.readLine();
		Matrix user_matrix = getmatrix(str).get();
		//user_matrix.printmatrix();
		System.out.print("Choose an operation (add,mpy,det,inv):");
		str = br.readLine();
		String op = str;
		if (str.compareTo("add") == 0 || str.compareTo("mpy") == 0)
		{
			System.out.print("Please enter a second matrix (B):");
			str = br.readLine();
			Matrix user_matrix2 = getmatrix(str).get();
			if (op.compareTo("add") == 0)
			{
				System.out.println("A + B =");
				try
				{
					Matrix.add(user_matrix, user_matrix2).printmatrix();
				}
				catch (NullPointerException exc)
				{
					System.err.println("matrix addition failed");
				}
			}
			else
			{
				System.out.println("A * B =");
				try
				{
					Matrix.mpy(user_matrix, user_matrix2).printmatrix();
				}
				catch (NullPointerException exc)
				{
					System.err.println("matrix multiplication failed");
				}
				
			}
		}
		else if (str.compareTo("det") == 0)
		{
			System.out.printf("det(A) = %5.1f\n", Matrix.det(user_matrix));
		}
		else if (str.compareTo("inv") == 0)
		{
			System.out.println("inv(A) =");
			try
			{
				Matrix.inv(user_matrix).printmatrix();
			}
			catch (NullPointerException exc)
			{
				System.err.println("inversion of matrix A failed");
			}
		}
		main(null);
	}
}
