import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class accountInfo {
	String accountName;
	JSONObject accountJSON;
	int[] postLikes;
	String[] postTypes;
	int averageLikes;
	String[] postIDs;
	
	public accountInfo(String a, JSONObject b) {
		accountName = a;
		accountJSON = b;
		postLikes = new int[12];
		averageLikes = 0;
		postIDs = new String[12];
		postTypes = new String[12];
	}
	public String getString() {
		return "Account: @" + accountName;
	}
	public void getPALikes() throws JSONException, FileNotFoundException {
		int totalLikes = 0;
		JSONObject a = (JSONObject) accountJSON.get("entry_data");
		JSONArray b = (JSONArray) a.get("ProfilePage");
		JSONObject c = b.getJSONObject(0);
		JSONObject d = c.getJSONObject("graphql");
		JSONObject e = d.getJSONObject("user");
		JSONObject f = e.getJSONObject("edge_owner_to_timeline_media");
		JSONArray g = f.getJSONArray("edges");
		for (int counter = 0; counter < 12; counter++) {
			String videoType;
			JSONObject h = g.getJSONObject(counter);
			JSONObject i = h.getJSONObject("node");
			boolean isVideo = i.getBoolean("is_video");
			String ID = i.getString("shortcode");
			JSONObject j = i.getJSONObject("edge_liked_by");
			if (isVideo) {
				videoType = "video";
			} else {
				videoType = "photo";
			}
			int likes = j.getInt("count");
			this.postIDs[counter] = ID;
			this.postLikes[counter] = likes;
			this.postTypes[counter] = videoType;
			totalLikes += likes;
		}
		this.averageLikes = totalLikes / 12;
	}
	
}
