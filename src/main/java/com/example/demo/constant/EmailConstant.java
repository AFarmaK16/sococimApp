package com.example.demo.constant;

public class EmailConstant {
    public static final String MAIL_PROTOCOL = "smtps";
    public static final String USERNAME = "socindustries12@gmail.com";
    public static final String PASSWORD = " ";
    public static final String FROM_EMAIL = "socindustries12@gmail.com";
//    public static final String CC_EMAIL = "socpass12";
    public static final String ACCOUNT_LOCKED_EMAIL_SUBJECT = "Compte Bloqué";
//    public static final String EMAIL_SUBJECT = "Votre compte a été bloque, veuillez contacter notre commercial au 77...";
    public static final String GMAIL_SMTP_SERVER = "smtp.gmail.com";
    public static final String SMTP_HOST = "mail.smtp.host";
    public static final String SMTP_AUTH = "mail.smtp.auth";
    public static final String SMTP_PORT = "mail.smtp.port";
    public static final int DEFAULT_PORT = 587;
    public static final String SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";
    public static final String SMTP_STARTTLS_REQUIRED = "mail.smtp.starttls.required";
    public static final String ACCOUNT_LOCKED_EMAIL= "\n" +
            "Nous regrettons de vous informer que votre compte a été bloqué en raison de nombreuses tentatives infructueuses. Votre sécurité est notre priorité, et cette mesure a été prise pour protéger votre compte.\n" +
            "\n" +
                "Pour résoudre ce problème et réactiver votre compte, veuillez contacter notre service commercial au numéro suivant : 338398860/61. Nos représentants se feront un plaisir de vous assister  dans le processus de déblocage de votre compte.\n" +
            "\n" +
            "Nous nous excusons pour les désagréments que cela a pu causer et vous remercions de votre compréhension.\n" +
            "\n" +
            "Cordialement,\n" +
            "L'équipe du support client\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n";
    public static final String PENDING_ORDER_EMAIL_SUBJECT="Commande enregistrée";
    public static final String PENDING_ORDER_EMAIL="Votre commande a bien été prise en compte,et est en attente de traitement";

    public static final String PENDING_FOR_DELIVERY_ORDER_EMAIL_SUBJECT="Commande enregistrée";
    public static final String DELIVERY_ORDER_EMAIL_SUBJECT="Livraison Commande";
    public static final String NEW_ACCOUNT_EMAIL_SUBJECT="Compte crée avec succés";

    public static final String PENDING_FOR_DELIVERY_ORDER_EMAIL="Votre commande a bien été validée.\n" +
            "Notre service commercial se chargera de vous contacter pour la livraisonet est en attente de traitement";


}
