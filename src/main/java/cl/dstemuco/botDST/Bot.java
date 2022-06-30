package cl.dstemuco.botDST;

import cl.dstemuco.botDST.registration.RegistrationListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;

public class Bot {
    public static JDA bot;

    public static void main(String[] args) {

        JDABuilder builder = JDABuilder.createDefault("OTcxOTA2MTAxMzkyMDcyNzU1.GjbwNv.S3ORiCoUg99KOlxhDpWeDCGiHhY2UQ6cfAhhrc");
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGES);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Estudiando..."));

        try {
            bot = builder.build().awaitReady();
            System.out.println("Bot iniciado correctamente.");

            bot.addEventListener(
                    new RegistrationListener(bot)
            );
            /*

            Guild guild = bot.getGuildById(948879102289539113L);
            guild.upsertCommand("codigo", "Ingresa el código enviado a tu correo para obtener acceso al resto del servidor.").addOption(OptionType.STRING, "código", "El código obtenido en el correo.", true).queue();
            guild.upsertCommand("verificar", "Verifica tu cuenta para obtener acceso al resto del servidor.").addOption(OptionType.STRING, "correo", "Tu correo institucional (ejemplo@alumnosdstemuco.cl o ejemplo@dstemuco.cl).", true).queue();

             */
        } catch (InterruptedException | LoginException var3) {
            System.out.println("Ha ocurrido un error iniciando el bot.");
        }

    }
}
