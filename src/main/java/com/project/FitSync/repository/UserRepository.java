package com.project.FitSync.repository;

import com.project.FitSync.model.AuthProviderType;
import com.project.FitSync.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

//    Optional<User> fingByProviderIdAndProviderType(String providerId, AuthProviderType providerType);
}
