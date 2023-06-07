package com.jdc.balance.model.domain.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
public class Balance implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "balance_seq")
    @SequenceGenerator(name = "balance_seq")
	private int id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable =  false)
    private String category;
    
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})  // user pee ma balance build loh ya ml
    private User user;

    private Type type;
    
    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
    public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public enum Type {
        Income,
        Expense
    }

}