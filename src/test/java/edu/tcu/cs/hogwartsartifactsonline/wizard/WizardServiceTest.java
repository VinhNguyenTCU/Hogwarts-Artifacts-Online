package edu.tcu.cs.hogwartsartifactsonline.wizard;

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
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class WizardServiceTest {

    @Mock
    WizardRepository wizardRepository;

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
                .isInstanceOf(WizardNotFoundException.class)
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
}
