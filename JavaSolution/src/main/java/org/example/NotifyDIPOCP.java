package org.example;

class SmtpMailer {
    void send(String templ, String to, String body) {
        System.out.println("[SMTP] template=" + templ + " to=" + to + " body=" + body);
    }
}

class TwilioClient {
    void sendOTP(String phone, String code) {
        System.out.println("[Twilio] OTP " + code + " -> " + phone);
    }
}

class User {
    String email;
    String phone;

    User(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}

class SignUpService {
    SmtpMailer mailer;
    TwilioClient sms;

    SignUpService(SmtpMailer smtpMailer, TwilioClient twilioClient) {
        this.mailer = smtpMailer;
        this.sms = twilioClient;
    }

    boolean signUp(User u) {
        if (u.email == null || u.email.isEmpty()) return false;
        // pretend DB save here…

        mailer.send("welcome", u.email, "Welcome!");

        sms.sendOTP(u.phone, "123456");
        return true;
    }
}

public class NotifyDIPOCP {
    public static void main(String[] args) {
        SmtpMailer mailer = new SmtpMailer();  // hard-coded
        TwilioClient sms = new TwilioClient(); // hard-coded
        SignUpService svc = new SignUpService(mailer, sms);
        svc.signUp(new User("user@example.com", "+15550001111"));
    }
}
