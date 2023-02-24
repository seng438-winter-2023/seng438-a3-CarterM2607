import static org.junit.Assert.*;
import java.security.InvalidParameterException;
import org.jfree.data.DataUtilities;
import org.jfree.data.KeyedValues;
import org.junit.Before;
import org.junit.Test;
import org.jfree.data.Values2D;
import org.jmock.Mockery;
import org.jmock.Expectations;
import java.util.Arrays;


public class DataUtilitiesTest extends DataUtilities {
	private Mockery contextOnlyRows; //initialize private variables for test runs
    private Mockery contextOnlyCols;
    private Mockery contextBrokenRowsCols;
    private Mockery contextMockingKeyedValuesInput;
    private Mockery contextMockingKeyedValuesOutput;
    private Values2D onlyRowsValues2D;
    private Values2D onlyColsValues2D;
    private Values2D brokenRowsColsValues2D;
    private double[] testDouble1DArray;
    private Number[] testNumber1DArray;
    private double[][] testDouble2DArray;
    private Number[][] testNumber2DArray;
    private KeyedValues mockKVInputValues;
    private KeyedValues mockKVOutputValues;

/* Some tests that weren't possible
 * calculateRowTotal() & calculateColumnTotal() -> method sums the correct column/row
 *  WHY: Because this is black box testing, it's impossible to know 
 *  whether the program simply calculated the sum wrong or just didn't pick the right column/row
 * 
 */

 /*
  * Context for "testCalculateRowTotalThreeCols()"
  */
    @Before
    public void contextOnlyRowsSetup()throws Exception{
        contextOnlyRows = new Mockery();
        onlyRowsValues2D = contextOnlyRows.mock(Values2D.class);
        contextOnlyRows.checking(new Expectations(){{
            one(onlyRowsValues2D).getRowCount(); will(returnValue(3)); //make 3 rows
            one(onlyRowsValues2D).getColumnCount(); will(returnValue(1)); //make 1 column
            one(onlyRowsValues2D).getValue(0,0); will(returnValue(-1.0/3.0));
            one(onlyRowsValues2D).getValue(1,0); will(returnValue(4.0/3.0));
            one(onlyRowsValues2D).getValue(2,0); will(returnValue(3.0));
            }
        });
    }
   
