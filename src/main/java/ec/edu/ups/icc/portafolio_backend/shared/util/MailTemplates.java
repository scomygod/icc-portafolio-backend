package ec.edu.ups.icc.portafolio_backend.shared.util;

import ec.edu.ups.icc.portafolio_backend.programmer.entity.Advisory;

public final class MailTemplates {
    private MailTemplates() {}

    public static String advisoryCreated(Advisory advisory) {
        return "Tu asesoría fue registrada para: " + advisory.getScheduledAt();
    }

    public static String advisoryUpdated(Advisory advisory) {
        return "Tu asesoría fue " + advisory.getStatus().name() + " para: " + advisory.getScheduledAt();
    }
}