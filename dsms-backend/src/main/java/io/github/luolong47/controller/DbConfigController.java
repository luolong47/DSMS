package io.github.luolong47.controller;

import io.github.luolong47.dto.DbConfigDTO;
import io.github.luolong47.dto.DbConfigSearchDTO;
import io.github.luolong47.entity.DbConfig;
import io.github.luolong47.service.DbConfigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/database/config")
@RequiredArgsConstructor
public class DbConfigController {
    private final DbConfigService dbConfigService;

    @PostMapping("/search")
    public ResponseEntity<List<DbConfig>> search(@RequestBody DbConfigSearchDTO searchDTO) {
        return ResponseEntity.ok(dbConfigService.search(searchDTO));
    }

    @PostMapping("/save")
    public ResponseEntity<DbConfig> save(@Valid @RequestBody DbConfigDTO dto) {
        return ResponseEntity.ok(dbConfigService.save(dto));
    }

    @DeleteMapping("/delete/{configName}")
    public ResponseEntity<Void> delete(@PathVariable String configName) {
        dbConfigService.delete(configName);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/test")
    public ResponseEntity<Boolean> testConnection(@Valid @RequestBody DbConfigDTO dto) {
        return ResponseEntity.ok(dbConfigService.testConnection(dto));
    }
} 