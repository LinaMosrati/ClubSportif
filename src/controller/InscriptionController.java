package controller;

import dao.InscriptionDao;
import model.Inscription;

import java.util.List;

public class InscriptionController {

    private InscriptionDao inscriptionDao = new InscriptionDao();

    public List<Inscription> getAllInscriptions() {
        return inscriptionDao.getAllInscriptions();
    }

    public boolean accepterInscription(int id) {
        return inscriptionDao.changerStatut(id, "ACCEPTEE");
    }

    public boolean refuserInscription(int id) {
        return inscriptionDao.changerStatut(id, "REFUSEE");
    }

    public boolean supprimerInscription(int id) {
        return inscriptionDao.supprimerInscription(id);
    }
    
    public java.util.Map<String, Integer> getParticipantsParActivite() {
        return inscriptionDao.getParticipantsParActivite();
    }
    public boolean inscrireMembre(String login, int activiteId) {
        return inscriptionDao.inscrireMembre(login, activiteId);
    }
    public boolean dejaInscrit(String login, int activiteId) {
        return inscriptionDao.dejaInscrit(login, activiteId);
    }
    public String getStatutInscription(String login, int activiteId) {
        return inscriptionDao.getStatutInscription(login, activiteId);
    }
    public java.util.List<Inscription> getInscriptionsByLogin(String login) {
        return inscriptionDao.getInscriptionsByLogin(login);
    }
    
    public boolean archiverToutesInscriptions() {
        return inscriptionDao.archiverToutesInscriptions();
    }
    
    public void archiverInscriptionsPassees() {
        inscriptionDao.archiverInscriptionsPassees();
    }
}