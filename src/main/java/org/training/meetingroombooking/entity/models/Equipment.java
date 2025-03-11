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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name; // Tên thiết bị (VD: Máy chiếu, Loa, Bàn,...)

    @Column(length = 255)
    private String description; // Mô tả thiết bị (VD: Máy chiếu 4K)

    @Column(nullable = false)
    private int quantity; // Số lượng thiết bị trong phòng

    @Column(nullable = false)
    private boolean available = true; // Trạng thái của thiết bị

    // Mỗi thiết bị thuộc về một phòng cụ thể
    @ManyToOne
    @JoinColumn(name = "roomId", nullable = true)
    private Room room;
}
