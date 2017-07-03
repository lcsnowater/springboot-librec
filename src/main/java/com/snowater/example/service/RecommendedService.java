package com.snowater.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import net.librec.conf.Configuration;
import net.librec.conf.Configuration.Resource;
import net.librec.data.DataModel;
import net.librec.data.model.TextDataModel;
import net.librec.eval.RecommenderEvaluator;
import net.librec.eval.rating.MAEEvaluator;
import net.librec.eval.rating.RMSEEvaluator;
import net.librec.filter.GenericRecommendedFilter;
import net.librec.filter.RecommendedFilter;
import net.librec.recommender.Recommender;
import net.librec.recommender.RecommenderContext;
import net.librec.recommender.cf.ItemKNNRecommender;
import net.librec.recommender.cf.UserKNNRecommender;
import net.librec.recommender.item.RecommendedItem;
import net.librec.similarity.PCCSimilarity;
import net.librec.similarity.RecommenderSimilarity;
@Service
public class RecommendedService {
 /* public static void main(String[] args) throws Exception {
      
     
//   List<RecommendedItem> recommendedItemList =  getItemListFromResource();
    List<RecommendedItem> recommendedItemList1 =  getItemListFromText();
   // print filter result
      for (RecommendedItem recommendedItem : recommendedItemList1) {
          System.out.println(
                  "user:" + recommendedItem.getUserId() + " " +
                  "item:" + recommendedItem.getItemId() + " " +
                  "value:" + recommendedItem.getValue()
          );
      }
      
  }*/
  /**根据配置文件读取数据
   * @param userId 用户id
   * @param itemId 商品id
   * @return
   * @throws Exception
   */
  public static List<RecommendedItem> getItemListFromResource(String userId,String itemId) throws Exception {
    
    // recommender configuration
    Configuration conf = new Configuration();
    Resource resource = new Resource("rec/cf/userknn-test.properties");
    conf.addResource(resource);
    
    // build data model
    DataModel dataModel = new TextDataModel(conf);
    dataModel.buildDataModel();
    
    // set recommendation context
    RecommenderContext context = new RecommenderContext(conf, dataModel);
    RecommenderSimilarity similarity = new PCCSimilarity();
    similarity.buildSimilarityMatrix(dataModel);
    context.setSimilarity(similarity);
    
    // training
    Recommender recommender = new UserKNNRecommender();
    recommender.recommend(context);
    
    // evaluation
    RecommenderEvaluator evaluator = new MAEEvaluator();
    recommender.evaluate(evaluator);
    
    // recommendation results
    List<RecommendedItem> recommendedItemList = recommender.getRecommendedList();
    GenericRecommendedFilter filter = new GenericRecommendedFilter();
    
      if(userId!=null){
        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        filter.setUserIdList(userIdList);
      }
      if(itemId != null){
          List<String> itemIdList = new ArrayList<>();
          itemIdList.add(itemId);
          filter.setItemIdList(itemIdList);
      }
    
    recommendedItemList = filter.filter(recommendedItemList);
    return recommendedItemList;
  }

  /**从文件查询推荐列表
   * @param userId 用户id
   * @param itemId 商品id
   * @return
   * @throws Exception
   */
  public static List<RecommendedItem> getItemListFromText(String userId,String itemId) throws Exception {
  
          // build data model
          Configuration conf = new Configuration();
          //conf.set("dfs.data.dir", "F:/down/librec-2.0.0/data");
          conf.set("data.input.path", "movielens/ml-100k");
          TextDataModel dataModel = new TextDataModel(conf);
          dataModel.buildDataModel();
  
          // build recommender context
          RecommenderContext context = new RecommenderContext(conf, dataModel);
  
          // build similarity
          conf.set("rec.recommender.similarity.key" ,"item");
          RecommenderSimilarity similarity = new PCCSimilarity();
          similarity.buildSimilarityMatrix(dataModel);
          context.setSimilarity(similarity);
  
          // build recommender
          conf.set("rec.neighbors.knn.number", "5");
          Recommender recommender = new ItemKNNRecommender();
          recommender.setContext(context);
  
          // run recommender algorithm
          recommender.recommend(context);
  
          // evaluate the recommended result
          RecommenderEvaluator evaluator = new RMSEEvaluator();
          System.out.println("RMSE:" + recommender.evaluate(evaluator));
  
          // filter the recommended result
          List<RecommendedItem> recommendedItemList = recommender.getRecommendedList();
          GenericRecommendedFilter filter = new GenericRecommendedFilter();
          
          if(userId!=null){
              List<String> userIdList = new ArrayList<>();
              userIdList.add(userId);
              filter.setUserIdList(userIdList);
          }
          if(itemId != null){
              List<String> itemIdList = new ArrayList<>();
              itemIdList.add(itemId);
              filter.setItemIdList(itemIdList);
          }
          recommendedItemList = filter.filter(recommendedItemList);
          
          return recommendedItemList;
  }

 
}

