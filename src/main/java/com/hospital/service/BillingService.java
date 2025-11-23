package com.hospital.service;

import com.hospital.exception.NotFoundException;
import com.hospital.exception.ValidationException;
import com.hospital.model.Invoice;
import com.hospital.model.InvoiceItem;
import com.hospital.model.InvoiceStatus;
import com.hospital.repository.impl.InMemoryInvoiceRepository;

import java.math.BigDecimal;
import java.util.List;

public class BillingService {
    private final InMemoryInvoiceRepository invoiceRepository;

    public BillingService(InMemoryInvoiceRepository invoiceRepository) {
        this.invoiceRepository = invoiceRepository;
    }

    public Invoice createInvoice(String patientId) {
        if (patientId == null || patientId.isEmpty()) {
            throw new ValidationException("Patient ID is required");
        }

        Invoice invoice = new Invoice();
        invoice.setPatientId(patientId);
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setTotal(BigDecimal.ZERO);

        return invoiceRepository.save(invoice);
    }

    public Invoice addItem(String invoiceId, String description, BigDecimal price, int quantity) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + invoiceId));

        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new ValidationException("Cannot modify a paid invoice");
        }

        validateInvoiceItem(description, price, quantity);

        InvoiceItem item = new InvoiceItem(description, price, quantity);
        invoice.getItems().add(item);
        invoice.calculateTotal();

        return invoiceRepository.save(invoice);
    }

    public Invoice finalizeInvoice(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + invoiceId));

        if (invoice.getItems().isEmpty()) {
            throw new ValidationException("Cannot finalize an invoice with no items");
        }

        invoice.setStatus(InvoiceStatus.UNPAID);
        return invoiceRepository.save(invoice);
    }

    public Invoice markAsPaid(String invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + invoiceId));

        if (invoice.getStatus() != InvoiceStatus.UNPAID) {
            throw new ValidationException("Only unpaid invoices can be marked as paid");
        }

        invoice.setStatus(InvoiceStatus.PAID);
        return invoiceRepository.save(invoice);
    }

    public List<Invoice> findByPatient(String patientId) {
        return invoiceRepository.findByPatientId(patientId);
    }

    public List<Invoice> findAll() {
        return invoiceRepository.findAll();
    }

    public Invoice findById(String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Invoice not found: " + id));
    }

    private void validateInvoiceItem(String description, BigDecimal price, int quantity) {
        if (description == null || description.isEmpty()) {
            throw new ValidationException("Item description is required");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Price must be non-negative");
        }
        if (quantity <= 0) {
            throw new ValidationException("Quantity must be positive");
        }
    }
}
