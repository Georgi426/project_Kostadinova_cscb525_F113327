package org.cscb525.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "client_payments")
@Getter
@Setter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Positive(message = "Amount must be positive")
    private Double amount;

    @Column(name = "payment_date", nullable = false)
    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    @Column(nullable = false)
    @NotBlank(message = "Reason is required")
    @Size(max = 255)
    private String reason;

    @NotNull(message = "Payment must be associated with a client.")
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Client client;

    public Payment(Double amount, LocalDate paymentDate, String reason, Client client) {
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.reason = reason;
        this.client = client;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PAYMENT RECEIPT\n");
        sb.append("\n"); // Please verify user wants this specific format
        sb.append("Receipt ID:   ").append(getId()).append("\n");
        sb.append("Date:         ").append(getPaymentDate()).append("\n");
        sb.append("\n");
        if (getClient() != null) {
            sb.append("Client Name:  ").append(getClient().getName()).append("\n");
        }
        sb.append("Reason:       ").append(getReason()).append("\n");
        sb.append("\n");
        sb.append("TOTAL AMOUNT: $").append(String.format("%.2f", getAmount())).append("\n");
        sb.append("\n");
        sb.append("Thank you for your business!");
        return sb.toString();
    }
}