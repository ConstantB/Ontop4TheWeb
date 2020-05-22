package SPARQLSon;

public class LoadTDB {

	public static void main(String[] args) {

		String TDBdirectory = "/Users/Constant/spapi-repo-yelp-chicago";
		DatabaseWrapper dbw = new DatabaseWrapper(TDBdirectory);
		//dbw.createDataset("/home/constant/Documents/spapi-yelp.nt", "NT");	
		dbw.createDataset("/Users/Constant/Documents/spapi-yelp-chicago.nt", "NT");		
		System.out.println("Dataset stored");
	}

}
