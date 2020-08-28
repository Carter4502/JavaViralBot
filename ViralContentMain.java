import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.json.JSONException;
import org.json.JSONObject;


public class ViralContentMain {
	String accounts;
	boolean videos;
	boolean photos;

	public ViralContentMain() {
		getSelection();
		System.out.println("Type your accounts separated by a space: ");
		Scanner accountsInput = new Scanner(System.in);
		accounts = accountsInput.nextLine();
	}
	public static void main(String[] args) throws IOException, JSONException {

		ViralContentMain m = new ViralContentMain();
		ArrayList<PostData> viralPosts = new ArrayList<PostData>();
		ArrayList<String> accountsList = new ArrayList<String>(Arrays.asList(m.accounts.split(" ")));
		for (String individualAccount: accountsList) {
			String HTMLofAccount = m.getHTML("https://www.instagram.com/" + individualAccount);
			int startIndex = HTMLofAccount.indexOf("window._sharedData") + 19;
			int endIndex = HTMLofAccount.indexOf("null}]}") + 7;
			String userJSON = HTMLofAccount.substring(startIndex, endIndex) + "}";
			JSONObject json = new JSONObject(userJSON);
			accountInfo currentAccount = new accountInfo(individualAccount, json);
			currentAccount.getPALikes();
			System.out.println("@" + currentAccount.accountName + " finished analyzing.");
			for (int ct = 0; ct < 12; ct++) {
				if (currentAccount.postLikes[ct] >= 1.4 * currentAccount.averageLikes) {
					if (m.videos && m.photos) {
						double viralScore = round((double)currentAccount.averageLikes / (double)currentAccount.postLikes[ct], 3);
						PostData account = new PostData(currentAccount.postLikes[ct], currentAccount.postIDs[ct], viralScore, currentAccount.accountName);
						viralPosts.add(account);
					}
					else if (!m.videos) {
						if (currentAccount.postTypes[ct].equals("photo")) {
							double viralScore = round((double)currentAccount.averageLikes / (double)currentAccount.postLikes[ct], 3);
							PostData account = new PostData(currentAccount.postLikes[ct], currentAccount.postIDs[ct], viralScore, currentAccount.accountName);
							viralPosts.add(account);
						}
					} else {
						if (currentAccount.postTypes[ct].equals("video")) {
							double viralScore = round((double)currentAccount.averageLikes / (double)currentAccount.postLikes[ct], 3);
							PostData account = new PostData(currentAccount.postLikes[ct], currentAccount.postIDs[ct], viralScore, currentAccount.accountName);
							viralPosts.add(account);
						}
 					}

				}
			}
		}
		Collections.sort(viralPosts, (post1, post2) -> post2.getViralscore() - post1.getViralscore());

		if (viralPosts.size() < 5) {
			for (int u = 0; u < viralPosts.size(); u++) {
				System.out.println("Account: @" + viralPosts.get(u).account + " https://www.instagram.com/p/" + viralPosts.get(u).postID + " Viral Score: " + viralPosts.get(u).viralScore);
			}
		} else {
			for (int u = 0; u < 5; u++) {
				System.out.println("Account: @" + viralPosts.get(u).account + " https://www.instagram.com/p/" + viralPosts.get(u).postID + " Viral Score: " + viralPosts.get(u).viralScore);
			}
		}

			}

	public String getHTML(String websiteURL) throws IOException {
		URL url = new URL(websiteURL);
		Scanner sc = new Scanner(url.openStream());
		StringBuffer sb = new StringBuffer();
		while(sc.hasNext()) {
		        sb.append(sc.next());
		     }
		return sb.toString();
		
	}
	public static double round(double value, int places) {
		if (places < 0) throw new IllegalArgumentException();

		long factor = (long) Math.pow(10, places);
		value = value * factor;
		long tmp = Math.round(value);
		return (double) tmp / factor;
	}

	public void getSelection() {
		Scanner accountsInput = new Scanner(System.in);
		System.out.println("Do you want just photos (p), just videos (v) or both(b)?");
		String selection = accountsInput.nextLine();
		if (selection.equals("b")) {
			videos = true;
			photos = true;
		} else if (selection.equals("p")) {
			photos = true;
			videos = false;
		} else if (selection.equals("v")) {
			photos = false;
			videos = true;
		} else {
			getSelection();
		}
	}

}