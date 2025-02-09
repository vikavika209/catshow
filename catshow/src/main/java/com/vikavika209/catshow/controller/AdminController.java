package com.vikavika209.catshow.controller;


import com.vikavika209.catshow.exception.CatNotFoundException;
import com.vikavika209.catshow.exception.OwnerNotFoundException;
import com.vikavika209.catshow.exception.ShowNotFoundException;
import com.vikavika209.catshow.model.Owner;
import com.vikavika209.catshow.model.Role;
import com.vikavika209.catshow.service.OwnerService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@Tag(name = "Admin API")
public class AdminController {
    private final OwnerService ownerService;

    @Autowired
    public AdminController(OwnerService ownerService) {
        this.ownerService = ownerService;
    }

    @GetMapping("/admin")
    @Operation(
            summary = "Получить список всех пользователей",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное получение пользователей",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = List.class))),
                    @ApiResponse(responseCode = "403", description = "Недостаточно прав доступа")
            }
    )
    public ResponseEntity<List<Owner>> allOwners() {
        return ResponseEntity.ok(ownerService.getAllOwners());
    }

    @PutMapping("/admin/{id}")
    @Operation(
            summary = "Добавить роль пользователю",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Успешное обновление роли", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Owner.class))),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
                    @ApiResponse(responseCode = "403", description = "Пользователь не авторизован")
            }
    )
    public ResponseEntity<Owner> updateUserRole(@PathVariable int id,
                                        @RequestBody Role role) throws OwnerNotFoundException, ShowNotFoundException, CatNotFoundException {
        return ResponseEntity.ok(ownerService.setOwnerRole(id, role));
    }
}
