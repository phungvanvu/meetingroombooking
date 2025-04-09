package org.training.meetingroombooking.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.training.meetingroombooking.entity.dto.PositionDTO;
import org.training.meetingroombooking.entity.dto.Response.ApiResponse;
import org.training.meetingroombooking.service.PositionService;

@RestController
@RequestMapping("/position")
public class PositionController {

    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @PostMapping
    public ApiResponse<PositionDTO> createPosition(@Valid @RequestBody PositionDTO positionDTO) {
        return ApiResponse.<PositionDTO>builder()
                .success(true)
                .data(positionService.create(positionDTO))
                .build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<PositionDTO>> getPositions() {
        return ApiResponse.<List<PositionDTO>>builder()
                .success(true)
                .data(positionService.getAll())
                .build();
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Page<PositionDTO>> searchPositions(
            @RequestParam(value = "positionName", required = false) String positionName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "positionName") String sortBy,
            @RequestParam(value = "sortDirection", defaultValue = "ASC") String sortDirection
    ) {
        Page<PositionDTO> pageResult = positionService.getPositions(positionName, description, page, size, sortBy, sortDirection);
        return ApiResponse.<Page<PositionDTO>>builder()
                .success(true)
                .data(pageResult)
                .build();
    }

    @PutMapping("/{positionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<PositionDTO> updatePosition(
            @PathVariable String positionName,
            @Valid @RequestBody PositionDTO positionDTO
    ) {
        PositionDTO updatedPosition = positionService.update(positionName, positionDTO);
        return ApiResponse.<PositionDTO>builder()
                .success(true)
                .data(updatedPosition)
                .build();
    }

    @DeleteMapping("/{positionName}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> deletePosition(@PathVariable String positionName) {
        positionService.deletePosition(positionName);
        return ApiResponse.<String>builder()
                .success(true)
                .data("Position has been deleted")
                .build();
    }
}
