package com.lamadrid.store.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.hibernate.annotations.Type;

import com.lamadrid.store.utilities.InvalidParamException;

@Entity(name = "dress_to_purchase")
public class PurchaseLine {
	
	@EmbeddedId
	private PurchaseLineId id;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("purchase_id")
	private Purchase purchase;
 
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("dress_id")
	private Dress dress;
 
	private double price_unit;
	private double quantity;
	private double subtotal;
	
	@Column (name= "is_paid_and_updated",nullable = false)
	@Type (type = "org.hibernate.type.NumericBooleanType")
	private boolean isPaidAndUpdated;
	
	private PurchaseLine() {}
 
	public PurchaseLine(Purchase purchase, Dress dress, double quantity) throws InvalidParamException {
		
		if(purchase == null) throw new InvalidParamException();
		
		if(dress == null) throw new InvalidParamException();
		
		if(quantity<0) throw new InvalidParamException();
		
    	this.purchase = purchase;
    	this.dress = dress;
    	this.id = new PurchaseLineId(purchase.getId(), dress.getId());
    	this.price_unit = dress.getPrice_sold();
    	this.quantity = quantity;
    	this.subtotal = dress.getPrice_sold()*quantity;
    	this.isPaidAndUpdated=false;
 	}

	public PurchaseLineId getId() {
		return id;
	}
	
	public boolean isPaidAndUpdated() {
		return isPaidAndUpdated;
	}

	public double getPrice_unit() {
		return price_unit;
	}

	public double getQuantity() {
		return quantity;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public Purchase getPurchase() {
		return purchase;
	}

	public void setPurchase(Purchase purchase) {
		this.purchase = purchase;
	}

	public Dress getDress() {
		return dress;
	}

	public void setDress(Dress dress) {
		this.dress = dress;
	}
	
	public double updateStock() {
		if(!isPaidAndUpdated) {
			isPaidAndUpdated=true;
			return quantity;
		}
		return 0;
		
	}

	
	@Override
	public boolean equals(Object o) {
    	if (this == o) return true;
 
    	if (o == null || getClass() != o.getClass())
        	return false;
 
    	PurchaseLine that = (PurchaseLine) o;
    	return Objects.equals(purchase, that.purchase) &&
           	Objects.equals(dress, that.dress);
	}
 
	@Override
	public int hashCode() {
    	return Objects.hash(purchase, dress);
	}


}
