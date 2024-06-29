package edu.tcu.cs.hogwartsartifactsonline.wizard;

import edu.tcu.cs.hogwartsartifactsonline.artifact.Artifact;
import edu.tcu.cs.hogwartsartifactsonline.artifact.ArtifactRepository;
import edu.tcu.cs.hogwartsartifactsonline.system.exception.ObjectNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

    @Mock
    ArtifactRepository artifactRepository;

    @InjectMocks
    WizardService wizardService;

    List<Wizard> wizards;

    @BeforeEach
    void setUp() {
        this.wizards = new ArrayList<>();
        Wizard wizard1 = new Wizard();
        Wizard wizard2 = new Wizard();
        Wizard wizard3 = new Wizard();

        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");

        wizard2.setId(2);
        wizard2.setName("Harry Potter");

        wizard3.setId(3);
        wizard3.setName("Neville Longbottom");

        this.wizards.add(wizard1);
        this.wizards.add(wizard2);
        this.wizards.add(wizard3);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    void testFindAllSuccess(){
        // Given. Arrange inputs and targets. Define the behavior of Mock object artifactRepository.
        given(this.wizardRepository.findAll()).willReturn(this.wizards);

        // When. Act on the target behaviour. Act steps should cover the method to be tested.
        List<Wizard> actualWizards = this.wizardService.findAll();

        // Then. Assert expected outcomes
        assertThat(actualWizards.size()).isEqualTo(this.wizards.size());

        // Verify wizardRepository.findAll() is called exactly once.
        verify(this.wizardRepository, times(1)).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Wizard wizard = new Wizard();
        wizard.setId(1);
        wizard.setName("Albus Dumbledore");

        // Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard));

        // When
        Wizard returnedWizard = this.wizardService.findById(1);

        // Then
        assertThat(returnedWizard.getId()).isEqualTo(1);
        assertThat(returnedWizard.getName()).isEqualTo("Albus Dumbledore");

        // Verify
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.wizardRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        // When
        Throwable thrown = catchThrowable(() -> {
            Wizard returnedWizard = this.wizardService.findById(1);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find wizard with Id: 1" );

        // Verify
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess() {
        // Given
        Wizard newWizard = new Wizard();
        newWizard.setName("Sirius Black");

        given(this.wizardRepository.save(newWizard)).willReturn(newWizard);

        // When
        Wizard savedWizard = this.wizardService.save(newWizard);

        // Then
        assertThat(savedWizard.getName()).isEqualTo("Sirius Black");

        // Verify
        verify(this.wizardRepository, times(1)).save(newWizard);
    }

    @Test
    void testUpdateSuccess() {
        Wizard oldWizard = new Wizard();
        oldWizard.setId(1);
        oldWizard.setName("Albus Dumbledore");

        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - UPDATED");

        // Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.of(oldWizard));
        given(this.wizardRepository.save(oldWizard)).willReturn(oldWizard);

        // When
        Wizard updatedWizard = this.wizardService.update(1, update);

        // Then
        assertThat(updatedWizard.getId()).isEqualTo(1);
        assertThat(updatedWizard.getName()).isEqualTo("Albus Dumbledore - UPDATED");

        // Verify
        verify(this.wizardRepository, times(1)).findById(1);
        verify(this.wizardRepository, times(1)).save(oldWizard);

    }

    @Test
    void testUpdateNotFound() {
        // Given
        Wizard update = new Wizard();
        update.setName("Albus Dumbledore - UPDATED");

        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.update(1, update);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess(){
        // Given
        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");

        given(this.wizardRepository.findById(1)).willReturn(Optional.of(wizard1));
        doNothing().when(this.wizardRepository).deleteById(wizard1.getId());

        // When
        this.wizardService.delete(1);

        // Then
        verify(this.wizardRepository, times(1)).deleteById(wizard1.getId());
    }

    @Test
    void testDeleteNotFound() {
        // Given
        given(this.wizardRepository.findById(1)).willReturn(Optional.empty());

        // When
        assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.delete(1);
        });

        // Then
        verify(this.wizardRepository, times(1)).findById(1);
    }

    @Test
    void testAssignArtifactSuccess(){
        // Given
        Artifact artifact1 = new Artifact();
        artifact1.setId("1250808601744904191");
        artifact1.setName("Deluminator");
        artifact1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absord (as well as return) the light from any light source to provide cover to the user.");
        artifact1.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);

        Wizard wizard2 = new Wizard();
        wizard2.setId(2);
        wizard2.setName("Harry Potter");

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact1));
        given(this.wizardRepository.findById(2)).willReturn(Optional.of(wizard2));

        // When
        this.wizardService.assignArtifact(2, "1250808601744904191");

        // Then
        assertThat(artifact1.getOwner().getId()).isEqualTo(2);
        assertThat(wizard2.getArtifacts()).contains(artifact1);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentWizardId(){
        // Given
        Artifact artifact1 = new Artifact();
        artifact1.setId("1250808601744904191");
        artifact1.setName("Deluminator");
        artifact1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absord (as well as return) the light from any light source to provide cover to the user.");
        artifact1.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);


        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.of(artifact1));
        given(this.wizardRepository.findById(2)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
           this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                        .hasMessage("Could not find wizard with Id: 2");
        assertThat(artifact1.getOwner().getId()).isEqualTo(1);
    }

    @Test
    void testAssignArtifactErrorWithNonExistentArtifactId(){
        // Given
        Artifact artifact1 = new Artifact();
        artifact1.setId("1250808601744904191");
        artifact1.setName("Deluminator");
        artifact1.setDescription("A Deluminator is a device invented by Albus Dumbledore that resembles a cigarette lighter. It is used to remove or absord (as well as return) the light from any light source to provide cover to the user.");
        artifact1.setImageUrl("ImageUrl");

        Wizard wizard1 = new Wizard();
        wizard1.setId(1);
        wizard1.setName("Albus Dumbledore");
        wizard1.addArtifact(artifact1);

        Wizard wizard2 = new Wizard();
        wizard2.setId(2);
        wizard2.setName("Harry Potter");

        given(this.artifactRepository.findById("1250808601744904191")).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.wizardService.assignArtifact(2, "1250808601744904191");
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find artifact with Id: 1250808601744904191");
    }
}
