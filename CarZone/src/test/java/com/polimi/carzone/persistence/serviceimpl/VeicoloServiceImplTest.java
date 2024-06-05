package com.polimi.carzone.persistence.serviceimpl;

import com.polimi.carzone.dto.request.AggiuntaVeicoloRequestDTO;
import com.polimi.carzone.dto.request.ModificaVeicoloRequestDTO;
import com.polimi.carzone.dto.response.DettagliVeicoloManagerResponseDTO;
import com.polimi.carzone.exception.*;
import com.polimi.carzone.model.Alimentazione;
import com.polimi.carzone.model.Appuntamento;
import com.polimi.carzone.model.Utente;
import com.polimi.carzone.model.Veicolo;
import com.polimi.carzone.persistence.repository.AppuntamentoRepository;
import com.polimi.carzone.persistence.repository.VeicoloRepository;
import com.polimi.carzone.state.State;
import com.polimi.carzone.state.implementation.Disponibile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VeicoloServiceImplTest {

    @Mock
    VeicoloRepository veicoloRepo;

    @Mock
    AppuntamentoRepository appuntamentoRepo;

    @InjectMocks
    VeicoloServiceImpl veicoloService;

    @Test
    void aggiungiVeicoloSuccessful() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("BENZINA");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertAll(() -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloThrowsImmagineNull() throws IOException {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("DIESEL");
        request.setPrezzo(10000.0);
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.aggiungiVeicolo(request, null));
    }

    @Test
    void aggiungiVeicoloSuccessfulDiesel() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("DIESEL");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertAll(() -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloSuccessfulIbrida() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("IBRIDA");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertAll(() -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloSuccessfulGpl() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("GPL");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertAll(() -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloSuccessfulElettrica() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("ELETTRICA");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.jpg");
        when(multipartFile.getContentType()).thenReturn("image/jpeg");
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertAll(() -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloThrowsRequestNullException() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.aggiungiVeicolo(null, null));
    }

    @Test
    void aggiungiVeicoloThrowsVeicoloAlreadyExistsException() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("BENZINA");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.of(new Veicolo()));
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloThrowsErroriIsEmpty() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga(null);
        request.setMarca(null);
        request.setModello(null);
        request.setChilometraggio(null);
        request.setAnnoProduzione(null);
        request.setPotenzaCv(null);
        request.setAlimentazione(null);
        request.setPrezzo(null);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void aggiungiVeicoloThrowsAlimentazioneNonValida() {
        AggiuntaVeicoloRequestDTO request = new AggiuntaVeicoloRequestDTO();
        request.setTarga("AA123BB");
        request.setMarca("Fiat");
        request.setModello("Panda");
        request.setChilometraggio(10000);
        request.setAnnoProduzione(2020);
        request.setPotenzaCv(70);
        request.setAlimentazione("CIAO");
        request.setPrezzo(10000.0);
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertThrows(AlimentazioneNonValidaException.class,
                () -> veicoloService.aggiungiVeicolo(request, multipartFile));
    }

    @Test
    void findAllSuccessful() {
        List<Veicolo> veicoli = new ArrayList<>();
        Veicolo veicolo = new Veicolo();
        veicolo.setFilePath("C:/Users/casca/Desktop/unigenerale/IngegneriaDS/progettofinale/Progetto/CarZone/src/main/resources/immagini/risultatotol.jpg");
        veicoli.add(veicolo);
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertAll(() -> veicoloService.findAll());
    }

    @Test
    void findByTargaSuccessful() {
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.findByTarga("AA123BB"));
    }

    @Test
    void findByTargaThrowsTargaNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByTarga(null));
    }

    @Test
    void findByTargaThrowsVeicoloNonTrovato() {
        when(veicoloRepo.findByTarga(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByTarga("AA123BB"));
    }

    @Test
    void findByIdSuccessful() {
        when(veicoloRepo.findById(1L)).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.findById(1L));
    }

    @Test
    void findByIdThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findById(-1L));
    }

    @Test
    void findByIdThrowsVeicoloNonTrovato() {
        when(veicoloRepo.findById(1L)).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findById(1L));
    }

    @Test
    void convertiVeicoliInVeicoliResponseSuccessful() {
        List<Veicolo> veicoli = new ArrayList<>();
        veicoli.add(new Veicolo());
        assertAll(() -> veicoloService.convertiVeicoliInVeicoliResponse(veicoli));
    }

    @Test
    void convertiVeicoliInVeicoliResponseThrowsVeicoliNonDisponibili() {
        List<Veicolo> veicoli = new ArrayList<>();
        assertThrows(VeicoliNonDisponibiliException.class,
                () -> veicoloService.convertiVeicoliInVeicoliResponse(veicoli));
    }

    @Test
    void findByMarcaSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByMarca(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByMarca("Fiat"));
    }

    @Test
    void findByMarcaThrowsMarcaNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByMarca(null));
    }

    @Test
    void findByMarcaThrowsVeicoloNonTrovato() {
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByMarca(any())).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByMarca("Fiat"));
    }

    @Test
    void findByMarcaAndModelloSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByMarcaAndModello(any(), any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByMarcaAndModello("Fiat", "Panda"));
    }

    @Test
    void findByMarcaAndModelloThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByMarcaAndModello(null, null));
    }

    @Test
    void findByMarcaAndModelloThrowsVeicoloNonTrovato() {
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByMarcaAndModello(any(), any())).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByMarcaAndModello("Fiat", "Panda"));
    }

    @Test
    void findByAlimentazioneSuccessful(){
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAlimentazione("BENZINA"));
    }

    @Test
    void findByAlimentazioneDieselSuccessful(){
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAlimentazione("DIESEL"));
    }

    @Test
    void findByAlimentazioneIbridaSuccessful(){
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAlimentazione("IBRIDA"));
    }

    @Test
    void findByAlimentazioneGplSuccessful(){
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAlimentazione("GPL"));
    }

    @Test
    void findByAlimentazioneElettricaSuccessful(){
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAlimentazione("ELETTRICA"));
    }



    @Test
    void findByAlimentazioneThrowsAlimentazioneNull(){
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByAlimentazione(null));
    }

    @Test
    void findByAlimentazioneThrowsAlimentazioneNonValida(){
        assertThrows(AlimentazioneNonValidaException.class,
                () -> veicoloService.findByAlimentazione("CIAO"));
    }

    @Test
    void findByAlimentazioneThrowsVeicoloNonTrovato(){
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByAlimentazione(any())).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByAlimentazione("BENZINA"));
    }

    @Test
    void findByAnnoProduzioneSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByAnnoProduzioneBetween(2020,2021)).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByAnnoProduzione(2020, 2021));
    }

    @Test
    void findByAnnoProduzioneThrowsAnnoProduzioneNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByAnnoProduzione(null, null));
    }

    @Test
    void findByAnnoProduzioneThrowsAnnoProduzioneMinimoNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByAnnoProduzione(null, 2025));
    }

    @Test
    void findByAnnoProduzioneThrowsAnnoProduzioneMassimoNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByAnnoProduzione(1800, null));
    }

    @Test
    void findByAnnoProduzioneThrowsAnnoProduzioneContrario() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByAnnoProduzione(2021, 2020));
    }

    @Test
    void findByAnnoProduzioneThrowsVeicoloNonTrovato() {
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByAnnoProduzioneBetween(2021,2022)).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByAnnoProduzione(2021, 2022));
    }

    @Test
    void findByPrezzoSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByPrezzoBetween(10000.0, 20000.0)).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByPrezzo(10000.0, 20000.0));
    }

    @Test
    void findByPrezzoThrowsPrezzoNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPrezzo(null, null));
    }

    @Test
    void findByPrezzoThrowsPrezzoMinimoNull() {
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByPrezzo(null, 20000.0));
    }

    @Test
    void findByPrezzoThrowsPrezzoMassimoNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPrezzo(-1.0, null));
    }

    @Test
    void findByPrezzoThrowsPrezzoContrario() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPrezzo(20000.0, 10000.0));
    }

    @Test
    void findByPotenzaSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByPotenzaCvBetween(70, 80)).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByPotenza(70, 80));
    }

    @Test
    void findByPotenzaThrowsPotenzaNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPotenza(null, null));
    }

    @Test
    void findByPotenzaThrowsPotenzaMinimaNull() {
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByPotenza(null, 80));
    }

    @Test
    void findByPotenzaThrowsPotenzaMassimaNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPotenza(-1, null));
    }

    @Test
    void findByPotenzaThrowsPotenzaContrario() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByPotenza(80, 70));
    }

    @Test
    void findByPotenzaThrowsVeicoloNonTrovato() {
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByPotenzaCvBetween(70, 80)).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByPotenza(70, 80));
    }

    @Test
    void findByChilometraggioSuccessful() {
        Optional<List<Veicolo>> veicoli = Optional.of(new ArrayList<>());
        veicoli.get().add(new Veicolo());
        when(veicoloRepo.findByChilometraggioBetween(10000, 20000)).thenReturn(veicoli);
        assertAll(() -> veicoloService.findByChilometraggio(10000, 20000));
    }

    @Test
    void findByChilometraggioThrowsChilometraggioNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByChilometraggio(null, null));
    }

    @Test
    void findByChilometraggioThrowsChilometraggioMinimoNull() {
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByChilometraggio(null, 20000));
    }

    @Test
    void findByChilometraggioThrowsChilometraggioMassimoNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByChilometraggio(-1, null));
    }

    @Test
    void findByChilometraggioThrowsChilometraggioContrario() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findByChilometraggio(20000, 10000));
    }

    @Test
    void findByChilometraggioThrowsVeicoloNonTrovato() {
        Optional<List<Veicolo>> veicoli = Optional.empty();
        when(veicoloRepo.findByChilometraggioBetween(10000, 20000)).thenReturn(veicoli);
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.findByChilometraggio(10000, 20000));
    }

    @Test
    void registraVenditaSuccessful() {
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.registraVendita(1L, new Utente()));
    }

    @Test
    void registraVenditaThrowsErroriIsEmpty() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.registraVendita(null, null));
    }

    @Test
    void registraVenditaThrowsVeicoloNonTrovato() {
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.registraVendita(1L, new Utente()));
    }

    @Test
    void registraVenditaThrowsVeicoloGiaVenduto() {
        Veicolo veicolo = new Veicolo();
        veicolo.setAcquirente(new Utente());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(veicolo));
        assertThrows(VeicoloVendutoException.class,
                () -> veicoloService.registraVendita(1L, new Utente()));
    }

    @Test
    void findAllConDettagliSuccessful() {
        List<Veicolo> veicoli = new ArrayList<>();
        veicoli.add(new Veicolo());
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertAll(() -> veicoloService.findAllConDettagli());
    }

    @Test
    void eliminaVeicoloSuccessful() {
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.eliminaVeicolo(1L));
    }

    @Test
    void eliminaVeicoloThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.eliminaVeicolo(null));
    }

    @Test
    void eliminaVeicoloThrowsVeicoloNonTrovato() {
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.eliminaVeicolo(1L));
    }

    @Test
    void eliminaVeicoloThrowsVeicoloVenduto() {
        Veicolo veicolo = new Veicolo();
        veicolo.setAcquirente(new Utente());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(veicolo));
        assertThrows(VeicoloVendutoException.class,
                () -> veicoloService.eliminaVeicolo(1L));
    }

    @Test
    void modificaVeicoloSuccessful() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "BENZINA", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloDieselSuccessful() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "DIESEL", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloIbridaSuccessful() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "IBRIDA", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloGplSuccessful() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "GPL", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloElettricaSuccessful() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "ELETTRICA", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloThrowsRequestNullException() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.modificaVeicolo(1L, null));
    }

    @Test
    void modificaVeicoloThrowsErroriIsEmpty() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO(null, null, null, null, null, null, null, null, null);
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.modificaVeicolo(null,request));
    }

    @Test
    void modificaVeicoloThrowsAlimentazioneNonValida() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "CIAO", 10000.0);
        assertThrows(AlimentazioneNonValidaException.class,
                () -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloThrowsVeicoloNonTrovato() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "BENZINA", 10000.0);
        when(veicoloRepo.findById(any())).thenReturn(Optional.empty());
        assertThrows(VeicoloNonTrovatoException.class,
                () -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void modificaVeicoloThrowsVeicoloVenduto() {
        ModificaVeicoloRequestDTO request = new ModificaVeicoloRequestDTO( 1L, "AB123CD", "marca", "modello", 70, 2020, 90, "BENZINA", 10000.0);
        Veicolo veicolo = new Veicolo();
        veicolo.setAcquirente(new Utente());
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(veicolo));
        assertThrows(VeicoloVendutoException.class,
                () -> veicoloService.modificaVeicolo(1L,request));
    }

    @Test
    void findAllDisponibiliSuccessful() {
        List<Veicolo> veicoli = new ArrayList<>();
        Veicolo veicolo = new Veicolo();
        veicolo.setId(1L);
        veicolo.setAcquirente(null);
        veicolo.setAppuntamentiVeicolo(new ArrayList<>());
        veicolo.setStato(new Disponibile(veicolo));
        veicoli.add(veicolo);
        when(appuntamentoRepo.findByVeicolo_IdAndEsitoRegistratoIsFalse(1L)).thenReturn(Optional.of(new ArrayList<>()));
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertAll(() -> veicoloService.findAllDisponibili());
    }

    @Test
    void findAllDisponibiliThrowsVeicoliNonDisponibili() {
        List<Veicolo> veicoli = new ArrayList<>();
        Veicolo veicolo = new Veicolo();
        veicolo.setAcquirente(new Utente());
        veicoli.add(veicolo);
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertThrows(VeicoliNonDisponibiliException.class,
                () -> veicoloService.findAllDisponibili());
    }

    @Test
    void findAllDisponibiliESelezionatoSuccessful() {
        List<Veicolo> veicoli = new ArrayList<>();
        Veicolo veicolo = new Veicolo();
        veicolo.setId(1L);
        veicolo.setAcquirente(null);
        veicolo.setAppuntamentiVeicolo(new ArrayList<>());
        veicolo.setStato(new Disponibile(veicolo));
        veicoli.add(veicolo);
        Appuntamento appuntamento = new Appuntamento();
        appuntamento.setVeicolo(veicolo);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(appuntamento));
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertAll(() -> veicoloService.findAllDisponibiliESelezionato(10L));
    }

    @Test
    void findAllDisponibiliESelezionatoThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.findAllDisponibiliESelezionato(null));
    }

    @Test
    void findAllDisponibiliESelezionatoThrowsAppuntamentoNonTrovato() {
        List<Veicolo> veicoli = new ArrayList<>();
        Veicolo veicolo = new Veicolo();
        veicolo.setId(1L);
        veicolo.setAcquirente(null);
        veicolo.setAppuntamentiVeicolo(new ArrayList<>());
        veicolo.setStato(new Disponibile(veicolo));
        veicoli.add(veicolo);
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.empty());
        when(veicoloRepo.findAll()).thenReturn(veicoli);
        assertThrows(AppuntamentoNonTrovatoException.class,
                () -> veicoloService.findAllDisponibiliESelezionato(10L));
    }

    @Test
    void findAllDisponibiliESelezionatoThrowsVeicoliNonDisponibili() {
        Appuntamento appuntamento = new Appuntamento();
        when(appuntamentoRepo.findById(any())).thenReturn(Optional.of(appuntamento));
        when(veicoloRepo.findAll()).thenReturn(new ArrayList<>());
        assertThrows(VeicoliNonDisponibiliException.class,
                () -> veicoloService.findAllDisponibiliESelezionato(10L));
    }

    @Test
    void estraiIdDaFindAllDisponibili() {
        List<DettagliVeicoloManagerResponseDTO> veicoli = new ArrayList<>();
        veicoli.add(new DettagliVeicoloManagerResponseDTO(1L, "AA123BB", "Fiat", "Panda", 10000, 2020, 70, Alimentazione.BENZINA, 10000.0, "DISPONIBILE", new byte[1]));
        assertAll(() -> veicoloService.estraiIdDaFindAllDisponibili(veicoli));
    }

    @Test
    void recuperaDettagliSuccessful() {
        when(veicoloRepo.findById(any())).thenReturn(Optional.of(new Veicolo()));
        assertAll(() -> veicoloService.recuperaDettagli(1L));
    }

    @Test
    void recuperaDettagliThrowsIdNull() {
        assertThrows(CredenzialiNonValideException.class,
                () -> veicoloService.recuperaDettagli(null));
    }

}
