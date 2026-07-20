package com.factoryflow.inventory;

import com.factoryflow.lot.Lot;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false, unique = true)
    private Lot lot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WarehouseType warehouse;

    @Column(nullable = false)
    private int qty;

    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}