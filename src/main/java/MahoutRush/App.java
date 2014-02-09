package MahoutRush;


import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.PreferenceArray;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

import java.io.File;
import java.util.List;

public class App  {
    public static void main( String[] args ) throws Exception{

	    userBased_CF();
	    itemBased_CF();

    }

	static void userBased_CF() throws Exception{
		/**
		 * This is not scalable
		 */
		long userId = 1;

	    FileDataModel dataModel = new FileDataModel(new File("data.txt"));
		int neighborhoodSize = dataModel.getNumUsers();
		int recommendItemSize = 2;

	    UserSimilarity userSimilarity = new PearsonCorrelationSimilarity(dataModel);

		UserNeighborhood neighborhood = new NearestNUserNeighborhood(neighborhoodSize, userSimilarity, dataModel);

		Recommender recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
		PreferenceArray preferenceArray = dataModel.getPreferencesFromUser(userId);
		List<RecommendedItem> recommendedItemList = recommender.recommend(userId, recommendItemSize);

		System.out.println("User " + userId + "'s preference: ");
		System.out.println(preferenceArray.toString());
		for (RecommendedItem item : recommendedItemList) {
			System.out.println(item.toString());
		}
	}


	static void itemBased_CF() throws Exception{
		/**
		 * This is comparatively better, because we can compute
		 */
		long userId = 1;
		int recommendItemSize = 2;

		FileDataModel dataModel = new FileDataModel(new File("data.txt"));

		ItemSimilarity itemSimilarity = new LogLikelihoodSimilarity(dataModel);

		Recommender recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);

		List<RecommendedItem> recommendedItemList = recommender.recommend(userId, recommendItemSize);

		System.out.println("User " + userId + "'s preference: ");
		for (RecommendedItem item : recommendedItemList) {
			System.out.println(item.toString());
		}
	}
}
