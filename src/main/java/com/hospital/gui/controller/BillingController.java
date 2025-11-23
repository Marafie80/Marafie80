package com.hospital.gui.controller;

import com.hospital.model.Invoice;
import com.hospital.model.InvoiceStatus;
import com.hospital.service.BillingService;
import com.hospital.service.PatientService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigDecimal;

public class BillingController implements BaseController {
    @FXML private TableView<Invoice> invoicesTable;
    @FXML private TableColumn<Invoice, String> colId;
    @FXML private TableColumn<Invoice, String> colPatient;
    @FXML private TableColumn<Invoice, BigDecimal> colTotal;
    @FXML private TableColumn<Invoice, InvoiceStatus> colStatus;
    @FXML private ComboBox<String> statusFilterComboBox;

    private BillingService billingService;
    private PatientService patientService;
    private ObservableList<Invoice> invoices = FXCollections.observableArrayList();

    public void setBillingService(BillingService billingService) {
        this.billingService = billingService;
    }

    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    @FXML
    public void initialize() {
        setupTable();
        setupFilter();
        loadInvoices();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colPatient.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPatientId()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        invoicesTable.setItems(invoices);
    }

    private void setupFilter() {
        statusFilterComboBox.getItems().addAll("All", "Draft", "Unpaid", "Paid");
        statusFilterComboBox.setValue("All");
        statusFilterComboBox.setOnAction(e -> loadInvoices());
    }

    private void loadInvoices() {
        if (billingService != null) {
            invoices.setAll(billingService.findAll());
            applyFilter();
        }
    }

    private void applyFilter() {
        String filter = statusFilterComboBox.getValue();
        if (filter != null && !filter.equals("All")) {
            InvoiceStatus status = InvoiceStatus.valueOf(filter.toUpperCase());
            invoices.setAll(billingService.findAll().stream()
                    .filter(i -> i.getStatus() == status)
                    .toList());
        }
    }

    @FXML
    private void onNewInvoice() {
        showAlert("New invoice form - Feature coming soon!");
    }

    @FXML
    private void onViewDetails() {
        Invoice selected = invoicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an invoice to view");
            return;
        }
        showAlert("Invoice details for ID: " + selected.getId() + " - Feature coming soon!");
    }

    @FXML
    private void onMarkPaid() {
        Invoice selected = invoicesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select an invoice to mark as paid");
            return;
        }

        try {
            billingService.markAsPaid(selected.getId());
            loadInvoices();
            showAlert("Invoice marked as paid");
        } catch (Exception e) {
            showAlert("Error: " + e.getMessage());
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
