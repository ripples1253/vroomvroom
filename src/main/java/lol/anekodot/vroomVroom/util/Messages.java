package lol.anekodot.vroomVroom.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

public enum Messages {
    SS_INFO("<gradient:{msg_info}><b>ShulkerStore ></b> %s</gradient>"),
    SS_SUCCESS("<gradient:{msg_success}><b>ShulkerStore ></b> %s</gradient>"),
    SS_WARNING("<gradient:{msg_error}><b>ShulkerStore ></b> %s</gradient>"),
    SS_ERROR("<gradient:{msg_warn}><b>ShulkerStore ></b> %s</gradient>"),
    DEBUG_LOG("<gradient:{msg_debug}><b>DBG ></b> %s</gradient>"),
    DEBUG_TOGGLE("<gradient:{msg_debug}}><b>DBG ></b> Toggled debugging messages.</gradient>"),
    NO_HTTPS("<gradient:{msg_error}><b>ShulkerStore ></b> Your URI isn't using https. Please use the -f flag to continue.</gradient>");

    private final String message;

    Messages(String message) {
        this.message = message;
    }

    public Component get(Object... args) {
        return MiniMessage
                .miniMessage()
                .deserialize(format(String.format(message, args)));
    }

    private String format(String input) {
        return input
                .replace("{msg_success}", "#98F59A:#29997F")
                .replace("{msg_info}", "#00DBDE:#94F7B2")
                .replace("{msg_warn}", "#e1ad01:#e1ad01")
                .replace("{msg_error}", "#EE5A24:#EA2027")
                .replace("{msg_debug}", "#fa675c:#d63227");
    }
}
