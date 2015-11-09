package com.project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import javax.swing.JOptionPane;


/**
 * @Fabian
 * CategoryCountApplication class to call the main method and parse the input file. As per the requirements these are the steps followed by this
 * class.
 * 
 * 1. Inside the main method first a check is done to validate the argument.
 * 2. If the argument is valid the input file is accepted
 * 3. Categories are parsed in the parseCategories method. Method parseCategories returns an object of ParserValues. Detailed explanation give 
 * 	    in method description
 * 4. The result id displayed in the displayResults method
 * 
 */

public class CategoryCountApplication {
	
	/**
	 * Main method.
	 * @param args				Accept single argument which is input file to be parsed.
	 */
	public static void main(String[] args) {
		 
		// Program is terminated and an error message is shown if there exist no argument and if there are more than one arguments entered.
		if(args.length == 0){
			JOptionPane.showMessageDialog(null, "No arguments entered, Please enter file path", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}else if(args.length > 1){
			JOptionPane.showMessageDialog(null, "Too many arguments please provide only the file path with no space", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}else if(!new File(args[0]).exists()){
			JOptionPane.showMessageDialog(null, "File doesn't exist.", "Error", JOptionPane.ERROR_MESSAGE);
			System.exit(-1);
		}
		
		//Define legal category to parse
		final String[] LEGAL_VALUES = new String[] { "PERSON", "PLACE", "ANIMAL", "COMPUTER", "OTHER"};
		//Get input file from arguments
		File inputFile = new File(args[0]);
		
		ParserValues result = null;
		try {
			//Parse the file
			result = parseCategories(LEGAL_VALUES, inputFile);
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		//Print the result
		displayResults(result);
	}// end of main
	
	/**
	 *  Method to parse categories with respect to the valid content inside the input text file. If any exception is faced during the program 
	 *  execution, it will be thrown to its calling method(main method). 
	 *   
	 * @param LEGAL_VALUES				A string array with values of categories 
	 * @param inputFile					The input file containing user defined categories and sub categories.
	 * 
	 * @return parseResult				Returns an object of type ParserValues containing the values for final result. Object contains
	 * 									1. a LinkedHashMap containing categories as keys and the count as values
	 * 									2. a LinkedHashSet containing the all the valid categories and sub-categories with duplicates removed.
	 */
	public static ParserValues parseCategories(String[] LEGAL_VALUES, File inputFile)throws FileNotFoundException, IOException{
		
		ParserValues parseResult = null;
		CategorySetParser cp = new CategorySetParser(LEGAL_VALUES, inputFile);
		try {
			parseResult = cp.parse();
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		return parseResult;
	}// end of parseCategories
	
	/**
	 *  Method to parse categories with respect to the valid content inside the input text file.
	 * @param result		Accept single argument of type ParserValues to display the result.
	 */
	public static void displayResults(ParserValues result){
		
		LinkedHashMap<String, Integer> count = result.getCategoryCount();
		LinkedHashSet<String> list = result.getCategoryOrderedList();
		
		System.out.printf("%-10s %s\n","CATEGORY","COUNT");
		
		for (Entry<String, Integer> entry : count.entrySet()) {
			System.out.printf("%-10s %d\n",entry.getKey(), entry.getValue());
		}
		
		System.out.println();
		for(String st: list){
			System.out.println(st);
		}
		
	}// end of displayResults
}// end of CategoryCountApplication class
