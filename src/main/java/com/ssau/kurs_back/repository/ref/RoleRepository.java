package com.ssau.kurs_back.repository.ref;

import com.ssau.kurs_back.entity.ref.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseReferenceRepository<Role> {
}