   /*
    * Mocking context for "testCalculateColumnTotalThreeRows()"
    */
    @Before
    public void contextOnlyColsSetup() throws Exception{
        contextOnlyCols = new Mockery();
        onlyColsValues2D = contextOnlyCols.mock(Values2D.class);
        contextOnlyCols.checking(new Expectations(){{
            one(onlyColsValues2D).getRowCount(); will(returnValue(1)); //make 1 row
            one(onlyColsValues2D).getColumnCount(); will(returnValue(3)); //make 3 columns
            one(onlyColsValues2D).getValue(0,0); will(returnValue(-1.0/3.0));
            one(onlyColsValues2D).getValue(0,1); will(returnValue(4.0/3.0));
            one(onlyColsValues2D).getValue(0,2); will(returnValue(3.0));
            }
        });
    }
    //Creates a mock Values2D object that returns impossible parameters:
    //-1 columns
    //-5 rows
    @Before
    public void contextBrokenValues2DSetUp()throws Exception{
        contextBrokenRowsCols= new Mockery();
        brokenRowsColsValues2D = contextBrokenRowsCols.mock(Values2D.class);
        contextBrokenRowsCols.checking(new Expectations(){{
            one(brokenRowsColsValues2D).getColumnCount(); will(returnValue(-1));
            one(brokenRowsColsValues2D).getRowCount(); will(returnValue(-5));
        }});
    }
    //creating a Number[] object
	@Before
    public void contextNumberArraySetup(){
        testDouble1DArray = new double[3];
        testNumber1DArray = new Number[3];
        testDouble1DArray[0] = -0.000000001;
        testDouble1DArray[1] = Math.E;
        testDouble1DArray[2] = Math.PI;
        for(int i = 0; i < 3; i++){
            testNumber1DArray[i] = Double.valueOf(testDouble1DArray[i]);
        }
    }
    //create a Number[][] object
    @Before
    public void contextNumberMatrixSetup(){
        testDouble2DArray =  new double[3][3];
        testNumber2DArray = new Number[3][3];
        for(int i = 0; i < testDouble2DArray.length; i++){
            for(int j=0; j<testDouble2DArray[i].length;j++){
                if((i + j) % 2 == 0){
                    testDouble2DArray[i][j] = Double.valueOf(Math.pow(-1,j*(i+1))*Math.PI);
                }
                else{
                    testDouble2DArray[i][j] = Double.valueOf(Math.pow(-1,j*(i+1))*Math.E);
                }
                testNumber2DArray[i][j] = Double.valueOf(testDouble2DArray[i][j]);
            }
        }
    }
    //Mock KeyedValues input value for getCumulativePercentages()
    @Before
    public void setupContextMockingKeyedValuesInput(){
        contextMockingKeyedValuesInput = new Mockery();
        mockKVInputValues = contextMockingKeyedValuesInput.mock(KeyedValues.class);
        contextMockingKeyedValuesInput.checking(new Expectations(){{
            //getValue(Comparable Object) OR getValue(int index);
            String[] arr = {"Key1","Key2","Key3"};
            double[] values = {1,2,3};
            int itemCount = 3;
            for(int i = 0; i<values.length;i++){
                atLeast(1).of(mockKVInputValues).getValue(i); 
                will(returnValue(Double.valueOf(values[i])));
                atLeast(1).of(mockKVOutputValues).getValue(arr[i]); 
                will(returnValue(Double.valueOf(values[i])));
                atLeast(1).of(mockKVInputValues).getKey(i); 
                will(returnValue(arr[i]));
            }
            allowing(mockKVInputValues).getItemCount(); will(returnValue(itemCount));
            allowing(mockKVInputValues).getKeys(); will(returnEnumeration(Arrays.asList(arr)));
        }});
    }
     //Mock KeyedValues output value for getCumulativePercentages()
    @Before
    public void setupContextMockingKeyedValueOutput(){
        contextMockingKeyedValuesOutput = new Mockery();
        mockKVOutputValues = contextMockingKeyedValuesOutput.mock(KeyedValues.class);
        contextMockingKeyedValuesOutput.checking(new Expectations(){{
            String[] arr = {"Key1","Key2","Key3"};
            double[] values = {(double)1.0/(1.0+2.0+3.0),(1.0+2.0)/(1.0+2.0+3.0),(1.0+2.0+3.0)/(1.0+2.0+3.0)};
            int itemCount = 3;
            for(int i = 0; i<values.length;i++){
                allowing(mockKVOutputValues).getValue(i); will(returnValue(Double.valueOf(values[i])));
                allowing(mockKVOutputValues).getValue(arr[i]); will(returnValue(Double.valueOf(values[i])));
                allowing(mockKVOutputValues).getKey(i); will(returnValue(arr[i]));
            }
            allowing(mockKVOutputValues).getItemCount(); will(returnValue(itemCount));
        }
        });
    }
    //Testing calculateColumnTotal() with a Values2D object with 3 rows, 1 column
    @Test
	public void testCalculateColumnTotalThreeRows(){
        double actual = DataUtilities.calculateColumnTotal(onlyRowsValues2D, 0);
        double expected = 4;
        double delta = 0;
        System.out.println("Testing calculateColumnTotal with 3 rows");
        try{
            assertEquals(expected,actual,delta);
            System.out.println("PASSED. Expected: " + expected + " , was " + actual + "");
        }catch(AssertionError e){
            System.out.println("FAILED!!! Expected: " + expected + " but was " + actual + "");
            fail();
        }
    }
    //Testing calculateColumnTotal() with a Values2D object with 1 row, 3 columns
    @Test
    public void testCalculateRowTotalThreeCols(){
        double actual = DataUtilities.calculateRowTotal(onlyColsValues2D, 0);
        double expected = 4;
        double delta = 0;
        String errMsg2 = "FAILED!!! Expected: " + expected + " but was " + actual + "";
        System.out.println("\nTesting calculateRowTotal with 3 columns:");
        try{
            assertEquals(expected,actual,delta);
            System.out.println("PASSED. Expected: " + expected + " , was " + actual + "");
        }catch(AssertionError e){
            System.out.println(errMsg2);
            fail();
        }
    }
    //calculateColumnTotal() should return "0.0" for an invalid input
    @Test
    public void testCalculateColumnTotalZeroForInvalidColumns(){
        System.out.println("\nTesting calculateColumnTotal with"+
        " an invalid columns input of -1: ");
        double actual = DataUtilities.calculateColumnTotal(brokenRowsColsValues2D, -1);
        double expected = 0.0;
        double delta = 0;
        String errMsg2 = "FAILED!!! Expected: " + expected + " but was " + actual + "";
        try{
            assertEquals(expected,actual,delta);
            System.out.println("PASSED. Expected: " + expected + " , was " + actual + "");
        }catch(Exception e){
            System.out.println(errMsg2);
            fail();
        }
    }
     //calculateRowTotal() should return "0.0" for an invalid input
    @Test
    public void testCalculateRowTotalZeroForInvalidRows(){
        System.out.println("\nTesting calculateRowTotal with"+
        " an invalid row input of -1: ");
        double actual = DataUtilities.calculateRowTotal(brokenRowsColsValues2D, -1);
        double expected = 0.0;
        double delta = 0;
        String errMsg2 = "FAILED!!! Expected: " + expected + " but was " + actual + "";
        try{
            assertEquals(expected,actual,delta);
            System.out.println("PASSED. Expected: " + expected + " , was " + actual + "");
        }catch(Exception e){
            System.out.println(errMsg2);
            fail();
        }
    }
    //Tests whether calculateRowTotal() 
    //throws an InvalidParameterException for an invalid input
    @Test
    public void testCalculateRowTotalThrowsInvalidParameterException(){
        System.out.println("\nTesting calculateRowTotal(Values2D data) with"+
        " an incompatible Values2D object on row 0: ");
        try{
            var output = DataUtilities.calculateRowTotal(brokenRowsColsValues2D, -1);
            System.out.println("FAILED!!! Expected java.security.InvalidParameterException, but was " + output);
            fail();
        }catch(InvalidParameterException e){
            System.out.println("PASSED. Expected: java.security.InvalidParameterException\n" + "Was: "+e.toString());
        }
    }
    //Tests whether calculateColumnTotal() 
    //throws InvalidParameterException for an invalid input
    @Test
    public void testCalculateColumnTotalThrowsInvalidParameterException(){
        System.out.println("\nTesting calculateColumnTotal(Values2D data) with"+
        " an incompatible Values2D object on row 0: ");
        try{
            var output = DataUtilities.calculateColumnTotal(brokenRowsColsValues2D, -1);
            System.out.println("FAILED!!! Expected java.security.InvalidParameterException, but was " + output);
            fail();
        }catch(InvalidParameterException e){
            System.out.println("PASSED. Expected: java.security.InvalidParameterException\n" + "Was: "+e.toString());
        }
    }
   
