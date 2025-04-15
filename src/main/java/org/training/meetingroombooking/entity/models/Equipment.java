package org.training.meetingroombooking.entity.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<RoomEquipment> roomEquipments;
}
