package com.animals.app.domain;

import java.io.Serializable;
import java.security.Principal;
import java.sql.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

/**
 * Created by oleg on 22.07.2015.
 */

@XmlRootElement
public class User implements Serializable, Principal {
	
	@DecimalMin(value = "1")
	private Integer id;
	    
	@Length(min = 2, max = 35, message =  "Name must be between {min} and {max}.")
    private String name;
    
	@Length(min = 2, max = 45, message =  "Surname must be between {min} and {max}.") 
    private String surname;

    @NotNull(message =  "Registration Date can not be null")
    private Date registrationDate;    
     
    @Email(message = "Wrong format for mail", regexp = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}")
    private String email;

    @NotNull(message =  "Password can not be null")
    @Length(max = 40, message =  "Password must be less than {max}  characters.")
    private String password;
    
    @Pattern(message = "Wrong format for email", regexp = "^[0-9]{3}-[0-9]{7}$") 
    private String phone;
    
    @Length(min = 4, max = 16, message = "The socialLogin must be between {min} and {max}.")
    private String socialLogin;
    
    @Length(max = 120, message =  "Address must be less than {max} characters.") 
    private String address = "N/A"; 
    
    @Length(max = 70, message =  "Organization Name must be less than {max} characters.") 
    private String organizationName = "N/A";
    
    @Length(max = 100, message =  "Organization Info  must be less than {max} characters.") 
    private String organizationInfo = "N/A";

    @Length(max = 100, message =  "GoogleId  must be less than {max} characters.")
    private String googleId;
    
    @Length(max = 100, message =  "FacebookId  must be less than {max} characters.")
    private String facebookId;
    
    @Length(max = 100, message =  "TwitterId  must be less than {max} characters.")
    private String twitterId;
    
    @Length(max = 300, message =  "SocialPhoto  must be less than {max} characters.")
    private String socialPhoto;
    
    private boolean isActive = true;
    
    @Valid
    @Size(min = 1, message =  "UserRole can not be empty")
    private List<UserRole> userRole;
    
    @Valid
    @NotNull(message =  "UserType can not be null")
    private UserType userType;

    @Length(max = 45, message =  "Verificator  must be less than {max} characters.")
    private String emailVerificator;

    public User() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSocialLogin() {
        return socialLogin;
    }

    public void setSocialLogin(String socialLogin) {
        this.socialLogin = socialLogin;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationInfo() {
        return organizationInfo;
    }

    public void setOrganizationInfo(String organizationInfo) {
        this.organizationInfo = organizationInfo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }

    public List<UserRole> getUserRole() {
        return userRole;
    }

    public void setUserRole(List<UserRole> userRole) {
        this.userRole = userRole;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
    
    

    public String getGoogleId() {
		return googleId;
	}

	public void setGoogleId(String googleId) {
		this.googleId = googleId;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public String getTwitterId() {
		return twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getSocialPhoto() {
		return socialPhoto;
	}

	public void setSocialPhoto(String socialPhoto) {
		this.socialPhoto = socialPhoto;
	}	

	public String getEmailVerificator() {
		return emailVerificator;
	}

	public void setEmailVerificator(String emailVerificator) {
		this.emailVerificator = emailVerificator;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isActive != user.isActive) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (surname != null ? !surname.equals(user.surname) : user.surname != null) return false;
        if (registrationDate != null ? !registrationDate.equals(user.registrationDate) : user.registrationDate != null)
            return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (phone != null ? !phone.equals(user.phone) : user.phone != null) return false;
        if (socialLogin != null ? !socialLogin.equals(user.socialLogin) : user.socialLogin != null) return false;
        if (organizationName != null ? !organizationName.equals(user.organizationName) : user.organizationName != null)
            return false;
        if (organizationInfo != null ? !organizationInfo.equals(user.organizationInfo) : user.organizationInfo != null)
            return false;
        if (userRole != null ? !userRole.equals(user.userRole) : user.userRole != null) return false;
        if (address != null ? !address.equals(user.address) : user.address != null) return false;        
        if (googleId != null ? !googleId.equals(user.googleId) : user.googleId != null) return false;
        if (facebookId != null ? !facebookId.equals(user.facebookId) : user.facebookId != null) return false;
        if (twitterId != null ? !twitterId.equals(user.twitterId) : user.twitterId != null) return false;
        if (socialPhoto != null ? !socialPhoto.equals(user.socialPhoto) : user.socialPhoto != null) return false;
        return !(userType != null ? !userType.equals(user.userType) : user.userType != null);
        

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (socialLogin != null ? socialLogin.hashCode() : 0);
        result = 31 * result + (organizationName != null ? organizationName.hashCode() : 0);
        result = 31 * result + (organizationInfo != null ? organizationInfo.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (userType != null ? userType.hashCode() : 0);
        result = 31 * result + (googleId != null ? googleId.hashCode() : 0);
        result = 31 * result + (facebookId != null ? facebookId.hashCode() : 0);
        result = 31 * result + (twitterId != null ? twitterId.hashCode() : 0);
        result = 31 * result + (socialPhoto != null ? socialPhoto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", registrationDate=" + registrationDate +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", socialLogin='" + socialLogin + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", organizationInfo='" + organizationInfo + '\'' +
                ", isActive=" + isActive +
                ", userRole=" + userRole +
                ", address='" + address + '\'' +
                ", userType=" + userType +
                ", googleId=" + googleId +
                ", facebookId=" + facebookId +
                ", twitterId=" + twitterId +
                ", socialPhoto=" + socialPhoto +
                '}';
    }
}
