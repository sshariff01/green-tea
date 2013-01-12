import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Solution {
	
	private static int N_MAX = 10000;
	int N, W, H;
	String[][] eventInfo;
	
	public Solution() {
		// TODO Auto-generated constructor stub
	}
	
	public Solution(int[] firstLineInput) {
		N = Math.abs(firstLineInput[0]); // N is the number of events
		W = Math.abs(firstLineInput[1]); // W is the time window representing the window of recent stories
		H = Math.abs(firstLineInput[2]); // H is the height of the browser in pixels
	}

	public int aggregateEventInfo(BufferedReader br) throws IOException{
		String str;
		while((str = br.readLine()) != null) {
			System.out.println(str);
		}
		return 0;
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
			Solution interpretSettings = new Solution(firstLineInput);
//			interpretSettings.aggregateEventInfo(br);

			int[][] eventsFromInput = new int[N_MAX][4];
			for(int i=0; i <= 9; i++){
				String[] nextString = br.readLine().split(" ");
				eventsFromInput[i][0] = nextString[0].charAt(0);
				
				for(int j=1; j<nextString.length; j++){
					eventsFromInput[i][j] = Integer.parseInt(nextString[j]);
				}
				System.out.println(eventsFromInput[i][0]);
			}
			
		} catch (Exception e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

}
