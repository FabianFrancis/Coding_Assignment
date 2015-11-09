package com.project.test;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.project.CategorySetParser;
import com.project.ParserValues;



public class TestCategorySetParser {

	BufferedWriter bw;
	CategorySetParser cp;
	ParserValues pr;
	static File testFile;
	
	private void writeLineToTestFile(String line) throws IOException{
		bw.write(line);
		bw.newLine();
		bw.flush();
	}
	
	/***
	 * Create a Test File before running tests
	 */
	@BeforeClass
	public static void createTestFile() throws IOException{
		testFile = new File("testFile.txt");
		testFile.createNewFile();
	}
	
	/**
	 * Create buffer writer and CatgoryParser object before each test
	 */
	@Before
	public void beforeTest() throws IOException{
		bw = new BufferedWriter(new FileWriter(new File("testFile.txt")));
		cp = new CategorySetParser(new String[] { "PERSON", "PLACE", "ANIMAL", "COMPUTER", "OTHER"}, new File ("testFile.txt"));
	}
	
	/***
	 * Delete test file after running tests.
	 */
	@AfterClass
	public static void deleteTestFile(){
		testFile = new File("testFile.txt");
		testFile.delete();
	}
	
	/**
	 * Create close buffer writer and delete test file after each test
	 */
	@After
	public void afterTest() throws IOException{
		bw.close();
		testFile = new File("testFile.txt");
		testFile.delete();
	}
	
	/**
	 * Test to check number of categories that were parsed to create result object
	 * Should be equal to 5 as 5 legal categories were passed to create CategroyParser
	 */
	@Test
	public void testParser_categoryCountSize() throws IOException {
		pr = cp.parse();
		assertTrue(pr.getCategoryCount().size() == 5 );
	}
	
	/**
	 * Test to check is count for the category is right
	 */
	@Test
	public void testParse_parseLegalCategoryCount() throws IOException {
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PERSON test1");
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("EXTRA illegal");
		pr = cp.parse();
		assertEquals(2, pr.getCountforCategory("PERSON"));
	}
	
	
	/**
	 * Test to duplicate are not considered for the count
	 */
	@Test
	public void testParse_ignoreDuplicates() throws IOException{
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PERSON test1");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("COMPUTER windows");
		writeLineToTestFile("COMPUTER windows");
		writeLineToTestFile("OTHER water");
		writeLineToTestFile("OTHER water");
		pr = cp.parse();
		assertEquals(2, pr.getCountforCategory("PERSON"));
		assertEquals(1, pr.getCountforCategory("PLACE"));
		assertEquals(1, pr.getCountforCategory("ANIMAL"));
		assertEquals(1, pr.getCountforCategory("COMPUTER"));
		assertEquals(1, pr.getCountforCategory("OTHER"));
	}
	
	
	/**
	 * Test to check order is maintained in the result object
	 */
	@Test
	public void testParse_order() throws IOException{
		String[] correctOrder = new String[] {"PERSON","ANIMAL", "PLACE", "COMPUTER", "OTHER"}; 
		int[] correctOrderCount = new int[] {2, 2, 1, 1, 1}; 
		writeLineToTestFile("PERSON test1");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("ANIMAL test1");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("ANIMAL test2");
		writeLineToTestFile("OTHER tree");
		writeLineToTestFile("PERSON test2");
		writeLineToTestFile("EXTRA illegal");
		pr = cp.parse();
		int i=0;
		for (Entry<String, Integer> entry : pr.getCategoryCount().entrySet()) {
			assertEquals(correctOrder[i], entry.getKey());
			assertTrue(correctOrderCount[i++]==entry.getValue());
		}
	}
	
