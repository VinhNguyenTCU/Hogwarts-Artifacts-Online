package edu.tcu.cs.hogwartsartifactsonline.system;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.user.UserEntity;
import edu.tcu.cs.hogwartsartifactsonline.user.UserRepository;
import edu.tcu.cs.hogwartsartifactsonline.user.UserService;
import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import edu.tcu.cs.hogwartsartifactsonline.wizard.WizardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DBDataInitializer implements CommandLineRunner {

    private final ArtifactRepository artifactRepository;

    private final WizardRepository wizardRepository;

    private final UserService userService;

    public DBDataInitializer(ArtifactRepository artifactRepository, WizardRepository wizardRepository, UserService userService) {
        this.artifactRepository = artifactRepository;
        this.wizardRepository = wizardRepository;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        Artifact artifact1 = new Artifact();
        artifact1.setId("1250808601744904191");
        artifact1.setName("Deluminator");
        artifact1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absord (as well as return) the light from any light source to provide cover to the user.");
        artifact1.setImageUrl("ImageUrl");

        Artifact artifact2 = new Artifact();
        artifact2.setId("1250808601744904192");
        artifact2.setName("Invisibility Cloak");
        artifact2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact2.setImageUrl("ImageUrl");

        Artifact artifact3 = new Artifact();
        artifact3.setId("1250808601744904193");
        artifact3.setName("Elder Wand");
        artifact3.setDescription("The Elder Wand, known throughout history as the Deathstick or the Wand of Destiny, is an extremely powerful wand made of elder wood with a core of Thestral tail hair.");
        artifact3.setImageUrl("ImageUrl");

        Artifact artifact4 = new Artifact();
        artifact4.setId("1250808601744904194");
        artifact4.setName("The Marauder's Map");
        artifact4.setDescription("A magical map of Hogwarts created by Remus Lupin, Peter Pettigrew, Sirius Black, and James Potter while they were students at Hogwarts.");
        artifact4.setImageUrl("ImageUrl");

        Artifact artifact5 = new Artifact();
        artifact5.setId("1250808601744904195");
        artifact5.setName("The Sword Of Gryffindor");
        artifact5.setDescription("A goblin-made sword adorned with large rubies on the pommel. It was once owned by Godric Gryffindor, one of the medieval founders of Hogwarts.");
        artifact5.setImageUrl("ImageUrl");

        Artifact artifact6 = new Artifact();
        artifact6.setId("1250808601744904196");
        artifact6.setName("Resurrection Stone");
        artifact6.setDescription("The Resurrection Stone allows the holder to bring back deceased loved ones, in a semi-physical form, and communicate with them.");
        artifact6.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);
        wizard1.addArtifact(artifact3);

        Wizard wizard2 = new Wizard();
        wizard2.setId(2);
        wizard2.setName("Harry Potter");
        wizard2.addArtifact(artifact2);
        wizard2.addArtifact(artifact4);

        Wizard wizard3 = new Wizard();
        wizard3.setId(3);
        wizard3.setName("Neville Longbottom");
        wizard3.addArtifact(artifact5);

        wizardRepository.save(wizard1);
        wizardRepository.save(wizard2);
        wizardRepository.save(wizard3);
        artifactRepository.save(artifact6);

        // Create some users
        UserEntity user1 = new UserEntity();
        user1.setId(1);
        user1.setUsername("john");
        user1.setPassword("123456");
        user1.setEnabled(true);
        user1.setRoles("admin user");

        UserEntity user2 = new UserEntity();
        user2.setId(2);
        user2.setUsername("eric");
        user2.setPassword("654321");
        user2.setEnabled(true);
        user2.setRoles("user");

        UserEntity user3 = new UserEntity();
        user3.setId(3);
        user3.setUsername("tom");
        user3.setPassword("qwerty");
        user3.setEnabled(false);
        user3.setRoles("user");

        this.userService.save(user1);
        this.userService.save(user2);
        this.userService.save(user3);
    }
}
