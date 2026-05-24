package controller;

import dao.StatDao;
import model.StatMembre;

import java.util.List;

public class StatController {

    private StatDao dao =
            new StatDao();

    public List<StatMembre>
    getStats(
            String debut,
            String fin
    ) {

        return dao
                .getMembresActifs(
                        debut,
                        fin
                );

    }

}