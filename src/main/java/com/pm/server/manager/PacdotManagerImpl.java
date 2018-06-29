package com.pm.server.manager;

import com.pm.server.datatype.Pacdot;
import com.pm.server.registry.PacdotRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacdotManagerImpl implements PacdotManager {

    private PacdotRegistry pacdotRegistry;

    @Autowired
    public PacdotManagerImpl(PacdotRegistry pacdotRegistry) {
        this.pacdotRegistry = pacdotRegistry;
    }

    @Override
    public void resetPacdots() {
        pacdotRegistry.resetPacdots();
    }

    @Override
    public List<Pacdot> getInformationOfAllPacdots() {
        return pacdotRegistry.getInformationOfAllPacdots();
    }

}