   //Tests createNumberArray -> resulting Number Array should be the same
   //length as the input array.
    @Test
    public void testCreateNumberArrayReturnsCorrectLength(){
        System.out.println("\nTesting createNumberArray(double[] data)");
        Number[] outputArray = DataUtilities.createNumberArray(testDouble1DArray);
        try{
            assertEquals(String.format("Failed!!! Expected %d but was % d",Integer.valueOf(testNumber1DArray.length),Integer.valueOf(outputArray.length)), outputArray.length);
            System.out.println(String.format("Passed. Expected %d, and was % d",Integer.valueOf(testNumber1DArray.length),Integer.valueOf(outputArray.length)));
        }
        catch(AssertionError e){
            System.out.println("FAILED!!!\n");
            System.out.println("Length does not match (expected "+ testDouble1DArray.length + ", but was " + outputArray.length + ")");
            fail();
        }
        catch(Exception exception){
            System.out.println("Unexpected exception thrown +\""+exception.toString()+"\"");
            fail();
        }
    }
    //Tests if createNumberArray() returns the correct data in the correct order.
    @Test
    public void testCreateNumberArray(){
        System.out.println("\nTesting createNumberArray(double[] data)");
        Number[] outputArray = DataUtilities.createNumberArray(testDouble1DArray);
        try{
            assertArrayEquals(testNumber1DArray, outputArray);
            System.out.println("PASSED. Output array contains the expected data in the correct order");
        }
        catch(AssertionError e){
                System.out.println("FAILED!!!\n");
                if(outputArray.length != testDouble1DArray.length){
                    System.out.println("Length does not match (expected "+ testDouble1DArray.length + ", but was "
                    + outputArray.length + ")");
                }
                for(int i = 0; i < outputArray.length; i++){
                    Integer index = Integer.valueOf(i);
                    try{
                        if(testNumber1DArray[i].doubleValue() != outputArray[i].doubleValue()){
                            String msg = String.format("\nAt index [%d]: \n\tExpected: %lf but was %lf",index,testNumber1DArray[i],outputArray[i]);
                            System.out.println(msg);
                        }
                    }catch(NullPointerException n){
                        String msg = String.format("\nAt index [%d]: \n\tExpected: %f but was NULL instead",index,testNumber1DArray[i]);
                        System.out.println(msg);
                    }
            }
            fail();
        }
    }
    //Tests if createNumberArray2D() has the correct length (number of rows)
    @Test
    public void createNumberArray2DHasCorrectLength(){
        System.out.println("\nTesting createNumberArray2D() for correct length");
        Number[][] actualArray = createNumberArray2D(testDouble2DArray);
        int expectedLength = testDouble2DArray.length;
        int actualLength = actualArray.length;
        assertEquals(String.format("FAILED. Expected %d, but was %d",Integer.valueOf(expectedLength),Integer.valueOf(actualLength)),expectedLength, actualLength,0);
        System.out.println(String.format("PASSED. Expected %d, was %d",Integer.valueOf(expectedLength),Integer.valueOf(actualLength)));
    }
    //Tests if createNumberArray2D() has the correct number of columns
    @Test
    public void createNumberArray2DHasCorrectNumberOfColumns(){
        System.out.println("\nTesting createNumberArray2D() for correct number of columns");
        Number[][] actualArray = createNumberArray2D(testDouble2DArray);
        int expected = testDouble2DArray[0].length;
        int actual = actualArray[0].length;
        assertEquals(String.format("FAILED. Expected %d columns, but was %d columns",Integer.valueOf(expected),Integer.valueOf(actual)),expected,actual,0);
        System.out.println(String.format("Passed. Expected %d columns, was %d columns",Integer.valueOf(expected),Integer.valueOf(actual)));
    }
    public void testCreateNumberArray2D(){
        System.out.println("\nTesting createNumberArray2D(double[][] data)");
        Number[][] outputArray = DataUtilities.createNumberArray2D(testDouble2DArray);
        try{
            int p = 0;
            for(Number[] actual: outputArray){
                assertArrayEquals(testNumber2DArray[p], actual);
                p++;
            }
            System.out.println("PASSED. Output 2D-array contains the expected data in the correct order");
        }
        catch(AssertionError e){
            System.out.println("\nFAILED!!!\n");
                for(int i=0;i<outputArray.length;i++){
                    for(int j=0;j<outputArray[i].length;j++){
                        if(outputArray[i][j] != null && (testDouble2DArray[i][j] != outputArray[i][j].doubleValue())){
                            System.out.println("At position ("+i+", " + j + "):\n"+
                            "\tValue does not match the expected value (expected " + testDouble2DArray[i][j] +
                            ", but was " + outputArray[i][j].doubleValue() + ")");
                            fail();
                        }
                        else{
                            if(outputArray[i][j] == null){
                                System.out.println("At position ("+i+", " + j + "):\n"+
                            "\tValue does not match the expected value (expected " + testDouble2DArray[i][j] +
                            ", but was null)");
                            }
                        }
                    }
                }
        }catch(Exception n){
            System.out.println("Unexpected exception thrown by method: "  + n.toString());
            fail();
        }
    }

