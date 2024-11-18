package fi.uba.memo1.apirest.finanzas.repository;

import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CostosMensualesRepository extends JpaRepository<CostosMensuales, Long> {
}
