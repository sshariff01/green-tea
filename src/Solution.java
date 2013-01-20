import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class Solution {

//	private static final int N_MAX = 10000;
	private int N, W, H;
	private int[][] eventsFromInput;
	private int[][] storyEvents;
	private int[][] reloadEvents;
	private int[][] binaryChart;

	public Solution() {
		// TODO Auto-generated constructor stub
	}

	public Solution(int[] firstLineInput) {
		this.N = Math.abs(firstLineInput[0]); // N is the number of events
		this.W = Math.abs(firstLineInput[1]); // W is the time window
												// representing the window of
												// recent stories
		this.H = Math.abs(firstLineInput[2]); // H is the height of the browser
												// in pixels
	}

	public void aggregateEventInfo(BufferedReader br) throws IOException {
		this.eventsFromInput = new int[this.N][4];
		int numReloads = 0;
		int numStories = 0;

		for (int i = 0; i < this.N; i++) {
			String[] nextString = br.readLine().split(" ");
			this.eventsFromInput[i][0] = nextString[0].charAt(0);

			if (this.eventsFromInput[i][0] == "R".charAt(0)) {
				numReloads++;
			} else if (this.eventsFromInput[i][0] == "S".charAt(0)) {
				numStories++;
			} else {
				System.out
						.println("There was a problem in determining whether the story was an R or S!");
			}

			for (int j = 1; j < nextString.length; j++) {
				this.eventsFromInput[i][j] = Integer.parseInt(nextString[j]);
			}
		}

		isolateEvents(this.eventsFromInput, numReloads, numStories);
		System.out.println("The isolateEvents method ran without blowing up");
	}

	private void isolateEvents(int[][] events, int numReloads, int numStories) {
		int r = 0; // Count of reload events
		int s = 0; // Count of story events
		this.reloadEvents = new int[numReloads][1];
		this.storyEvents = new int[numStories][3];

		for (int i = 0; i < this.N; i++) {
			if (events[i][0] == "R".charAt(0)) { // If the event is a reload,
													// append a copy to the
													// reloadEvents array
				this.reloadEvents[r][0] = events[i][1];
				r++;
			} else if (events[i][0] == "S".charAt(0)) { // If the event is a
														// story, append a copy
														// to the storyEvents
														// array
				for (int k = 0; k < 3; k++) {
					this.storyEvents[s][k] = events[i][k + 1];
				}
				s++;
			} else { // Display a message in the console for development
						// purposes if the event is neither an R or S
				System.out
						.println("There was a problem in isolating the events!");
			}
		}
	}

	private void traverseAndCalculate() {
		int[][] targetStories = new int[this.W - 1][3];
		int numTargetedStories = 0;
		int timeOfReload;
		int totalScoreOfAllTargetedStories = 0;
		int totalSizeOfAllTargetedStories = 0;
		int[] outputData;
		StringBuilder targetedStoryIDs = new StringBuilder(); // Use
																// StringBuilder
																// to aggregate
																// IDs of all
																// Targeted
																// stories
		boolean flag = false; // TODO: Refine usage of flag

		for (int i = 0; i < this.reloadEvents.length; i++) { // Begin by
																// traversing
																// through
																// reloadEvents
																// to determine
																// the time
																// frame of
																// interest
			timeOfReload = this.reloadEvents[i][0];

			for (int j = 0; j < this.storyEvents.length; j++) {
				if ((this.storyEvents[j][0] >= (timeOfReload - this.W))
						&& (this.storyEvents[j][0] < timeOfReload)) {

					if (flag == false) {
						// Raise flag after some data has been populated in
						// targetEvents so that for-loop is only broken
						// if there is something in the loop. Worst case:
						// for-loop is run all the way through
						flag = true;
					}

					for (int k = 1; k < 3; k++) {
						targetStories[numTargetedStories][k] = this.storyEvents[j][k];
					}
					targetStories[numTargetedStories][0] = j + 1;
//					targetedStoryIDs.append(" ");
//					targetedStoryIDs
//							.append(targetStories[numTargetedStories][0]);
//					totalScoreOfAllTargetedStories += targetStories[numTargetedStories][1];
//					totalSizeOfAllTargetedStories += targetStories[numTargetedStories][2];
					numTargetedStories++;
				} else if (flag) { // Break out of the for-loop because we've
									// gotten all the stories we need to target
					break;
				}
			}
//			System.out.println("Ready to perform calculation");
			// For reload event at i, we now need to calculate the optimized
			// score and output the appropriate information,
			// and then carry on to the next iteration of the for-loop (next
			// reload event)

//			for (int l = 0; l < targetStories.length; l++) {
//			if (this.H > totalSizeOfAllTargetedStories) {
//				System.out.println(totalScoreOfAllTargetedStories + " "
//						+ numTargetedStories + " " + targetedStoryIDs);
//			} else {
			this.binaryChart = sortInBinaryChart(targetStories, numTargetedStories);
			outputData = new int[numTargetedStories+1];
			outputData = getDataForOutput(outputData, numTargetedStories, targetStories);
//				System.out.println(outputData);
			System.out.println(outputData[numTargetedStories] + " " + getNumberOfStories(outputData, numTargetedStories) + " " + getStoryIds(outputData, numTargetedStories, targetStories));
//			}
//			}
			
			targetStories = new int[this.W - 1][3];
			numTargetedStories = 0;
			flag = false;
//			totalScoreOfAllTargetedStories = 0;
//			totalSizeOfAllTargetedStories = 0;

		}
	}

	private int[][] sortInBinaryChart(int[][] targetStories, int numStories){
		int[][] binaryChart = new int[(int) Math.pow(2, numStories)][numStories+1];
		String binaryRep;
		StringBuffer reversedBinary;
		int combinationScore;
		
		for(int i=0; i<Math.pow(2, numStories); i++){
			binaryRep = Integer.toBinaryString(i);
			reversedBinary = new StringBuffer();
			reversedBinary.append(binaryRep).reverse();
//			System.out.println(reversedBinary);
			combinationScore = 0;
			for(int j=numStories-1; j>=0; j--){
				if(j >= reversedBinary.length()){
					binaryChart[i][j] = 0;
				} else {
					if(reversedBinary.charAt(j) == 48){
						binaryChart[i][j] = 0;
					}
					else if(reversedBinary.charAt(j) == 49){
						binaryChart[i][j] = 1;
					}
				}
			}
			for(int k=0; k < numStories; k++){
				if(binaryChart[i][k] == 1)
					combinationScore += targetStories[k][1];
			}
			binaryChart[i][numStories] = combinationScore;
		}
		Arrays.sort(binaryChart[numStories]);
		
		return binaryChart;
	}

	private int[] getDataForOutput(int[] outputData, int numStories, int[][] targetStories) {
		int combinationSize;
		for(int i=(int) (Math.pow(2, numStories)-1); i>=0; i--){
			combinationSize=0;
			for(int k=0; k<numStories; k++){
				if (this.binaryChart[i][k] == 1){
					combinationSize += targetStories[k][2];
				}
			}
			if (combinationSize <= this.H){
				outputData = this.binaryChart[i];
				return outputData;
			}
		}
		return null;
	}

	private int getNumberOfStories(int[] outputData, int n) {
		int numberOfStories = 0;
		for(int i=0; i<n; i++){
			if(outputData[i] == 1)
				numberOfStories++;
		}
		return numberOfStories;
	}
	
	@SuppressWarnings("null")
	private StringBuilder getStoryIds(int[] outputData, int n, int[][] targetStories) {
		StringBuilder storyIds = new StringBuilder();
		for(int i=0; i<n; i++){
			if(outputData[i] == 1){
				if(storyIds == null){
					storyIds.append(targetStories[i][0]);
				} else {
					storyIds.append(" ");
					storyIds.append(targetStories[i][0]);
				}
			}
		}
		return storyIds;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			String[] firstLine = br.readLine().split(" ");
			int[] firstLineInput = { Integer.parseInt(firstLine[0]),
					Integer.parseInt(firstLine[1]),
					Integer.parseInt(firstLine[2]) };

			Solution soln = new Solution(firstLineInput);
			soln.aggregateEventInfo(br);
			soln.traverseAndCalculate();

		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

}
