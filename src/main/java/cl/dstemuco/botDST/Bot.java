package cl.dstemuco.botDST;

import cl.dstemuco.botDST.events.Join;
import cl.dstemuco.botDST.registration.RegistrationListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.security.auth.login.LoginException;
import java.nio.file.Paths;

public class Bot {
    public static JDA bot;

    public static void main(String[] args) {

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(Paths.get("token.yml")).build();
        CommentedConfigurationNode node;
        try {
            node = loader.load().node("token");
        } catch (ConfigurateException e) {
            e.printStackTrace();
            return;
        }

        JDABuilder builder;
        try {
            builder = JDABuilder.createDefault(node.get(String.class));
        } catch (SerializationException e) {
            e.printStackTrace();
            return;
        }
        builder.enableIntents(GatewayIntent.DIRECT_MESSAGES);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("Estudiando..."));

        try {
            bot = builder.build().awaitReady();
            System.out.println("Bot iniciado correctamente.");

            if (args.length > 0) {
                if (args[0].equals("shut")) {

                    bot.shutdown();
                    System.out.println("Bot apagado correctamente.");
                    return;

                }
            }

            bot.addEventListener(
                    new RegistrationListener(bot),
                    new Join(bot)
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
