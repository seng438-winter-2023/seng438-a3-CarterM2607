package org.jfree.data.test;

import static org.junit.Assert.*;

import org.jfree.data.Range;
import org.junit.*;

public class RangeTest {
	
	//because this is the Range class, all the methods in here don't use getters or setters, instead they use the values inputed into the
	//thus we don't need to use Jmock for any of these methods to be fully tested because we are the ones entering the values

	//copied form the given code in the Gitgub 
	private Range exampleRange;
    @BeforeClass public static void setUpBeforeClass() throws Exception {
    }

    
    //copied form the given code in the Gitgub. 
    //Creates a new Range object with a range of -1 to 1
    @Before
    public void setUpq() throws Exception { 
    	exampleRange = new Range(-1, 1);
    }


    //this isn't a test we created as it was copied from the Github but it does still test he getCentralValue method
    //getCenterValueTest tests the getCenterValue method which should return the median of the two end points of the range 
    //in the case of Range(-1, 1),  -1 to 1 should be 0
    @Test
    public void getCentraValueTest() {
    	
    	//create a expected return value
    	double expected = 0.0;
        
    	//try the getCentraValue method
    	try {
    		
    		System.out.print("Testing Range.getCentraValue: "); 
    		assertEquals("The central value of -1 and 1 should be 0\n",
    		        expected, exampleRange.getCentralValue(), .000000001d);
    		System.out.print("Test Passed \n");
    		System.out.println(String.format("Expected \"%s\" and was \"%s\" \n", expected, exampleRange.getCentralValue()));
    		
    	}catch(AssertionError e) {	//if error was thrown then the expected value was not returned
    		
    		System.out.print("Test FAILED\n");
			System.out.println(e.toString() + "\n");
			fail();
    		
    	}
    	
    	
    }
    
    //getLengthTest tests the getLength method which should returns the length between the two end points 
    //in the case of Range(-1, 1) it should return 2 since 1 - (-1) = 2
    @Test
    public void getLengthTest() {
    	
    	//create the expected return value
    	double expected = 2.0;
    	
    	//try the getLength method
    	try {
    		
    		System.out.print("Testing Range.getLength: "); 
    		assertEquals("The length value of -1 and 1 should be 2.0\n",
    				expected, exampleRange.getLength(), .000000001d);
    		System.out.print("Test Passed \n");
    		System.out.println(String.format("Expected \"%s\" and was \"%s\" \n", expected, exampleRange.getLength()));
    		
    	}catch(AssertionError e) {	//if error was thrown then the expected value was not returned
    		
    		System.out.print("Test FAILED\n");
			System.out.println(e.toString() + "\n");
			fail();
    		
    	}
    }
    
    //getLowerBoundTest test the getLowerBound method which should return the lower bound Range
    //in the case of Range(-1, 1) it should be -1
    @Test
    public void getLowerBoundTest() {
    	
    	//create the expected return value
    	double expected = -1.0;
    	
    	//try the getLowerBound method
    	try {
    		
    		System.out.print("Testing Range.getLowerBound: "); 
    		assertEquals("The lower bound value for the range should be -1.0",
    				expected, exampleRange.getLowerBound(), .000000001d);
    		System.out.print("Test Passed \n");
    		System.out.println(String.format("Expected \"%s\" and was \"%s\" \n", expected, exampleRange.getLowerBound()));
    		
    	}catch(AssertionError e) {	//if the AssertionError is thrown that means the expected value was not returned
    		
    		System.out.print("Test FAILED\n");
			System.out.println(e.toString() + "\n");
			fail();
    		
    	} 
    }

    //getUpperBoundTest tests the getUpperBound method which should return the upper bound Range
    //in the case of Range(-1, 1) it should be 1
    @Test
    public void getUpperBoundTest() {
    	
    	//create the expected return value
    	double expected = 1.0;
    	
    	//try the getUpperBound method
    	try {	
    		
    		System.out.print("Testing Range.getUpperBound: "); 
    		assertEquals("The upper bound value for the range should be 1:",
    				expected, exampleRange.getUpperBound(), .000000001d);
    		System.out.print("Test Passed \n");
    		System.out.println(String.format("Expected \"%s\" and was \"%s\" \n", expected, exampleRange.getUpperBound()));
    	
    	} catch(AssertionError e) {	//if the AssertionError is thrown that means the expected value was not returned 
    		
    		System.out.print("Test FAILED\n");
			System.out.println(e.toString() + "\n");
			fail();
    	
    	}
        
    }
    
    //toStringTest test the toString overloaded method which should return "Range[(double)lower, (double)upper]"
    //In the case of Range(-1, 1) it should return "Range[-1.0, 1.0]"
    @Test
    public void toStringTest() {
    	
		//created the expected return string
		String expected = "Range[-1.0, 1.0]";
			
		//try the toString method
		try{
			
			System.out.print("Testing Range.toString(): "); 
			assertEquals("Result does not match the expected output:\n", expected, exampleRange.toString());
			System.out.print("Test PASSED\n");
			System.out.println(String.format("Expected \"%s\" and was \"%s\" \n",expected, exampleRange.toString()));
		
		}catch(AssertionError e){		//if the AssertionError is thrown that means the expected value was not returned
			
			System.out.print("Test FAILED\n");
			System.out.println(e.toString() + "\n");
			fail();
		
		}catch(Exception e){	//if an unknown error occurred, print out Test failed, the error, and the expected value
			
			System.out.print("Test FAILED \nUnexpected exception thrown: \"" + 
					e.toString() + "\" \n");
			System.out.println("Expected: \"" + expected + "\" \n");
			fail();
		
		}
    }
    @After
    public void tearDown() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }
}
