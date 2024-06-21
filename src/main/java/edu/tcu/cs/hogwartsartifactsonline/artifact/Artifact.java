package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Setter
@Getter
public class Artifact implements Serializable {

    @Id
    private String id;

    private String name;

    private String description;

    private String imageUrl;

    @ManyToOne
    private Wizard owner;

    public Artifact() {
    }
}
