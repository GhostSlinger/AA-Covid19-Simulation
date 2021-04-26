/**
 * DynamicIntArr - Implementation of a dynamic integer array
 */
public class DynamicIntArr
{
    protected int array[];
    protected int arraySize;
    protected static final int initialArraySize = 2;

    // Constructor
    public DynamicIntArr() {
        array = new int[initialArraySize];
        arraySize = 0;
    }

    /**
     * Replaces the value on index with newValue
     * @param index index to replace
     * @param newValue the new value
     */
    public void set(int index, int newValue) throws IndexOutOfBoundsException {
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
    public int get(int index) throws IndexOutOfBoundsException {
        if (index >= arraySize || index < 0) {
            throw new IndexOutOfBoundsException("Supplied index is invalid.");
        }

        return array[index];
    }

    /**
     * Add new element to the array
     * @param newValue value to add
     */
    public void add(int newValue) {
        // if not at memory size, we don't need to reallocate memory
        if (arraySize < array.length) {
            array[arraySize] = newValue;
            arraySize++;
        }
        else {
            // Double the size
            int newSize = array.length*2;
            int newArray[] = new int[newSize];

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
     * Searches for the value to get back its index
     * @param value index to remove
     */
    public int search(int value) {
        if (array != null) {
            for (int i = 0; i < arraySize; ++i) {
                if (array[i] == value) {
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
    public void removeIntAt(int index)
    {
        if (arraySize > 0)
        {
            for (int i = index; i < arraySize - 1; i++)
            {
                // move all to left
                array[i] = array[i + 1];
            }
            array[arraySize-1] = -1;
            arraySize--;
        }
        else
        {
            System.out.println("Array is empty");
        }
    }

}
