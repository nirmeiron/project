package clids.ex4.misc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

import clids.ex4.exceptions.InvalidValueException;
import clids.ex4.exceptions.NoValueForFinalVarException;
import clids.ex4.parser.Creator;

public class IraTest {

	public static void main(String[] args) throws IOException,
			InvalidValueException, NoValueForFinalVarException {

		BufferedReader reader = new BufferedReader(new FileReader(new File(
				"C:/Users/t6631696/Desktop/blob/test.txt")));

		String line1 = reader.readLine();
		String line2 = reader.readLine();
		String line3 = reader.readLine();

		


		System.out.println(Creator.parseAssignLine("   valsdfue   =   ;  ")[1]);


	}
}
