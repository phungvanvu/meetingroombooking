package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.RequestStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "requests")
public class Request {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long requestId;

  private String title; // Tiêu đề
  private String location; // Địa điểm
  private String description; // Mô tả
  private String jobLevel; // Mức độ công việc

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private RequestStatus status;

  private String approval; // Ghi chú về quyết định phê duyệt hoặc lý do từ chối

  @Column(name = "target_date")
  private LocalDateTime target;

  @Column(name = "onboard_date")
  private LocalDateTime onboard;

  @ManyToOne
  @JoinColumn(name = "created_by")
  private User createdBy; // Người tạo

  @ManyToOne
  @JoinColumn(name = "hr_pic")
  private User hrPic; // Người phụ trách

  private String action;
}
