/**
 * DynamicStringArr - Implementation of a dynamic string array
 */
public class DynamicStringArr
{
    protected String array[];
    protected int arraySize;
    protected static final int initialArraySize = 2;

    // Constructor
    public DynamicStringArr() {
        array = new String[initialArraySize];
        arraySize = 0;
    }

    /**
     * Replaces the value on index with newValue
     * @param index index to replace
     * @param newValue the new value
     */
    public void set(int index, String newValue) throws IndexOutOfBoundsException {
        if (index >= arraySize || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }
        array[index] = newValue;
    }

    /**
     * Gets back the value of that index position
     * @param index index to find in array
     * @return the value of given index
     */
    public String get(int index) throws IndexOutOfBoundsException {
        if (index >= arraySize || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        return array[index];
    }

    /**
     * Add new element to the array
     * @param newValue value to add
     */
    public void add(String newValue) {
        // if not at memory size, we don't need to reallocate memory
        if (arraySize < array.length) {
            array[arraySize] = newValue;
            arraySize++;
        }
        else {
            // Double the size
            int newSize = array.length*2;
            String newArray[] = new String[newSize];

            // copy back all values before the expand
            for (int i = 0; i < array.length; i++) {
                newArray[i] = array[i];
            }

            // add new entry
            newArray[array.length] = newValue;

            array = newArray;
            arraySize++;
        }
    }

    /**
     * Searches for the string value to get back its index
     * @param value index to remove
     */
    public int search(String value) {
        if (array != null) {
            for (int i = 0; i < arraySize; ++i) {
                if (array[i].equalsIgnoreCase(value)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Searches for the char value to get back its index
     * @param value index to remove
     */
    public int search(char value) {

        String tmpVal = String.valueOf(value);
        if (array != null) {
            for (int i = 0; i < arraySize; ++i) {
                if (array[i].equalsIgnoreCase(tmpVal)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * Searches for the index to remove it
     * @param index index to remove
     */
    public void removeStringAt(int index)
    {
        if (arraySize > 0)
        {
            for (int i = index; i < arraySize - 1; i++)
            {
                // move all to left
                array[i] = array[i + 1];
            }
            array[arraySize-1] = null;
            arraySize--;
        }
        else
        {
            System.out.println("Array is empty");
        }
    }

    /**
     * Return all values in array
     */
    public String returnAll() {

        String output = "";
        if (array != null) {
            for (int i = 0; i < arraySize; i++) {
                output = output + array[i] + " ";
            }
        }
        return output;
    } // end of print()

}
