import java.io.PrintWriter;
import java.util.HashMap;

public class IncidenceMatrix extends AbstractGraph
{
    private static int numVertex = 0;
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

    // Hash map for column edges
    private static HashMap<String, Integer> edgeMap = new HashMap<String, Integer>();
    // initial row size
    protected static final int initialMatrixSize = 2;

    //Actual matrix row size
    protected int matrixRowSize;
    //Actual matrix col size
    protected int matrixColSize;

    public IncidenceMatrix() {
        matrix = new int[initialMatrixSize][1];

    } // end of IncidenceMatrix()

    public int getNumVertex()
    {
        return numVertex;
    }

    public void growMatrixRow()
    {
        // Increase the size of matrix row by double
        int newRowSize = 2*matrix.length;
        int newColSize = matrix[0].length;
        int[][] newMatrix = new int[newRowSize][newColSize];

        //copy values
        for(int i=0; i < matrix.length; i++)
            for(int j=0; j < matrix[0].length; j++)
                newMatrix[i][j]=matrix[i][j];

        matrix = newMatrix;
        System.out.println("Row length: " + matrix.length);
        System.out.println("Col length: " + matrix[0].length);
    }

    public void growMatrixCol()
    {
        // Increase the size of matrix col by double
        int newRowSize = matrix.length;
        int newColSize = 2*matrix[0].length;
        int[][] newMatrix = new int[newRowSize][newColSize];

        //copy values
        for(int i=0; i < matrix.length; i++)
            for(int j=0; j < matrix[0].length; j++)
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

            if(matrixRowSize < matrix.length)
            {
                //Initialize vertex value to 0 in adj matrix
                numVertex++;        //Increment vertex
                matrixRowSize++;		//Increment matrix actual size
            }
            else
            {
                growMatrixRow();
                numVertex++;        //Increment vertex
                matrixRowSize++;		//Increment matrix actual size
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
            // 1. removing vertex from vertexList

            vertexList.removeStringAt(idx);
            vertexState.removeStringAt(idx);


            // 2. removing vertex from matrix
            // Clear matrix
            for (int i=0; i < matrix.length; i++)
            {
                for (int j=0; j < matrix[0].length; j++)
                {
                    matrix[i][j] = 0;
                }
            }

            DynamicStringArr tempEdgeList = new DynamicStringArr();

            for(String edge : edgeMap.keySet())
            {
                // Filter out vertex to be deleted vertLabel
                if (edge.contains(vertLabel))
                {
                    continue;
                }
                else
                {
                    tempEdgeList.add(edge);
                }

            }
            // Clear edge HashMap
            edgeMap.clear();
            matrixColSize = 0;

            // Re-apply the edges
            for(int i=0; i< tempEdgeList.arraySize; i++)
            {
                String vert1 = tempEdgeList.get(i).split(",")[0].trim();
                String vert2 = tempEdgeList.get(i).split(",")[1].trim();
                addEdge(vert1, vert2);
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
        int colIdx = -1;

        idx1 = getVertIndex(vert1);
        idx2 = getVertIndex(vert2);

        // Check if edge already exist
        if(edgeMap.get(srcLabel+","+tarLabel) == null)
        {
            // Check if the two vertex exist
            if (idx1 > -1 && idx2 > -1)
            {
                // would need to resize col if not enough col for new edge
                if(matrixColSize < matrix[0].length)
                {
                    edgeMap.put(srcLabel+","+tarLabel, matrixColSize);
                    edgeMap.put(tarLabel+","+srcLabel, matrixColSize);
                    matrix[idx1][matrixColSize] = 1;
                    matrix[idx2][matrixColSize] = 1;

                    matrixColSize++;
                }
                else
                {
                    // Grow the matrix for new edge
                    growMatrixCol();
                    edgeMap.put(srcLabel+","+tarLabel, matrixColSize);
                    edgeMap.put(tarLabel+","+srcLabel, matrixColSize);
                    matrix[idx1][matrixColSize] = 1;
                    matrix[idx2][matrixColSize] = 1;

                    matrixColSize++;
                }

            }
            else
            {
                System.out.println("Sorry, vertex was not found, please try again");
            }
        }
//        else
//        {
//            System.out.println("Edge already exist..");
//        }
    } // end of addEdge()


    public void deleteEdge(String srcLabel, String tarLabel) {
        if(StartDeleteEdge)
            StartDE = System.nanoTime();
        StartDeleteEdge = false;
        String vert1 = srcLabel;
        String vert2 = tarLabel;
        int idx1 = -1;
        int idx2 = -1;
        int colIdx = -1;

        idx1 = getVertIndex(vert1);
        idx2 = getVertIndex(vert2);

        // Check if edge exist
        if(edgeMap.get(srcLabel+","+tarLabel) != null)
        {
            colIdx = edgeMap.get(srcLabel+","+tarLabel);
            matrix[idx1][colIdx] = 0;
            matrix[idx2][colIdx] = 0;

            // remove edge from HashMap
            edgeMap.remove(srcLabel+","+tarLabel);
            edgeMap.remove(tarLabel+","+srcLabel);
        }
        else
        {
            System.out.println("Sorry, Edge was not found, please try again");
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
                System.out.println("Vertex status has been changed to infected (I)");
            }
            else if(vertexState.get(idx).equalsIgnoreCase("I"))
            {
                vertexState.set(idx, "R");
                System.out.println("Vertex status has been changed to recovered (R)");
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

        DynamicStringArr newNeighbourV = new DynamicStringArr();
        DynamicIntArr newNeighbourIdx = new DynamicIntArr();
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
        else
        {
            neighIdxList.add(rootIdx);
            String[] keys = new String[edgeMap.size()];
            int[] values = new int[edgeMap.size()];
            int x = 0;
            for (String key : edgeMap.keySet())
            {
                keys[x++] = key;
            }
            x = 0;
            for (Integer val : edgeMap.values())
            {
                values[x++] = val;
            }

            while(hop>0)
            {
                // Check if there are still neighbours
                while(neighIdxList.arraySize > 0)
                {
                    // reset new neighbour list
                    newNeighbourV = new DynamicStringArr();
                    newNeighbourIdx = new DynamicIntArr();

                    int idx = neighIdxList.get(0);

                    // go through edges of idx
                    for(int j=0 ; j<matrixColSize ; j++)
                    {
                        if(matrix[idx][j] == 1)
                        {
                            // loop through values to get keys
                            for(int k=0 ; k<values.length ; k++)
                            {

                                if (j == values[k] )
                                {
                                    newNeighbourV.add(keys[k]);
                                }
                            }

                            // Get index of new neighbours vertex and add them to newNeighbourIdx
                            for(int l=0 ; l<newNeighbourV.arraySize; l++)
                            {
                                String edge = newNeighbourV.get(l);

                                //Edge: AB
                                String vert1 = edge.split(",")[0].trim();
                                String vert2 = edge.split(",")[1].trim();

                                int idx1 = vertexList.search(vert1);
                                int idx2 = vertexList.search(vert2);

                                if (newNeighbourIdx.search(idx1) == -1 && outStringList.search(idx1) == -1)
                                {
                                    newNeighbourIdx.add(idx1);
                                    outStringList.add(idx1);
                                }
                                if (newNeighbourIdx.search(idx2) == -1 && outStringList.search(idx2) == -1)
                                {
                                    newNeighbourIdx.add(idx2);
                                    outStringList.add(idx2);
                                }
                            }
                        }
                    }
                    neighIdxList.removeIntAt(0);
                }
                // replace idx list with new neighbours
                neighIdxList = newNeighbourIdx;
                hop--;
            }

            // Remove root Index
            // Look for the correct index of root in dynamic array
            int idxToDelete = outStringList.search(rootIdx);
            outStringList.removeIntAt(idxToDelete);

            if(outStringList.arraySize > 0)
            {
                result = new String[outStringList.arraySize];

                for(int i=0 ; i < outStringList.arraySize; i++)
                {
                    result[i] = vertexList.get(outStringList.get(i));
                }
            }
            KNend = System.nanoTime();
//            System.out.println("K-hop Neighbour took :"+ (((double)(KNend-KNstart))/Math.pow(10,9))+" Seconds");
//            System.out.println("Add Edges took : "+ (((double)(EndAE-StartAE))/Math.pow(10,9))+" Seconds");
//            System.out.println("Delete Edges took : "+ (((double)(EndDE-StartDE))/Math.pow(10,9))+" Seconds");
//            System.out.println("Add Vertex took : "+ (((double)(EndAV-StartAV))/Math.pow(10,9))+" Seconds");
            System.out.println("Delete Vertex took : "+ (((double)(EndDV-StartDV))/Math.pow(10,9))+" Seconds");
            return result;

        }
        KNend = System.nanoTime();
//        System.out.println("K-hop Neighbour took :"+ (((double)(KNend-KNstart))/Math.pow(10,9))+" Seconds");
//        System.out.println("Add Edges took : "+ (((double)(EndAE-StartAE))/Math.pow(10,9))+" Seconds");
//        System.out.println("Delete Edges took : "+ (((double)(EndDE-StartDE))/Math.pow(10,9))+" Seconds");
//        System.out.println("Add Vertex took : "+ (((double)(EndAV-StartAV))/Math.pow(10,9))+" Seconds");
        System.out.println("Delete Vertex took : "+ (((double)(EndDV-StartDV))/Math.pow(10,9))+" Seconds");
        return result;

    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
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

        for(String edge : edgeMap.keySet())
        {
            os.println(edge.split(",")[0].trim() + " " + edge.split(",")[1].trim());
        }

    } // end of printEdges()


} // end of class IncidenceMatrix