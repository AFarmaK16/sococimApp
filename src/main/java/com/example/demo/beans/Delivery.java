package com.example.demo.beans;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
@Entity
public class Delivery  implements Serializable {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer delivery_ID;
    private String delivery_address;
//    private String delivery_destination;

    @OneToOne( fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Destination destination;
    private String delivery_comment;
    private boolean isRendu;
    private boolean isDecharged;

//TODO ajouter un field pour le commercial saisisse les infos de livraison if rendu checked

}
