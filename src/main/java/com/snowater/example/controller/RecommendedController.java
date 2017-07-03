package com.snowater.example.controller;

import java.util.List;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.snowater.example.service.RecommendedService;
import com.snowater.example.util.ResponseUtils;

import net.librec.recommender.item.RecommendedItem;

@RestController
public class RecommendedController {

	@Autowired
	private RecommendedService service;

	/**根据商品查找感兴趣的用户
	 * @param response
	 * @param itemId
	 */
	@RequestMapping(value = "recommended/itemBased")
	public void itemBasedRecommender(HttpServletResponse response, @WebParam String itemId) {
		Gson gson = new Gson();
		List<RecommendedItem> recommendedItemList1 = null;
		
		try {
			recommendedItemList1 = service.getItemListFromText(null, itemId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, gson.toJson(recommendedItemList1));
	}

	/**根据用户推荐感兴趣的商品
	 * @param response
	 * @param userId
	 */
	@RequestMapping(value = "recommended/userBased")
	public void userBasedRecommender(HttpServletResponse response, @WebParam String userId) {
		Gson gson = new Gson();
		List<RecommendedItem> recommendedItemList1 = null;
		
		try {
			recommendedItemList1 = service.getItemListFromText(userId, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResponseUtils.renderJson(response, gson.toJson(recommendedItemList1));
	}

}
