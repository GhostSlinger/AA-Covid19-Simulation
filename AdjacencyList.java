import java.io.PrintWriter;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{
    Node[] temp = new Node[100000];
    Node[] adjList = null;
    int mLength=0;
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
    /**
     * Contructs empty graph.
     */
    public AdjacencyList() {



        // Implement me!
    } // end of AdjacencyList()

    public int getNumVertex() {
        int size = 0;
        for(int i=0 ; i<adjList.length; i++)
        {
            size++;
        }
        return size;
    }

    public void addVertex(String vertLabel) {
        if(StartAddVertex)
            StartAV = System.nanoTime();
        StartAddVertex = false;
        Node newNode = new Node(vertLabel,SIRState.S);
        temp[mLength++]=newNode;
        addIntoArray();
    } // end of add()

    public void addIntoArray() {
        adjList = new Node[mLength];
        for(int i =0; i<adjList.length;i++) {
            adjList[i]=temp[i];
        }
    }

    public int search(String value) {
        for(int k=0;k<adjList.length;k++) {
            if(adjList[k].getValue().equals(value)) {
                return k;
            }
        }
        return -1;
    }

    public void addEdge(String srcLabel, String tarLabel) {
        if(StartAddEdge)
            StartAE = System.nanoTime();
        StartAddEdge = false;
        int v1 = search(srcLabel);
        int v2 = search(tarLabel);

        Node currNode = adjList[v1];
        while(currNode.getNext() !=null) {
            currNode= currNode.getNext();
        }
        Node newNode = new Node(adjList[v2].getValue(),adjList[v2].state);
        currNode.setNext(newNode);

        currNode = adjList[v2];
        while(currNode.getNext() !=null) {
            currNode = currNode.getNext();
        }
        Node newNode2= new Node(adjList[v1].getValue(),adjList[v1].state);
        currNode.setNext(newNode2);
        // Implement me!
    }// end of addEdge()

    public void toggleVertexState(String vertLabel) {

        for(int i=0; i<adjList.length;i++) {
            if(adjList[i].getValue().equals(vertLabel)) {
                if(adjList[i].state == SIRState.S) {
                    adjList[i].state = SIRState.I;
                }
                else if(adjList[i].state==SIRState.I) {
                    adjList[i].state = SIRState.R;
                }
                else
                    adjList[i].state = SIRState.R;
            }
        }

        // Implement me!
    } // end of toggleVertexState()

    public void deleteEdge(String srcLabel, String tarLabel) {
        if(StartDeleteEdge)
            StartDE = System.nanoTime();
        StartDeleteEdge = false;
        int v1 = search(srcLabel);
        int v2 = search(tarLabel);

        Node currNode = adjList[v1];
        while(currNode.getNext()!=null) {
            if(!currNode.getNext().getValue().equals(tarLabel)) {
                currNode = currNode.getNext();
            }
            else {
                currNode.setNext(currNode.getNext().getNext());
                break;
            }
        }
        currNode = adjList[v2];
        while(currNode.getNext()!=null) {
            if(!currNode.getNext().getValue().equals(srcLabel)) {
                currNode = currNode.getNext();
            }
            else {
                currNode.setNext(currNode.getNext().getNext());
                break;
            }
        }

        // Implement me!
    } // end of deleteEdge()

    public Node[] remove(Node[] arr,String input) {
        Node[] removed = new Node[arr.length-1];

        for(int i=0,k=0; i<arr.length;i++) {
            if(arr[i].getValue().equals(input)) {
                continue;
            }
            removed[k++]= arr[i];
        }
        return removed;
    }

    public void deleteVertex(String vertLabel) {
        if(StartDeleteVertex)
            StartDV = System.nanoTime();
        StartDeleteVertex = false;
        int v = search(vertLabel);
        if(v == -1){
            System.out.println("The vertex is not in the List.");
        }
        else {
            adjList = remove(adjList, vertLabel);
            for (int i = 0; i < adjList.length; i++) {
                int v1 = search(adjList[i].getValue());
                Node currNode = adjList[v1];
                while (currNode.getNext() != null) {
                    if (!currNode.getNext().getValue().equals(vertLabel)) {
                        currNode = currNode.getNext();
                    } else {
                        currNode.setNext(currNode.getNext().getNext());
                        break;
                    }
                }
            }
        }
        // Implement me!
    } // end of deleteVertex()

    public String[] kHopNeighbours(int k, String vertLabel) {
        EndAE = System.nanoTime();
        EndDV = System.nanoTime();
        EndAV = System.nanoTime();
        EndDE = System.nanoTime();
        DynamicStringArr out = new DynamicStringArr();
        DynamicIntArr newNeighbourIndex = new DynamicIntArr();
        DynamicIntArr neighbourIndex = new DynamicIntArr();
        long KNstart = 0;
        long KNend = 0;
        String[] finalOut = {};

        KNstart = System.nanoTime();
        int v = search(vertLabel);
        if (v ==-1){
            System.out.println("Vertex does not exist.");
        }
        else {
            Node currNode =adjList[v];
            neighbourIndex.add(v);
            // ADDED THE FIRST NODE <<<<<<<<<<<<<<<<
            out.add(currNode.getValue());

            // hop
            while (k > 0) {

                while (neighbourIndex.arraySize > 0) {

                    //reset newNeighbourIndex
                    newNeighbourIndex = new DynamicIntArr();
                    v = neighbourIndex.get(0);
                    currNode = adjList[v];

                    while (currNode.getNext() != null) {

                        currNode = currNode.getNext();
                        newNeighbourIndex.add(search(currNode.getValue()));

                        if (out.search(currNode.getValue()) == -1) {
                            out.add(currNode.getValue());
                        }
                    }
                    neighbourIndex.removeIntAt(0);
                }
                neighbourIndex = newNeighbourIndex;
                k--;

            }

            // Removes the first node
            out.removeStringAt(0);


            if (out.arraySize != 0) {
                finalOut = new String[out.arraySize];
                //DynamicStringArr outNoNull = new DynamicStringArr();

                int count = 0;
                for (int x = 0; x < out.arraySize; x++) {
                    // Filter null values
                    if (out.get(x).equalsIgnoreCase(null)) {
                        continue;
                    } else {
                        finalOut[count] = out.get(x);
                        count++;
                    }
                }
            }
        }
        KNend = System.nanoTime();
        System.out.println("K-hop Neighbour took : "+ (((double)(KNend-KNstart))/Math.pow(10,9))+" Seconds");
//		System.out.println("Add Edges took : "+ (((double)(EndAE-StartAE))/Math.pow(10,9))+" Seconds");
		System.out.println("Delete Edges took : "+ (((double)(EndDE-StartDE))/Math.pow(10,9))+" Seconds");
//		System.out.println("Add Vertex took : "+ (((double)(EndAV-StartAV))/Math.pow(10,9))+" Seconds");
//		System.out.println("Delete Vertex took : "+ (((double)(EndDV-StartDV))/Math.pow(10,9))+" Seconds");
        return finalOut;

    } // end of kHopNeighbours()

    public void printVertices(PrintWriter os) {

        for(int i=0;i<adjList.length;i++) {
            System.out.print("("+adjList[i].getValue()+","+adjList[i].state+")");
        }
        System.out.print("\n");
        // Implement me!
    } // end of printVertices()

    public void printEdges(PrintWriter os) {

        for(int i=0;i<adjList.length;i++) {
            Node currNode = adjList[i];
            while(currNode.getNext()!=null) {
                System.out.print(adjList[i].getValue()+" "+currNode.mNext.getValue()+"\n");
                currNode = currNode.getNext();
            }
        }

        System.out.print("\n");
        // Implement me!
    } // end of printEdges()


    class Node
    {
        /** Stored value of node. */
        protected String mValue;
        /** Reference to next node. */
        protected Node mNext;
        SIRState state;
        public Node(String value,SIRState state) {
            mValue = value;
            this.state = state;
            mNext = null;
        }
        public String getValue() {
            return mValue;
        }
        public Node getNext() {
            return mNext;
        }
        public void setValue(String value) {
            mValue = value;
        }
        public void setNext(Node next) {
            mNext = next;
        }
    } // end of inner class Node

} // end of class AdjacencyList