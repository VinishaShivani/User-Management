package com.trb.allocationservice.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trb.allocationservice.entity.UsersModel;

@Repository
public interface UserRepo extends JpaRepository<UsersModel, Long> {

	UsersModel findByfirstNameAndIdentityNumber(String firstName,String IdentifyNumber);
	
	UsersModel findByfirstName(String firstName);
	
	UsersModel findByuserId(String userId);
	
	@Query(value = "select r.code from thrdexams.users u inner join thrdexams.users_roles ur on ur.user_pk = u.user_pk inner join thrdexams.roles r on r.role_id = ur.role_id where u.user_id=?1", nativeQuery = true)
	String getUserRoles(String IdNum);
}
