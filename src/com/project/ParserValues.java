package com.project;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author Fabian
 * The result of the parsed values will be saved in the objects of ParserValues class.
 * 
 * 
 */
public class ParserValues {
	
	private LinkedHashMap<String, Integer> categoryCount;
	private LinkedHashSet<String> categoryOrderedList;
	
	/**
	 * Constructor to to create ParserResult object
	 * PRECONDITION/REQUIRED: 
	 *  @param categoryCount 			LinkedHashMap of Category(Key) and Count(value)
	 *  @param categoryOrderedList 		LinkedHashSet of Category and sub-Category 
	 */
	public ParserValues(LinkedHashMap<String, Integer> categoryCount, LinkedHashSet<String> categoryOrderedList) {
		this.categoryCount = categoryCount;
		this.categoryOrderedList = categoryOrderedList;
	}
	
	/**
	 * Getter method to retrieve the LinkedHashMap
	 * @return			LinkedHashMap object "categoryCount" consisting of the category and its count
	 * */
	public LinkedHashMap<String, Integer> getCategoryCount() {
		return categoryCount;
	}
	
	/**
	 * Getter method to retrieve the LinkedHashSet
	 * @return			LinkedHashSet object "categoryOrderedList" consisting of the category and its sub-category
	 * */
	public LinkedHashSet<String> getCategoryOrderedList() {
		return categoryOrderedList;
	}
	
	/**
	 * Instance method to return the count of a certain category, mainly used for testing purpose
	 * PRECONDITION/REQUIRED:
	 * @param category
	 * return 				count value for that category
	 *  
	 */
	public int getCountforCategory(String category){
		return categoryCount.get(category);
	}
		
}
