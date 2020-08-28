
public class PostData {
	int numOfLikes;
	String postID;
	double viralScore;
	String account;
	public PostData(int likes, String id, double vs, String account) {
		this.numOfLikes = likes;
		this.postID = id;
		this.viralScore = vs;
		this.account = account;
	}
	public int getViralscore() {
		double valxonethousand = viralScore * 1000;
		int finalViral = (int)valxonethousand;
 		return finalViral;
	}
}
