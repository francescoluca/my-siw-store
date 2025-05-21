package it.uniroma3.siw.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class PickUpRequest {
	
	private enum PicKUpStatus{
		REFUSED,APPROVED,PENDING,DRAFT
	};
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	@NonNull
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime requestDate;
	@NonNull
	private PicKUpStatus status;
	private String note;
	private List<String> photos;
	
	@OneToMany(mappedBy = "pickUpRequest")
	private List<PickUpRequestItem> pickUpRequestItems;
	
	@ManyToOne
	private User user;
	
	public LocalDateTime getRequestDate() {
		return requestDate;
	}
	public void setRequestDate(LocalDateTime requestDate) {
		this.requestDate = requestDate;
	}
	public PicKUpStatus getStatus() {
		return status;
	}
	public void setStatus(PicKUpStatus status) {
		this.status = status;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public List<String> getPhotos() {
		return photos;
	}
	public void setPhotos(List<String> photos) {
		this.photos = photos;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<PickUpRequestItem> getPickUpRequestItems() {
		return pickUpRequestItems;
	}
	public void setPickUpRequestItems(List<PickUpRequestItem> pickUpRequestItems) {
		this.pickUpRequestItems = pickUpRequestItems;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
