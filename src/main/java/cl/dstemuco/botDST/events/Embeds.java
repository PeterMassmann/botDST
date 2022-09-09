package cl.dstemuco.botDST.events;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class Embeds extends ListenerAdapter {

    private final JDA bot;

    public Embeds(JDA bot) {
        this.bot = bot;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getId().equals("972598095152037958")) {
            if (!event.getMember().getId().equals("464966939039760395")) {
                return;
            }
            if (event.getMessage().getContentDisplay().startsWith("embed")) {

                String embedID = event.getMessage().getContentDisplay().replace("embed ", "");

                EmbedBuilder builder = new EmbedBuilder();

                builder.setColor(new Color(47, 49, 54));

                TextChannel channel = bot.getTextChannelById("950779654879465472");

                switch (embedID) {
                    case "reglasImage":
                        builder.setImage("https://media.discordapp.net/attachments/950779427007131740/1017881450776907796/reglas_banner.png");
                        break;
                    case "reglas1":
                        builder.setTitle("|1| Reglamento Escolar:");
                        builder.setDescription("Todos los [reglamentos del Colegio Alemán de Temuco](https://dstemuco.cl/informacion-escolar/reglamentos/) aplican directamente aquí. Todas las acciones realizadas aquí podrán ser sancionadas en el Colegio sin excepción.");
                        break;
                    case "reglas2":
                        builder.setTitle("|2| Reglamento IB:");
                        builder.setDescription("Se deben seguir todas las normas establecidas por el Bachillerato Internacional respecto a la conducta de los candidatos. Por ejemplo, se prohibe conversar sobre los exámenes hasta 24 horas luego de haberlo rendido.");
                        break;
                    case "reglas3":
                        builder.setTitle("|3| Contenido NSFW:");
                        builder.setDescription("Está terminantemente prohibido enviar contenido no apropiado (NSFW) a este servidor. Esto aplica tanto a imágenes, vídeos, *text-art*, transmisiones, enlaces, mensajes y cualquier otro medio.");
                        break;
                    case "reglas4":
                        builder.setTitle("|4| Causar daño a otros:");
                        builder.setDescription("Se prohibe el acto de causar daño a otra persona de la comunidad. Esto engloba el daño físico, psicológico, social, digital (EJ: Enlaces maliciosos), etc.");
                        break;
                    case "reglas5":
                        builder.setTitle("|5| Toxicidad:");
                        builder.setDescription("Actos de toxicidad como enviar repetitivamente mensajes, interrumpir conversaciones, o cualquier acto que impida la normal convivencia intencionadamente será sancionado.");
                        break;
                    case "reglas6":
                        builder.setTitle("|6| Mal uso del servidor:");
                        builder.setDescription("Este servidor de Discord fue creado con la intención de ofrecer a la comunidad un espacio para relacionarse incluso fuera del Colegio. Utilizar esta plataforma como un método para copiar, atacar a otras personas o cualquier otro acto fuera del objetivo del servidor podrá ser sancionado.");
                        break;
                    case "reglas7":
                        builder.setTitle("|7| Sanciones:");
                        builder.setDescription("Cada mensaje enviado aquí quedará registrado incluso si este es eliminado. Todas las acciones tomadas acá podrán ser sancionadas por las autoridades correspondientes del Colegio.");
                        break;
                }

                channel.sendMessageEmbeds(builder.build()).queue();
                event.getMessage().delete().queue();

            }
        }
    }

}
