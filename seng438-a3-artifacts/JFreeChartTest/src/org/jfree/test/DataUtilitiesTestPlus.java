package org.jfree.test;
import static org.junit.Assert.*;
import org.jfree.data.*;
import org.junit.Before;
import org.junit.Test;
import org.jmock.Mockery;
import org.jmock.internal.ExpectationBuilder;
import org.jmock.Expectations;
//Could optimize by separating the tests that don't need the same mocking context
public class DataUtilitiesTestPlus{
	private Mockery contextValues2D;
	private Values2D mockValues2D;
	private Mockery mockingContextKVInput;
	private Mockery mockingContextKVOutput;
	private KeyedValues mockKVInput;
	private KeyedValues mockKVOutput;
	@Before
	public void setUpContextKeyedValuesInput(){
		mockingContextKVInput = new Mockery();
		mockKVInput = mockingContextKVInput.mock(KeyedValues.class);
		String[] keys = {"KEY 0","KEY 1","KEY 2","KEY 3"};
		Number[] expectedOutputs = {Integer.valueOf(1),null, Integer.valueOf(2),Double.valueOf(1.0/3.0)};
		mockingContextKVInput.checking(new Expectations(){{
			atLeast(1).of(mockKVInput).getItemCount();
			will(returnValue(4));
			for(int i = 0; i < 4; i++){
				atLeast(1).of(mockKVInput).getValue(i);
				will(returnValue(expectedOutputs[i]));
				oneOf(mockKVInput).getKey(i);
				will(returnValue(keys[i]));
			}
		}});
		//Key-Value mapping:
		/*
		 * "KEY 0" -> 1
		 * "KEY 1" -> NULL
		 * "KEY 2" -> 2
		 * "KEY 3" -> 
		 */
		

	}
	@Before 
	public void setUpContextKeyedValuesOutput(){

	}
	@Before
	public void setUpContextValues2D(){
		//Setting up a Values2D object that will return a unique sum for all rows and columns
		contextValues2D = new Mockery();
		mockValues2D = contextValues2D.mock(Values2D.class);
		contextValues2D.checking(new Expectations(){{
			oneOf(mockValues2D).getColumnCount(); will(returnValue(5));
			oneOf(mockValues2D).getRowCount(); will(returnValue(5));
			double writeValue = 0;
			for(int i = 0; i < 4; i++){
				for(int j = 0; j < 5; j++){
					allowing(mockValues2D).getValue(i, j);
					if(j == 4){
						will(returnValue(null)); continue;
					}
					else if(j % 2 == 0 && i != j)
						writeValue = (-10*(i+1));
					else if(i == j)
						writeValue = (double)i/(double)3;
					else
						writeValue = 1;
					will(returnValue(writeValue));
				}
				System.out.println();
			}
			for(int p = 0; p < 5; p++){
				allowing(mockValues2D).getValue(4,p);
				will(returnValue(null));
			}
		}});
		/* 
			The Values2D object should look like:
			i = 0: {   0,    1,   -10,    1, null} -> SUM =   -8
			i = 1: { -20,  1/3,   -20,    1, null} -> SUM = -116/3
			i = 2: { -30,    1,   2/3,    1, null} -> SUM =  -82/3
			i = 3: { -40,    1,   -40,    1, null} -> SUM =  -78
			i = 4: {null, null,	 null, null, null} -> SUM =  0
			__________________________________________________
			total: { -90, 10/3,-208/3,    4, null} (of columns)
			This will make sure that all of the sums are unique
			*/
	}
	@Test
	public void testCalculateColumnTotalTwoArguments(){
		double expected = 3 + 1/3;
		double actual = DataUtilities.calculateColumnTotal(mockValues2D, 1);
		assertEquals("FAILED: Sum does not match (expected " + expected + ", but was "+actual+")",expected,actual,0);
	}
	@Test
	public void testCalculateColumnTotalThreeArgs(){
		double expected = (double)1 + (double)1/(double)3;
		int rows[] = {1,2,4};
		try{
			double actual = DataUtilities.calculateColumnTotal(mockValues2D, 1,rows);
			assertEquals("FAILED: Sum does not match",expected,actual,0);
			System.out.println(String.format("PASSED: Expected %f, was %f.",Double.valueOf(expected),Double.valueOf(actual)));
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	} 
	@Test 
	public void testCalculateRowTotalTwoArguments(){
		double expected = -8;
		double actual = DataUtilities.calculateRowTotal(mockValues2D, 0);
		assertEquals("FAILED: Sum does not match (expected -8, but was "+actual+")",expected,actual,0);
	}
	@Test
	public void testCalculateRowTotalThreeArgs(){
		double expected = -9;
		int cols[] = {2,3,4};
		try{
			double actual = DataUtilities.calculateRowTotal(mockValues2D, 0,cols);
			assertEquals("FAILED: Sum does not match (expected " + expected + " but was "+actual+")",expected,actual,0);
			System.out.println(String.format("PASSED: Expected %f, was %f.",Double.valueOf(expected),Double.valueOf(actual)));
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	}
	@Test
	public void testCreateNumberArray(){
		double[] data = new double[3];
		Number[] expecteds = new Number[3];
		for(int i = 0; i < 3; i++){
			data[i] = Math.PI;
			expecteds[i]= Double.valueOf(data[i]);
		}
		try{
			Number[] actual = DataUtilities.createNumberArray(data);
			assertArrayEquals(expecteds, actual);
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	}
	@Test
	public void testCreateNumberArray2D(){
		double[][] data = new double[3][3];
		Number[][] expecteds = new Number[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				data[i][j] = j % 2 == 0 ? (double)1/3 : (double)-1/3;
				expecteds[i][j] = Double.valueOf(data[i][j]);
			}
		}
		try{
			Number[][] actual = DataUtilities.createNumberArray2D(data);
			for(int i = 0; i < 3; i++){
				assertArrayEquals(expecteds[i], actual[i]);
			}
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	}
	@Test
	public void testDataUtilitiesCloneForValidArray(){
		double[][] data = new double[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				data[i][j] = j % 2 == 0 ? (double)1/3 : (double)-1/3;
			}
		}
		try{
			double[][] output = DataUtilities.clone(data);
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					assertEquals("FAILED: data at ["+i+"]["+j+"] does not match expected value", data[i][j],output[i][j],0);
				}
			}
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	}
	@Test
	public void testDataUtilitiesCloneForInvalidArray(){
		double[][] data = new double[3][3];
		for(int i = 0; i < 3; i++){
			for(int j = 0; j < 3; j++){
				data[i][j] = j % 2 == 0 ? (double)1/3 : (double)-1/3;
			}
		}
		try{
			double[][] output = DataUtilities.clone(data);
			for(int i = 0; i < 3; i++){
				for(int j = 0; j < 3; j++){
					assertEquals("FAILED: data at ["+i+"]["+j+"] does not match expected value", data[i][j],output[i][j],0);
				}
			}
		}catch(AssertionError ae){
			System.out.println(ae.toString());
			fail();
		}
		catch(Exception e){
			System.out.println("FAILED: Unexpected exception thrown \""+e.toString()+"\"");
			fail();
		}
	}
	@Test 
	public void testDataUtilitiesEqualsMethodForUnequal2DArrays(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = new double[2][2];
		double[][] testMatrix2 = new double[2][2];
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				testMatrix1[i][j] = counter++;
				testMatrix2[i][j] = -testMatrix1[i][j];
			}
		}
		assertFalse(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualMethodForEqual2DArrays(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for equal 2D double arrays");
		double[][] testMatrix1 = new double[2][2];
		double[][] testMatrix2 = new double[2][2];
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				testMatrix1[i][j] = counter++;
				testMatrix2[i][j] = testMatrix1[i][j];
			}
		}
		assertTrue(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualMethodForDifferentCols2DArrays(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = new double[2][2];
		double[][] testMatrix2 = new double[2][3];
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				testMatrix1[i][j] = counter++;
				testMatrix2[i][j] = testMatrix1[i][j];
			}
		}
		for(int i = 0; i < 2; i++){
			testMatrix2[i][2] = -(counter--);
		}
		assertFalse(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualsMethod4(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = new double[2][2];
		double[][] testMatrix2 = new double[3][3];
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				testMatrix1[i][j] = counter++;
				testMatrix2[i][j] = testMatrix1[i][j];
			}
		}
		for(int i = 0; i < 3; i++){
			for(int j = 0; i < 3; i++){
				testMatrix2[i][j] = -(counter--);
			}
		}
		assertFalse(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualsMethod5(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = new double[2][2];
		double[][] testMatrix2 = null;
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				testMatrix1[i][j] = counter++;
			}
		}
		assertFalse(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualsMethod6(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = null;
		double[][] testMatrix2 = new double[3][3];
		int counter = 0;
		for(int i = 0; i < 2; i++){
			for(int j = 0; j < 2; j++){
				
				testMatrix2[i][j] = counter++;
			}
		}
		assertFalse(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test 
	public void testDataUtilitiesEqualsMethod7(){
		System.out.println("Testing DataUtilities.equal(double[][] a, double[][] b) for unequal 2D double arrays");
		double[][] testMatrix1 = null;
		double[][] testMatrix2 = null;
		assertTrue(DataUtilities.equal(testMatrix1,testMatrix2));
	}
	@Test
	public void testKeyedValues(){

	}
}
