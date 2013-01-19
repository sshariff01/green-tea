import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Solution {
	
	private static final int N_MAX = 10000;
	private int N, W, H;
	private int[][] eventsFromInput;
	private int[][] storyEvents; 
	private int[][] reloadEvents;
	
//	private String[][] eventInfo;
	
	public Solution() {
		// TODO Auto-generated constructor stub
	}
	
	public Solution(int[] firstLineInput) {
		this.N = Math.abs(firstLineInput[0]); // N is the number of events
		this.W = Math.abs(firstLineInput[1]); // W is the time window representing the window of recent stories
		this.H = Math.abs(firstLineInput[2]); // H is the height of the browser in pixels
	}

	public void aggregateEventInfo(BufferedReader br) throws IOException{
		this.eventsFromInput = new int[this.N][4];
		int numReloads = 0;
		int numStories = 0;
		
		for(int i=0; i<this.N; i++){
			String[] nextString = br.readLine().split(" ");
			this.eventsFromInput[i][0] = nextString[0].charAt(0);
			
			if(this.eventsFromInput[i][0] == "R".charAt(0)){
				numReloads++;
			} else if(this.eventsFromInput[i][0] == "S".charAt(0)){
				numStories++;
			} else {
				System.out.println("There was a problem in determining whether the story was an R or S!");
			}
			
			for(int j=1; j<nextString.length; j++){
				this.eventsFromInput[i][j] = Integer.parseInt(nextString[j]);
			}
		}
		
		isolateEvents(this.eventsFromInput, numReloads, numStories);
		System.out.println("The isolateEvents method ran without blowing up.");
	}
	
	private void isolateEvents(int[][] events, int numReloads, int numStories) {
		int r = 0; // Count of reload events
		int s = 0; // Count of story events
		this.reloadEvents = new int[numReloads][1];
		this.storyEvents = new int[numStories][3];
		
		for(int i=0; i < this.N; i++){
			if(events[i][0] == "R".charAt(0)){ // If the event is a reload, append a copy to the reloadEvents array
				this.reloadEvents[r][0] = events[i][1];
				r++;
			} else if(events[i][0] == "S".charAt(0)) { // If the event is a story, append a copy to the storyEvents array
				for(int k=0; k<3; k++){
					this.storyEvents[s][k] = events[i][k+1];
				}
				s++;
			} else { // Display a message in the console for development purposes if the event is neither an R or S
				System.out.println("There was a problem in isolating the events!");
			}
		}
	}
	
	private void traverseAndCalculate(){
		int[][] targetStories = new int[this.W - 1][3];
		int numTargetedStories = 0;
		int timeOfReload;
		int totalScoreOfAllTargetedStories = 0;
		int totalSizeOfAllTargetedStories = 0;
		StringBuilder TargetedStoryIDs = new StringBuilder(); // Use StringBuilder to aggregate IDs of all Targeted stories
		boolean flag = false; //TODO: Refine usage of flag
		
		for(int i=0; i<this.reloadEvents.length; i++){ // Begin by traversing through reloadEvents to determine the time frame of interest
			timeOfReload = this.reloadEvents[i][0];
			
			for(int j=0; j<this.storyEvents.length; j++){
				if((this.storyEvents[j][0] > (timeOfReload - this.W)) && 
						(this.storyEvents[j][0] < timeOfReload)) {

					if(flag == false){
						// Raise flag after some data has been populated in targetEvents so that for-loop is only broken
						// if there is something in the loop. Worst case: for-loop is run all the way through
						flag = true;
					}
					
					for(int k=1; k<3; k++){
						targetStories[numTargetedStories][k] = this.storyEvents[j][k];
					}
					targetStories[numTargetedStories][0] = j+1;					
					TargetedStoryIDs.append(" ");
					TargetedStoryIDs.append(targetStories[numTargetedStories][0]);
					totalScoreOfAllTargetedStories += targetStories[numTargetedStories][1];
					totalSizeOfAllTargetedStories += targetStories[numTargetedStories][2]; 
					numTargetedStories++;
				} else if(flag){ // Break out of the for-loop because we've gotten all the stories we need to target
					break; 
				}
			}
			System.out.println("Ready to perform calculation");
			// For reload event at i, we now need to calculate the optimized score and output the appropriate information, 
			// and then carry on to the next iteration of the for-loop (next reload event)
			
			for(int l=0; l<targetStories.length; l++){
				if(this.H > totalSizeOfAllTargetedStories){
					System.out.println(totalScoreOfAllTargetedStories + " " + numTargetedStories + " " + TargetedStoryIDs);
				}
			}
			
			numTargetedStories = 0;
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String[] firstLine = br.readLine().split(" ");
			int[] firstLineInput = {
				Integer.parseInt(firstLine[0]),
				Integer.parseInt(firstLine[1]),
				Integer.parseInt(firstLine[2])
				};
			
			Solution soln = new Solution(firstLineInput);
			soln.aggregateEventInfo(br);
			soln.traverseAndCalculate();
			
			
		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

}
