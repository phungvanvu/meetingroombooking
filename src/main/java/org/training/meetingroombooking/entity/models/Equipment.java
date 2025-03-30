package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Equipments")
public class Equipment {

    @Id
    @Column(nullable = false, length = 50)
    private String equipmentName; // Tên thiết bị (VD: Máy chiếu, Loa, Bàn,...)

    @Column(length = 255)
    private String description; // Mô tả thiết bị (VD: Máy chiếu 4K)

    @Column(nullable = false)
    private boolean available = true; // Trạng thái của thiết bị
}
