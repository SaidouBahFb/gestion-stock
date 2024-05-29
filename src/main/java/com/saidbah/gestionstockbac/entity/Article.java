package com.saidbah.gestionstockbac.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "articles")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "designation")
    private String designation;

    @Column(name = "price_unit_ht")
    private float priceUnitHt;

    @Column(name = "rate_tva")
    private float rateTva;

    @Column(name = "price_unit_ttc")
    private float priceUnitTtc;

    @Column(name = "photo")
    private String photo;

    @ManyToOne
    @JoinColumn(name = "idCategory")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "idCompany")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    @OneToMany(mappedBy = "article")
    private List<SaleLine> saleLines;

    @OneToMany(mappedBy = "article")
    private List<CustomerOrderLine> customerOrderLines;

    @OneToMany(mappedBy = "article")
    private List<SupplierOrderLine> supplierOrderLines;

    @OneToMany(mappedBy = "article")
    private List<MvtStock> mvtStocks;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
