package com.sampi.denunciaservice.controller;

import com.sampi.denunciaservice.entity.Denuncia;
import com.sampi.denunciaservice.service.DenunciaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/denuncias")
@CrossOrigin(origins = "*")
public class DenunciaController {

    private final DenunciaService service;

    public DenunciaController(DenunciaService service) {
        this.service = service;
    }

    @PostMapping({"", "/"})
    public ResponseEntity<Denuncia> registrar(@RequestBody Denuncia denuncia) {
        Denuncia creada = service.registrar(denuncia);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping({"", "/"})
    public ResponseEntity<List<Denuncia>> listar(
            @RequestParam(name = "offset", defaultValue = "0") int offset,
            @RequestParam(name = "limit", defaultValue = "5") int limit) {
        return ResponseEntity.ok(service.listarPaginadas(offset, limit));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Denuncia> obtenerPorId(@PathVariable("id") Integer id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/ciudadano/{dni}")
    public ResponseEntity<List<Denuncia>> obtenerPorDni(@PathVariable("dni") String dni) {
        return ResponseEntity.ok(service.obtenerPorDni(dni));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Denuncia> atender(@PathVariable("id") Integer id) {
        return service.atenderDenuncia(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
