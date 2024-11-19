package fi.uba.memo1.apirest.finanzas.service;

import fi.uba.memo1.apirest.finanzas.model.CostosMensuales;
import fi.uba.memo1.apirest.finanzas.repository.CostosMensualesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CostosMensualesService {

    @Autowired
    private CostosMensualesRepository repository;

    public List<CostosMensuales> findAll() {
        return repository.findAll();
    }

    public CostosMensuales findById(String id) {
        return repository.findById(Long.parseLong(id)).orElse(null);
    }

    public CostosMensuales save(CostosMensuales costos) {
        return repository.save(costos);
    }
}
