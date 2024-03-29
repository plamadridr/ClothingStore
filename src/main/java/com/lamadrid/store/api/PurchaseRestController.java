package com.lamadrid.store.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lamadrid.store.application.PurchaseController;
import com.lamadrid.store.application.dto.PurchaseLineDTO;
import com.lamadrid.store.application.dto.PurchaseDTO;
import com.lamadrid.store.utilities.InvalidParamException;
import com.lamadrid.store.utilities.NotFoundException;

@RestController
@CrossOrigin
public class PurchaseRestController {

	@Autowired
	private PurchaseController controller;

	private String toJson(Object object) {

		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return gson.toJson(object);
	}

	@PostMapping(value = "/users/{userId}/purchases", produces = "application/json;charset=UTF-8")
	public String createNewPurchase(@PathVariable int userId) throws InvalidParamException, NotFoundException {

		PurchaseDTO purchase = controller.createPurchase(userId);

		return toJson(purchase);
	}

	@PostMapping(value = "/users/{userId}/purchases/{purchaseId}/dresses/{dressId}", produces = "application/json;charset=UTF-8")
	public String addToCart(@PathVariable int purchaseId, @PathVariable int dressId, @RequestBody String json)
			throws NotFoundException, InvalidParamException {

		PurchaseLineDTO purchaseDressDTO = new Gson().fromJson(json, PurchaseLineDTO.class);
		PurchaseLineDTO purchaseDress = controller.addToCart(purchaseId, dressId, purchaseDressDTO);

		return toJson(purchaseDress);

	}

	@PostMapping(value = "/users/{userId}/purchases/{purchaseId}", produces = "application/json;charset=UTF-8")
	public String toPay(@PathVariable int userId, @PathVariable int purchaseId)
			throws InvalidParamException, NotFoundException {

		PurchaseDTO purchase = controller.calculateTotalToPay(purchaseId);
		purchase = controller.toPay(userId, purchaseId);

		return toJson(purchase);

	}

	@GetMapping(value = "/users/{userId}/purchases/{purchaseId}", produces = "application/json;charset=UTF-8")
	public String getPurchase(@PathVariable int userId, @PathVariable int purchaseId)
			throws NotFoundException, InvalidParamException {

		PurchaseDTO purchase = controller.getPurchase(userId, purchaseId);

		return toJson(purchase);
	}

	@GetMapping(value = "/users/{userId}/purchases", produces = "application/json;charset=UTF-8")
	public String getAllPurchases(@PathVariable int userId) throws NotFoundException, InvalidParamException {

		List<PurchaseDTO> purchases = controller.getAllPurchases(userId);

		return new Gson().toJson(purchases);
	}

	@DeleteMapping(value = "/users/{userId}/purchases", produces = "application/json;charset=UTF-8")
	public void removeAllPurchases(@PathVariable int userId) throws Exception {

		controller.removePurchases(userId);
	}

	@DeleteMapping(value = "/users/{userId}/purchases/{purchaseId}", produces = "application/json;charset=UTF-8")
	public void removePurchase(@PathVariable int userId, @PathVariable int purchaseId)
			throws NotFoundException, InvalidParamException {
		
		controller.cancelPurchaseOfUser(userId, purchaseId);
	}

	@DeleteMapping(value = "/users/{userId}/purchases/{purchaseId}/dresses/{dressId}", produces = "application/json;charset=UTF-8")
	public void removeDressOfPurchase(@PathVariable int dressId, @PathVariable int purchaseId)
			throws NotFoundException, InvalidParamException {

		controller.deletePurchaseLine(purchaseId, dressId);
	}

}
