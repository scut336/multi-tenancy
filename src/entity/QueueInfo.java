package entity;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class QueueInfo {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	private String QueueName;
	private double Capacity;
	private int ParentQueue;
	private char IsLeafQueue;
	private String ResourceLimit;
	private char Enable;
	
	
	public QueueInfo(){}


	public QueueInfo(long id, String queueName, double capacity,
			int parentQueue, char isLeafQueue, String resourceLimit,
			char enable) {
		this.id = id;
		QueueName = queueName;
		Capacity = capacity;
		ParentQueue = parentQueue;
		IsLeafQueue = isLeafQueue;
		ResourceLimit = resourceLimit;
		Enable = enable;
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getQueueName() {
		return QueueName;
	}


	public void setQueueName(String queueName) {
		QueueName = queueName;
	}


	public double getCapacity() {
		return Capacity;
	}


	public void setCapacity(double capacity) {
		Capacity = capacity;
	}


	public int getParentQueue() {
		return ParentQueue;
	}


	public void setParentQueue(int parentQueue) {
		ParentQueue = parentQueue;
	}


	public char getIsLeafQueue() {
		return IsLeafQueue;
	}


	public void setIsLeafQueue(char isLeafQueue) {
		IsLeafQueue = isLeafQueue;
	}


	public String getResourceLimit() {
		return ResourceLimit;
	}


	public void setResourceLimit(String resourceLimit) {
		ResourceLimit = resourceLimit;
	}


	public char getEnable() {
		return Enable;
	}


	public void setEnable(char enable) {
		Enable = enable;
	}

}
