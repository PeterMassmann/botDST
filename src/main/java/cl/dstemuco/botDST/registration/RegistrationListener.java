package cl.dstemuco.botDST.registration;

import cl.dstemuco.botDST.CodeGenerator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.awt.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RegistrationListener extends ListenerAdapter {


    private final Map<String, String> correos = new HashMap<>();
    private final Map<String, String> usuarios = new HashMap<>();

    private final Role unverified;
    private final Role verified;
    private final Role profesor;
    private final Role alumno;
    private final Role cuarto;
    private final Role tercero;
    private final Role segundo;
    private final Role primero;
    private final Role octavo;
    private final Role septimo;
    private final Role sexto;
    private final Role quinto;

    private final CommentedConfigurationNode root;

    public RegistrationListener(@NotNull JDA bot) {
        unverified = bot.getRoleById(950774251881902200L);
        verified = bot.getRoleById(950774355581865985L);
        profesor = bot.getRoleById(949194667524763699L);
        alumno = bot.getRoleById(950789102725365780L);
        cuarto = bot.getRoleById(949195576229113878L);
        tercero = bot.getRoleById(949195984150351872L);
        segundo = bot.getRoleById(949196493565337671L);
        primero = bot.getRoleById(949196610758393856L);
        octavo = bot.getRoleById(949196808528232488L);
        septimo = bot.getRoleById(949196899347484733L);
        sexto = bot.getRoleById(949197023821836348L);
        quinto = bot.getRoleById(949195739869896715L);
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder().path(Paths.get("data/nombresPorCorreo.yml")).build();
        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getTextChannel().getId().equals("948885571282030592") && !event.getMessage().getAuthor().getId().equals("971906101392072755") && !event.getMessage().getAuthor().getId().equals("714847710745722915")) {
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        if (event.getName().equals("verificar")) {
            String correo = event.getOption("correo").getAsString().toLowerCase();

            if (correo.matches("([a-zñ]{1,100}\\.[a-zñ]{1,100}@alumnosdstemuco\\.cl)|([a-zñ]{1,100}@dstemuco\\.cl)")) {

                if (correos.containsValue(correo)) {
                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setTitle("Enviaste un código de verificación a este correo hace menos de 10 minutos.")
                                    .build()
                    ).queue(
                            msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                    );
                    return;
                }

                if (usuarios.containsValue(event.getUser().getId())) {
                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setTitle("Enviaste un código de verificación hace menos de 10 minutos con esta misma cuenta.")
                                    .build()
                    ).queue(
                            msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                    );
                    return;
                }

                String from = "bot.dstemuco@gmail.com";

                Properties properties = System.getProperties();

                properties.put("mail.smtp.host", "smtp.gmail.com");
                properties.put("mail.smtp.port", "587");
                properties.put("mail.smtp.auth", "true");
                properties.put("mail.smtp.starttls.enable", "true");
                properties.put("mail.smtp.starttls.required", "true");
                properties.put("mail.smtp.ssl.protocols", "TLSv1.2");

                Session session = Session.getInstance(properties, new Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, "vlvhkcqolmmpoghu");
                    }
                });

                session.setDebug(true);

                try {

                    MimeMessage mail = new MimeMessage(session);

                    mail.setFrom(new InternetAddress(from));

                    mail.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));

                    mail.setSubject("Código de verificación de Discord.");

                    String code = CodeGenerator.generateCode(8, correos.keySet());

                    // Tú código de verificación de Discord es ABCDEFGH.
                    //
                    // Usa el comando **/verificar** en Discord para registrar tu cuenta y acceder al resto del servidor.
                    //
                    // El código expira en 10 minutos.
                    //
                    // Este mensaje fue enviado por Pizzax#9454.

                    mail.setContent("Tu código de verificación en Discord es <b>" + code + "</b>.<br><br>Usa el comando <b>/codigo</b> en Discord para registrar tu cuenta y acceder al resto del servidor.<br><br><i>El código expira en 10 minutos.</i><br><br>Este mensaje fue enviado por " + event.getUser().getName() + "#" + event.getUser().getDiscriminator() + ".", "text/html");

                    Transport.send(mail);

                    correos.put(code, correo);
                    usuarios.put(code, event.getUser().getId());

                    new Timer().schedule(
                            new TimerTask() {
                                @Override
                                public void run() {
                                    correos.remove(code);
                                    usuarios.remove(code);
                                }
                            },
                            600000
                    );

                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.GREEN)
                                    .setTitle("Te hemos enviado un correo con tu código de verificación.")
                                    .build()
                    ).queue(
                            msg -> msg.deleteOriginal().queueAfter(2, TimeUnit.MINUTES)
                    );

                } catch (MessagingException exception) {
                    exception.printStackTrace();
                }

            } else {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("Introduce un correo institucional válido.")
                                .build()
                ).queue(
                        msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                );
            }
        } else if (event.getName().equals("codigo")) {
            String code = event.getOption("código").getAsString();

            if (code.matches("[a-z]{8}")) {
                if (correos.containsKey(code)) {
                    if (usuarios.get(code).equals(event.getUser().getId())) {
                        String correo = correos.get(code);
                        Member member = event.getMember();
                        Guild guild = event.getGuild();

                        if (member == null || guild == null) {
                            return;
                        }

                        // NICKNAME
                        if (correo.contains("alumnosdstemuco.cl")) {

                            String nameToSearch = correo.split("@")[0].replace(".", "_");

                            Map<String, Map<String, String>> map = getMapFromNode(root);

                            boolean found = false;

                            for (Map.Entry<String, Map<String, String>> curso : map.entrySet()) {


                                Map<String, String> correos = curso.getValue();

                                if (correos.containsKey(nameToSearch)) {

                                    found = true;

                                    String nombre = correos.get(nameToSearch);

                                    guild.modifyNickname(member, nombre).queue();

                                    guild.removeRoleFromMember(member, unverified).queue();
                                    guild.addRoleToMember(member, verified).queue();
                                    guild.addRoleToMember(member, alumno).queue();

                                    guild.addRoleToMember(member, numberToRole(Integer.parseInt(curso.getKey()))).queue();

                                    this.correos.remove(code);
                                    usuarios.remove(code);

                                }

                            }

                            if (!found) {
                                event.replyEmbeds(
                                        new EmbedBuilder()
                                                .setColor(Color.RED)
                                                .setTitle("No se ha encontrado tu correo en la lista de nombres.")
                                                .build()
                                ).queue(
                                        msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                                );
                                correos.remove(code);
                                usuarios.remove(code);
                                return;
                            }


                        } else {

                            String name = correo.split("@")[0];

                            String inicial = name.substring(0,1);

                            String apellido = name.replaceFirst(inicial, "");

                            guild.modifyNickname(member, inicial.toUpperCase() + ". " + apellido.substring(0,1).toUpperCase() + apellido.substring(1)).queue();

                            guild.removeRoleFromMember(member, unverified).queue();
                            guild.addRoleToMember(member, verified).queue();
                            guild.addRoleToMember(member, profesor).queue();

                            correos.remove(code);
                            usuarios.remove(code);
                        }

                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setTitle("Cuenta verificada con éxito.")
                                        .setColor(Color.GREEN)
                                        .build()
                        ).queue(
                                msg -> msg.deleteOriginal().queueAfter(2, TimeUnit.MINUTES)
                        );

                    } else {
                        event.replyEmbeds(
                                new EmbedBuilder()
                                        .setColor(Color.RED)
                                        .setTitle("El código no fue enviado por esta cuenta.")
                                        .build()
                        ).queue(
                                msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                        );
                    }
                } else {
                    event.replyEmbeds(
                            new EmbedBuilder()
                                    .setColor(Color.RED)
                                    .setTitle("El código introducido no existe.")
                                    .build()
                    ).queue(
                            msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                    );
                }
            } else {
                event.replyEmbeds(
                        new EmbedBuilder()
                                .setColor(Color.RED)
                                .setTitle("El código introducido en inválido.")
                                .build()
                ).queue(
                        msg -> msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS)
                );

            }


        }
    }

    private Role numberToRole(int number) {

        switch (number) {
            case 5:
                return quinto;
            case 6:
                return sexto;
            case 7:
                return septimo;
            case 8:
                return octavo;
            case 9:
                return primero;
            case 10:
                return segundo;
            case 11:
                return tercero;
            default:
                return cuarto;
        }

    }

    private @NotNull Map<String, Map<String, String>> getMapFromNode(@NotNull CommentedConfigurationNode node) {

        Map<String, Map<String, String>> result = new HashMap<>();

        for (Map.Entry<Object, CommentedConfigurationNode> first : node.childrenMap().entrySet()) {

            Map<String, String> value = new HashMap<>();

            for (Map.Entry<Object, CommentedConfigurationNode> second : first.getValue().childrenMap().entrySet()) {

                value.put((String) second.getKey(), second.getValue().getString());

            }

            result.put((String) first.getKey(), value);

        }

        return result;

    }

}
