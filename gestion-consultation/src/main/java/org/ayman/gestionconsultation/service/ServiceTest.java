package org.ayman.gestionconsultation.service;

import org.ayman.gestionconsultation.entities.Patient;

import java.util.List;

public class ServiceTest {
    public static void main(String[] args) {
        ICabinetService service = new CabinetService();
        Patient patient = service.getPatientById(1);
        patient.setPrenom("Ayman");
        service.modifierPatient(patient);
        System.out.println(patient);

    }
}
