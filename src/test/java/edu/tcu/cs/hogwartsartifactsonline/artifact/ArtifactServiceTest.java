package edu.tcu.cs.hogwartsartifactsonline.artifact;

import edu.tcu.cs.hogwartsartifactsonline.wizard.Wizard;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtifactServiceTest {

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    ArtifactService artifactService;

    List<Artifact> artifacts;

    @BeforeEach
    void setUp() {
        this.artifacts = new ArrayList<>();
        Artifact artifact1 = new Artifact();
        artifact1.setId("1250808601744904191");
        artifact1.setName("Deluminator");
        artifact1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absord (as well as return) the light from any light source to provide cover to the user.");
        artifact1.setImageUrl("ImageUrl");
        this.artifacts.add(artifact1);

        Artifact artifact2 = new Artifact();
        artifact2.setId("1250808601744904192");
        artifact2.setName("Invisibility Cloak");
        artifact2.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact2.setImageUrl("ImageUrl");
        this.artifacts.add(artifact2);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository.
        /*
        * "id": "1250808601744904192",
        * "name": "Invisibility Cloak",
        * "description": "An invisibility cloak is used to make the wearer invisible.",
        * "imageUrl": "ImageUrl",
        * */

        Artifact artifact = new Artifact();
        artifact.setId("1250808601744904192");
        artifact.setName("Invisibility Cloak");
        artifact.setDescription("An invisibility cloak is used to make the wearer invisible.");
        artifact.setImageUrl("ImageUrl");

        Wizard wizard = new Wizard();
        wizard.setId(2);
        wizard.setName("Harry Potter");

        artifact.setOwner(wizard);

        given(artifactRepository.findById("1250808601744904192")).willReturn(Optional.of(artifact)); // Defines the behavior of the mock object.

        // When. Act on the target behavior. When steps should cover the method to be tested.
        Artifact returnedArtifact = artifactService.findById("1250808601744904192");

        // Then. Assert expected outcomes.
        assertThat(returnedArtifact.getId()).isEqualTo(artifact.getId());
        assertThat(returnedArtifact.getName()).isEqualTo(artifact.getName());
        assertThat(returnedArtifact.getDescription()).isEqualTo(artifact.getDescription());
        assertThat(returnedArtifact.getImageUrl()).isEqualTo(artifact.getImageUrl());

        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindByIdNotFound(){
        // Given
        given(artifactRepository.findById(Mockito.any(String.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Artifact returnedArtifact = artifactService.findById("1250808601744904192");
        });

        // Then
        assertThat(thrown).
                isInstanceOf(ArtifactNotFoundException.class).
                hasMessage("Could not find artifact with Id: 1250808601744904192");

        verify(artifactRepository, times(1)).findById("1250808601744904192");
    }

    @Test
    void testFindAllSuccess() {
        // Given
        given(artifactRepository.findAll()).willReturn(this.artifacts);

        // When
        List<Artifact> actualArtifacts = artifactService.findAll();

        // Then
        assertThat(actualArtifacts.size()).isEqualTo(this.artifacts.size());
        verify(artifactRepository, times(1)).findAll();
    }

}