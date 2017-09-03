package com.pm.server.manager;

import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminManager {

    private PacdotRegistry pacdotRegistry;

    @Autowired
    public AdminManager(PacdotRegistry pacdotRegistry) {
        this.pacdotRegistry = pacdotRegistry;
    }

    public void resetPacdots() {
        List<Pacdot> pacdotList = pacdotRegistry.getAllPacdots();
        for(Pacdot pacdot : pacdotList) {
            pacdot.setUneaten();
        }
    }

}
