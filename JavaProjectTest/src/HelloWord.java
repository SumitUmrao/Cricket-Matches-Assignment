
// Method to send HTTP GET request to the provided API and return the JSON response
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HelloWord {


	    // Method to fetch match data from the provided API
	    public static String fetchMatchData(String apiUrl) throws Exception {
	        URL url = new URL(apiUrl);
	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("apiKey", "test-creds@2320"); 

	        // Read the API response
	        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String inputLine;
	        StringBuilder content = new StringBuilder();
	        while ((inputLine = in.readLine()) != null) {
	            content.append(inputLine);
	        }

	        // Close connections
	        in.close();
	        connection.disconnect();
	        return content.toString();
	    }

	    // Method to find the highest score and count of 300+ matches
	    public static void processMatchesData(String jsonData) {
	        // Remove outer square brackets if it's a list and split by objects
	        String[] matches = jsonData.split("\\},\\{");

	        int highestScore = 0;
	        String highestScoringTeam = "";
	        int matchesWith300PlusScore = 0;

	        for (String match : matches) {
	            // Clean up JSON object string
	            match = match.replace("[{", "").replace("}]", "").replace("{", "").replace("}", "");

	            // Extract values using basic string manipulation
	            String t1Name = extractValue(match, "t1");
	            String t2Name = extractValue(match, "t2");
	            String t1ScoreStr = extractValue(match, "t1s");
	            String t2ScoreStr = extractValue(match, "t2s");

	            int t1Score = extractScore(t1ScoreStr);
	            int t2Score = extractScore(t2ScoreStr);

	            // Determine highest score
	            if (t1Score > highestScore) {
	                highestScore = t1Score;
	                highestScoringTeam = t1Name;
	            }
	            if (t2Score > highestScore) {
	                highestScore = t2Score;
	                highestScoringTeam = t2Name;
	            }

	            // Check if total score is 300 or more
	            if ((t1Score + t2Score) >= 300) {
	                matchesWith300PlusScore++;
	            }
	        }

	        // Print the results
	        System.out.println("Highest Score: " + highestScore + " and Team Name is: " + highestScoringTeam);
	        System.out.println("Number Of Matches with total 300 Plus Score: " + matchesWith300PlusScore);
	    }

	    // Utility method to extract value from key in JSON-like string
	    public static String extractValue(String jsonString, String key) {
	        String searchKey = "\"" + key + "\":";
	        int startIndex = jsonString.indexOf(searchKey) + searchKey.length();
	        if (startIndex == -1) {
	            return "";
	        }
	        int endIndex = jsonString.indexOf(",", startIndex);
	        if (endIndex == -1) {
	            endIndex = jsonString.length();
	        }
	        return jsonString.substring(startIndex, endIndex).replace("\"", "").trim();
	    }

	    // Utility method to extract integer score from string format like "200/3"
	    public static int extractScore(String score) {
	        if (score == null || score.isEmpty() || !score.contains("/")) {
	            return 0; // Handle cases where the score is not available or malformed
	        }
	        try {
	            String[] scoreParts = score.split("/");
	            return Integer.parseInt(scoreParts[0]); // Extract the score part (runs)
	        } catch (NumberFormatException e) {
	            return 0; // If score parsing fails, return 0
	        }
	    }

	    public static void main(String[] args) {
	        try {
	            // Fetch the cricket match data from the API
	            String apiUrl = "https://api.cuvora.com/car/partner/cricket-data";
	            String jsonData = fetchMatchData(apiUrl);

	            // Process and compute the required match statistics
	            processMatchesData(jsonData);

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}