package org.cscb525.service;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

import org.cscb525.configure.SessionFactoryUtil;
import org.cscb525.entity.Client;
import org.cscb525.entity.Payment;
import org.hibernate.Session;
import org.hibernate.Transaction;
public class PaymentService {
    // ======= create =======
    public void createPayment(Client client, double amount, String reason) {
        // 1. Създаване на обекта
        Payment payment = new Payment(amount, LocalDate.now(), reason, client);
        // 2. Запис в Базата Данни (MySQL)
        try (Session session = SessionFactoryUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(payment);
            transaction.commit();
        }
        // 3. Генериране на текстова бележка (Receipt)
        saveReceiptToFile(payment);
    }
    // ======= file =======
    private void saveReceiptToFile(Payment payment) {
        String folderName = "receipts";
        File uploadsFolder = new File(folderName);
        // Създава папката ако я няма
        if (!uploadsFolder.exists()) {
            uploadsFolder.mkdir();
        }
        // Име на файла: receipts/Receipt_123.txt
        String fileName = folderName + File.separator + "Receipt_" + payment.getId() + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(payment.toString()); // Използва чистия формат от Payment.java
            System.out.println("Receipt generated: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving receipt file: " + e.getMessage());
        }
    }
}