    //testing if getCumulativePercentages() returns the correct value for each row
    @Test
    public void testGetCumulativePercentagesReturnsCorrectRowValues(){
        System.out.println("Testing getCumulative percentages");
        KeyedValues actual = DataUtilities.getCumulativePercentages(mockKVInputValues);
        boolean failFlag = false;
        try{
            if(!(mockKVOutputValues.getValue("Key1").equals(actual.getValue("Key1")))){
                System.out.println("FAILED!!!");
                String expectedValue =  mockKVOutputValues.getValue("Key1").toString();
                String actualValue = actual.getValue("Key1").toString();
                System.out.println(String.format("Expected: (Key1, %s), but was (Key1,%s)",expectedValue,actualValue));
                failFlag = true;
            }
            else{
                System.out.println("PASSED.");
                String expectedValue =  mockKVOutputValues.getValue("Key1").toString();
                String actualValue = actual.getValue("Key1").toString();
                System.out.println(String.format("Expected: (Key1, %s), was (Key1,%s)",expectedValue,actualValue));
            }
            if(!(mockKVOutputValues.getValue("Key2").equals(actual.getValue("Key2")))){
                System.out.println("FAILED!!!");
                String expectedValue =  mockKVOutputValues.getValue("Key2").toString();
                String actualValue = actual.getValue("Key2").toString();
                System.out.println(String.format("Expected: (Key2, %s) but was (Key2,%s)",expectedValue,actualValue));
                failFlag = true;
            }
            else{
                System.out.println("PASSED.");
                String expectedValue =  mockKVOutputValues.getValue("Key2").toString();
                String actualValue = actual.getValue("Key2").toString();
                System.out.println(String.format("Expected: (Key2, %s), was (Key2,%s)",expectedValue,actualValue));
            }
            if(!(mockKVOutputValues.getValue("Key3").equals(actual.getValue("Key3")))){
                System.out.println("FAILED!!!");
                String expectedValue =  mockKVOutputValues.getValue("Key3").toString();
                String actualValue = actual.getValue("Key3").toString();
                System.out.println(String.format("Expected: (Key3, %s) but was (Key3,%s)",expectedValue,actualValue));
                failFlag = true;
            }
            else{
                System.out.println("PASSED.");
                String expectedValue =  mockKVOutputValues.getValue("Key3").toString();
                String actualValue = actual.getValue("Key3").toString();
                System.out.println(String.format("Expected: (Key3, %s), was (Key3,%s)",expectedValue,actualValue));
            }
            if(failFlag == true){
                fail();
            }
        }catch(Exception e){
            System.out.println("FAILED!!!");
            System.out.println("Unexpected exception thrown by method: \""
            +e.toString()+"\"");
            fail();
        }
    }
    //Tests if getCumulativePercentages() throws an InvalidParameterException for
    //invalid KeyedValues input
    @Test
    public void getCumulativePercentagesThrowsInvalidParameterException(){
        try{
            KeyedValues output = DataUtilities.getCumulativePercentages(null);
            fail();
        }catch(InvalidParameterException exception){
            System.out.println(String.format("Expected: java.security.InvalidParameterException, was %s",exception.toString()));
        }
        catch(Exception exception){
            System.out.println("FAILED!!!");
            System.out.println(String.format("Expected: java.security.InvalidParameterException,but was %s",exception.toString()));
            fail();

        }
    }
    //Testing getCumulativePercentages() returns the correct number of items in its output.
    @Test
    public void testGCPReturnsCorrectNoRows(){
        KeyedValues output = getCumulativePercentages(mockKVInputValues);
        assertEquals(mockKVOutputValues.getItemCount(),output.getItemCount());
    }
}

