package com.pm.server.manager;

import com.pm.server.registry.PacdotRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PacdotManagerImpl implements PacdotManager {

    private PacdotRegistry pacdotRegistry;

    @Autowired
    public PacdotManagerImpl(PacdotRegistry pacdotRegistry) {
        this.pacdotRegistry = pacdotRegistry;
    }

    public void resetPacdots() {
        pacdotRegistry.resetPacdots();
    }

}
