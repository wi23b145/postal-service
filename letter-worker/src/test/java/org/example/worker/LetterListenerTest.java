package org.example.worker;

import org.example.entities.LetterEntity;
import org.example.repo.LetterRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.example.entities.LetterEntity;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LetterListenerTest {

    @Test
    void allowedCountry_setsSent_andSaves() {
        LetterRepository repo = mock(LetterRepository.class);
        LetterListener listener = new LetterListener(repo);

        UUID id = UUID.randomUUID();
        LetterEntity e = new LetterEntity();
        e.setId(id);
        e.setCountry(" at ");       // mit Trim/Uppercase
        e.setStatus("waiting");

        when(repo.findById(id)).thenReturn(Optional.of(e));

        listener.onMessage(id.toString());

        ArgumentCaptor<LetterEntity> capt = ArgumentCaptor.forClass(LetterEntity.class);
        verify(repo).save(capt.capture());
        assertEquals("sent", capt.getValue().getStatus());
    }

    @Test
    void unknownCountry_setsRejected_andSaves() {
        LetterRepository repo = mock(LetterRepository.class);
        LetterListener listener = new LetterListener(repo);

        UUID id = UUID.randomUUID();
        LetterEntity e = new LetterEntity();
        e.setId(id);
        e.setCountry("us");         // nicht in {AT, DE, CH}
        e.setStatus("waiting");

        when(repo.findById(id)).thenReturn(Optional.of(e));

        listener.onMessage(id.toString());

        ArgumentCaptor<LetterEntity> capt = ArgumentCaptor.forClass(LetterEntity.class);
        verify(repo).save(capt.capture());
        assertEquals("rejected_unknown_country", capt.getValue().getStatus());
    }

    @Test
    void notFound_doesNotSave() {
        LetterRepository repo = mock(LetterRepository.class);
        LetterListener listener = new LetterListener(repo);

        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        listener.onMessage(id.toString());

        verify(repo, never()).save(any());
    }

    @Test
    void invalidUuid_doesNotQueryOrSave() {
        LetterRepository repo = mock(LetterRepository.class);
        LetterListener listener = new LetterListener(repo);

        listener.onMessage("not-a-uuid");

        verify(repo, never()).findById(any());
        verify(repo, never()).save(any());
    }
}