	/**
	 * Test to check Legal Categories are parsed and counted
	 */
	@Test
	public void testParse_parseLegalCategory() throws IOException {
		writeLineToTestFile("PERSON John");
		writeLineToTestFile("PLACE Washington");
		writeLineToTestFile("ANIMAL Lion");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("OTHER laptop");
		pr = cp.parse();
		assertEquals(1, pr.getCountforCategory("PERSON"));
		assertEquals(1, pr.getCountforCategory("PLACE"));
		assertEquals(1, pr.getCountforCategory("ANIMAL"));
		assertEquals(1, pr.getCountforCategory("COMPUTER"));
		assertEquals(1, pr.getCountforCategory("OTHER"));
	}
	
	
	/**
	 * Test to check category count is zero when input file doesn't have the respective category
	 */
	@Test
	public void testParse_ZeroCountForCategory() throws IOException {
		writeLineToTestFile("PERSON John");
		writeLineToTestFile("PERSON Chris");
		writeLineToTestFile("PLACE Fairfax");
		writeLineToTestFile("PLACE Sterling");
		writeLineToTestFile("ANIMAL Lion");
		writeLineToTestFile("COMPUTER Mac");
		pr = cp.parse();
		assertEquals(0, pr.getCountforCategory("OTHER"));
	}
	
	
	/**
	 * Test to check illegal category is ignored for count
	 */
	@Test(expected = NullPointerException.class)
	public void testParse_doNotParseIllegalCategory() throws IOException {
		writeLineToTestFile("COOL test");
		writeLineToTestFile("REST test");
		writeLineToTestFile("RANK test");
		writeLineToTestFile("PLACE Reston");
		writeLineToTestFile("ANIMAL Tiger");
		writeLineToTestFile("COMPUTER Mac");
		pr = cp.parse();
		assertEquals(1, pr.getCountforCategory("PLACE"));
		assertEquals(1, pr.getCountforCategory("ANIMAL"));
		assertEquals(1, pr.getCountforCategory("COMPUTER"));
		pr.getCountforCategory("COOL");
	}
	
	
	/**
	 * Test to check empty file  parsing
	 */
	@Test
	public void testParser_emptyFile() throws IOException {
		pr = cp.parse();
		assertTrue(pr.getCategoryCount().size() == 5 );
		assertEquals(0, pr.getCountforCategory("PERSON"));
		assertEquals(0, pr.getCountforCategory("PLACE"));
		assertEquals(0, pr.getCountforCategory("ANIMAL"));
		assertEquals(0, pr.getCountforCategory("COMPUTER"));
		assertEquals(0, pr.getCountforCategory("OTHER"));
	}
	
	
	/**
	 * Test to check Category Pair Ordering
	 */
	@Test
	public void testParser_CategoryPairOrder() throws IOException {
		String[] correctOrder = new String[] {"PERSON test","PLACE test", "ANIMAL test", "PERSON test2", "COMPUTER Mac", "OTHER laptop"}; 
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("PERSON test2");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("OTHER laptop");
		pr = cp.parse();
		int i= 0;
		for(String entry:pr.getCategoryOrderedList()){
			assertTrue(correctOrder[i++].equals(entry));
		}

	}
	
	
	/**
	 * Test to check Category Pair Ordering ignores duplicates
	 */
	@Test
	public void testParser_CategoryPairIgnoreDuplicates() throws IOException {
		String[] correctOrder = new String[] {"PERSON test","PLACE test", "ANIMAL test", "COMPUTER Mac", "OTHER laptop"}; 
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("OTHER laptop");
		writeLineToTestFile("PERSON test");
		pr = cp.parse();
		int i= 0;
		for(String entry:pr.getCategoryOrderedList()){
			assertTrue(correctOrder[i++].equals(entry));
		}
	}
	
	
	/**
	 * Test to check empty lines in input file are ignored 
	 */
	@Test
	public void testParser_testEmptyPairLineInFile() throws IOException {
		String[] correctOrder = new String[] {"PERSON test","PLACE test", "ANIMAL test", "COMPUTER Mac", "OTHER laptop"}; 
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PERSON test");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile("");
		writeLineToTestFile("PLACE test");
		writeLineToTestFile(" ");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("ANIMAL test");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("COMPUTER Mac");
		writeLineToTestFile("OTHER laptop");
		writeLineToTestFile("PERSON test");
		pr = cp.parse();
		int i= 0;
		for(String entry:pr.getCategoryOrderedList()){
			assertTrue(correctOrder[i++].equals(entry));
		}
	}
	
	
	/**
	 * Test to check whitespace at start of lines  
	 */
	@Test
	public void testParser_whiteSpaceAtStartOfLine() throws IOException {
		writeLineToTestFile(" PERSON test1");
		writeLineToTestFile("PERSON test2");
		pr = cp.parse();
		assertEquals(1,pr.getCountforCategory("PERSON"));
	}
	

}
