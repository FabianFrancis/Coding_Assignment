package com.project;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author Fabian
 * CategorySetParser Class object to Parse a file for Categories and Sub Categories.
 * Provides parse method to return the ParserValues Object. 
 * 
 */
public class CategorySetParser {
	
	private final HashSet<String> LEGAL_CATEGORY_LIST;
	private final File inputFile;
	
		
	/**
	 * Constructor to create CategorySetParser object
	 * PRECONDITION/REQUIRED: 
	 *  @param legalCategoryList 			String Array of Legal Category List, Example: {"PERSON", "PLACE", "OTHER"}
	 *  @param inputFile 					Input file to parse with single space separated category and sub-category
	 */
	public CategorySetParser(String[] legalCategoryList, File inputFile) {
		LEGAL_CATEGORY_LIST= new HashSet<String>(Arrays.asList(legalCategoryList));
		this.inputFile = inputFile;
	}
	
	/**
	 * Instance method to parse the file with provided input file and Legal category list.
	 * PRECONDITION/REQUIRED: 	
	 * 	Provided input file should exist else will throw FileNotFoundException.
	 * 
	 * @return		object values for ParserValues class consisting.
	 * 				1. a LinkedHashMap containing categories as keys and the count as values
	 * 				2. a LinkedHashSet containing the all the valid categories and sub-categories with duplicates removed.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 *  
	 */
	public ParserValues parse() throws FileNotFoundException, IOException{
	
		String currentLine;
		String currentCategory;
		LinkedHashMap<String, Integer> categoryCount = new LinkedHashMap<>();
		LinkedHashSet<String> categoryOrderedList = new LinkedHashSet<>();
		
		//Reading each line of the input file till the last line is reached
		try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
			while ((currentLine = br.readLine()) != null ) {
				try {
					currentCategory = readCategory(currentLine);
				} catch (IllegalArgumentException e) {
					continue;
				}
				//To check if there are no illegal category values
				if(isLegalCategory(currentCategory)){
					//To check if the key(category) already exists in the LinkedHashMap.
					if(categoryCount.containsKey(currentCategory)){
						//To check if the current line category and sub-category is not equal to any of the values in the categoryOrderedList.
						if(!categoryOrderedList.contains(currentLine)){			
							categoryCount.put(currentCategory, categoryCount.get(currentCategory)+1);
						}
					}
					else{
						//If key(category) does not exist in categoryOrderedList then insert the new value in the LinkedHashMap.
						categoryCount.put(currentCategory,1);
					}
					categoryOrderedList.add(currentLine);
				}
			}// end of While loop
			
		} catch (FileNotFoundException e) {
			System.err.println("ERROR: File Not Found.");
			throw e;
		} catch (IOException e) {
			System.err.println("ERROR: I/O Exception.");
			throw e;
		}
		
		//If a category value is not given in the file then insert the category with a count of zero. 
		if(categoryCount.size() != LEGAL_CATEGORY_LIST.size()){
			for(String category: LEGAL_CATEGORY_LIST){
				if(!categoryCount.containsKey(category)){
					categoryCount.put(category, 0);
				}
			}
		}
		return new ParserValues(sortByValues(categoryCount), categoryOrderedList);
	}

	
	/**
	 * Instance method to parse a input line to find the Category in the input line
	 * PRECONDITION/REQUIRED: 	
	 * 	Provided input line should be valid, Category and  sub-Category should be separated with single space 
	 *  
	 * @param  line						Single input line from input file.
	 * @return 							Returns a Category String parsed from the file.
	 * @throws IllegalArgumentException
	 *  
	 */
	private String readCategory(String line) throws IllegalArgumentException{
		final String SEPARATOR = " ";
		final int CATEGORY_INDEX = 0;
		String[] temp = line.split(SEPARATOR);
		if(temp.length < 2){
			throw new IllegalArgumentException();
		}
		return line.split(SEPARATOR)[CATEGORY_INDEX];
	}
	
	
	/**
	 * Instance method to check if provide category is legal or not
	 * PRECONDITION/REQUIRED: 	
	 * 	All legal categories should be defined 
	 *  
	 * @param  category						Parsed category from the input line.
	 * @return 								Returns boolean value if the category is legal
	 *  
	 */
	private boolean isLegalCategory(String category){
		return LEGAL_CATEGORY_LIST.contains(category);
	}
	
	
	/**
	 * Class method to do a stable sort on HashMap values (Category Count). A Comparator is used to sort the LinkedHashMap 
	 * PRECONDITION/REQUIRED: 	
	 *   
	 * @param  categoryCount				Unsorted Linked HashMap for with Category count values
	 * @return 								Returns LinkedHashMap, will keep the keys in the order they are inserted and sort according to values or count
	 *  
	 */
	private static LinkedHashMap<String,Integer> sortByValues(LinkedHashMap<String,Integer> categoryCount){
		
        List<Map.Entry<String,Integer>> entries = new LinkedList<Map.Entry<String,Integer>>(categoryCount.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String,Integer>>() {

            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
           LinkedHashMap<String,Integer> sortedMap = new LinkedHashMap<String,Integer>();
      
        for(Map.Entry<String,Integer> entry: entries){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
	
}
