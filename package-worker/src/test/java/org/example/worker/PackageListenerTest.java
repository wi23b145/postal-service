package org.example.worker;

import static org.junit.jupiter.api.Assertions.*;

import org.example.entities.PackageEntity;
import org.example.repo.PackageRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PackageListenerTest {
    @Test
    void under25kg_setsSent_andSaves() {
        PackageRepository repo = mock(PackageRepository.class);
        PackageListener listener = new PackageListener(repo);

        UUID id = UUID.randomUUID();
        PackageEntity entity = new PackageEntity();
        entity.setId(id);
        entity.setWeightKg(new BigDecimal("24.99")); // < 25
        entity.setStatus("waiting");

        when(repo.findById(id)).thenReturn(Optional.of(entity));

        listener.onMessage(id.toString());

        ArgumentCaptor<PackageEntity> capt = ArgumentCaptor.forClass(PackageEntity.class);
        verify(repo).save(capt.capture());
        assertEquals("sent", capt.getValue().getStatus());
    }

    @Test
    void atOrOver25kg_setsRejected_andSaves() {
        PackageRepository repo = mock(PackageRepository.class);
        PackageListener listener = new PackageListener(repo);

        UUID id = UUID.randomUUID();
        PackageEntity entity = new PackageEntity();
        entity.setId(id);
        entity.setWeightKg(new BigDecimal("25.00")); // >= 25
        entity.setStatus("waiting");

        when(repo.findById(id)).thenReturn(Optional.of(entity));

        listener.onMessage(id.toString());

        ArgumentCaptor<PackageEntity> capt = ArgumentCaptor.forClass(PackageEntity.class);
        verify(repo).save(capt.capture());
        assertEquals("rejected_overweight", capt.getValue().getStatus());
    }

    @Test
    void notFound_doesNotSave() {
        PackageRepository repo = mock(PackageRepository.class);
        PackageListener listener = new PackageListener(repo);

        UUID id = UUID.randomUUID();
        when(repo.findById(id)).thenReturn(Optional.empty());

        listener.onMessage(id.toString());

        verify(repo, never()).save(any());
    }

    @Test
    void invalidUuid_logsError_andDoesNotSave() {
        PackageRepository repo = mock(PackageRepository.class);
        PackageListener listener = new PackageListener(repo);

        listener.onMessage("not-a-uuid");

        verify(repo, never()).findById(any());
        verify(repo, never()).save(any());

    }


}