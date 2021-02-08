package org.enset.demo.repository;

import org.enset.demo.entities.Billing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface BillingRepository extends JpaRepository<Billing,Long> {
}
