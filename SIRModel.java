import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel
{

    /**
     * Default constructor, modify as needed.
     */
    public SIRModel() {

    } // end of SIRModel()


    /**
     * Run the SIR epidemic model to completion, i.e., until no more changes to the states of the vertices for a whole iteration.
     *
     * @param graph Input contracts graph.
     * @param seedVertices Set of seed, infected vertices.
     * @param infectionProb Probability of infection.
     * @param recoverProb Probability that a vertex can become recovered.
     * @param sirModelOutWriter PrintWriter to output the necessary information per iteration (see specs for details).
     */
    public void runSimulation(ContactsGraph graph, String[] seedVertices,
                              float infectionProb, float recoverProb, PrintWriter sirModelOutWriter)
    {

        // seedVertices will be the root of the infection
        DynamicStringArr vertexInfected = new DynamicStringArr();
        DynamicStringArr vertexRecovered = new DynamicStringArr();

        for (int i=0 ; i < seedVertices.length ; i++)
        {
            vertexInfected.add(seedVertices[i]);
            graph.toggleVertexState(seedVertices[i]);
        }

        String[] newNeighbour = new String[100];
        int iteration = 0;

        while(vertexInfected.arraySize > 0)
        {
            // recoverProb = probability to be infected (e.g 0.5)
            for (int x=0 ; x < vertexInfected.arraySize ; x++)
            {
                if (Math.random() <= recoverProb)
                {
                    vertexRecovered.add(vertexInfected.get(x));
                    System.out.println("Vertex recovered:" + vertexInfected.get(x));
                    graph.toggleVertexState(vertexInfected.get(x));
                    vertexInfected.removeStringAt(x);
                }
            }

            // infectionProb = probability to be infected (e.g 0.8)
            // get the list of neighbours
            for (int j=0 ; j < vertexInfected.arraySize ; j++)
            {
                // get neighbour of infected vertex
                newNeighbour = graph.kHopNeighbours(1, vertexInfected.get(j));

                for (int k=0 ; k < newNeighbour.length ; k++)
                {
                    // math random here, if infected add in to vertexInfected
                    if(Math.random() <= infectionProb)
                    {
                        // check if vertex alr infected
                        if(vertexInfected.search(newNeighbour[k]) == -1 && vertexRecovered.search(newNeighbour[k]) == -1)
                        {
                            vertexInfected.add(newNeighbour[k]);
                            System.out.println("Ouch, new neighbour infected:" + newNeighbour[k]);
                            System.out.println("Vertex infected:" + newNeighbour[k]);
                            graph.toggleVertexState(newNeighbour[k]);
                        }
                    }
                }
            }

            //System.out.println("Next Iteration: ");
            String vInf = vertexInfected.returnAll();
            String vRec = vertexRecovered.returnAll();


            sirModelOutWriter.println(++iteration + ": [" + vInf + "]  :  [" + vRec + "]");
            //System.out.println("Susceptible count: " + (graph.getNumVertex() - vertexInfected.arraySize - vertexRecovered.arraySize));
            //System.out.println("Infected Count: " + vertexInfected.arraySize);
            //System.out.println("Recovered Count: " + vertexRecovered.arraySize);



        }

    } // end of runSimulation()
} // end of class SIRModel
