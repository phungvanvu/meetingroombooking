package org.training.meetingroombooking.entity.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.training.meetingroombooking.entity.enums.BookingStatus;
import org.training.meetingroombooking.entity.enums.Purpose;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "roomBookings")
public class RoomBooking {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bookingId;

  @ManyToOne
  @JoinColumn(name = "roomId", nullable = false)
  private Room room;

  @ManyToOne
  @JoinColumn(name = "bookedBy", nullable = false)
  private User bookedBy;

  @FutureOrPresent
  @Column(nullable = false)
  private LocalDateTime startTime;

  @FutureOrPresent
  @Column(nullable = false)
  private LocalDateTime endTime;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private Purpose purpose;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private BookingStatus status = BookingStatus.CONFIRMED;

  @Column(columnDefinition = "TEXT", length = 255)
  private String description;

  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    this.createdAt = LocalDateTime.now();
    if (this.status == null) {
      this.status = BookingStatus.CONFIRMED;
    }
  }
}
