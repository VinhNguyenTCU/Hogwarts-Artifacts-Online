package edu.tcu.cs.hogwartsartifactsonline.wizard;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.system.StatusCode;
import edu.tcu.cs.hogwartsartifactsonline.wizard.dto.WizardDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.mockito.BDDMockito.given;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest
public class WizardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    WizardService wizardService;

    @Autowired
    ObjectMapper objectMapper;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() throws Exception {
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

        this.wizards = new ArrayList<>();
        Wizard wizard1 = new Wizard();
        Wizard wizard2 = new Wizard();
        Wizard wizard3 = new Wizard();

        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);
        wizard1.addArtifact(artifact3);

        wizard2.setId(2);
        wizard2.setName("Harry Potter");
        wizard2.addArtifact(artifact2);
        wizard2.addArtifact(artifact4);

        wizard3.setId(3);
        wizard3.setName("Neville Longbottom");
        wizard3.addArtifact(artifact5);

        this.wizards.add(wizard1);
        this.wizards.add(wizard2);
        this.wizards.add(wizard3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAllWizardsSuccess() throws Exception {
        // Given
        given(this.wizardService.findAll()).willReturn(this.wizards);

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Wizards Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.wizards.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].name").value("Albus Dumbledore"))
                .andExpect(jsonPath("$.data[1].id").value(2))
                .andExpect(jsonPath("$.data[1].name").value("Harry Potter"))
                .andExpect(jsonPath("$.data[2].id").value(3))
                .andExpect(jsonPath("$.data[2].name").value("Neville Longbottom"));
    }

    @Test
    void testFindWizardByIdSuccess() throws Exception {
        // Given
        given(this.wizardService.findById(1)).willReturn(this.wizards.get(0));

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find Wizard By Id Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore"));
    }

    @Test
    void testFindWizardByIdNotFound() throws Exception {
        // Given
        given(this.wizardService.findById(5)).willThrow(new WizardNotFoundException(5));

        // When and Then
        this.mockMvc.perform(get("/api/v1/wizards/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find a wizard with Id: 5"))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void testAddWizardSuccess() throws Exception {

        WizardDto wizardDto = new WizardDto(null, "Hermione Granger", 0);

        String json = this.objectMapper.writeValueAsString(wizardDto);
        // Given
        Wizard savedWizard = new Wizard();
        savedWizard.setId(4);
        savedWizard.setName("Hermione Granger");

        given(this.wizardService.save(Mockito.any(Wizard.class))).willReturn(savedWizard);

        // When and Then
        this.mockMvc.perform(post("/api/v1/wizards").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Wizard Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.name").value("Hermione Granger"));
    }

    @Test
    void testUpdateWizardSuccess() throws Exception {
        WizardDto wizardDto = new WizardDto(null, "Albus Dumbledore - UPDATED", 0);

        String json = this.objectMapper.writeValueAsString(wizardDto);

        Wizard updatedWizard = new Wizard();
        updatedWizard.setId(1);
        updatedWizard.setName("Albus Dumbledore - UPDATED");

        // Given
        given(this.wizardService.update(eq(1), Mockito.any(Wizard.class))).willReturn(updatedWizard);

        // When and Then
        this.mockMvc.perform(put("/api/v1/wizards/1").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Wizard Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("Albus Dumbledore - UPDATED"));
    }
}
