package cl.dstemuco.botDST.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Join extends ListenerAdapter {

    private final Role unverified;

    public Join(@NotNull JDA bot) {

        this.unverified = bot.getRoleById(950774251881902200L);

    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        event.getGuild().addRoleToMember(event.getUser(), unverified).queue();

        // TODO SEND WELCOME MESSAGE

        event.getUser().openPrivateChannel().queue(
                channel -> channel.sendMessage("**¡Bienvenid@ al servidor de Discord del Colegio Alemán de Temuco!**\n\nPara obtener acceso a los canales, usa `/verificar <correo institucional>` en <#948885571282030592>. Luego introduce el código que se te enviará a tu correo en el comando `/codigo <código>.`").queue()
        );

    }

}
