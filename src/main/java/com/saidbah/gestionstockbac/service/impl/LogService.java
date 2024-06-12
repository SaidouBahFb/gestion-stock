package com.saidbah.gestionstockbac.service.impl;

import com.saidbah.gestionstockbac.utils.Helpers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LogService {
    private final Logger logger = LoggerFactory.getLogger("inventoryManagement");

    public void log(Helpers.LogLevel level, String path, String message) {
        String s = path + " log - " + message;
        switch (level) {
            case INFO -> logger.info(s);
            case WARN -> logger.warn(s);
            case ERROR -> logger.error(s);
        }
    }
}
