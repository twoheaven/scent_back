package com.scent.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;

	/**
	 * @return the authorityName
	 */
	public String getAuthorityName() {
		return authorityName;
	}

	/**
	 * @param authorityName the authorityName to set
	 */
	public void setAuthorityName(String authorityName) {
		this.authorityName = authorityName;
	}
	
    public static AuthorityBuilder builder() {
        return new AuthorityBuilder();
    }
    
    public static class AuthorityBuilder {
        private String authorityName;

        private AuthorityBuilder() {
            // AuthorityBuilder의 private 생성자
        }

        public AuthorityBuilder authorityName(String authorityName) {
            this.authorityName = authorityName;
            return this;
        }

        public Authority build() {
            Authority authority = new Authority();
            authority.authorityName = this.authorityName;
            return authority;
        }
    }
	
	
}