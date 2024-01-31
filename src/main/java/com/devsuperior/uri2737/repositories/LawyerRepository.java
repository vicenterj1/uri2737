package com.devsuperior.uri2737.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.devsuperior.uri2737.entities.Lawyer;
import com.devsuperior.uri2737.projections.LawyerMinProjection;


public interface LawyerRepository extends JpaRepository<Lawyer, Long> {

	// teve que colocar o alias porque o Projection tem o nome escrito de forma diferente da base de dados
	@Query(nativeQuery = true, value = "select name, customers_number AS customersNumber "
			+ "from lawyers l "
			+ "where	l.customers_number = ( select max(customers_number) from lawyers)"
			+ "union all "
			+ "select name, customers_number "
			+ "from lawyers l "
			+ "where	l.customers_number = ( select min(customers_number) from lawyers) "
			+ "union all "
			+ "(select 'Average' as name, round(avg(customers_number)) "
			+ "from lawyers)"
			)
	List<LawyerMinProjection> searchSQL();
	
	// a JPQL não tem UNION e a reescrita pode dar muito trabalho
	// por isso AQUI só tem SQL RAIZ
}
