package lk.GamerShop.mail;

import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lk.GamerShop.Utils.Env;

public class ForgotPasswordMail extends Mailable {

    private final String to;
    private final String verificationCode;

    public ForgotPasswordMail(String to, String verificationCode) {

        this.to = to;
        this.verificationCode = verificationCode;
    }

    @Override
    public void build(Message message) throws MessagingException {

        message.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(to)
        );

        message.setSubject(
                "Reset Your Password - " + Env.get("app.name")
        );

        String appURL = Env.get("app.url");



        HtmlTextEmail htmlTextEmail = getEmailTemplateBuilder()

                .header()

                .logo("lk/GamerShop/mail/logo.png").logoHeight(40).and()

                .text("PASSWORD RESET REQUEST")
                .h1()
                .center()
                .and()

                .text("We received a request to reset your password.")
                .center()
                .and()

                .text("Use the verification code below to continue.")
                .center()
                .and()

                .text("Verification Code: " + verificationCode)
                .h2()
                .center()
                .and()



                .copyright(Env.get("app.name"))
                .url(appURL)
                .suffix(". All Rights Reserved")
                .and()

                .build();

        message.setContent(htmlTextEmail.getHtml(), "text/html; charset=utf-8");
    }
}