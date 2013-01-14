import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class Solution {
	
	private static final int N_MAX = 10000;
	private int N, W, H;
	private int[][] eventsFromInput;
	
//	private String[][] eventInfo;
	
	public Solution() {
		// TODO Auto-generated constructor stub
	}
	
	public Solution(int[] firstLineInput) {
		N = Math.abs(firstLineInput[0]); // N is the number of events
		W = Math.abs(firstLineInput[1]); // W is the time window representing the window of recent stories
		H = Math.abs(firstLineInput[2]); // H is the height of the browser in pixels
	}

	public void aggregateEventInfo(BufferedReader br) throws IOException{
		this.eventsFromInput = new int[N_MAX][4];
		for(int i=0; i < this.N; i++){
			String[] nextString = br.readLine().split(" ");
			eventsFromInput[i][0] = nextString[0].charAt(0);
			
			for(int j=1; j<nextString.length; j++){
				eventsFromInput[i][j] = Integer.parseInt(nextString[j]);
			}
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
			
			
		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

}
