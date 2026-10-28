package com.sampi.denunciaservice.service;

import com.sampi.denunciaservice.entity.Denuncia;
import com.sampi.denunciaservice.repository.DenunciaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DenunciaService {

    private final DenunciaRepository repository;

    public DenunciaService(DenunciaRepository repository) {
        this.repository = repository;
    }

    public Denuncia registrar(Denuncia d) {
        d.setId(null);
        d.setEstado(1);
        if (d.getFecha() == null) {
            d.setFecha(LocalDateTime.now());
        }
        return repository.save(d);
    }

    public List<Denuncia> listarPaginadas(int offset, int limit) {
        List<Denuncia> todas = repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        if (offset < 0) {
            offset = 0;
        }
        if (limit <= 0 || offset >= todas.size()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(todas.subList(offset, Math.min(offset + limit, todas.size())));
    }

    public Optional<Denuncia> obtenerPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Denuncia> obtenerPorDni(String dni) {
        return repository.findByDniOrderByIdDesc(dni);
    }

    public Optional<Denuncia> atenderDenuncia(Integer id) {
        Optional<Denuncia> opt = repository.findById(id);
        if (opt.isEmpty()) {
            return Optional.empty();
        }
        Denuncia denuncia = opt.get();
        if (denuncia.getEstado() != null && denuncia.getEstado() == 1) {
            denuncia.setEstado(0);
        }
        return Optional.of(repository.save(denuncia));
    }
}
