package com.TempoLux.domain.constans;

/**
 * Constantes globales del dominio TempoLux (nombres de beans, claims JWT, roles).
 */
public final class ApplicationConstants {

    private ApplicationConstants() {
    }

    // WebClient de Groq
    public static final String GROQ_WEB_CLIENT_BEAN = "groqWebClient";

    // Seguridad / JWT
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String CLAIM_ROLE = "role";
    public static final String ROLE_CLIENT = "ROLE_CLIENT";

    // Identidad de los socios
    public static final String MEMBER_ID_PREFIX = "TL-";
    public static final String MEMBER_ID_SUFFIX = "-CH";
    public static final String DEFAULT_TIER = "Coleccionista VIP";
    public static final String DEFAULT_PREFERRED_COLLECTION = "Edición Alta Relojería";

    // Chat con LuxBot
    public static final int CHAT_MAX_TOOL_ITERATIONS = 4;
    public static final int CHAT_MAX_HISTORY_MESSAGES = 20;
    public static final int CHAT_MAX_TOKENS = 1024;
    /* Temperatura baja: el asistente debe ceñirse a los datos del sistema, no improvisar. */
    public static final double CHAT_TEMPERATURE = 0.2;
}
