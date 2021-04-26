import java.io.PrintWriter;

public class AdjacencyMatrix extends AbstractGraph {

    private static int numVertex;
    private static int[][] matrix;
    private static DynamicStringArr vertexList = new DynamicStringArr();
    private static DynamicStringArr vertexState = new DynamicStringArr();
    boolean StartAddVertex = true;
    boolean StartAddEdge = true;
    boolean StartDeleteVertex = true;
    boolean StartDeleteEdge = true;
    long StartAE = 0;
    long EndAE = 0;
    long StartDE = 0;
    long EndDE = 0;
    long StartAV = 0;
    long EndAV = 0;
    long StartDV = 0;
    long EndDV = 0;

    protected static final int initialMatrixSize = 2;
    //Actual matrix size
    protected int matrixSize;

    public AdjacencyMatrix() {

        //resize matrix
        matrix = new int[initialMatrixSize][initialMatrixSize];

    } // end of AdjacencyMatrix()

    public int getNumVertex()
    {
        return numVertex;
    }

    public void growMatrix()
    {
        // Increase the size of matrix by double
        int newSize = 2*matrix.length;
        int[][] newMatrix = new int[newSize][newSize];

        //copy values
        for(int i=0; i < matrix.length; i++)
            for(int j=0; j < matrix.length; j++)
                newMatrix[i][j]=matrix[i][j];

        matrix = newMatrix;
        System.out.println("Row length: " + matrix.length);
        System.out.println("Col length: " + matrix[0].length);
    }

    public int getVertIndex(String vertLabel)
    {
        int idx = -1;
        for (int i=0; i < vertexList.arraySize; i++)
        {
            if (vertLabel.equalsIgnoreCase(vertexList.get(i)))
            {
                idx = i;
            }
        }
        return idx;
    }

    public void addVertex(String vertLabel) {
        if(StartAddVertex)
            StartAV = System.nanoTime();
        StartAddVertex = false;
        if(vertexList.search(vertLabel) == -1)
        {
            vertexList.add(vertLabel);
            vertexState.add("S");

            if(matrixSize < matrix.length)
            {
                //Initialize vertex value to 0 in adj matrix
                for (int i=0; i < matrixSize; i++)
                {
                    matrix[numVertex][i] = 0;
                    matrix[i][numVertex] = 0;
                }
                numVertex++;        //Increment vertex
                matrixSize++;		//Increment matrix actual size
            }
            else
            {
                growMatrix();
                for (int i=0; i < matrixSize; i++)
                {
                    matrix[numVertex][i] = 0;
                    matrix[i][numVertex] = 0;
                }
                numVertex++;        //Increment vertex
                matrixSize++;		//Increment matrix actual size
            }

        }
        else
        {
            System.out.println("Vertex already exist..");
        }

    } // end of addVertex()

    public void deleteVertex(String vertLabel) {
        if(StartDeleteVertex)
            StartDV = System.nanoTime();
        StartDeleteVertex = false;
        int idx = -1;
        idx = getVertIndex(vertLabel);
        DynamicStringArr tempList = new DynamicStringArr();

        if (idx > -1)
        {

            //removing vertex from vertexList

            vertexList.removeStringAt(idx);
            vertexState.removeStringAt(idx);

            //removing vertex from matrix
            int i = idx;
            while(i<numVertex-1)
            {
                // moving col left
                for (int j = 0; j < numVertex; j++) {
                    matrix[j][i] = matrix[j][i+1];
                    matrix[j][i+1] = 0;
                }

                // moving row up
                for (int j = 0; j < numVertex; j++) {
                    matrix[i][j] = matrix[i+1][j];
                    matrix[i+1][j] = 0;
                }
                i++;
            }
        }
        else
        {
            System.out.println("Sorry, vertex was not found, please try again");
        }

        numVertex--;            //decrement vertex
        System.out.println(vertLabel + " have been successfully deleted from the matrix");

    } // end of deleteVertex()


    public void addEdge(String srcLabel, String tarLabel) {
        if(StartAddEdge)
            StartAE = System.nanoTime();
        StartAddEdge = false;
        String vert1 = srcLabel;
        String vert2 = tarLabel;
        int idx1 = -1;
        int idx2 = -1;

        idx1 = getVertIndex(vert1);
        idx2 = getVertIndex(vert2);

        // Check if the two vertex exist
        if (idx1 > -1 && idx2 > -1)
        {
            if(matrix[idx1][idx2] > 0)
            {
                System.out.println("Edge already exist..");
            }
            else
            {
                matrix[idx1][idx2] = 1;
                matrix[idx2][idx1] = 1;
            }
        }
        else
        {
            System.out.println("Sorry, vertex was not found, please try again");
        }

    } // end of addEdge()

