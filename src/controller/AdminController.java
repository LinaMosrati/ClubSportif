package controller;

import dao.DashboardDao;

public class AdminController {

    private DashboardDao dashboardDao = new DashboardDao();

    public int getNombreMembres() {
        return dashboardDao.getNombreMembres();
    }

    public int getNombreActivites() {
        return dashboardDao.getNombreActivites();
    }

    
    public java.util.List<model.Activite> getDashboardActivites() {
        return dashboardDao.getDashboardActivites();
    }
    
    public int getTauxRemplissage() {
        return dashboardDao.getTauxRemplissage();
    }
}