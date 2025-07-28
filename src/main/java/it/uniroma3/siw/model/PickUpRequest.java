package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import io.micrometer.common.lang.NonNull;
import it.uniroma3.siw.model.Util.PicKUpStatus;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class PickUpRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotNull
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate requestDate;
	@NonNull
	private PicKUpStatus status;
	@Column(columnDefinition = "TEXT")
	@Size(max = 2000)
	private String note;
	@Lob
	@Basic(fetch = FetchType.LAZY)
	private byte[] photo;
	@OneToMany(mappedBy = "pickUpRequest")
	private List<PickUpRequestItem> pickUpRequestItems;
	@NonNull
	private String name;
	@NonNull
	private String surname;
	@NonNull
	private String address;
	@NonNull
	private String phone;
	@NotNull
	private String email;

	@ManyToOne
	private User user;
	@Column(columnDefinition = "TEXT")
	@Size(max = 2000)
	private String adminNote;

	public LocalDate getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(LocalDate requestDate) {
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

	public byte[] getPhoto() {
		return photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAdminNote() {
		return adminNote;
	}

	public void setAdminNote(String adminNote) {
		this.adminNote = adminNote;
	}

}