    public void deleteEdge(String srcLabel, String tarLabel) {
        if(StartDeleteEdge)
            StartDE = System.nanoTime();
        StartDeleteEdge = false;
        String vert1 = srcLabel;
        String vert2 = tarLabel;
        int idx1 = -1;
        int idx2 = -1;

        idx1 = getVertIndex(vert1);
        idx2 = getVertIndex(vert2);

        //  if vertex not found dont update, put print string

        if (idx1 > -1 && idx2 > -1)
        {
            matrix[idx1][idx2] = 0;
            matrix[idx2][idx1] = 0;
        }
        else
        {
            System.out.println("Sorry, vertex was not found, please try again");
        }

    } // end of deleteEdge()


    public void toggleVertexState(String vertLabel) {
        int idx = -1;

        idx = getVertIndex(vertLabel);

        if (idx > -1)
        {
            if (vertexState.get(idx).equalsIgnoreCase("S"))
            {
                vertexState.set(idx, "I");
                System.out.println(vertLabel + "Vertex status has been changed to infected (I)");
            }
            else if(vertexState.get(idx).equalsIgnoreCase("I"))
            {
                vertexState.set(idx, "R");
                System.out.println(vertLabel + "Vertex status has been changed to recovered (R)");
            }
            else
            {
                System.out.println("This vertex (Person) already has recovered, status remains as (R)");
            }

        }
        else
        {
            System.out.println("Sorry, vertex was not found, please try again");
        }
    } // end of toggleVertexState()

    public String[] kHopNeighbours(int hop, String vertLabel) {
        EndAE = System.nanoTime();
        EndDV = System.nanoTime();
        EndAV = System.nanoTime();
        EndDE = System.nanoTime();
        DynamicIntArr neighIdxList = new DynamicIntArr();
        DynamicIntArr newNeighbour = new DynamicIntArr();
        DynamicIntArr outStringList = new DynamicIntArr();
        String[] result = {};
        long KNstart = 0;
        long KNend = 0;
        KNstart = System.nanoTime();

        int rootIdx = getVertIndex(vertLabel);
        if(rootIdx == -1)
        {
            System.out.println("Vertex: " + vertLabel + " not found..");
        }
        else {
            neighIdxList.add(rootIdx);
            outStringList.add(rootIdx);

            while (hop > 0) {
                // Check if there are still neighbours
                while (neighIdxList.arraySize > 0) {
                    // reset new neighbour list
                    newNeighbour = new DynamicIntArr();
                    rootIdx = neighIdxList.get(0);

                    // go through edges of idx
                    for (int j = 0; j < numVertex; j++) {
                        if (matrix[rootIdx][j] == 1 && outStringList.search(j) == -1) {
                            newNeighbour.add(j);
                            outStringList.add(j);
                        }
                    }
                    neighIdxList.removeIntAt(0);
                }
                // replace idx list with new neighbours
                neighIdxList = newNeighbour;
                hop--;

            }

            result = new String[outStringList.arraySize - 1];
            for (int i = 1; i < outStringList.arraySize; i++) {
                result[i - 1] = vertexList.get(outStringList.get(i));
            }
            KNend = System.nanoTime();
            //System.out.println("K-hop Neighbour took :" + (((double) (KNend - KNstart)) / Math.pow(10, 9)) + " Seconds");
            //System.out.println("Add Edges took : " + (((double) (EndAE - StartAE)) / Math.pow(10, 9)) + " Seconds");
            //System.out.println("Delete Edges took : " + (((double) (EndDE - StartDE)) / Math.pow(10, 9)) + " Seconds");
            //System.out.println("Add Vertex took : " + (((double) (EndAV - StartAV)) / Math.pow(10, 9)) + " Seconds");
            //System.out.println("Delete Vertex took : " + (((double) (EndDV - StartDV)) / Math.pow(10, 9)) + " Seconds");
            return result;
        }
        KNend = System.nanoTime();
        //System.out.println("K-hop Neighbour took :" + (((double) (KNend - KNstart)) / Math.pow(10, 9)) + " Seconds");
        //System.out.println("Add Edges took : " + (((double) (EndAE - StartAE)) / Math.pow(10, 9)) + " Seconds");
        //System.out.println("Delete Edges took : " + (((double) (EndDE - StartDE)) / Math.pow(10, 9)) + " Seconds");
        //System.out.println("Add Vertex took : " + (((double) (EndAV - StartAV)) / Math.pow(10, 9)) + " Seconds");
        //System.out.println("Delete Vertex took : " + (((double) (EndDV - StartDV)) / Math.pow(10, 9)) + " Seconds");
        return result;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        // Implement me!
        os.flush();
        for (int i=0; i < numVertex; i++)
        {
            os.print( "(" + vertexList.get(i) + "," + vertexState.get(i) + ") " );
        }
        os.flush();
        os.println();
        // Flush after print, then write

    } // end of printVertices()


    public void printEdges(PrintWriter os) {
        // Implement me!
        for (int i=0; i < numVertex; i++)
        {
            for (int j=0; j < numVertex; j++)
                if(matrix[i][j] == 1)
                {
                    os.println( "(" + vertexList.get(i) + "," + vertexList.get(j) + ") " );
                }
        }
    } // end of printEdges()

} // end of class AdjacencyMatrix