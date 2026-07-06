package lk.GamerShop.mail;

import io.rocketbase.mail.model.HtmlTextEmail;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import lk.GamerShop.Utils.Env;

public class VerificationMail extends Mailable {
    private final String to;
    private final String verificationCode;

    public VerificationMail(String to, String verificationCode) {
        this.to = to;
        this.verificationCode = verificationCode;
    }

    @Override
    public void build(Message message) throws MessagingException {
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("Email Verification Code - " + Env.get("app.name"));


        String appURL = Env.get("app.url");

        HtmlTextEmail htmlTextEmail = getEmailTemplateBuilder()
                .header()
                .logo("lk/GamerShop/mail/logo.png").logoHeight(40).and()
                .text("WELCOME " + to).h1().center().and()
                .text("Thanks for register in our website").center().and()
                .text("Use the verification code below to continue.").center().and()
                .text("Your Verification Code: " + verificationCode).center().and()
                .copyright(Env.get("app.name")).url(appURL).suffix(". All Rights Reserved").and()
                .build();


        message.setContent(htmlTextEmail.getHtml(), "text/html; charset=utf-8");
    }
}
