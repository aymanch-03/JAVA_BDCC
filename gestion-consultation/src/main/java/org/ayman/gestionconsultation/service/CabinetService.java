package org.ayman.gestionconsultation.service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.ayman.gestionconsultation.entities.Consultation;
import org.ayman.gestionconsultation.entities.Patient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CabinetService implements ICabinetService {
    private static CabinetService instance;
    private ObservableList<Patient> patients = FXCollections.observableArrayList();
    private List<Consultation> consultations = new ArrayList<>();
    private AtomicInteger patientIdCounter = new AtomicInteger(1);
    private AtomicInteger consultationIdCounter = new AtomicInteger(1);

    public static CabinetService getInstance() {
        if (instance == null) {
            instance = new CabinetService();
        }
        return instance;
    }

    public CabinetService() {
        // No-arg constructor for in-memory storage
    }

    @Override
    public void ajouterPatient(Patient patient) {
        patient.setId_patient(patientIdCounter.getAndIncrement());
        patients.add(patient);
    }

    @Override
    public void supprimerPatient(Patient patient) {
        patients.remove(patient);
        // Remove consultations for this patient
        consultations
                .removeIf(c -> c.getPatient() != null && c.getPatient().getId_patient() == patient.getId_patient());
    }

    @Override
    public void modifierPatient(Patient patient) {
        // Already updated by reference in JavaFX ObservableList
    }

    @Override
    public Patient getPatientById(int id) {
        return patients.stream().filter(p -> p.getId_patient() == id).findFirst().orElse(null);
    }

    @Override
    public List<Patient> getAllPatient() {
        return new ArrayList<>(patients);
    }

    @Override
    public List<Patient> searchByQuery(String query) {
        String lower = query.toLowerCase();
        return patients.stream().filter(p -> p.getNom().toLowerCase().contains(lower) ||
                p.getPrenom().toLowerCase().contains(lower)).collect(Collectors.toList());
    }

    @Override
    public void ajouterConsultation(Consultation consultation) {
        consultation.setId_consultation(consultationIdCounter.getAndIncrement());
        consultations.add(consultation);
    }

    @Override
    public void supprimerConsultation(Consultation consultation) {
        consultations.remove(consultation);
    }

    @Override
    public void modifierConsultation(Consultation consultation) {
        // Already updated by reference in the list
    }

    @Override
    public Consultation getConsultationById(int id) {
        return consultations.stream().filter(c -> c.getId_consultation() == id).findFirst().orElse(null);
    }

    @Override
    public List<Consultation> getAllConsultation() {
        return new ArrayList<>(consultations);
    }

    @Override
    public ObservableList<Patient> getPatients() {
        return patients;
    }

    @Override
    public void refreshPatients() {
        // No-op for in-memory
    }
}
