import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Solution {
	private int N, W, H;
	private int[][] eventsFromInput;
	private int[][] storyEvents;
	private int[][] reloadEvents;

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
		int[] outputData;
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
					numTargetedStories++;
				} else if (flag) { // Break out of the for-loop because we've
									// gotten all the stories we need to target
					break;
				}
			}
			// For reload event at i, we now need to calculate the optimized
			// score and output the appropriate information,
			// and then carry on to the next iteration of the for-loop (next
			// reload event)
			if (numTargetedStories != 0){
				outputData = createAndExtractBinaryChart(targetStories,
						numTargetedStories);

				System.out
						.println(outputData[numTargetedStories]
								+ " "
								+ getNumberOfStories(outputData, numTargetedStories)
								+ " "
								+ getStoryIds(outputData, numTargetedStories,
										targetStories));
			}

			targetStories = new int[this.W - 1][3];
			numTargetedStories = 0;
			flag = false;

		}
	}

	private int[] createAndExtractBinaryChart(int[][] targetStories,
			int numStories) {
		int[][] binaryChart = new int[(int) Math.pow(2, numStories)][numStories + 1];
		String binaryRep;
		StringBuffer reversedBinary;
		int combinationScore;

		for (int i = 0; i < Math.pow(2, numStories); i++) {
			binaryRep = Integer.toBinaryString(i);
			reversedBinary = new StringBuffer();
			reversedBinary.append(binaryRep).reverse();
			combinationScore = 0;
			for (int j = numStories - 1; j >= 0; j--) {
				if (j >= reversedBinary.length()) {
					binaryChart[i][j] = 0;
				} else {
					if (reversedBinary.charAt(j) == 48) {
						binaryChart[i][j] = 0;
					} else if (reversedBinary.charAt(j) == 49) {
						binaryChart[i][j] = 1;
					}
				}
			}
			for (int k = 0; k < numStories; k++) {
				if (binaryChart[i][k] == 1)
					combinationScore += targetStories[k][1];
			}
			binaryChart[i][numStories] = combinationScore;
		}
		int[] outputData = new int[numStories + 1];
		outputData = getDataFromBinaryChart(binaryChart, numStories,
				targetStories);

		return outputData;
	}

	private int[] getDataFromBinaryChart(int[][] binaryChart, int numStories,
			int[][] targetStories) {
		// First we need to sort the two-dimensional array, but instead we sort
		// their scores and use it to find the appropriate row in the
		// binaryChart
		int val = 0;
		int[] scores = new int[(int) (Math.pow(2, numStories) - 1)];
		for (int i = (int) (Math.pow(2, numStories) - 1); i > 0; i--) {
			scores[i - 1] = binaryChart[i][numStories];
		}
		Arrays.sort(scores);

		int combinationSize;
		int[] outputData = new int[numStories + 1];
		for (int j = scores.length; j > 0; j--) {
			for (int l = scores.length; l > 0; l--) {
				if (scores[j - 1] == binaryChart[l][numStories]) {
					val = l;
				}
			}
			combinationSize = 0;
			for (int k = 0; k < numStories; k++) {
				if (binaryChart[val][k] == 1) {
					combinationSize += targetStories[k][2];
				}
			}
			if (combinationSize <= this.H) {
				outputData = binaryChart[val];
				return outputData;
			}
		}

		return outputData;
	}

	private int getNumberOfStories(int[] outputData, int n) {
		int numberOfStories = 0;
		for (int i = 0; i < n; i++) {
			if (outputData[i] == 1)
				numberOfStories++;
		}
		return numberOfStories;
	}

	private StringBuilder getStoryIds(int[] outputData, int n,
			int[][] targetStories) {
		StringBuilder storyIds = new StringBuilder();
		boolean flag = false;
		for (int i = 0; i < n; i++) {
			if (outputData[i] == 1) {
				if (flag == false) {
					storyIds.append(targetStories[i][0]);
					flag = true;
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
